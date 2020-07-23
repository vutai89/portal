package com.mcredit.business.mobile.aggregate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.mcredit.business.mobile.callout.EsbApi;
import com.mcredit.business.mobile.converter.Converter;
import com.mcredit.business.mobile.utils.FTPListner;
import com.mcredit.business.mobile.utils.FTPUtils;
import com.mcredit.business.mobile.utils.PDFUtils;
import com.mcredit.business.mobile.utils.ZipUtils;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.mobile.entity.ExternalUserMapping;
import com.mcredit.data.mobile.entity.UplCreditAppDocument;
import com.mcredit.data.mobile.entity.UplCreditAppFiles;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.model.dto.ProductDTO;
import com.mcredit.model.dto.ResponseSuccess;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.MessageLogStatus;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.TemplateEnum;
import com.mcredit.model.enums.UplCreAppFileStatus;
import com.mcredit.model.enums.UplCreAppRequestStatus;
import com.mcredit.model.enums.UplCreditAppDocumentStatus;
import com.mcredit.model.enums.ValidateCreateCase;
import com.mcredit.model.object.ApprovalReport;
import com.mcredit.model.object.DataReport;
import com.mcredit.model.object.mobile.UplCreditAppFilesDTO;
import com.mcredit.model.object.mobile.UploadDocumentDTO;
import com.mcredit.model.object.mobile.dto.CancelCaseDTO;
import com.mcredit.model.object.mobile.dto.CheckListDTO;
import com.mcredit.model.object.mobile.dto.DocumentTypeDTO;
import com.mcredit.model.object.mobile.dto.InfoMessForThirdPartyDTO;
import com.mcredit.model.object.mobile.dto.InforMessForSaleDTO;
import com.mcredit.model.object.mobile.dto.NoteDto;
import com.mcredit.model.object.mobile.dto.NotiForThirdPartyDTO;
import com.mcredit.model.object.mobile.dto.RACCaseResultDTO;
import com.mcredit.model.object.mobile.dto.ReturnedCaseDTO;
import com.mcredit.model.object.mobile.dto.SaleDTO;
import com.mcredit.model.object.mobile.dto.SeachFilterDto;
import com.mcredit.model.object.mobile.dto.UpdateNotificationIdDTO;
import com.mcredit.model.object.mobile.dto.UploadDocumentReturnDTO;
import com.mcredit.model.object.mobile.enums.MfsMsgTransType;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.cache.DocumentsCachManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class MobileAggregate {
	/**************** Property ***************/
	private UnitOfWork _uok = null;

	private static final String DASH = "-";
	private static final String ZERO = "0";
	private static final String EXTEND = ".pdf";
	private static final String SOURCE = "/";
	private ParametersCacheManager _cParam = CacheManager.Parameters();
	private SimpleDateFormat _sdf = new SimpleDateFormat("dd/MM/yyyy");
	private ModelMapper _modelMapper = new ModelMapper();
	private EsbApi _esbApi = null;
	private CodeTableCacheManager _ctCache = CacheManager.CodeTable();

	/**************** Constructor ***************/

	public MobileAggregate(UnitOfWork _uok) {
		this._uok = _uok;
		_esbApi = new EsbApi();
	}


	/**************** Public Methods ***************/
	/**
	 * @author sonhv.ho routeCaseBPM Job : fixedDelay = 10 minute
	 * @return RACCaseResultDTO RACCaseResultDTO.successCount: So luong ho so route
	 *         thanh cong RACCaseResultDTO.failCount: So luong ho so route that bai
	 *         RACCaseResultDTO.total: Tong so ho so route
	 * @throws Exception
	 */
	public RACCaseResultDTO routeCaseBPM() throws Exception {
		int failCount = 0;
		int successCount = 0;

		// Lay thong tin can route tu bang MessLog
		List<MessageLog> messLogList = this._uok.common.messageLogRepo().getMessageBy(
				MfsMsgTransType.MSG_TRANS_TYPE_ROUTE_CASE_BPM.value(),
				new String[] { MessageLogStatus.NEW.value(), MessageLogStatus.ERROR.value() });

		for (MessageLog mess : messLogList) {
			try {

				mess.setProcessTime(new Timestamp(new Date().getTime()));
				ApiResult apiResult = routeCaseBPM(mess);

				mess.setResponseTime(new Timestamp(new Date().getTime()));
				mess.setMsgResponse(apiResult.getBodyContent());
				mess.setResponseCode(String.valueOf(apiResult.getCode()));

				if (apiResult.getStatus()) {
					mess.setMsgStatus(MessageLogStatus.SUCCESS.value());
					this._uok.mobile.uplCreditAppRequestRepo().changeStatus(UplCreAppRequestStatus.C.value(),
							mess.getTransId());
					successCount++;
				} else {
					mess.setMsgStatus(MessageLogStatus.ERROR.value());
					failCount++;
				}

				this._uok.common.messageLogRepo().update(mess);
			} catch (Exception e) {
				e.printStackTrace();
				failCount++;
				continue;
			}
		}

		return new RACCaseResultDTO(successCount, failCount, messLogList.size());
	}

	/**
	 * @author sonhv.ho updateTokenMobile Job : fixedDelay = 24 hours
	 * @return ExternalUserMapping List ExternalUserMapping.accessToken
	 *         ExternalUserMapping.refreshToken
	 * @throws Exception
	 */
	public List<ExternalUserMapping> updateTokenMobile() throws Exception {
		ExternalUserMapping eum = this._uok.mobile.externalUserMappingRepo()
				.getById(1L);

			if (!isNullorEmpty(eum.getAccessToken()) && !isNullorEmpty(eum.getRefreshToken())) {
				try {
					refreshToken(eum);
				} catch (Exception e) {
					e.printStackTrace();
					authenEUM(eum);
				}
			} else {
				authenEUM(eum);
			}
		
		
		List<ExternalUserMapping> externalUserMappingList = this._uok.mobile.externalUserMappingRepo()
				.getExternalUserMappingList();
		
		for(ExternalUserMapping externalUserMapping : externalUserMappingList) {
			externalUserMapping.setAccessToken(eum.getAccessToken());
			externalUserMapping.setRefreshToken(eum.getRefreshToken());
			this._uok.mobile.externalUserMappingRepo().update(externalUserMapping);
		}

		return externalUserMappingList;
	}

	/**
	 * Get list case note
	 * 
	 * @author cuongvt.ho
	 * @editor dongtd.ho
	 * @param appNumber
	 *            cua ho so sinh ra tu BPM
	 * @return list case note cua ho so
	 * @throws Exception
	 */
	public Object getCaseNote(String appNumber, UserDTO userDTO) throws Exception {
		SaleDTO dto = this._uok.mobile.uplCreditAppRequestRepo().getSaleByAppNumber(appNumber);
		if (dto == null)
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.caseId.required")));
		if (!userDTO.getEmpId().equals(dto.getSaleId().toString()))
			throw new ValidationException(Messages.getString("mfs.dowload.permission.required"));

		return this._esbApi.getCaseNote(dto.getAppId());
	}

	/**
	 * Send case note
	 * 
	 * @author cuongvt.ho
	 * @editor dongtd.ho
	 * @param noteDto
	 *            bao gom appID cua ho so va noi dung cua note
	 * @return message success
	 * @throws Exception
	 */
	public ResponseSuccess sendCaseNote(NoteDto noteDto, UserDTO userDTO) throws Exception {
		SaleDTO dto = this._uok.mobile.uplCreditAppRequestRepo().getSaleByAppNumber(noteDto.getAppNumber());
		if (dto == null)
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.caseId.required")));
		if (!userDTO.getEmpId().equals(dto.getSaleId().toString()))
			throw new ValidationException(Messages.getString("mfs.dowload.permission.required"));

		ExternalUserMapping userMapping = this._uok.mobile.externalUserMappingRepo()
				.getEUMappingByEmpId(dto.getSaleId().toString());

		this._esbApi.addCaseNote(userMapping.getAccessToken(), dto.getAppId(), noteDto.getNoteContent());
		return new ResponseSuccess();
	}

	/**
	 * @author sonhv.ho createCaseToBPM Job : fixedDelay = 10 minute
	 * @return RACCaseResultDTO RACCaseResultDTO.successCount: So luong ho so create
	 *         thanh cong RACCaseResultDTO.failCount: So luong ho so create that bai
	 *         RACCaseResultDTO.total: Tong so ho so create
	 * @throws Exception
	 */
	public RACCaseResultDTO createCaseToBPM() throws Exception {
		int failCount = 0;
		int successCount = 0;

		// Lay thong tin can route tu bang MessLog
		List<MessageLog> messLogList = this._uok.common.messageLogRepo().getMessageBy(
				MfsMsgTransType.MSG_TRANS_TYPE_CREATE_CASE_BPM.value(),
				new String[] { MessageLogStatus.NEW.value(), MessageLogStatus.ERROR.value() });

		JSONObject jsonObj = null;
		for (MessageLog mess : messLogList) {
			try {
				mess.setProcessTime(new Timestamp(new Date().getTime()));
				ApiResult apiResult = createCaseToBPM(mess);
				mess.setMsgResponse(apiResult.getBodyContent());
				mess.setResponseTime(new Timestamp(new Date().getTime()));
				mess.setResponseCode(String.valueOf(apiResult.getCode()));

				if (apiResult.getStatus()) {
					mess.setMsgStatus(MessageLogStatus.SUCCESS.value());

					jsonObj = new JSONObject(apiResult.getBodyContent());
					this._uok.mobile.uplCreditAppRequestRepo().updateForCreateCaseBPM(UplCreAppRequestStatus.S.value(),
							jsonObj.getLong("app_number"), jsonObj.getString("app_uid"), mess.getTransId());

					successCount++;
				} else {
					mess.setMsgStatus(MessageLogStatus.ERROR.value());
					failCount++;
				}

				this._uok.common.messageLogRepo().update(mess);
			} catch (Exception e) {
				e.printStackTrace();
				failCount++;
				continue;
			}
		}

		return new RACCaseResultDTO(successCount, failCount, messLogList.size());

	}

	/**
	 * Lay danh sach checklist anh cua san pham
	 * 
	 * @author linhtt2.ho
	 * @editor dongtd.ho
	 * @param mobileSchemaProductCode:
	 *            ma san pham
	 * @param mobileTemResidence:
	 *            dia chi song trung voi ho khau
	 * @return danh sach checklist anh can thiet tuong ung
	 * @throws Exception
	 */
	public String checklist(String mobileSchemaProductCode, String mobileTemResidence) throws Exception {
		ApiResult resultApi = this._esbApi.getCheckList(mobileSchemaProductCode, mobileTemResidence,
				ParametersName.MFS_PROCESS_ID, ParametersName.MFS_TASK_ID);

		if (resultApi.getCode() != 200) {
			throw new ValidationException(Messages.getString("mfs.get.checklist.required"));			
		}

		return resultApi.getBodyContent().toString();
	}

	

	public void validateReturnCase(UploadDocumentDTO upload) throws ValidationException {

		BigDecimal res = this._uok.mobile.uplCreditAppRequestRepo().isValid(upload.getRequest().getId());
		if (null == res)
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.caseId.required")));

	}

	/**
	 * Tao moi ho so hoac cap nhat anh cho ho so tra ve
	 * 
	 * @author linhtt2.ho
	 * @param upload:
	 *            dinh dang thong tin ho so duoi dang class
	 * @param fileName:
	 *            ten file .zip
	 * @param _user:
	 *            thong tin NVKD tao moi hoac cap nhat anh ho so tra ve
	 * @return ket qua tao file anh thanh cong
	 * @throws Exception:
	 *             that bai khi xu ly tao moi hoac cap nhat lai ho so
	 */
	public UploadDocumentReturnDTO createCase(UploadDocumentDTO upload, String fileName, UserDTO _user)
			throws Exception {

		UploadDocumentReturnDTO res = new UploadDocumentReturnDTO(-1L);

		// unzip file
		String pathFile = "";
		if (upload.getInfo().size() > 0) {
			pathFile = ZipUtils.unzipNewFile(upload.getFilePath(), fileName, upload.getInfo());

			// convert document code to document Id
			upload = convertDocumentCodetoId(upload);
		}

		if (upload.getAppStatus() == ValidateCreateCase.NEW_CASE.intValue()) {

			// check duplicate
			checkDuplicate(upload);

			// create new in upl_credit_app_request
			Long id = insertCreditAppRequest(upload, _user);

			// update returned value
			res.setId(id);

			// create new in upl_credit_app_files
			insertCreditAppFiles(upload, id, pathFile, _user);

			// create message log request
			insertMessageLog(upload, id, _user);

		} else {

			if (upload.getInfo().size() > 0) { // co anh gui len
				// get max version with document_id respectively
				List<ReturnedCaseDTO> lstDTO = getMaxDocumentVersion(upload.getInfo(), upload.getRequest().getId());

				// insert new document to upl_credit_app_files
				insertReturnedAppFiles(upload, pathFile, _user, lstDTO);
			}

			// update credit app request
			updateCreditAppRequest(upload.getRequest().getId(), _user.getLoginId());

			// update returned value
			res.setId(upload.getRequest().getId());
		}

		return res;
	}

	public void validateCheckList(UploadDocumentDTO upload) throws Exception {
		ProductDTO product = CacheManager.Product().findProductById(upload.getRequest().getProductId());
		ObjectMapper mapper = new ObjectMapper();
		List<CheckListDTO> lstCheckList = mapper.readValue(
				checklist(product.getProductCode(), upload.getRequest().getTempResidence()),
				new TypeReference<List<CheckListDTO>>() {
				});

		upload.getRequest().setCheckList(JSONConverter.toJSON(lstCheckList));

		for (CheckListDTO ck : lstCheckList) {
			if (ck.getMandatory() == ValidateCreateCase.MANDATORY.intValue()) {
				boolean isValid = false;

				for (UplCreditAppFilesDTO upl : upload.getInfo()) {
					if (ck.getGroupId().equals(upl.getGroupId())) {
						isValid = true;
						break;
					}

					if (null != ck.getAlternateGroups() && !ck.getAlternateGroups().isEmpty()) {
						if (ck.getAlternateGroups().contains(upl.getGroupId())) {
							isValid = true;
							break;
						}
					}
				}

				if (!isValid)
					throw new ValidationException(Messages.getString("mfs.validation.image.false"));

			}
		}
	}

	

	/**
	 * Cancel Case: Huy case tra ve
	 * 
	 * @author linhtt2.ho
	 * @param ccDTO:
	 *            thong tin huy case: ma ho so, ma li do va li do huy case
	 * @param _user:
	 *            thong tin sale huy case
	 * @return ket qua tra case: thanh cong hoac that bai
	 * @throws ValidationException
	 */
	public ResponseSuccess cancelCase(CancelCaseDTO ccDTO, UserDTO _user) throws ValidationException {
		// get upl_credit_app_request tuong ung
		UplCreditAppRequest upl = this._uok.mobile.uplCreditAppRequestRepo().getCancelCase(ccDTO.getId(),
				_user.getEmpCode());

		if (null == upl)
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.get.upload.caseId.required")));

		if (ccDTO.getReason().equals(ValidateCreateCase.MFS_CUST_NOT_NEED_BORROW.longValue()))
			upl.setAbortReason(_ctCache.getIdBy(CTCodeValue1.MFS_CUST_NOT_NEED_BORROW, CTCat.MFS_ABORT_REASON));
		else
			upl.setAbortReason(_ctCache.getIdBy(CTCodeValue1.MFS_OTHER_REASON, CTCat.MFS_ABORT_REASON));

		upl.setAbortComment(ccDTO.getComment());
		upl.setStatus(UplCreAppRequestStatus.A.value());
		this._uok.mobile.uplCreditAppRequestRepo().update(upl);
		MessageLog msgLog = Converter.convertCancelToMessageLog(upl);
		this._uok.common.messageLogRepo().add(msgLog);

		return new ResponseSuccess();
	}

	/**
	 * Function sync file to FTP
	 * 
	 * @author cuongvt.ho
	 * @return RACCaseResultDTO RACCaseResultDTO.successCount: So luong case sync
	 *         thanh cong. RACCaseResultDTO.failCount: So case sync that bai.
	 *         RACCaseResultDTO.total: Tong so case can sync.
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	public RACCaseResultDTO syncFileToFTP() throws Exception {
		List<UplCreditAppRequest> appRequests = this._uok.mobile.uplCreditAppRequestRepo().getCaseForSyncFile();
		RACCaseResultDTO result = new RACCaseResultDTO();

		int success = 0;
		int fail = 0;
		int tolal = 0;
		FTPClient client = FTPUtils.createFTPConnection();

		if (appRequests != null && appRequests.size() > 0) {
			tolal = appRequests.size();

			for (UplCreditAppRequest appRequest : appRequests) {
				try {

					List<UplCreditAppFiles> appFiles = this._uok.mobile.uplCreditAppFilesRepo()
							.getFileForSync(appRequest.getId());
					if (null == appFiles || appFiles.size() == 0) {
						if (appRequest.getStatus().equals(UplCreAppRequestStatus.I.value())) {
							appRequest.setStatus(UplCreAppRequestStatus.T.value());
							MessageLog msgLog = Converter.convertRouteToMessageLog(appRequest);
							this._uok.common.messageLogRepo().add(msgLog);
							success++;
						} else
							fail++;
						continue;
					}

					List<UplCreditAppDocument> listDoc = convertToPDF(appFiles);
					if (null == listDoc || listDoc.size() == 0) {
						fail++;
						continue;
					}

					String ftpDirectory = initFTPDiretory(appRequest, client);
					DocumentsCachManager docCache = CacheManager.DocumentCache();

					for (UplCreditAppDocument document : listDoc) {
						String documentCode = docCache.getDocumentCodeById(document.getDocumentId());
						DocumentTypeDTO docType = docCache.groupDocument(documentCode);
						String subFolder = docType.getSubFolder();

						try {
							client.changeDirectory(ftpDirectory + SOURCE + subFolder);
						} catch (Exception e) {
							client.createDirectory(ftpDirectory + SOURCE + subFolder);
							client.changeDirectory(ftpDirectory + SOURCE + subFolder);
						}

						String fileName = appRequest.getAppNumber() + DASH + docType.getDocTypeVn() + DASH
								+ appRequest.getCustomerName() + EXTEND;
						document.setRemotePathServer(ftpDirectory + SOURCE + subFolder + SOURCE + fileName);
						document.setUplCreditAppId(appRequest.getId());

						try {
							client.deleteFile(document.getRemotePathServer());
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {
							client.upload(fileName, new FileInputStream(document.getLocalPathServer()), 0, 0,
									new FTPListner());
							document.setStatus(UplCreditAppDocumentStatus.F.value());

							if (appRequest.getStatus().equals(UplCreAppRequestStatus.S.value()))
								appRequest.setStatus(UplCreAppRequestStatus.P.value());

							if (appRequest.getStatus().equals(UplCreAppRequestStatus.I.value()))
								appRequest.setStatus(UplCreAppRequestStatus.T.value());

							this._uok.mobile.uplCreditAppRequestRepo().upsert(appRequest);
							this._uok.mobile.creditAppDocumentRepo().upsert(document);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					MessageLog msgLog = Converter.convertRouteToMessageLog(appRequest);
					this._uok.common.messageLogRepo().add(msgLog);
					success++;

				} catch (Exception ex) {
					System.out.println(ex.getMessage());
					fail++;
				}
			}
			result.setSuccessCount(success);
			result.setFailCount(fail);
		} else {
			result.setSuccessCount(0);
			result.setFailCount(0);
		}

		result.setTotal(tolal);
		return result;
	}

	/**
	 * @author sonhv.ho sendNotificationForSale Job : fixedDelay = 5 minute
	 * @return RACCaseResultDTO RACCaseResultDTO.successCount: So luong gui thong
	 *         bao thanh cong. RACCaseResultDTO.failCount: So luong gui thong bao
	 *         that bai. RACCaseResultDTO.total: Tong so thong bao can gui di.
	 * @throws Exception
	 */
	public RACCaseResultDTO sendNotificationForSale() throws Exception {
		int successCount = 0;
		int failCount = 0;

		// Lay ra list messe can gui tu bang messe_log
		List<InforMessForSaleDTO> inforMessForSaleDTOList = this._uok.mobile.uplCreditAppRequestRepo()
				.findInfoMessForSale(MfsMsgTransType.MSG_TRANS_TYPE_MFS_NOTIFICATION.value(),
						new String[] { MessageLogStatus.NEW.value(), MessageLogStatus.ERROR.value() });
		for (InforMessForSaleDTO info : inforMessForSaleDTOList) {
			MessageLog messLog = this._uok.common.messageLogRepo().getMessageByMessId(info.getMessId());
			try {
				String mess = buildMessForSale(info);
				messLog.setProcessTime(new Timestamp(new Date().getTime()));
				ApiResult apiResult = this._esbApi.notify(info.getNotificationId(), "Thông báo", mess,
						info.getOsType());
				messLog.setResponseTime(new Timestamp(new Date().getTime()));
				messLog.setMsgResponse(apiResult.getBodyContent());
				messLog.setMsgRequest(mess);

				if (apiResult.getStatus()) {
					messLog.setMsgStatus(MessageLogStatus.SUCCESS.value());
					successCount++;
				} else {
					failCount++;
					messLog.setMsgStatus(MessageLogStatus.ERROR.value());
				}

				this._uok.common.messageLogRepo().update(messLog);

			} catch (Exception e) {
				failCount++;
				messLog.setMsgStatus(MessageLogStatus.ERROR.value());
				this._uok.common.messageLogRepo().update(messLog);
				e.printStackTrace();
				continue;
			}
		}

		return new RACCaseResultDTO(successCount, failCount, inforMessForSaleDTOList.size());
	}

	/**
	 * @author linhtt2.ho sendNotificationForThirdParty Job : fixedDelay = 5 minute
	 * @return RACCaseResultDTO RACCaseResultDTO.successCount: So luong gui thong
	 *         bao thanh cong. RACCaseResultDTO.failCount: So luong gui thong bao
	 *         that bai. RACCaseResultDTO.total: Tong so thong bao can gui di.
	 * @throws Exception
	 */
	public RACCaseResultDTO sendNotificationForThirdParty() throws Exception {
		int successCount = 0;
		int failCount = 0;

		// Lay ra list messe can gui tu bang messe_log
		List<InfoMessForThirdPartyDTO> inforMessForSaleDTOList = this._uok.mobile.uplCreditAppRequestRepo()
				.findInfoMessForThirdParty(MfsMsgTransType.MSG_TRANS_TYPE_MFS_NOTIFICATION.value(),
						new String[] { MessageLogStatus.NEW.value(), MessageLogStatus.ERROR.value() });
		for (InfoMessForThirdPartyDTO info : inforMessForSaleDTOList) {
			MessageLog messLog = this._uok.common.messageLogRepo().getMessageByMessId(info.getMessId());
			try {
				String currentStatus = buildMessForThirdParty(info);
				ApiResult apiResult = new ApiResult();
				messLog.setProcessTime(new Timestamp(new Date().getTime()));
				NotiForThirdPartyDTO noti = new NotiForThirdPartyDTO(messLog.getTransId(), currentStatus,
						info.getAppNumber(), info.getAppId());
				String mess = JSONConverter.toJSON(noti);
				messLog.setProcessTime(new Timestamp(new Date().getTime()));
				apiResult = this._esbApi.postThirdParty(info.getApi(), mess);
				messLog.setResponseTime(new Timestamp(new Date().getTime()));
				messLog.setMsgResponse(apiResult.getBodyContent());

				if (apiResult.getStatus()) {
					messLog.setMsgRequest(mess);
					messLog.setMsgStatus(MessageLogStatus.SUCCESS.value());
					successCount++;
				} else {
					failCount++;
					messLog.setMsgStatus(MessageLogStatus.ERROR.value());
				}

				this._uok.common.messageLogRepo().update(messLog);

			} catch (Exception e) {
				failCount++;
				messLog.setMsgStatus(MessageLogStatus.ERROR.value());
				this._uok.common.messageLogRepo().update(messLog);
				e.printStackTrace();
				continue;
			}
		}

		return new RACCaseResultDTO(successCount, failCount, inforMessForSaleDTOList.size());
	}

	/**
	 * Function huy case
	 * 
	 * @author cuongvt.ho
	 * @editor dongtd.ho
	 * @return RACCaseResultDTO RACCaseResultDTO.successCount: So luong case huy
	 *         thanh cong. RACCaseResultDTO.failCount: So case huy that bai.
	 *         RACCaseResultDTO.total: Tong so case can huy.
	 * @throws Exception
	 */
	public RACCaseResultDTO abortCase() throws Exception {
		RACCaseResultDTO result = new RACCaseResultDTO();
		int failCount = 0;
		int successCount = 0;
		List<MessageLog> messLogList = this._uok.common.messageLogRepo().getMessageBy(
				MfsMsgTransType.MSG_TRANS_TYPE_ABORT_CASE_BPM.value(),
				new String[] { MessageLogStatus.NEW.value(), MessageLogStatus.ERROR.value() });

		int totalCount = 0;
		if (messLogList != null && messLogList.size() > 0) {
			totalCount = messLogList.size();

			for (MessageLog log : messLogList) {
				try {
					log.setProcessTime(new Timestamp(new Date().getTime()));
					ApiResult apiResult = abortCase(log);
					if (apiResult.getStatus()) {

						ApiResult apiRoute = routeCaseBPM(log);

						log.setResponseTime(new Timestamp(new Date().getTime()));
						log.setMsgResponse(apiResult.getBodyContent());
						log.setResponseCode(String.valueOf(apiResult.getCode()));

						if (apiRoute.getStatus()) {
							log.setMsgStatus(MessageLogStatus.SUCCESS.value());
							this._uok.common.messageLogRepo().update(log);
							this._uok.mobile.uplCreditAppRequestRepo().changeLastUpdatedDate(log.getTransId());
							;
							successCount++;
						} else {
							log.setMsgStatus(MessageLogStatus.ERROR.value());
							this._uok.common.messageLogRepo().update(log);
							failCount++;
						}
					} else {
						log.setMsgResponse(apiResult.getBodyContent());
						log.setMsgStatus(MessageLogStatus.ERROR.value());
						log.setResponseTime(new Timestamp(new Date().getTime()));
						this._uok.common.messageLogRepo().update(log);
						failCount++;
					}
				} catch (Exception e) {
					failCount++;
					e.printStackTrace();
					continue;
				}
			}
		}
		result.setTotal(totalCount);
		result.setFailCount(failCount);
		result.setSuccessCount(successCount);
		return result;
	}

	/**
	 * Lấy danh sách báo cáo
	 * 
	 * @param dateExport
	 *            ngày chọn xem báo cao
	 * @param user
	 *            UserDTO của người dùng
	 * @return Danh sách báo cáo
	 * @throws Exception
	 */
	public List<DataReport> getReport(String dateExport, UserDTO user) throws Exception {
		if (StringUtils.isNullOrEmpty(dateExport))
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.report.dateExport")));
		return this._esbApi.getReport(dateExport, user);
	}

	/**
	 * Lấy danh sách báo cáo được phê duyệt
	 * 
	 * @param dateExport
	 *            ngày chọn xem báo cáo phê duyệt
	 * @param user
	 *            UserDTO của người dùng
	 * @return Danh sách báo cáo phê duỵet
	 * @throws Exception
	 */
	public List<ApprovalReport> getApprovalReport(String dateExport, UserDTO user) throws Exception {
		if (StringUtils.isNullOrEmpty(dateExport))
			throw new ValidationException(Messages.getString("validation.field.madatory",
					Labels.getString("label.mfs.get.report.dateExport")));
		return this._esbApi.getApprovalReport(dateExport, user);
	}
	
	/**
	 * update NotificationId
	 * 
	 * @author sonhv.ho
	 * @param 
	 * @return 1L
	 * @throws Exception
	 */
	public int updateNotificationId(UpdateNotificationIdDTO updateNotificationIdDTO, UserDTO _user) {
		return this._uok.mobile.usersProfilesRepo().updateNotifiCationId(updateNotificationIdDTO.getNotificationId(),
				_user.getId());
	}
	
	/************ Private Methods **************/

	private void checkDuplicate(UploadDocumentDTO upload) throws ValidationException {
		UplCreditAppRequest ucar = this._uok.mobile.uplCreditAppRequestRepo().checkDuplicate(upload);
		if (null != ucar) {
			throw new ValidationException(Messages.getString("validation.field.duplicate",
					Labels.getString("label.mfs.get.checklist.caseId.required")));
		}
	}
	
	private Long insertCreditAppRequest(UploadDocumentDTO upload, UserDTO _user)
			throws ParseException, ValidationException {
		updateInfoCreditAppRequest(upload, _user);
		UplCreditAppRequest upl = _modelMapper.map(upload.getRequest(), UplCreditAppRequest.class);
		Long id = this._uok.mobile.uplCreditAppRequestRepo().add(upl);

		return id;
	}

	private void updateInfoCreditAppRequest(UploadDocumentDTO upload, UserDTO _user)
			throws ParseException, ValidationException {
		// NEW
		if (_user.getUsrType().equals(ValidateCreateCase.THIRD_PARTY.value())) { // third party
			if (StringUtils.isNullOrEmpty(upload.getRequest().getSaleCode())) {
				throw new ValidationException(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.mfs.post.upload.saleCode.required")));
			}
		} else { // mobile day len
			upload.getRequest().setSaleCode(_user.getEmpCode());
		}
		upload.getRequest().setCreatedBy(_user.getLoginId());
		upload.getRequest().setLastUpdatedBy(_user.getLoginId());
		upload.getRequest().setCreatedDate(new Date());
		upload.getRequest().setStatus(UplCreAppRequestStatus.X.value());
		upload.getRequest().setIssueDateCitizen(_sdf.parse(upload.getMobileIssueDateCitizen()));

		// get Imei
		String imei = this._uok.mobile.usersProfilesRepo().getImei(_user.getLoginId());
		if (StringUtils.isNullOrEmpty(imei))
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.post.upload.imei.required")));

		upload.getRequest().setMobileImei(imei);

		// get Sale Id
		BigDecimal saleId = this._uok.mobile.employeeRepo().getSaleId(upload.getRequest().getSaleCode());
		if (null == saleId)
			throw new ValidationException(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.mfs.post.upload.saleId.required")));

		upload.getRequest().setSaleId(saleId.longValue());
	}

	private void insertCreditAppFiles(UploadDocumentDTO upload, Long id, String pathFile, UserDTO _user) {
		HashMap<Long, Long> documentList = new HashMap<>();
		for (UplCreditAppFilesDTO appFiles : upload.getInfo()) {
			UplCreditAppFiles uplFiles = Converter.convertFrom(_modelMapper, appFiles, documentList, id, pathFile,
					_user);
			this._uok.mobile.uplCreditAppFilesRepo().add(uplFiles);
		}
	}

	private void updateCreditAppRequest(Long mobileAppCode, String saleName) {
		this._uok.mobile.uplCreditAppRequestRepo().updateReturnedCase(mobileAppCode, saleName);
	}

	private List<ReturnedCaseDTO> getMaxDocumentVersion(List<UplCreditAppFilesDTO> list, Long id) {
		List<Long> lstDocument = new ArrayList<>();

		for (UplCreditAppFilesDTO doc : list) {
			if (!lstDocument.contains(doc.getDocumentId()))
				lstDocument.add(doc.getDocumentId());
		}
		return this._uok.mobile.uplCreditAppFilesRepo().getMaxDocumentVersion(lstDocument, id);
	}

	private void insertReturnedAppFiles(UploadDocumentDTO upload, String pathFile, UserDTO _user,
			List<ReturnedCaseDTO> lstDTO) {
		HashMap<Long, Long> documentList = new HashMap<>();
		HashMap<Long, Long> lstReturnedCase = new HashMap<>();

		for (ReturnedCaseDTO returnedCase : lstDTO)
			lstReturnedCase.put(returnedCase.getDocumentId(), returnedCase.getMaxVersion());

		for (UplCreditAppFilesDTO appFiles : upload.getInfo()) {
			UplCreditAppFiles uplFiles = Converter.converFrom(_modelMapper, appFiles, documentList, upload, pathFile,
					_user, lstReturnedCase);
			this._uok.mobile.uplCreditAppFilesRepo().add(uplFiles);
		}
	}
	
	private void insertMessageLog(UploadDocumentDTO upload, Long id, UserDTO _user) throws ParseException {
		MessageLog message = Converter.convertFrom(upload, id, _user, this._uok.mobile);
		this._uok.common.messageLogRepo().add(message);
	}

	private UploadDocumentDTO convertDocumentCodetoId(UploadDocumentDTO upload) {
		for (int i = 0; i < upload.getInfo().size(); i++) {
			upload.getInfo().get(i).setDocumentId(
					CacheManager.DocumentCache().getDocumentIdByCode(upload.getInfo().get(i).getDocumentCode()));
		}
		return upload;
	}

	private ApiResult abortCase(MessageLog mess) throws Exception {
		ExternalUserMapping externalUserMapping = this._uok.mobile.externalUserMappingRepo()
				.getEUMappingByUplId(mess.getTransId());
		return this._esbApi.abortCase(externalUserMapping.getAccessToken(), mess.getRelationId(), mess.getMsgRequest());
	}

	private List<UplCreditAppDocument> convertToPDF(List<UplCreditAppFiles> listCreditAppFiles) {
		try {
			String filepath = null;
			Map<String, List<UplCreditAppFiles>> lstFileGrouped = PDFUtils.groupCreditAppFiles(listCreditAppFiles);
			Set<String> lstFileGroupKey = lstFileGrouped.keySet();
			String[] docTypes = lstFileGroupKey.toArray(new String[lstFileGroupKey.size()]);

			if (docTypes.length < 1)
				return null;

			List<UplCreditAppDocument> PDFFileList = new ArrayList<UplCreditAppDocument>();

			for (String docType : docTypes) {
				List<UplCreditAppFiles> lstFileConvert = lstFileGrouped.get(docType);
				if (lstFileConvert.size() < 1)
					continue;

				Document document = new Document();
				try {
					String filename = new StringBuilder(
							docType + DASH + lstFileGrouped.get(docType).get(0).getUplCreditAppId()).append(EXTEND).toString();
					String createdBy = lstFileConvert.get(0).getCreatedBy();
					if (createdBy.contains("."))
						createdBy = createdBy.substring(0, createdBy.indexOf("."));
					String tmpPDFDir = _cParam.findParamValueAsString(ParametersName.MFS_TEMP_DIR)+ createdBy + SOURCE;
					File fileDir = null;
				    fileDir = new File(tmpPDFDir);
				    fileDir.mkdir();
					filepath = tmpPDFDir + filename;
					PdfWriter.getInstance(document, new FileOutputStream(filepath));
					document.open();

					for (UplCreditAppFiles item : lstFileConvert) {
						Image img = Image.getInstance(item.getFilePathServer());
						img.scalePercent(25f);
						Rectangle rec = new Rectangle(img.getPlainWidth(), img.getPlainHeight());
						document.setPageSize(rec);
						document.newPage();
						img.setAbsolutePosition(0, 0);
						document.add(img);
						item.setStatus(UplCreAppFileStatus.F.value());
						item.setLastUpdatedDate(new Date());
						this._uok.mobile.uplCreditAppFilesRepo().upsert(item);
					}

				} finally {
					document.close();
				}

				SeachFilterDto searchFilter = new SeachFilterDto();
				searchFilter.setId(lstFileConvert.get(0).getUplCreditAppId());
				searchFilter.setDocumentId(lstFileConvert.get(0).getDocumentId());
				List<UplCreditAppDocument> lstCheck = this._uok.mobile.creditAppDocumentRepo()
						.getListCreditAppDocument(searchFilter);
				UplCreditAppDocument PDFFile = new UplCreditAppDocument();
				if (lstCheck == null || lstCheck.size() < 1) {
					PDFFile.setDocumentId(lstFileConvert.get(0).getDocumentId());
					PDFFile.setVersion(1L);
				} else {
					Long maxVersion = lstCheck.stream().max(Comparator.comparing(UplCreditAppDocument::getVersion))
							.orElseThrow(NoSuchElementException::new).getVersion();
					PDFFile = lstCheck.get(0);
					PDFFile.setDocumentId(searchFilter.getDocumentId());
					PDFFile.setVersion(maxVersion + 1L);
				}
				PDFFile.setLocalPathServer(filepath);
				PDFFileList.add(PDFFile);
			}
			return PDFFileList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String buildMessForSale(InforMessForSaleDTO info) throws Exception {
		if (MessageLogStatus.NEW.value().equals(info.getMessStatus())) {
			String mess = CacheManager.NotificationTemplateCacheManager()
					.findByNotificationCode(TemplateEnum.MFS_CHANGE_STATUS.value()).getNotificationTemplate();
			Clob cl = info.getAppStatus();
			String str = "";
			if (cl != null) {
				str = cl.getSubString(1, (int) cl.length());
			}
			JSONArray jsonArr = new JSONArray(str);
			return MessageFormat.format(mess, info.getDocumentId().toString(), info.getCitizenId(),
					info.getCustomerName(), _ctCache.getbyID(jsonArr.getInt(0)).getDescription2());
		} else {
			return info.getAppStatus().getSubString(1, (int) info.getAppStatus().length());
		}
	}

	private String buildMessForThirdParty(InfoMessForThirdPartyDTO info) throws Exception {
		Clob cl = info.getAppStatus();
		String str = "";
		if (null != cl) {
			int status = Integer.valueOf(cl.getSubString(2, (int) cl.length()).split(",")[0]);
			str = CacheManager.CodeTable().getbyID(status).getDescription2();
		}
		return str;
	}

	/**
	 * @author sonhv.ho
	 * @editor dongtd.ho
	 * @return ApiResult
	 * @throws Exception
	 */
	private ApiResult routeCaseBPM(MessageLog mess) throws Exception {
		ExternalUserMapping userMapping = this._uok.mobile.externalUserMappingRepo()
				.getEUMappingByUplId(mess.getTransId());
		return this._esbApi.routeCaseBPM(userMapping.getAccessToken(), mess.getRelationId());
	}

	/**
	 * @author sonhv.ho
	 * @editor dongtd.ho
	 * @param MessageLog
	 *            Create case to BPM from MessageLog
	 * @return ApiResult
	 * @throws Exception
	 */
	private ApiResult createCaseToBPM(MessageLog mess) throws Exception {
		ExternalUserMapping externalUserMapping = this._uok.mobile.externalUserMappingRepo()
				.getEUMappingByUplId(mess.getTransId());
		return this._esbApi.createCase(externalUserMapping.getAccessToken(), mess.getMsgRequest());
	}

	/**
	 * @author sonhv.ho
	 * @editor dongtd.ho
	 * @param ExternalUserMapping
	 *            basic authen for ExternalUserMapping
	 * @return Boolean
	 * @throws Exception
	 */
	private Boolean authenEUM(ExternalUserMapping eum) throws Exception {
		ApiResult resultApi = this._esbApi.authorized(eum);

		JSONObject jsonObj = new JSONObject(resultApi.getBodyContent());
		eum.setAccessToken(jsonObj.getString("access_token"));
		eum.setRefreshToken(jsonObj.getString("refresh_token"));
		eum.setUpdatedDate(new Date());

		this._uok.mobile.externalUserMappingRepo().update(eum);
		return resultApi.getStatus();
	}

	/**
	 * @author sonhv.ho
	 * @editor dongtd.ho
	 * @param ExternalUserMapping
	 *            refresh token for ExternalUserMapping
	 * @return Boolean
	 * @throws Exception
	 */
	private Boolean refreshToken(ExternalUserMapping eum) throws Exception {
		ApiResult resultApi = this._esbApi.refreshToken(eum.getRefreshToken());

		JSONObject jsonObj = new JSONObject(resultApi.getBodyContent());
		eum.setAccessToken(jsonObj.getString("access_token"));
		eum.setRefreshToken(jsonObj.getString("refresh_token"));
		eum.setUpdatedDate(new Date());

		this._uok.mobile.externalUserMappingRepo().update(eum);

		return resultApi.getStatus();
	}

	private void changeDir(FTPClient client, String dir, int year, int month, int date, String citizenId)
			throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		try {
			client.changeDirectory(dir);
		} catch (Exception e) {
			client.createDirectory(dir);
			client.changeDirectory(dir);
		}
		try {
			client.changeDirectory(Integer.toString(year));
		} catch (Exception e) {
			client.createDirectory(Integer.toString(year));
			client.changeDirectory(Integer.toString(year));
		}
		try {
			if (month < 10) {
				client.changeDirectory(ZERO + month);
			} else {
				client.changeDirectory(Integer.toString(month));
			}
		} catch (Exception e) {
			if (month < 10) {
				client.createDirectory(ZERO + Integer.toString(month));
				client.changeDirectory(ZERO + Integer.toString(month));
			} else {
				client.createDirectory(Integer.toString(month));
				client.changeDirectory(Integer.toString(month));
			}
		}
		try {
			client.changeDirectory(Integer.toString(date));
		} catch (Exception e) {
			client.createDirectory(Integer.toString(date));
			client.changeDirectory(Integer.toString(date));
		}
		try {
			client.changeDirectory(citizenId);
		} catch (Exception e) {
			client.createDirectory(citizenId);
			client.changeDirectory(citizenId);
		}
	}

	private String initFTPDiretory(UplCreditAppRequest uplCreditAppRequest, FTPClient _client)
			throws IllegalStateException, IOException, FTPIllegalReplyException, FTPException {
		String ftpDirectory = null;
		Date datecreate = uplCreditAppRequest.getCreatedDate();
		Integer year = datecreate.getYear() + 1900;
		Integer month = datecreate.getMonth() + 1;
		Integer date = datecreate.getDate();
		String FTP_REMOTE_DIRECTORY = CacheManager.Parameters()
				.findParamValueAsString(ParametersName.MFS_FTP_ROOT_FOLDER);
		changeDir(_client, FTP_REMOTE_DIRECTORY, year, month, date, uplCreditAppRequest.getCitizenId());
		ftpDirectory = FTP_REMOTE_DIRECTORY + year + SOURCE;
		if (month < 10)
			ftpDirectory += ZERO + month + SOURCE + date + SOURCE + uplCreditAppRequest.getCitizenId();
		else
			ftpDirectory += month + SOURCE + date + SOURCE + uplCreditAppRequest.getCitizenId();

		return ftpDirectory;
	}

	private boolean isNullorEmpty(String input) {
		return com.mcredit.util.StringUtils.isNullOrEmpty(input);
	}

}
