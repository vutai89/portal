package com.mcredit.business.mobile.converter;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.mobile.UnitOfWorkMobile;
import com.mcredit.data.mobile.entity.ExternalUserMapping;
import com.mcredit.data.mobile.entity.UplCreditAppFiles;
import com.mcredit.data.mobile.entity.UplCreditAppRequest;
import com.mcredit.data.user.entity.Employee;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.MessageLogStatus;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.UplCreAppFileStatus;
import com.mcredit.model.object.mobile.UplCreditAppFilesDTO;
import com.mcredit.model.object.mobile.UploadDocumentDTO;
import com.mcredit.model.object.mobile.dto.AbortCaseDTO;
import com.mcredit.model.object.mobile.dto.CategoryDTO;
import com.mcredit.model.object.mobile.dto.CreCaseBPMDTO;
import com.mcredit.model.object.mobile.dto.InfoCreCaseDTO;
import com.mcredit.model.object.mobile.enums.MfsMsgTransType;
import com.mcredit.model.object.mobile.enums.MfsServiceName;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.cache.CompanyCacheManager;
import com.mcredit.sharedbiz.cache.ProductCacheManager;
import com.mcredit.sharedbiz.dto.UserDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;

public class Converter {
	private static final String source = "/";
	
	public static MessageLog convertRouteToMessageLog(UplCreditAppRequest uplCreditAppRequest) {
		MessageLog msgLog = new MessageLog();
		Date dateLog = new Date();
		msgLog.setRelationId(uplCreditAppRequest.getAppId());
		msgLog.setTransId(uplCreditAppRequest.getId());
		msgLog.setTransType(MfsMsgTransType.MSG_TRANS_TYPE_ROUTE_CASE_BPM.value());
		msgLog.setFromChannel(BusinessConstant.MCP);
		msgLog.setToChannel(BusinessConstant.BPM);
		msgLog.setRequestTime(new Timestamp(dateLog.getTime()));
		msgLog.setServiceName(MfsServiceName.SERVICE_ROUTE_CASE_BPM.value());
		msgLog.setMsgStatus(MessageLogStatus.NEW.value());
		return msgLog;
	}
	
	public static MessageLog convertCancelToMessageLog(UplCreditAppRequest upl) throws NumberFormatException, ValidationException {
		MessageLog msgLog = new MessageLog();
		AbortCaseDTO abortCaseDTO = new AbortCaseDTO(1, CodeTableCacheManager.getInstance()
				.getbyID(Integer.parseInt(upl.getAbortReason().toString())).getDescription1(),upl.getAbortComment());
		msgLog.setRelationId(upl.getAppId());
		msgLog.setTransId(upl.getId());
		msgLog.setTransType(MfsMsgTransType.MSG_TRANS_TYPE_ABORT_CASE_BPM.value());
		msgLog.setFromChannel(BusinessConstant.MCP);
		msgLog.setToChannel(BusinessConstant.BPM);
		msgLog.setRequestTime(new Timestamp((new Date()).getTime()));
		msgLog.setServiceName(MfsServiceName.SERVICE_ABORT_CASE_BPM.value());
		msgLog.setMsgRequest(JSONConverter.toJSON(abortCaseDTO).toString());
		msgLog.setMsgStatus(MessageLogStatus.NEW.value());
		return msgLog;
	}
	
	public static MessageLog convertFrom(UploadDocumentDTO upload, Long id, UserDTO _user, UnitOfWorkMobile _unitOfWorkMobile) throws ParseException {
		MessageLog message = new MessageLog();
		ExternalUserMapping externalUserMapping = _unitOfWorkMobile.externalUserMappingRepo()
				.getEUMappingByEmpId("" + upload.getRequest().getSaleId());
		
		message.setRelationId(String.valueOf(_user.getId()) + "-" + String.valueOf(externalUserMapping.getId()) + "-" +String.valueOf(id));
		message.setTransId(id);
		message.setTransType(MfsMsgTransType.MSG_TRANS_TYPE_CREATE_CASE_BPM.value());
		message.setFromChannel(BusinessConstant.MCP);
		message.setToChannel(BusinessConstant.BPM);
		message.setRequestTime(new Timestamp(new Date().getTime()));
		message.setMsgStatus(MessageLogStatus.NEW.value()); // new case message log
		message.setServiceName(MfsServiceName.SERVICE_CREATE_CASE_BPM.value());
		
		// set message_req
		String proUid = CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_CREATE_CASE_PRO_UID);
		String tasUid = CacheManager.Parameters().findParamValueAsString(ParametersName.MFS_CREATE_CASE_TAS_UID);
		Gson gson = new GsonBuilder().create();
		CreCaseBPMDTO creCaseBPMDTO = new CreCaseBPMDTO();
		creCaseBPMDTO.setPro_uid(proUid);
		creCaseBPMDTO.setTas_uid(tasUid);
		List<InfoCreCaseDTO> variables = new ArrayList<>();
		InfoCreCaseDTO info = new InfoCreCaseDTO();
		if ("1".equals(CacheManager.Product().findProductById(upload.getRequest().getProductId()).getIsCheckCat())) {
			info.setCat_result_company_name(CacheManager.CompanyCache().checkCategoryById(upload.getRequest().getCompanyTaxNumber()).getCompName());
			info.setCompanyTaxNumber(upload.getRequest().getCompanyTaxNumber());
		} else {
			info.setCat_result_company_name("");
			info.setCompanyTaxNumber("");
		}
		Date datecreate = upload.getRequest().getCreatedDate();
		Integer year = datecreate.getYear()+1900;
        Integer month = datecreate.getMonth()+1;
        Integer date = datecreate.getDate();
        String monStr = month + "";
        if(month <10) monStr = "0" + month;
		String filePath = CacheManager.Parameters()
				.findParamValueAsString(ParametersName.MFS_FTP_ROOT_FOLDER)
				+ source + year + source +monStr + source + date + source +upload.getRequest().getCitizenId()+ source;
		info.setFilesPath(filePath);
		info.setCat_result_type("");
		info.setCitizenID_sale(upload.getRequest().getCitizenId());
		info.setCreateFromMobile("1");
		info.setCustomerName_sale(upload.getRequest().getCustomerName());
		info.setHasInsurrance_sale(upload.getRequest().getHasInsurance());
		info.setIssueDateCitizenID_sale(new SimpleDateFormat("yyyy-MM-dd").format(new SimpleDateFormat("dd/MM/yyyy").parse(upload.getMobileIssueDateCitizen())));
		info.setLoanAmount_sale(upload.getRequest().getLoanAmount());
		info.setLoanTenor_sale(upload.getRequest().getLoanTenor());
		info.setProductSchemesFull("");
		info.setSaleCode(upload.getRequest().getSaleCode());
		info.setSaleCode_label(upload.getRequest().getSaleCode());
		info.setSaleID(externalUserMapping.getUserUid());
		
		// get Sale Mobile
		String phoneNumber = _unitOfWorkMobile.employeeRepo().getSaleMobile(_user.getEmpId());
		if (null == phoneNumber) {
			info.setSaleMobile("");
		} else {
			info.setSaleMobile(phoneNumber);			
		}
		Employee employee = _unitOfWorkMobile.employeeRepo().findById(upload.getRequest().getSaleId());
		String saleMobile = employee != null ? employee.getMobilePhone() : "";
		String saleName = employee != null ? employee.getFullName() : "";
		info.setSaleMobile(saleMobile);
		info.setSaleName(saleName);
		info.setSchemeProduct_sale(CacheManager.Product().findProductById(upload.getRequest().getProductId()).getProductCode());
		CodeTableDTO ctDTO = CodeTableCacheManager.getInstance().getIdByCategoryCodeValue(CTCat.TRAN_OFF.value(), upload.getRequest().getShopCode());
		info.setSignContractAddress(ctDTO == null ? "" : ctDTO.getDescription2());
		info.setSignContractAt(upload.getRequest().getShopCode());
		info.setStartUserName(upload.getRequest().getCreatedBy());
		info.setTemporaryResidence_sale(upload.getRequest().getTempResidence());
		variables.add(info);
		creCaseBPMDTO.setVariables(variables);
		message.setMsgRequest(JSONConverter.toJSON(creCaseBPMDTO));
		
		return message;
	}
	
	public static UplCreditAppFiles convertFrom(ModelMapper modelMapper, UplCreditAppFilesDTO appFiles, HashMap<Long, Long> documentList, Long id, String pathFile, UserDTO _user) {
		UplCreditAppFiles uplFiles = new UplCreditAppFiles();
		Long document_seq = 1L;
		Long documentId = appFiles.getDocumentId();
		if (documentList.containsKey(documentId)) {
			document_seq = documentList.get(appFiles.getDocumentId()) + 1;
			documentList.put(documentId, document_seq);
		} else {
			documentList.put(appFiles.getDocumentId(), document_seq);
		}
		appFiles.setCreatedBy(_user.getLoginId());
		appFiles.setCreatedDate(new Date());
		appFiles.setDocumentSeq(document_seq);
		appFiles.setFilePathServer(pathFile + File.separator + appFiles.getFileName());
		appFiles.setFileType("IMAGE");
		appFiles.setStatus(UplCreAppFileStatus.X.value()); // trang thai document tao moi
		appFiles.setVersion(1L);
		appFiles.setUplCreditAppId(id);
		uplFiles = modelMapper.map(appFiles, UplCreditAppFiles.class);
		return uplFiles;
	}
	
	public static UplCreditAppFiles converFrom(ModelMapper modelMapper, UplCreditAppFilesDTO appFiles, HashMap<Long, Long> documentList, UploadDocumentDTO upload, String pathFile, UserDTO _user, HashMap<Long, Long> lstReturnedCase) {
		UplCreditAppFiles uplFiles = new UplCreditAppFiles();
		Long document_seq = 1L;
		Long documentId = appFiles.getDocumentId();
		if (documentList.containsKey(documentId)) {
			document_seq = documentList.get(appFiles.getDocumentId()) + 1;
			documentList.put(documentId, document_seq);
		} else {
			documentList.put(appFiles.getDocumentId(), document_seq);
		}
		appFiles.setCreatedBy(_user.getLoginId());
		appFiles.setCreatedDate(new Date());
		appFiles.setDocumentSeq(document_seq);
		appFiles.setFilePathServer(pathFile + File.separator + appFiles.getFileName());
		appFiles.setFileType("IMAGE");
		appFiles.setStatus(UplCreAppFileStatus.X.value()); // trang thai document tao moi
		if (lstReturnedCase.containsKey(documentId)) {
			appFiles.setVersion(lstReturnedCase.get(documentId) + 1);
		} else {
			appFiles.setVersion(1L);
		}
		appFiles.setUplCreditAppId(upload.getRequest().getId());
		uplFiles = modelMapper.map(appFiles, UplCreditAppFiles.class);
		
		return uplFiles;
	}
 }
