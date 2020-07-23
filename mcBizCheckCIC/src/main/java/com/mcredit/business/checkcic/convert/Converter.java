package com.mcredit.business.checkcic.convert;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.pcb.entity.CreditBureauData;
import com.mcredit.model.dto.cic.CICDetailDTO;
import com.mcredit.model.dto.cic.CICDetailForBpmDTO;
import com.mcredit.model.dto.cic.CICRequestClaim;
import com.mcredit.model.enums.MsgType;
import com.mcredit.model.enums.cic.CICRequestSource;
import com.mcredit.model.enums.cic.CICRequestState;
import com.mcredit.model.enums.cic.TypeChanel;
import com.mcredit.model.object.mobile.enums.MfsMsgTransType;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class Converter {
	
	private static final String DEFAULT_CIC_SOURCE = "C";	// C: CIC; P: PCB
	private static final String DEFAULT_CIC_TYPE = "C";		// C: Customer
	private static final String DEFAULT_RECORD_STATUS = "A";

	/**
	 * Generate new CreditBureauData
	 * @author catld.ho
	 * @param custId : customer id (id table cust_personal_info)
	 * @param identity : cmnd/cccd/cmqd
	 * @param customerName
	 * @return CreditBureauData
	 */
	public static CreditBureauData getDefaultCICData(Long custId, String identity, String customerName) {
		CreditBureauData result = new CreditBureauData();
		CICDetailDTO cbDetail = new CICDetailDTO(identity, customerName);
		
		result.setRecordStatus(DEFAULT_RECORD_STATUS);
		result.setCustId(custId);
		result.setCbSource(DEFAULT_CIC_SOURCE);
		result.setCbType(DEFAULT_CIC_TYPE);
		result.setCustIdentityNumber(identity);
		result.setCbDataDetail(JSONConverter.toJSON(cbDetail));
		result.setCreatedDate(new Date());
		result.setLastUpdatedDate(new Date());
		
		return result;
	}
	
	/**
	 * Generate new MessageLog
	 * @author catld.ho
	 * @param identity : cmnd/cccd/cmqd
	 * @param cbdId : id of table CreditBureauData
	 * @param username
	 * @return  MessageLog
	 */
	public static MessageLog getMessageLogObject(String identity, Long cbdId, String username, Integer msgOrder) {
		MessageLog result = new MessageLog();
		
		result.setRelationId(identity);
		result.setMsgRequest(identity);
		result.setTransId(cbdId);
		result.setTransType(MfsMsgTransType.MSG_TRANS_TYPE_MFS_CHECK_CIC_MANUAL.value());
		result.setFromChannel(TypeChanel.MCP.value());
		result.setToChannel(TypeChanel.CIC.value());
		result.setRequestTime(new Timestamp((new Date()).getTime()));
		result.setMsgType(MsgType.REQUEST.value());
		result.setMsgStatus(CICRequestState.NEW.value());
		result.setServiceName(username);
		result.setMsgOrder(msgOrder);	// source sent request check cic (web portal == 1)
	
		return result;
	}
	
	/**
	 * Convert cic detail dto
	 * @param cicDes : dto dest
	 * @param cicSrc : dto source
	 * @param cicResult : cic result
	 * @throws ParseException
	 */
	public static void getCICResultDetail(CICDetailDTO cicDes, CICDetailDTO cicSrc, CreditBureauData cicResult) throws ParseException {
		
		cicDes.setCustomerName(cicSrc.getCustomerName());
		cicDes.setCicResult(cicSrc.getCicResult());
		cicDes.setDescription(getCICResultDescription(cicSrc.getCicResult()));
		cicDes.setCicImageLink(cicSrc.getCicImageLink());
		cicDes.setStatus(cicSrc.getStatus());
		cicDes.setLastUpdateTime(DateUtil.toDateString(cicResult.getLastUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
		cicDes.setNumberOfRelationOrganize(cicSrc.getNumberOfRelationOrganize());
	}
	
	/**
	 * Convert cic result to cic detail bpm dto
	 * @param cicDes
	 * @param cicSrc
	 * @param cicResult
	 * @param listIdentity
	 * @throws ParseException
	 */
	public static void getCICResultDetailForBpm(CICDetailForBpmDTO cicDes, CICDetailDTO cicSrc, CreditBureauData cicResult, String listIdentity) throws ParseException {
		
		cicDes.setCustomerName(cicSrc.getCustomerName());
		if (!StringUtils.isNullOrEmpty(cicSrc.getCicResult())) {
			// Do tool nay trien khai portal truoc bi nguoc value 5 va 6 nen khi tich hop bpm sau phai chuyen nguoc lai
					// khong chuyen portal duoc vi anh huong den ket qua da luu
			if ("5".equals(cicSrc.getCicResult()))
				cicSrc.setCicResult("6");
			else if ("6".equals(cicSrc.getCicResult()))
				cicSrc.setCicResult("5");
		}
		cicDes.setCicResult(cicSrc.getCicResult());
		cicDes.setDescription(getCICResultDescriptionForBpm(cicSrc.getCicResult()));
		cicDes.setCicImageLink(cicSrc.getCicImageLink());
		cicDes.setStatus(cicSrc.getStatus());
		cicDes.setLastUpdateTime(DateUtil.toDateString(cicResult.getLastUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
		cicDes.setCicExtensionLink(listIdentity);
	}
	
	/**
	 * Convert from cic request to cic dto
	 * @author catld.ho
	 * @param request : cic request from message log
	 * @return CICDetailDTO
	 * @throws ParseException
	 * @throws SQLException
	 */
	public static CICDetailDTO getCICDetailFromRequest(CICRequestClaim request, CreditBureauData data) throws ParseException, SQLException {
		CICDetailDTO result = new CICDetailDTO(data.getCustIdentityNumber());
		result.setRequestId(request.getRequestId());
		result.setLastUpdateTime(DateUtil.toDateString(data.getLastUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
		
//		if (data.getCbDataDetail() != null) {
//			String s = request.getCicDataDetail().getSubString(1, (int) request.getCicDataDetail().length());
			if (!StringUtils.isNullOrEmpty(data.getCbDataDetail())) {
				CICDetailDTO cicChecked = JSONConverter.toObject(data.getCbDataDetail(), CICDetailDTO.class);
				if (cicChecked != null) {
					result.setCustomerName(cicChecked.getCustomerName());
					result.setCicResult(cicChecked.getCicResult());
					result.setDescription(getCICResultDescription(cicChecked.getCicResult()));
					result.setCicImageLink(cicChecked.getCicImageLink());
					result.setStatus(cicChecked.getStatus());
					result.setNumberOfRelationOrganize(cicChecked.getNumberOfRelationOrganize());
				}
			}
//		}

		return result;
	}
	
	public static Boolean getCICStatusForThirdParty(String cicResult) {
		if (StringUtils.isNullOrEmpty(cicResult))
			return false;
		
		switch(cicResult) {
		case "1":	// Đang có dư nợ, không có nợ xấu hay nợ cần chú ý
		case "4": 	// Có thông tin nhưng không có dự nợ
		case "5": 	// Không có thông tin
		case "6": 	// CIC lỗi
			return true;
		case "2": 	// Đang có dư nợ, đang có nợ cần chú ý
		case "3": 	// Đang có dư nợ, đang có nợ xấu
			return false;
		
		default: // TH co gia tri khac khong mong muon thi mac dinh la pass
			return true;
		}
	}
	
	
	/**
	 * Mapping cic result to description
	 * @author catld.ho
	 * @param result
	 * @return
	 */
	public static String getCICResultDescription(String result) {
		if (StringUtils.isNullOrEmpty(result))
			return "";
		switch(result) {
		case "1": return "Đang có dư nợ, không có nợ xấu hay nợ cần chú ý";
		case "2": return "Đang có dư nợ, đang có nợ cần chú ý";
		case "3": return "Đang có dư nợ, đang có nợ xấu";
		case "4": return "Có thông tin nhưng không có dự nợ";
		case "5": return "Không có thông tin";
		case "6": return "CIC lỗi";
		default: return "";
		}
	}
	
	public static Integer getCICRequestSource(String cicResult) {
		if ("6".equals(cicResult))
			return CICRequestSource.WEB_REPORT_ERROR.value();
		
		return CICRequestSource.WEB_REPORT_WRONG.value();
	}
	
	public static String getCICResultDescriptionForBpm(String result) { 
		if (StringUtils.isNullOrEmpty(result))
			return "";
		switch(result) {
		case "1": return "Đang có dư nợ, không có nợ xấu hay nợ cần chú ý";
		case "2": return "Đang có dư nợ, đang có nợ cần chú ý";
		case "3": return "Đang có dư nợ, đang có nợ xấu";
		case "4": return "Có thông tin nhưng không có dự nợ";
		case "5": return "CIC lỗi";
		case "6": return "Không có thông tin";
		default: return "";
		}
	}
}
