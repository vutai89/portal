package com.mcredit.business.checkcic.aggregate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections4.map.HashedMap;

import com.google.common.reflect.TypeToken;
import com.mcredit.business.checkcic.callout.EsbApi;
import com.mcredit.business.checkcic.convert.Converter;
import com.mcredit.business.checkcic.object.ApproveLeadgenDataResult;
import com.mcredit.business.checkcic.object.IdentityInfo;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.NotificationTemplate;
import com.mcredit.data.common.entity.Parameters;
import com.mcredit.data.pcb.entity.CreditBureauData;
import com.mcredit.data.telesale.entity.UplCustomer;
import com.mcredit.data.telesale.entity.UplCustomerHistory;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.data.telesale.entity.UplMaster;
import com.mcredit.model.dto.cic.CICDetailDTO;
import com.mcredit.model.dto.cic.CICDetailForBpmDTO;
import com.mcredit.model.dto.cic.CICRequestClaim;
import com.mcredit.model.dto.cic.CICUpdateQualifySttDTO;
import com.mcredit.model.dto.cic.CICUpdateQualifySttResp;
import com.mcredit.model.dto.cic.LeadDataTSReportDTO;
import com.mcredit.model.dto.cic.LeadDataTSReportResp;
import com.mcredit.model.enums.ConstantTelesale;
import com.mcredit.model.enums.LeadDataTsReportStatus;
import com.mcredit.model.enums.LeadGenEnums;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.RelationshipWithApplicant;
import com.mcredit.model.enums.TemplateEnum;
import com.mcredit.model.enums.cic.CICRequestSource;
import com.mcredit.model.enums.cic.CICRequestState;
import com.mcredit.model.enums.cic.CICResultStatus;
import com.mcredit.model.object.ESBResponseDTO;
import com.mcredit.model.object.ESBResponseResultDTO;
import com.mcredit.model.object.SendEmailDTO;
import com.mcredit.model.object.mobile.enums.MfsMsgTransType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.model.Result;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.util.DateTimeFormat;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class CheckCICAggregate {

	private static final String CIC_DOWNLOAD_IMAGE_LINK = "/api/v1.0/mcportal/check-cic/download-image?identity={0}";
	private static final String CIC_EXTENSION_LINK = "/check-cic/extension/search?citizenList={0}";
	private static final String MCREDIT_MAIL_DOMAIN = "@mcredit.com.vn";
	private static final Long CIC_RESULT_EXPIRE_DAYS_DEFAULT = 15L;
	public static final String STATUS_CODE_OK = "200";
	public static final String STATUS_CODE_400 = "400";
	public static final String STATUS_CODE_401 = "401";
	public static final String STATUS_CODE_500 = "500";
	public static final int QUALIFY_STATUS_PASSED = 0;
	public static final int QUALIFY_STATUS_FAILED = 1;
	public static final int QUALIFY_REJECT_CODE_PASSED = 0;
	public static final int QUALIFY_REJECT_CODE_FAILED = 7;
	public static final String QUALIFY_REASON_PASSED = "PASSED";
	public static final String QUALIFY_REASON_FAILED = "FAILED";
	public static final String RESULT_CIC_SUCCESS = "0";
	private static ParametersCacheManager parameter = CacheManager.Parameters();
	
	private UnitOfWork _uok = null;
	private EsbApi esbApi = null;
	private Long cicResultExpireDays;
	private String cicResultFileDir;
	private String processIdConcentrating;
	
	public CheckCICAggregate(UnitOfWork _uok) {
		this._uok = _uok;
		esbApi = new EsbApi();
		
		cicResultExpireDays = parameter.findParamValueAsLong(ParametersName.CIC_RESULT_EXPIRE_DAYS);
		if (cicResultExpireDays == null || cicResultExpireDays <= 0)
			cicResultExpireDays = CIC_RESULT_EXPIRE_DAYS_DEFAULT;
		
		cicResultFileDir = parameter.findParamValueAsString(ParametersName.CIC_RESULT_DIR) + File.separator;
		processIdConcentrating = parameter.findParamValueAsString(ParametersName.BPM_PROCESS_ID_CONCENTRATING);
	}
	
	/**
	 * Process request create new cic request
	 * @author catld.ho
	 * @param citizenID : cmnd
	 * @param oldCitizenID : cccd
	 * @param militaryID : cmqd
	 * @param customerName : customer name
	 * @param user : login id
	 * @return list CICDetailDTO
	 * @throws Exception
	 */
	public List<CICDetailDTO> checkCIC(String citizenID, String oldCitizenID, String militaryID, String customerName, String user, Integer fromSource) throws Exception {
		System.out.println("Receive request checkCIC: citizenID=" + citizenID + ", oldCitizenID=" + oldCitizenID + ",  militaryID="+ militaryID + ",  customerName=" + customerName + ", user=" + user + ", fromSource=" + fromSource);
		List<CICDetailDTO> result = new ArrayList<CICDetailDTO>();
		
		// get cic result or create request if result not found. Request sent from web portal
		result.add(getCICResult(citizenID, customerName, user, fromSource));
		
		if (!StringUtils.isNullOrEmpty(oldCitizenID))
			result.add(getCICResult(oldCitizenID, customerName, user, fromSource));
		
		if (!StringUtils.isNullOrEmpty(militaryID))
			result.add(getCICResult(militaryID, customerName, user, fromSource));
		
		return result;
	}
	
	/**
	 * Get cic result or create new requets if not exists
	 * @param identity : cmnd/cccd/cmqd
	 * @param customerName : customer name
	 * @param user : login id
	 * @return CICDetailDTO
	 * @throws Exception
	 */
	private CICDetailDTO getCICResult(String identity, String customerName, String user, Integer msgOrder) throws Exception {
		CICDetailDTO result = new CICDetailDTO(identity, customerName);
		
		CreditBureauData cicDetail = _uok.pcb.creditBureauDataRepository().getCICResponse(identity);
		
		// cic result not found. create new cic request
		if (null == cicDetail) {
			cicDetail = Converter.getDefaultCICData(_uok.pcb.creditBureauDataRepository().getCustIdByIdentity(identity), identity, customerName);
			// create cic info in CreditBureauData
			_uok.pcb.creditBureauDataRepository().upsert(cicDetail);
			if (cicDetail == null || cicDetail.getId() == null)
				throw new Exception(Messages.getString("insert new cic to CreditBureauData fail"));
			
			// create cic request in MessageLog
			MessageLog messageLog = Converter.getMessageLogObject(identity, cicDetail.getId(), user, msgOrder);
			_uok.common.messageLogRepo().upsert(messageLog);
			if (messageLog == null || messageLog.getId() == null)
				throw new Exception(Messages.getString("insert new cic request to MessageLog fail"));
			
			result.setStatus(CICResultStatus.NEW.value());
		} else if (StringUtils.isNullOrEmpty(cicDetail.getCbDataDetail())) {		// cic request is exists but not checked
			result.setStatus(CICResultStatus.CHECKING.value());
		} else {		// cic request is exist
			CICDetailDTO cicChecked = JSONConverter.toObject(cicDetail.getCbDataDetail(), CICDetailDTO.class);
			if (cicChecked == null)
				throw new Exception(Messages.getString("Convert cic result detail to object CICDetailDTO fail"));
			
			if (CICResultStatus.SUCCESS.value().equals(cicChecked.getStatus())) {	// have result
				// check result over 15 days
				Long diffDays = DateUtil.getDateDiff(DateUtil.getDateWithoutTime(cicDetail.getLastUpdatedDate()), DateUtil.getDateWithoutTime(new Date()), TimeUnit.DAYS);
				if (diffDays > cicResultExpireDays) {		// over 15 days. recreate request check cic
					// update status to renew
					cicChecked.setStatus(CICResultStatus.RENEW.value());
					cicDetail.setCbDataDetail(JSONConverter.toJSON(cicChecked));
					_uok.pcb.creditBureauDataRepository().upsert(cicDetail);
					
					// recreate new request check cic
					MessageLog messageLog = Converter.getMessageLogObject(identity, cicDetail.getId(), user, msgOrder);
					_uok.common.messageLogRepo().upsert(messageLog);
					if (messageLog == null || messageLog.getId() == null)
						throw new Exception(Messages.getString("insert (re)new cic request to MessageLog fail"));
					
					result.setStatus(CICResultStatus.RENEW.value());
				} else {	// have result valid
					Converter.getCICResultDetail(result, cicChecked, cicDetail);
				}
			} else {	// have cic request but not checked
				result.setStatus(CICResultStatus.CHECKING.value());
			}
		}
		
		return result;
	}
	
	/**
	 * Search cic result
	 * @author catld.ho
	 * @param citizenID : cmnd/cccd/cmqd
	 * @param customerName : customer name
	 * @return list CICDetailDTO
	 * @throws Exception
	 */
	public List<CICDetailDTO> searchCIC(String citizenID, String customerName) throws Exception {
		List<CICDetailDTO> result = new ArrayList<CICDetailDTO>();
		CICDetailDTO cicResult = new CICDetailDTO(citizenID);
		
		// get available cic result (not over 15 days)
		CreditBureauData cicDetail = _uok.pcb.creditBureauDataRepository().getCICResponseAvailable(citizenID, cicResultExpireDays);
		if (null == cicDetail || StringUtils.isNullOrEmpty(cicDetail.getCbDataDetail()))
			return result;
		
		// check result state and customer name
		CICDetailDTO cicChecked = JSONConverter.toObject(cicDetail.getCbDataDetail(), CICDetailDTO.class);
		if (null == cicChecked || !CICResultStatus.SUCCESS.value().equals(cicChecked.getStatus()) ||
				(!StringUtils.isNullOrEmpty(customerName) && !customerName.equalsIgnoreCase(cicChecked.getCustomerName())))
			return result;
		
		Converter.getCICResultDetail(cicResult, cicChecked, cicDetail);
		result.add(cicResult);
		
		return result;
	}
	
	/**
	 * Search list cic result
	 * @author catld.ho
	 * @param listIdentity
	 * @return list CICDetailForBpmDTO
	 * @throws Exception
	 */
	public List<CICDetailForBpmDTO> searchListCIC(List<String> listIdentity) throws Exception {
		List<CICDetailForBpmDTO> result = new ArrayList<CICDetailForBpmDTO>();
		
		if (listIdentity == null || listIdentity.isEmpty())
			return result;
		
		// get available cic result (not over 15 days)
		List<CreditBureauData> cicDetails = _uok.pcb.creditBureauDataRepository().getListCICResponseAvailable(listIdentity, cicResultExpireDays);
		if (null == cicDetails || cicDetails.isEmpty())
			return result;
		
		for (CreditBureauData cicDetail : cicDetails) {
			if (null == cicDetail || StringUtils.isNullOrEmpty(cicDetail.getCbDataDetail()))
				continue;
			
			CICDetailForBpmDTO cicResult = new CICDetailForBpmDTO(cicDetail.getCustIdentityNumber());
			// check result state and customer name
			CICDetailDTO cicChecked = JSONConverter.toObject(cicDetail.getCbDataDetail(), CICDetailDTO.class);
			if (null == cicChecked || !CICResultStatus.SUCCESS.value().equals(cicChecked.getStatus()))
				continue;
			
			Converter.getCICResultDetailForBpm(cicResult, cicChecked, cicDetail, MessageFormat.format(CIC_EXTENSION_LINK, String.join(",", listIdentity)));
			result.add(cicResult);
		}
		
		return result;
	}
	
	/**
	 * Claim cic request to process
	 * @author catld.ho
	 * @param username : login id
	 * @return list CICDetailDTO
	 * @throws Exception
	 */
	public List<CICDetailDTO> claimRequest(String username) throws Exception {
		List<CICDetailDTO> result = new ArrayList<CICDetailDTO>();
		List<Long> listId = new ArrayList<>();
		List<Long> listTransId = new ArrayList<>();
		
		// update assigned request for 'username' to unassign.
		_uok.pcb.creditBureauDataRepository().resetStatusCICRequest(username);
		
		// get top 10 request unassign. priority manual request. (job (procedure) update_cic_request_status will update assigned request over 20 minutes to unassign)
		List<CICRequestClaim> cicRequest = _uok.pcb.creditBureauDataRepository().claimCICRequest();
		
		if (null == cicRequest)
			throw new Exception("Claim manual request error");
		
		// get top 10 request unassign created by autoJob.  (job (procedure) update_cic_request_status will update assigned request over 20 minutes to unassign)
		if (cicRequest.size() < 10) {
			List<CICRequestClaim> cicRequestAutoJob = _uok.pcb.creditBureauDataRepository().claimCICRequestAutoJob(10 - cicRequest.size());
			if (cicRequestAutoJob != null && cicRequestAutoJob.size() > 0)
				cicRequest.addAll(cicRequestAutoJob);
		}
		
		if (cicRequest.isEmpty())
			return result;
		
//		for(CICRequestClaim item : cicRequest) {
//			result.add(Converter.getCICDetailFromRequest(item));
//			listId.add(item.getRequestId());
//		}
		
		for(CICRequestClaim item : cicRequest) {
			listTransId.add(item.getTransId());
			listId.add(item.getRequestId());
		}

		// get cic info
		List<CreditBureauData> listCicInfo = _uok.pcb.creditBureauDataRepository().getCICInfoById(listTransId);

		if (null == listCicInfo)
			throw new Exception("Get cic info can not null");
		
		for(CICRequestClaim item1 : cicRequest) {
			for(CreditBureauData item : listCicInfo) {
				if (item.getId().equals(item1.getTransId()))
					result.add(Converter.getCICDetailFromRequest(item1, item));
			}
		}

		// update state cic request after claimed
		if (listId.size() > 0)
			_uok.pcb.creditBureauDataRepository().updateStatusRequestClaimed(username, listId);
		
		return result;
	}
	
	/**
	 * Update cic result
	 * @author catld.ho
	 * @param fileContent : file cic result
	 * @param cic : cic result
	 * @param currentUser : login id
	 * @return CICDetailDTO
	 * @throws Exception
	 */
	public Object updateCIC(InputStream fileContent, CICDetailDTO cic, String currentUser) throws Exception {
		
		// check request claim over 20 minutes getMessageByMessId
		MessageLog msgLog = _uok.common.messageLogRepo().getMessageByMessId(cic.getRequestId());
		if (null == msgLog)
			throw new Exception(Messages.getString("Request " + cic.getRequestId() + " do not exists"));
		
		// over 20 minutes or other user claim this request
		if ((msgLog.getProcessTime() != null && (new Date().getTime() - msgLog.getProcessTime().getTime()) > 1200000) || 
				(!StringUtils.isNullOrEmpty(msgLog.getResponsePayloadId()) && !currentUser.equalsIgnoreCase(msgLog.getResponsePayloadId())) )		
			return new Result(STATUS_CODE_400, "Request over");
//			throw new Exception(Messages.getString("Request " + cic.getRequestId() + " claimed over 20 minutes"));
		
		// save cic image result
		String fileName = saveCICmage(fileContent, cic.getIdentifier(), cic.getCicImageLink());
		cic.setStatus(CICResultStatus.SUCCESS.value());
		cic.setCicImageLink(fileName);
		cic.setLastUpdateTime(DateUtil.toDateString(new Date(), "dd-MM-yyyy HH:mm:ss"));
		cic.setDescription(Converter.getCICResultDescription(cic.getCicResult()));
		
		// update cic result
		_uok.pcb.creditBureauDataRepository().updateCICResult(JSONConverter.toJSON(cic), cic.getIdentifier());
		
		// update cic request
		msgLog.setResponseTime(new Timestamp((new Date()).getTime()));
		msgLog.setMsgStatus(CICRequestState.RESULT.value());
		msgLog.setMsgResponse(JSONConverter.toJSON(cic));
		
		// send email when request created from web portal or report cic error
		if (msgLog.getMsgOrder() == CICRequestSource.WEB_PORTAL.value() || msgLog.getMsgOrder() == CICRequestSource.WEB_REPORT_ERROR.value() || 
				msgLog.getMsgOrder() == CICRequestSource.WEB_REPORT_WRONG.value()) {
			try {
				sendEmail(cic, msgLog.getServiceName());
				msgLog.setMsgStatus(CICRequestState.EMAIL.value());
			} catch(Exception ex) {
				System.out.println("Cic send email to: " + msgLog.getServiceName() + " error: " + ex.getMessage());
			}
		}
		
		// save cic request
		_uok.common.messageLogRepo().upsert(msgLog);
		return cic;
	}
	
	/**
	 * Download cic result file
	 * @author catld.ho
	 * @param identity : cmnd/cccd/cmqd
	 * @return File
	 * @throws Exception
	 */
	public File getDownloadFile(String identity) throws Exception {
		
		CreditBureauData cicDetail = _uok.pcb.creditBureauDataRepository().getCICResponse(identity);
		if (null == cicDetail || StringUtils.isNullOrEmpty(cicDetail.getCbDataDetail()))
			throw new Exception(Messages.getString("CIC result of " + identity + " not found"));
		
		// check cic file link
		CICDetailDTO cicChecked = JSONConverter.toObject(cicDetail.getCbDataDetail(), CICDetailDTO.class);
		if (cicChecked == null || StringUtils.isNullOrEmpty(cicChecked.getCicImageLink()))
			throw new Exception(Messages.getString("CIC file result not found"));
		
		return new File(cicResultFileDir + URLEncoder.encode(cicChecked.getCicImageLink(), "UTF-8"));
	}
	
	/**
	 * job auto create cic request
	 * @author catld.ho
	 * @return
	 * @throws Exception
	 */
	public Object autoCreateCICRequest() throws Exception {
		
		// Get last scan new identity time
		Parameters lastScanTime = _uok.common.parametersRepo().findParameter(ParametersName.CIC_AUTO_CREATE_REQ_TIME.value());
		String paramLastScanTime = "", paramCurrentScanTime = DateUtil.toDateString(new Date(), DateTimeFormat.yyyyMMddHHmmss.getDescription());
		if (lastScanTime == null)
			throw new Exception(Messages.getString("param CIC_AUTO_CREATE_REQ_TIME not found"));
		
		if (StringUtils.isNullOrEmpty(lastScanTime.getParamValue())) {
			paramLastScanTime = DateUtil.toDateString(DateUtil.addDay(new Date(), -7), DateTimeFormat.yyyyMMddHHmmss.getDescription());
		} else {
			paramLastScanTime = lastScanTime.getParamValue();
			paramLastScanTime = DateUtil.toDateString(DateUtil.addSecond(DateUtil.toDate(paramLastScanTime, DateTimeFormat.yyyyMMddHHmmss), -60), DateTimeFormat.yyyyMMddHHmmss.getDescription());
		}
		
		// get identity of new contract from bpm
		List<CICDetailDTO> result = new ArrayList<CICDetailDTO>();
		ApiResult apiResult = esbApi.getNewIdentity(paramLastScanTime, paramCurrentScanTime, processIdConcentrating);
		
		if (apiResult == null || !apiResult.getStatus())
			throw new Exception(Messages.getString("esb result CIC_CLAIM_NEW_IDENTITY_FROM_BPM error"));
		
		// parse result, get list identity
		Map<String, String> listIdentity = parseListIdentity(apiResult);	// Map<identity, customerName>
		if (listIdentity == null || listIdentity.isEmpty()) {
			// update last scan time
			lastScanTime.setParamValue(paramCurrentScanTime);
			_uok.common.parametersRepo().upsert(lastScanTime);
			return new Result(STATUS_CODE_OK, "No new identity found");
		}
		
		// Create new request check cic if not exists
		for (Map.Entry<String, String> entry : listIdentity.entrySet()) {
			if (StringUtils.isNullOrEmpty(entry.getKey()))
				continue;
			
			CICDetailDTO cdc = getCICResult(entry.getKey(), entry.getValue(), "autoJob", null);
			if (cdc != null && (CICResultStatus.NEW.value().equals(cdc.getStatus()) || CICResultStatus.RENEW.value().equals(cdc.getStatus())))
				result.add(cdc);
		}
		
		// update last scan new identity time to table Paramerters
		lastScanTime.setParamValue(paramCurrentScanTime);
		_uok.common.parametersRepo().upsert(lastScanTime);
		return new Result(STATUS_CODE_OK, result.size() + "  new identity");
	}
	
	/**
	 * Save cic result file
	 * @author catld.ho
	 * @param fileContent
	 * @param identity
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public String saveCICmage(InputStream fileContent, String identity, String fileName) throws Exception {
		String fileSave = null;
		String dirPath = parameter.findParamValueAsString(ParametersName.CIC_RESULT_DIR) + File.separator;
		
		String filePath = dirPath + URLEncoder.encode(fileName, "UTF-8");
		File theDir = new File(dirPath);
		if (!theDir.exists())
			theDir.mkdirs();

		byte[] buffer = new byte[8192];
		int read = 0;
		OutputStream out = null;

		try {
			// save file
			out = new FileOutputStream(new File(filePath));
			while ((read = fileContent.read(buffer)) > 0) {
				out.write(buffer, 0, read);
			}
			fileSave = fileName;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
		
		if (StringUtils.isNullOrEmpty(fileSave))
			throw new Exception("Save file " + fileName + " fail");
		
		return fileSave;
	}
	
	/**
	 * Parse result get from pbm
	 * @author catld.ho
	 * @param apiResult
	 * @return map identity
	 * @throws Exception
	 */
	public Map<String, String> parseListIdentity(ApiResult apiResult) throws Exception {
		Map<String, String> list = new HashedMap<>();
		
		if (StringUtils.isNullOrEmpty(apiResult.getBodyContent()))
			throw new Exception(Messages.getString("esb content CIC_CLAIM_NEW_IDENTITY_FROM_BPM error"));
		
		List<IdentityInfo> listResult = null;
		try {
			listResult = JSONConverter.toObject(apiResult.getBodyContent(), new TypeToken<List<IdentityInfo>>() {}.getType());
		} catch(Exception e) {}		// TH khong co thong tin IdentityInfo se tra ve '{}' nen parse se loi
		
		if (listResult != null) {
			for (IdentityInfo item : listResult) {
				if (!StringUtils.isNullOrEmpty(item.getCitizenID()) && !list.containsKey(item.getCitizenID()))
					list.put(item.getCitizenID(), item.getCustomerName());
				
				if (!StringUtils.isNullOrEmpty(item.getCitizenIdOld()) && !list.containsKey(item.getCitizenIdOld()))
					list.put(item.getCitizenIdOld(), item.getCustomerName());
				
				if (!StringUtils.isNullOrEmpty(item.getMilitaryId()) && !list.containsKey(item.getMilitaryId()))
					list.put(item.getMilitaryId(), item.getCustomerName());
				
				if (!StringUtils.isNullOrEmpty(item.getRelationshipWithApplicant()) && RelationshipWithApplicant.SPOUSE.value().equals(item.getRelationshipWithApplicant()) && 
						!StringUtils.isNullOrEmpty(item.getSpouseIDNumber()) && !list.containsKey(item.getSpouseIDNumber()))
					list.put(item.getSpouseIDNumber(), item.getSpouseName());
			}
		}
		
		return list;
	}
	
	public String syncFileName(String encode) throws Exception {
		File folder = new File(cicResultFileDir);
		File[] listOfFiles = folder.listFiles();
		Long count = 0L;
		for (File f : listOfFiles) {
			System.out.println(++count + ": change file name: " + f.getName() +  " to: " + URLEncoder.encode(f.getName(), "UTF-8"));
			if ("1".equals(encode))
				f.renameTo(new File(cicResultFileDir + URLEncoder.encode(f.getName(), "UTF-8")));
			else
				f.renameTo(new File(cicResultFileDir + URLDecoder.decode(f.getName(), "UTF-8")));
		}
		
		return "SUCCESS";
	}
	
	/**
	 * Send email notification cic result to user create request from web portal
	 * @author catld.ho
	 * @param cic
	 * @param sendTo
	 * @throws Exception
	 */
	public void sendEmail(CICDetailDTO cic, String sendTo) throws Exception {
		if (StringUtils.isNullOrEmpty(sendTo))
			throw new Exception("Cic send email: dest email empty");
		
		sendTo += MCREDIT_MAIL_DOMAIN;
		
		String email = parameter.findParamValueAsString(ParametersName.CIC_NOTIFICATIONS_EMAIL);
		String username = parameter.findParamValueAsString(ParametersName.CIC_NOTIFICATIONS_EMAIL_SMTP_USER);
		String password = parameter.findParamValueAsString(ParametersName.CIC_NOTIFICATIONS_EMAIL_SMTP_PASS);
		String esbHost = parameter.findParamValueAsString(ParametersName.ESB_HOST);
		
		if (StringUtils.isNullOrEmpty(email) || StringUtils.isNullOrEmpty(username) || StringUtils.isNullOrEmpty(password))
			throw new Exception("Cic Notification mail info not found");
		
		// get mail template
		NotificationTemplate notiTemplate = this._uok.common.getNotificationTemplateRepository().findByCode(TemplateEnum.MAIL_CIC_NOTI.toString());
		
		if (null == notiTemplate)
			throw new Exception("Cic Notification mail template not found");
		
		String mailSubject = MessageFormat.format(notiTemplate.getNotificationName(), cic.getCustomerName(), cic.getIdentifier());
		String mailContent = MessageFormat.format(notiTemplate.getNotificationTemplate(), cic.getCustomerName(), cic.getIdentifier(), cic.getDescription(), esbHost + MessageFormat.format(CIC_DOWNLOAD_IMAGE_LINK, cic.getIdentifier()), cic.getLastUpdateTime(), cic.getStatus());
		
		SendEmailDTO send = new SendEmailDTO(username, password, email, new ArrayList<String>(Arrays.asList(sendTo)), null, null, mailSubject, mailContent);
		new com.mcredit.sharedbiz.service.MessageService(send).send();
	}
	
	/**
	 * Scheduler job scan and approved leadgen data if pass cic result
	 * @return num of leadgen data approved
	 */
	public ApproveLeadgenDataResult jobApproveLeadgenData() throws Exception {
		Long numOfData = 0L, approved = 0L,  disapproved = 0L;
		
		// lay tat ca leadgen data dang o trang thai approving
		List<UplCustomer> listUplCustomer = this._uok.telesale.uplCustomerRepo().getListDataWaitForApprove();
		Map<Long, Long>	importIncre = new ConcurrentHashMap<Long, Long>();	// <upl_detail_id, num of lead data actived> : danh sach so luong lead data duoc active cua tung upl_detail
		List<UplCustomerHistory> listCompletedData = new ArrayList<>();		// danh sach lead data duoc phe duyet (approved/ disapproved)
		
		if (null != listUplCustomer && listUplCustomer.size() > 0) {
			numOfData = (long) listUplCustomer.size();
			UplCustomerHistory uch = null;
			CICDetailDTO cicResult = null;
			
			for (UplCustomer uc : listUplCustomer) {
				Boolean checked = null;
				uch = null;
				cicResult = null;
				
				if (StringUtils.isNullOrEmpty(uc.getIdentityNumber())) {
					// Leadgen data khong co thong tin cmnd mac dinh pass CIC
					cicResult = new CICDetailDTO();
					cicResult.setDescription("Không có thông tin");
					checked = true;
				} else {
					// kiem tra ket qua cic. Neu TH request chua duoc tao thi tao request check cic
					cicResult = getCICResult(uc.getIdentityNumber(), StringUtils.isNullOrEmpty(uc.getCustomerName())?"No Name" : uc.getCustomerName(), "Leadgen", null);
					if (null == cicResult || !CICResultStatus.SUCCESS.value().equals(cicResult.getStatus()))  // TH chua co ket qua cic,  de lan chay job sau kiem tra lai
						continue;
					
					checked = Converter.getCICStatusForThirdParty(cicResult.getCicResult());
				}
				
				if (null == checked) 	// chua co ket qua cic, de lan sau kiem tra lai
					continue;
				
				if (checked) {		// pass cic
					approved++;
					uc.setUplDetailId(Long.valueOf(uc.getUdf08())); // cap nhat lai upl detail id
					uc.setUdf08(null);
					uc.setResponseCode(LeadGenEnums.OK.value());
					this._uok.telesale.uplCustomerRepo().upsert(uc);
					
					// cap nhat lai upl customer history
					uch = this._uok.telesale.uplCustomerHistoryRepo().getBy(uc.getId(), uc.getRefId());
					if (null == uch) {
						System.out.println("Approve leadgen data error UplCustomerHistory null with uplCustomerId: " + uc.getId());
						// tao lai history
						uch = new UplCustomerHistory(uc.getId(), 0L, uc.getRefId(), LeadGenEnums.SUCCESS.value(),  null);
					}
					
					uch.setResponseCode(LeadGenEnums.SUCCESS.value());
					uch.setMessage(null==cicResult?null:cicResult.getDescription());
					this._uok.telesale.uplCustomerHistoryRepo().saveOrUpdate(uch);
					
					if (importIncre.get(uc.getUplDetailId()) == null)
						importIncre.put(uc.getUplDetailId(), 1L);
					else 
						importIncre.put(uc.getUplDetailId(), importIncre.get(uc.getUplDetailId()) + 1);
					
					listCompletedData.add(uch);
				} else {		// not pass cic
					disapproved++;
					
					// giu nguyen upl detail id bang 0 voi lead reject
					uc.setUdf08(null);
					uc.setResponseCode(LeadGenEnums.REJECT.value());
					uc.setMinScore(-1L);
					uc.setMaxScore(-1L);
					uc.setProductId(-1L);
					this._uok.telesale.uplCustomerRepo().upsert(uc);
					
					// cap nhat lai upl customer history
					uch = this._uok.telesale.uplCustomerHistoryRepo().getBy(uc.getId(), uc.getRefId());
					if (null == uch) {
						System.out.println("Dis Approve leadgen data error UplCustomerHistory null with uplCustomerId: " + uc.getId());
						// tao lai history
						uch = new UplCustomerHistory(uc.getId(), 0L, uc.getRefId(), LeadGenEnums.REJECT.value(),  "");
					}
					
					uch.setResponseCode(LeadGenEnums.REJECT.value());
					uch.setMessage(null==cicResult?null:cicResult.getDescription());
					this._uok.telesale.uplCustomerHistoryRepo().saveOrUpdate(uch);
					
					listCompletedData.add(uch);
				}
			}
		}
		
		// cap nhat lai so luong imported lead data
		for (Map.Entry<Long, Long> entry : importIncre.entrySet()) {
			
			UplDetail uplDetail = this._uok.telesale.uplDetailRepo().getUplDetailbyId(entry.getKey());
			if (null == uplDetail) {
				System.out.println("400. Get upl detail to increment imported null: " + entry.getKey());
				continue;
			}
			
			int resultUpdateUplMaster = this._uok.telesale.uplCustomerRepo()
					.increaseImportedUplMaster(entry.getValue(), uplDetail.getUplMasterId());

			if (resultUpdateUplMaster > 0) {
				int resultUpdateUplDetail = this._uok.telesale.uplCustomerRepo()
						.increaseImportedUplDetail(entry.getValue(), uplDetail.getId());
				if (resultUpdateUplDetail > 0) {
					System.out.println("200. incre imported uplmaster, upldetail: " + uplDetail.getUplMasterId() + ":" + uplDetail.getId() +  " by : " + entry.getValue() + " success");
					
					// Kiem tra neu trang thai phan bo hien tai la A (Allocated All) thi chuyen thanh P (Partial Allocated) de co the phan bo tiep
					if ("A".equalsIgnoreCase(uplDetail.getStatus())) 
						this._uok.telesale.uplCustomerRepo().changeUplDetailStatusToPartialAllocated(uplDetail.getId());
				} else 
					System.out.println("402. Can not incre imported uplDetail: " + uplDetail.getId());
			} else {
				System.out.println("401. Can not incre imported uplMaster: " + uplDetail.getUplMasterId());
			}
		}
		
		// gui report lead data den third party (TS)
		// thuc hien cuoi cung tranh viec gui sang doi tac xong ma xu ly noi bo co loi. 
		String bankCode = parameter.findParamValueAsString(ParametersName.TS_LEAD_REPORT_BANK_CODE);
		String bankApiKey = parameter.findParamValueAsString(ParametersName.TS_LEAD_REPORT_BANK_API_KEY);
		
		for (UplCustomerHistory uch : listCompletedData) {
			String partnerName = getThirdPartyNameByUplHistory(uch);
			if(LeadGenEnums.PARTNER_LEADGEN.equals(partnerName) || LeadGenEnums.PARTNER_TSNTB.equals(partnerName)){
				try {
					System.out.println("Send report to TS: uplCustomerId=" + uch.getUplCustomerId() + ", refId=" + uch.getRefId() + ", responseCode=" + uch.getResponseCode() + ", message=" + uch.getMessage());
					// call esb send report to third party
					UplCustomerHistory uchReport = null;
					String status = LeadGenEnums.SUCCESS.value().equals(uch.getResponseCode())?LeadDataTsReportStatus.APPROVED.value():LeadDataTsReportStatus.DISAPPROVED.value();
					
					LeadDataTSReportDTO report = new LeadDataTSReportDTO(uch.getRefId(), bankCode, bankApiKey, status, uch.getMessage());
					
					ApiResult result = esbApi.sendLeadgenReport(report);
					if (null == result || !result.getStatus()) {
						System.out.println("Response report lead data to 3th null or status error: uplCustomerId=" + uch.getUplCustomerId() + ", status=" + (null==result?"null":result.getCode()));
						uchReport = new UplCustomerHistory(uch.getUplCustomerId(), uch.getUplMasterId(), ConstantTelesale.CUS_HIS_REFID_REPORT, "Fail", "Send report to 3th fail");
						this._uok.telesale.uplCustomerHistoryRepo().add(uchReport);
						continue;
					}
					
					LeadDataTSReportResp resp = JSONConverter.toObject(result.getBodyContent(),  LeadDataTSReportResp.class);
					if (null == resp) {
						System.out.println("Response report lead data to 3th unexpect: uplCustomerId=" + uch.getUplCustomerId() + ", resp=" + result.getBodyContent());
						uchReport = new UplCustomerHistory(uch.getUplCustomerId(), uch.getUplMasterId(), ConstantTelesale.CUS_HIS_REFID_REPORT, "Fail", "Response content unexpect");
						this._uok.telesale.uplCustomerHistoryRepo().add(uchReport);
						continue;
					}
					
					uchReport = new UplCustomerHistory(uch.getUplCustomerId(), uch.getUplMasterId(), ConstantTelesale.CUS_HIS_REFID_REPORT, resp.getStatus(), resp.getMessage());
					this._uok.telesale.uplCustomerHistoryRepo().add(uchReport);
					
				} catch (Exception e) {
					System.out.println("Send report lead data to 3th error uplCustomerId: " + uch.getUplCustomerId() + " : " + e.getMessage());
					UplCustomerHistory uchError = new UplCustomerHistory(uch.getUplCustomerId(), uch.getUplMasterId(), ConstantTelesale.CUS_HIS_REFID_REPORT, "Exception", "Send report to 3th exception");
					this._uok.telesale.uplCustomerHistoryRepo().add(uchError);
				}
			} else if (LeadGenEnums.PARTNER_CICLG.value().equals(partnerName)) {
				try {
					System.out.println("Send qualification to CIC: uplCustomerId=" + uch.getUplCustomerId() + ", refId="
							+ uch.getRefId() + ", responseCode=" + uch.getResponseCode() + ", message="
							+ uch.getMessage());
					// call esb send update qualify status to CIC
					UplCustomerHistory uchReport = null;
					boolean isPassed = LeadGenEnums.SUCCESS.value().equals(uch.getResponseCode());
					String randomUUIDString = UUID.randomUUID().toString();

					CICUpdateQualifySttDTO report = new CICUpdateQualifySttDTO();
					report.setRequestId(randomUUIDString);
					report.setLeadId(uch.getRefId());
					report.setQualifyTime(
							DateUtil.toDateString(new Date(), DateTimeFormat.yyyyMMddHHmmss.getDescription()));
					report.setQualifyStatus(isPassed ? QUALIFY_STATUS_PASSED : QUALIFY_STATUS_FAILED);
					report.setQualifyRejectCode(
							isPassed ? QUALIFY_REJECT_CODE_PASSED : QUALIFY_REJECT_CODE_FAILED);
					report.setQualifyReason(isPassed ? QUALIFY_REASON_PASSED : QUALIFY_REASON_FAILED);

					ApiResult result = esbApi.updateLeadQualifyStatus(report);
					if (null == result || !result.getStatus()) {
						System.out.println(
								"Response update lead's qualification to 3th null or status error: uplCustomerId="
										+ uch.getUplCustomerId() + ", status="
										+ (null == result ? "null" : result.getCode()) + ", requestId= "
										+ randomUUIDString);
						uchReport = new UplCustomerHistory(uch.getUplCustomerId(), uch.getUplMasterId(),
								ConstantTelesale.CUS_HIS_UPDATE_QUALIFY, "failed",
								"Send lead's qualification to 3th fail");
						this._uok.telesale.uplCustomerHistoryRepo().add(uchReport);
						continue;
					}

					ESBResponseResultDTO resp = JSONConverter.toObject(result.getBodyContent(),
							ESBResponseResultDTO.class);
					if (null == resp) {
						System.out.println("Response update lead's qualification to 3th unexpect: uplCustomerId="
								+ uch.getUplCustomerId() + ", requestId= " + randomUUIDString + ", resp="
								+ result.getBodyContent());
						uchReport = new UplCustomerHistory(uch.getUplCustomerId(), uch.getUplMasterId(),
								ConstantTelesale.CUS_HIS_UPDATE_QUALIFY, "failed", "Response content unexpect");
						this._uok.telesale.uplCustomerHistoryRepo().add(uchReport);
						continue;
					}
					
					System.out.println("Response update lead's qualification to 3th unexpect: returnCode="
							+ resp.getResult().getReturnCode() + ", returnMes= " + resp.getResult().getReturnMes());
					uchReport = new UplCustomerHistory(uch.getUplCustomerId(), uch.getUplMasterId(),
							ConstantTelesale.CUS_HIS_UPDATE_QUALIFY, resp.getResult().getReturnCode().equals(RESULT_CIC_SUCCESS) ? "success" : "failed",
							randomUUIDString + "@@" + resp.getResult().getReturnMes());
					
					this._uok.telesale.uplCustomerHistoryRepo().add(uchReport);

				} catch (Exception e) {
					System.out.println("Send qualification to 3th error uplCustomerId: " + uch.getUplCustomerId()
							+ " : " + e.getMessage());
					UplCustomerHistory uchError = new UplCustomerHistory(uch.getUplCustomerId(), uch.getUplMasterId(),
							ConstantTelesale.CUS_HIS_REFID_REPORT, "Exception", "Send report to 3th exception");
					this._uok.telesale.uplCustomerHistoryRepo().add(uchError);
				}
			}

		}
		
		return new ApproveLeadgenDataResult(numOfData, approved, disapproved);
	}
	
	/**
	 * Report ket qua cic khong dung
	 * Tao lai request
	 * @param citizenID
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public Object reportCICResult(String citizenID, String userName) throws Exception {
		// lay thong tin hien tai
		CreditBureauData cicDetail = _uok.pcb.creditBureauDataRepository().getCICResponse(citizenID);
		if (null == cicDetail)
			throw new Exception("CIC result not found " + citizenID);
		
		// Kiem tra xem co request cho cmnd nay dang duoc xu ly khong
		List<MessageLog> ml = _uok.common.messageLogRepo().getMessageBy(MfsMsgTransType.MSG_TRANS_TYPE_MFS_CHECK_CIC_MANUAL.value(), cicDetail.getId(), 
				new String[] { CICRequestState.NEW.value(), CICRequestState.ASSIGN.value() });
		if (null != ml && ml.size() > 0)
			return new Result(STATUS_CODE_400, "Request is checking");
		
		// Tao lai request moi de cap nhat lai ket qua
		if (StringUtils.isNullOrEmpty(cicDetail.getCbDataDetail()))
			throw new Exception("CIC result data null " + citizenID);
		
		CICDetailDTO cicChecked = JSONConverter.toObject(cicDetail.getCbDataDetail(), CICDetailDTO.class);
		if (null == cicChecked)
			throw new Exception("CIC result extract fail " + citizenID);
		
		Integer msgOrder = Converter.getCICRequestSource(cicChecked.getCicResult());
		MessageLog messageLog = Converter.getMessageLogObject(citizenID, cicDetail.getId(), userName, msgOrder);
		_uok.common.messageLogRepo().upsert(messageLog);
		if (messageLog == null || messageLog.getId() == null)
			throw new Exception(Messages.getString("report insert new cic request to MessageLog fail"));
		
		return new Result(STATUS_CODE_OK, "Success");
	}
	
	public String getThirdPartyNameByUplHistory(UplCustomerHistory uplCustomerHistory){
		UplMaster uplMaster = _uok.telesale.uplMasterRepo().findUplMasterbyID(uplCustomerHistory.getUplMasterId());
		String uplCode = uplMaster.getUplCode();
		if(uplCode.contains(LeadGenEnums.PREFIX_LEADGEN.value())){
			return LeadGenEnums.PARTNER_LEADGEN.value();
		} else if (uplCode.contains(LeadGenEnums.PREFIX_TS.value())){
			return LeadGenEnums.PARTNER_TSNTB.value();
		} else if(uplCode.contains(LeadGenEnums.PREFIX_CIC.value())){
			return LeadGenEnums.PARTNER_CICLG.value();
		}
		return "";
	}

	
}
