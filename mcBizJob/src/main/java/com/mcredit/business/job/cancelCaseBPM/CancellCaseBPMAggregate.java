package com.mcredit.business.job.cancelCaseBPM;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.cancelcasebpm.entity.UplAppAutoAbort;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.MessageTask;
import com.mcredit.data.common.entity.NotificationTemplate;
import com.mcredit.model.enums.AuthorizationToken;
import com.mcredit.model.enums.AutoAbortStsEnum;
import com.mcredit.model.enums.CommonStatus;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.ProductGroupEnum;
import com.mcredit.model.enums.TemplateEnum;
import com.mcredit.model.object.SendEmailDTO;
import com.mcredit.model.object.appInfo.DelIndexCancelCase;
import com.mcredit.model.object.appInfo.ResultDelIndex;
import com.mcredit.model.object.cancelCaseBPM.AuthInfo;
import com.mcredit.model.object.cancelCaseBPM.CancelCaseInfoDTO;
import com.mcredit.model.object.cancelCaseBPM.CancelCaseObject;
import com.mcredit.model.object.cancelCaseBPM.DisbursementObject;
import com.mcredit.model.object.cancelCaseBPM.MResponseAuthoInfo;
import com.mcredit.model.object.cancelCaseBPM.NewStatusEnum;
import com.mcredit.model.object.cancelCaseBPM.ReasonCacelDetailEnum;
import com.mcredit.model.object.cancelCaseBPM.ReasonCacelEnum;
import com.mcredit.model.object.cancelCaseBPM.ResultCancelCaseBPM;
import com.mcredit.model.object.cancelCaseBPM.StatusCancelCaseBPMEnum;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.PartitionListHelper;

public class CancellCaseBPMAggregate {
	private UnitOfWork unitOfWork = null;
	private String  usrUidTarget = null ;
	private String abortProcess = "1" ;
	private String esbHost = null ;
	//	private String sendTest = "http://172.16.132.69:8080" ; test
	protected String clientID =  null ;
	protected String clientSecret =  null ;
	protected String uerAutoAbort =  null ;
	protected String passAutoAbort  =  null ;
	protected String emailAutoAbort  =  null ;
	protected String emailUserAutoAbort  =  null ;
	protected String emailPassAutoAbort  =  null ;
	protected String refeshTokenCancelCaseBPM =  null ;
	protected String tokenCancelCaseBPM =  "" ;
	protected  int pageSizeDSS = 100 ;
	protected  int pageSizegetDisbursement = 300 ;
	protected static String grant_type =  "password";
	protected static String scope  =  "*" ;
	
	protected BasedHttpClient bs = null ;
	
	public CancellCaseBPMAggregate(String usrUidTarget, String esbHost, String clientID, int pageSizeDSS, String clientSecret,
			String uerAutoAbort, String passAutoAbort, String tokenCancelCaseBPM, String refeshTokenCancelCaseBPM, String emailAutoAbort,
			String emailUserAutoAbort, String emailPassAutoAbort, UnitOfWork uok) {		
		this.usrUidTarget = usrUidTarget ;
		this.esbHost = esbHost ;
		this.clientID = clientID ;
		this.clientSecret = clientSecret ;
		this.pageSizeDSS = pageSizeDSS ;
		this.uerAutoAbort = uerAutoAbort ;
		this.passAutoAbort = passAutoAbort ;	
		this.tokenCancelCaseBPM = tokenCancelCaseBPM ;	
		this.refeshTokenCancelCaseBPM = refeshTokenCancelCaseBPM ;
		this.emailAutoAbort = emailAutoAbort ; 
		this.emailUserAutoAbort = emailUserAutoAbort ;
		this.emailPassAutoAbort = emailPassAutoAbort ;
		
		// Pass Unit of Work
		this.unitOfWork = uok;
	}

	public int sendCase(CancelCaseObject object) {
		try {
			boolean claim = true , reassign = true , variable = true , route = true  , caseContract = true, delete  = true ;
			boolean round5err = false;
			int step = 0 ;
			System.out.println( " send cancelCaseBPM : " + new Date());  
			System.out.println( " send CancelCaseObject : " + JSONConverter.toJSON(object));  
			if (object.getCaseId() != null) {
				StatusCancelCaseBPMEnum statusCase = StatusCancelCaseBPMEnum.from(object.getCaseStatus());
				
				 /*Claim case*/
				if( (1 == object.getCasegroup() && StatusCancelCaseBPMEnum.POS_PENDING == statusCase) ) {
					 claim = claimCaseBPM(object);
					 step ++ ;
				}
				
				 /*Reassign Case*/
				if( (1 == object.getCasegroup() && StatusCancelCaseBPMEnum.POS_OPEN == statusCase) ||
					2 == object.getCasegroup() || 
					4 == object.getCasegroup() ) {
					reassign = reassignCaseBPM(object);
					step ++ ;
				}
				
				 /*Variable Case*/
				/*claim && reassign &&*/
				if( 3 != object.getCasegroup() &&
					5 != object.getCasegroup() ) {
					variable = variableCaseBPM(object);
					
					 /*Route Case*/
					/*if (variable)*/
					route = routeCaseBPM(object);
					step ++ ;
				}
				
				/*cancel Contract*/
				if( 3 == object.getCasegroup() ) {
					caseContract = cancelContractBPM(object);
					step ++ ;
				}
				
				 /*Delete case trash*/
				if( 5 == object.getCasegroup() ) {
					 delete = deleteCaseBPM(object, statusCase);
					 step ++ ;
				}
				
				if(/*claim && reassign && variable &&*/ route && caseContract && delete){
					this.updateAutoAbortSts(object.getCaseId(), AutoAbortStsEnum.ABORT.value(), "Kết quả hủy thành công", "system");
					System.out.println("==>> AutoCancelCaseBPM Result: Success");
				} else {
					if (object.getErrorCount() >= 5) {	// Reject when case is error at 5 times
						this.updateAutoAbortSts(object.getCaseId(), AutoAbortStsEnum.REJECT.value(), "Reject case", "system");
						round5err = true;
						System.out.println("==>> AutoCancelCaseBPM Result: Reject");
					} else {
						this.updateAutoAbortSts(object.getCaseId(), AutoAbortStsEnum.ERROR.value(), "Error service", "system");
						System.out.println("==>> AutoCancelCaseBPM Result: Error");
					}
				}
			
				// Send mail:
				if( route && caseContract && delete && object.getSaleEmail()!= null && step != 0){
					SendEmailDTO send = new SendEmailDTO(emailUserAutoAbort, emailPassAutoAbort, emailAutoAbort,new ArrayList<String>(Arrays.asList(object.getSaleEmail())), null, null, object.getSubjectEmail(), object.getContenEmail());
					try {
						new com.mcredit.sharedbiz.service.MessageService(send).send();
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			} else {
				return 0;
			}
			
			if (round5err == false) return/* claim && reassign && variable */ (route && caseContract && delete) ? 1 : 0;
				else return /* reject case when take it 2 times */ 2;
		} catch (Exception e) {
			e.printStackTrace();
			return 3;
		}
	}
	
	private void updateAutoAbortSts(String appuid, String status, String description, String author) {
		UplAppAutoAbort uplAppAutoAbort = this.unitOfWork.uplAppAbort.uplAppAutoAbortDataRepository().getUplAppAutoAbortByAppuid(appuid);
		uplAppAutoAbort.setAutoAbortSts(status);
		if (AutoAbortStsEnum.REJECT.value().equals(status) || AutoAbortStsEnum.ERROR.value().equals(status)) {
			uplAppAutoAbort.setErrorCount(uplAppAutoAbort.getErrorCount()+1);
		}
		uplAppAutoAbort.setAutoAbortTm(new Date());
		uplAppAutoAbort.setAutoAbortDesc(description);
		uplAppAutoAbort.setAutoAbortBy(author);
		this.unitOfWork.uplAppAbort.uplAppAutoAbortDataRepository().upsert(uplAppAutoAbort);
	}

	private String buidContentRequestReassign(CancelCaseObject input) {	
		String result = "{\"appuid\":\"{appuid}\",\"del_index\":\"{del_index}\",\"usr_uid_target\":\"{usr_uid_target}\"}" ;
		return result.replace("{appuid}", input.getCaseId()).replace("{del_index}", String.valueOf(input.getDelIndex())).replace("{usr_uid_target}",usrUidTarget);
	}
	
	private String buidContentRequestCancelCase(CancelCaseObject caseCancel) {
		StatusCancelCaseBPMEnum statusCase = StatusCancelCaseBPMEnum.from(caseCancel.getCaseStatus());
		String result = new String("{\"appuid\": \"{appuid}\",\"abortProcess\": \"{abortProcess}\",\"abortProcessReason\": \"{abortProcessReason}\",\"abortProcessComment\": \"{abortProcessComment}\",\"appStatus\": \"{appStatus}\"}");
		
		/*Case 1 POS_PENDING/ POS_OPEN*/
		if((StatusCancelCaseBPMEnum.POS_PENDING ==  statusCase || StatusCancelCaseBPMEnum.POS_OPEN ==  statusCase )){
			return result.replace("{appuid}", caseCancel.getCaseId())
			.replace("{abortProcess}", abortProcess)
			.replace("{appStatus}", NewStatusEnum.POS_AUTO_ABORT.value())
			.replace("{abortProcessReason}", ReasonCacelEnum.AUTO_ABORT.value())
			.replace("{abortProcessComment}", ReasonCacelDetailEnum.APPROVAL_RESULTS_EXPIRE.value());
			
		}
		/*Case 2 POS_RETURN_PENDING/ POS_RETURN_OPEN*/
		if(StatusCancelCaseBPMEnum.POS_RETURN_PENDING ==  statusCase || StatusCancelCaseBPMEnum.POS_RETURN_OPEN ==  statusCase ){
			return result.replace("{appuid}", caseCancel.getCaseId())
			.replace("{abortProcess}", abortProcess)
			.replace("{appStatus}", NewStatusEnum.POS_AUTO_ABORT.value())
			.replace("{abortProcessReason}", ReasonCacelEnum.AUTO_ABORT.value())
			.replace("{abortProcessComment}", ReasonCacelDetailEnum.EXPIRY_OF_ADDITIONAL_PAPER_TIME.value());
		}				
		/*Case 4 SALE_RETURN_PENDING/ SALE_RETURN_OPEN*/
		if(StatusCancelCaseBPMEnum.SALE_RETURN_PENDING ==  statusCase || StatusCancelCaseBPMEnum.SALE_RETURN_OPEN ==  statusCase ){
			return result.replace("{appuid}", caseCancel.getCaseId())
			.replace("{abortProcess}", abortProcess)
			.replace("{appStatus}", NewStatusEnum.SALE_AUTO_ABORT.value())
			.replace("{abortProcessReason}", ReasonCacelEnum.AUTO_ABORT.value())
			.replace("{abortProcessComment}", ReasonCacelDetailEnum.TIME_LIMIT_FOR_PROCESSING_DOCUMENTS.value());
		}
		/*Case 4 DE_RETURN_PENDING/ DE_RETURN_OPEN/ DE_ONLINE_RETURN_PENDING/ DE_ONLINE_RETURN_OPEN*/
		if (StatusCancelCaseBPMEnum.DE_RETURN_PENDING == statusCase
				|| StatusCancelCaseBPMEnum.DE_RETURN_OPEN == statusCase
				|| StatusCancelCaseBPMEnum.DE_ONLINE_RETURN_PENDING == statusCase
				|| StatusCancelCaseBPMEnum.DE_ONLINE_RETURN_OPEN == statusCase) { 
			
			return result.replace("{appuid}", caseCancel.getCaseId())
			.replace("{abortProcess}", abortProcess)
			.replace("{appStatus}", NewStatusEnum.DE_AUTO_ABORT.value())
			.replace("{abortProcessReason}", ReasonCacelEnum.AUTO_ABORT.value())
			.replace("{abortProcessComment}", ReasonCacelDetailEnum.TIME_LIMIT_FOR_PROCESSING_DOCUMENTS.value());
		}
		System.out.println( "  result test : " + result);
		return  null;
	}
	
	public String refeshAccessToken() {
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_REFESH_TOKEN;
		AuthInfo authInfo = new AuthInfo(grant_type, scope, clientID, clientSecret, uerAutoAbort, passAutoAbort,refeshTokenCancelCaseBPM);
		bs = new BasedHttpClient();
		try {
			ApiResult result = bs.doPost(url, JSONConverter.toJSON(authInfo), ContentType.Json);

			if (result.getCode() == 200) {
				ObjectMapper mapper = new ObjectMapper();
				MResponseAuthoInfo  resoult =  mapper.readValue(result.getBodyContent().toString(), new TypeReference<MResponseAuthoInfo>() {});			
				return resoult.getAccess_token();
			}

		} catch (Exception e) {
			
		}
		return null;
	}
	
	public boolean claimCaseBPM(CancelCaseObject object) {
		
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_CLAIM_CASE_BPM;
		String content = "{\"appuid\":\"caseId\"}".replace("caseId", object.getCaseId()) ;
		MessageLog msglog = createMessageLog(1, content, object.getCaseId(),"CLAIM", 0);
		
		bs = new BasedHttpClient(AuthorizationToken.BEARER.value(),tokenCancelCaseBPM); 
		ApiResult result = new ApiResult();
		try {
			 result = bs.doPost(url, content , ContentType.Json, AcceptType.NotSet);					
			
			if(result.getCode() == 401){
				bs = new BasedHttpClient(AuthorizationToken.BEARER.value(), refeshAccessToken()); 
				 	result = bs.doPost(url, null , ContentType.Json, AcceptType.NotSet);
			}
			
			if(resultService(result)){
				msglog.setMsgStatus(MsgStatus.SUCCESS.value());
			}else {
				msglog.setMsgStatus(MsgStatus.ERROR.value());
			}
			msglog.setProcessTime(new Timestamp(new Date().getTime()));
			msglog.setResponseCode(String.valueOf(result.getCode()));
			msglog.setMsgResponse(result.getBodyContent());
			unitOfWork.common.messageLogRepo().upsert(msglog);
			
		} catch (ISRestCoreException e) {
			e.printStackTrace();
		}
		
		return result.getCode() == 200 ? true : false ;
	} 
	
	public boolean routeCaseBPM(CancelCaseObject object) {
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_ROUTE_CASE_BPM;
		System.out.println( " url routeCaseBPM :" + url.replace("{caseId}",object.getCaseId()));
		bs = new BasedHttpClient(AuthorizationToken.BEARER.value(), tokenCancelCaseBPM); 
		MessageLog msglog = createMessageLog(3, null, object.getCaseId(), "ROUTE", 0);		
		ApiResult result = new ApiResult();
		try {
			 result = bs.doPost(url.replace("{caseId}",object.getCaseId()), null , ContentType.Json, AcceptType.NotSet);					
			
			if(result.getCode() == 401){
				bs = new BasedHttpClient(AuthorizationToken.BEARER.value(),refeshAccessToken()); 
			 	result = bs.doPost(url.replace("{caseId}",object.getCaseId()), null , ContentType.Json, AcceptType.NotSet);
			}
			
			if(resultService(result)){
				msglog.setMsgStatus(MsgStatus.SUCCESS.value());
			}else {
				msglog.setMsgStatus(MsgStatus.ERROR.value());
			}
			msglog.setProcessTime(new Timestamp(new Date().getTime()));
			msglog.setResponseCode(String.valueOf(result.getCode()));
			msglog.setMsgResponse(result.getBodyContent());
			unitOfWork.common.messageLogRepo().upsert(msglog);
			
		} catch (ISRestCoreException e) {
			e.printStackTrace();
		}
		
		return result.getCode() == 200 ? true : false ;
	} 
	
	public boolean reassignCaseBPM(CancelCaseObject object) {
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_REASSIGN_CASE_BPM;		
		bs = new BasedHttpClient(AuthorizationToken.BEARER.value(), tokenCancelCaseBPM);
		String content = buidContentRequestReassign(object);
		MessageLog msglog = createMessageLog(1, content, object.getCaseId(), "REASSIGN", 0);
		ApiResult result = new ApiResult();
		try {
			 result = bs.doPost(url, content , ContentType.Json, AcceptType.NotSet);					
			
			if(401 == result.getCode()){
				bs = new BasedHttpClient(AuthorizationToken.BEARER.value(), refeshAccessToken()); 
			 	result = bs.doPost(url, content , ContentType.Json, AcceptType.NotSet);
			}
			if(200 == result.getCode()){
				msglog.setMsgStatus(MsgStatus.SUCCESS.value());
			}else {
				msglog.setMsgStatus(MsgStatus.ERROR.value());
			}
		} catch (ISRestCoreException e) {
			e.printStackTrace();
		}
		msglog.setProcessTime(new Timestamp(new Date().getTime()));
		msglog.setResponseCode(String.valueOf(result.getCode()));
		msglog.setMsgResponse(result.getBodyContent());
		unitOfWork.common.messageLogRepo().upsert(msglog);
		
		return result.getCode() == 200 ? true : false ;
	}
	
	public boolean variableCaseBPM(CancelCaseObject object) {
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_VARIABLE_CASE_BPM;		
		bs = new BasedHttpClient(AuthorizationToken.BEARER.value(), tokenCancelCaseBPM); 
		String content = buidContentRequestCancelCase(object);
		MessageLog msglog = createMessageLog(2, content, object.getCaseId(), "VARIABLE", 0);
		
		ApiResult result = new ApiResult();
		try {
			result = bs.doPut(url, content , AcceptType.NotSet,ContentType.Json);			
			
			if(result.getCode() == 401){
				bs = new BasedHttpClient(AuthorizationToken.BEARER.value(), refeshAccessToken()); 
				result = bs.doPut(url, content , AcceptType.NotSet,ContentType.Json);	
			}
			
			if(resultService(result)){
				msglog.setMsgStatus(MsgStatus.SUCCESS.value());
			}else {
				msglog.setMsgStatus(MsgStatus.ERROR.value());
			}
			msglog.setProcessTime(new Timestamp(new Date().getTime()));
			msglog.setResponseCode(String.valueOf(result.getCode()));
			msglog.setMsgResponse(result.getBodyContent());
			unitOfWork.common.messageLogRepo().upsert(msglog);
			
		} catch (ISRestCoreException e) {
			e.printStackTrace();
		}
		return result.getCode() == 200 ? true : false ;
	} 
	
	public ApiResult disbursementCaseBPM(CancelCaseObject object) {
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_CONTACT_CANCEL_CASE_BPM;
		
		bs = new BasedHttpClient(AuthorizationToken.BEARER.value(), tokenCancelCaseBPM); 
		ApiResult result = new ApiResult();
		try {
			 result = bs.doPost(url, object.getCaseNumber().toString() , ContentType.Json, AcceptType.NotSet);					
			
			if(result.getCode() == 401){
				bs = new BasedHttpClient(AuthorizationToken.BEARER.value(), refeshAccessToken()); 
			 	result = bs.doPost(url, object.getCaseNumber().toString() , ContentType.Json, AcceptType.NotSet);
			}
			
			
		} catch (ISRestCoreException e) {
			e.printStackTrace();
		}
		
		return result ;
	}
	public boolean deleteCaseBPM(CancelCaseObject object, StatusCancelCaseBPMEnum statusCase ) {
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_DELETE_CASE_BPM.replace("{caseId}", object.getCaseId());
		MessageLog msglog = createMessageLog(1, null, object.getCaseId(), "DELETE_DRAFT_CORE_BPM", 0);
		bs = new BasedHttpClient(AuthorizationToken.BEARER.value(),tokenCancelCaseBPM); 
		System.out.println( " Delete  url " + url);
		ApiResult result = new ApiResult();
		boolean deleteCaseBPMTransaction= true ;
		try {
			 result = bs.doDelete(url,  AcceptType.NotSet, ContentType.Json);					
			
			if(401 == result.getCode()){
				new BasedHttpClient(AuthorizationToken.BEARER.value(),tokenCancelCaseBPM);
				 result = bs.doDelete(url,  AcceptType.NotSet, ContentType.Json);
			}
			
			if(resultService(result)){
				msglog.setMsgStatus(MsgStatus.SUCCESS.value());			
				deleteCaseBPMTransaction = deleteCaseBPMTransaction(object, statusCase);
			}else {
				msglog.setMsgStatus(MsgStatus.ERROR.value());
			}
			msglog.setProcessTime(new Timestamp(new Date().getTime()));
			msglog.setResponseCode(String.valueOf(result.getCode()));
			msglog.setMsgResponse(result.getBodyContent());
			unitOfWork.common.messageLogRepo().upsert(msglog);
			
		} catch (ISRestCoreException e) {
			e.printStackTrace();
		}
		
		return result.getCode() == 200 && deleteCaseBPMTransaction ? true : false ;
	} 
	
	public boolean deleteCaseBPMTransaction(CancelCaseObject object, StatusCancelCaseBPMEnum statusCase) {
		String content ="{\"appNumber\": \"{appNumber}\",\"processName\": \"{processName}\",\"updateBy\": \"auto_abort\",\"action\": \"DELETE\"}"
				.replace("{appNumber}", object.getCaseNumber())
				.replace("{processName}", StatusCancelCaseBPMEnum.DE_OPEN == statusCase ? "Installment Loan" : "Concentrating Data Entry");
		
		MessageLog msglog = createMessageLog(2, content, object.getCaseId(), "DELETE_DRAFT_APP_BPM", 0);

		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_DELETE_CASE_TRANSACTION_BPM;

		bs = new BasedHttpClient();
		ApiResult result = new ApiResult();
		try {
			result = bs.doPost(url, content, ContentType.Json, AcceptType.NotSet);

			if (200 == result.getCode()) {
				msglog.setMsgStatus(MsgStatus.SUCCESS.value());
			} else {
				msglog.setMsgStatus(MsgStatus.ERROR.value());
			}

			msglog.setProcessTime(new Timestamp(new Date().getTime()));
			msglog.setResponseCode(String.valueOf(result.getCode()));
			msglog.setMsgResponse(result.getBodyContent());
			unitOfWork.common.messageLogRepo().upsert(msglog);
			
		} catch (ISRestCoreException e) {
			return false;
		}
		return result.getCode() == 200 ? true : false;
	}
	
	public boolean cancelContractBPM(CancelCaseObject object) {
		boolean final_result = false;
		String content = "{\"appNumber\": \"{appNumber}\",\"processName\": \"Concentrating Data Entry\","
				+ "\"newStatus\": \"DONE_AUTO_ABORT\",\"abortProcessReason\": \"Huy tu dong\","
				+ "\"abortProcessComment\": \"Het thoi han hieu luc cua ma nhan tien\","
				+ "\"updateBy\": \"auto_abort\",\"action\": \"EDIT\"}";
		content = content.replace("{appNumber}", object.getCaseNumber());
		
		MessageLog msglog = createMessageLog(1, content, object.getCaseId(), "CANCEL_CASE_DONE_BPM", 0);

		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_CANCEL_CONTRACT_BPM;

		bs = new BasedHttpClient();
		ApiResult result = new ApiResult();
		try {
			result = bs.doPost(url, content, ContentType.Json, AcceptType.NotSet);
			
			if (200 == result.getCode()) {
				boolean cancelDISBUR = cancelContractDISBURSEMENT(object);
				final_result = cancelDISBUR;
				if(cancelDISBUR){
					insertOrUPdateTableTMPCancelCaseDE(object, CommonStatus.SUCCESS.value());
				}else {
					insertOrUPdateTableTMPCancelCaseDE(object, CommonStatus.ERROR.value());
				}
				
				msglog.setMsgStatus(MsgStatus.SUCCESS.value());
			} else {
				msglog.setMsgStatus(MsgStatus.ERROR.value());
				insertOrUPdateTableTMPCancelCaseDE(object, CommonStatus.ERROR.value());
			}

			msglog.setProcessTime(new Timestamp(new Date().getTime()));
			msglog.setResponseCode(String.valueOf(result.getCode()));
			msglog.setMsgResponse(result.getBodyContent());
			unitOfWork.common.messageLogRepo().upsert(msglog);
			
		} catch (ISRestCoreException e) {
			return false;
		}
		return final_result;
	}
	
	public boolean cancelContractDISBURSEMENT(CancelCaseObject object) {
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_CANCEL_CONTRACT_DISBURSEMENT;		
		String content = "{\"contractNumber\":\"{contract}\"}".replace("{contract}", object.getContractNumber()) ;
		MessageLog msglog = createMessageLog(1, content, object.getCaseId(), "CANCEL_CASE_DONE_INTEGATION_SERVICES", 0);
		
		bs = new BasedHttpClient(); 
		ApiResult result = new ApiResult();
		try {
			 result = bs.doPost(url,content , ContentType.Json, AcceptType.NotSet);					
			
			if(200 == result.getCode()){				
				msglog.setMsgStatus(MsgStatus.SUCCESS.value());
			}else {
				msglog.setMsgStatus(MsgStatus.ERROR.value());
			}
			msglog.setProcessTime(new Timestamp(new Date().getTime()));
			msglog.setResponseCode(String.valueOf(result.getCode()));
			msglog.setMsgResponse(result.getBodyContent());
			unitOfWork.common.messageLogRepo().upsert(msglog);
			
		} catch (ISRestCoreException e) {
			e.printStackTrace();
			return  false ;
		}
		
		return result.getCode() == 200 ? true : false ;
	}
	
	public  List<ResultDelIndex> checkDelIndex(List<CancelCaseObject> caseLst) {
		
		List<String> caseNumAll = new ArrayList<>();
		List<ResultDelIndex> out = new ArrayList<>();
		
		for (CancelCaseObject caseOjbect : caseLst) {
			if(null != caseOjbect && caseOjbect.getCaseId() != null && caseOjbect.getCaseNumber()!= null && caseOjbect.getCaseStatus() != null){
					caseNumAll.add(caseOjbect.getCaseNumber());
				}
			}
		
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_GET_DELINDEX_CASE_BPM;
		
		try {
			String content =  caseNumAll.stream().map( n -> n.toString() ).collect( Collectors.joining( "," ));
			DelIndexCancelCase respon = new DelIndexCancelCase();
			ApiResult result = bs.doGet(url.replace("{ltsAppNumber}",content));
			
			if (401 == result.getCode()) {
				bs = new BasedHttpClient(AuthorizationToken.BEARER.value(), refeshAccessToken());
				result = bs.doPost(url, null, ContentType.Json, AcceptType.NotSet);
			}
			
			if ( 200 == result.getCode()) {
				ObjectMapper mapper = new ObjectMapper();try {
					respon =  mapper.readValue(result.getBodyContent().toString(), new TypeReference<DelIndexCancelCase>() {});
				} catch (Exception e) {
					e.printStackTrace();
				} 
				List<ResultDelIndex> tmp  = respon.getResult();
				out.addAll(tmp);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return  out ;
	}
	
	public List<CancelCaseObject> trunctCheckCaseDE(List<CancelCaseObject> caseLst , List<ResultDelIndex> listKey ){
		List<CancelCaseObject> tmp = new ArrayList<>();
		for (CancelCaseObject o : caseLst) {
			for (ResultDelIndex obj : listKey) {
				if(null != o && null !=  obj && o.getCaseId().equals(obj.getAPPUID())){
					o.setDelIndex(obj.getDELINDEX());					
					tmp.add(o);
				}
			}
		}
		return tmp;
	}
	
	public List<CancelCaseObject> getCancelCaseInstallmentLoan() {
		List<CancelCaseObject> resoult = new ArrayList<>();
		Integer reSet = 0 ;
		boolean nextpage = true ;
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_GET_INSTALLMENTLOAN_CASE_BPM;
		while (nextpage) {

			ResultCancelCaseBPM rp = new ResultCancelCaseBPM();
			
			try {
				bs = new BasedHttpClient();
				ApiResult result = bs.doGet(url.replace("{pageSize}",String.valueOf(pageSizeDSS)).replace("{rowBegin}",reSet.toString()));

				if ( 200 == result.getCode()) {
					
					ObjectMapper mapper = new ObjectMapper();try {
						rp =  mapper.readValue(result.getBodyContent().toString(), new TypeReference<ResultCancelCaseBPM>() {});
					} catch (Exception e) {
						e.printStackTrace();
					} 
					List<CancelCaseObject> tmp  = rp.getResult();
					resoult.addAll(tmp);
					
					if(pageSizeDSS ==  tmp.size()){
						reSet = reSet + tmp.size() ;
					}else{
						nextpage = false ;
					}
				}else{
					nextpage = false ;
				}

			} catch (ISRestCoreException e) {
				e.printStackTrace();
				nextpage = false ;
			}
		}
		resoult.stream().forEach(elt -> elt.setTypeOfLoan(ProductGroupEnum.INSTALLMENTLOAN.value()));
		return resoult;
	}

	public List<CancelCaseObject> getCancelCaseConcentratingDataEntry() {
		List<CancelCaseObject> resoult = new ArrayList<>();
		Integer reSet = 0 ;
		boolean nextpage = true ;
		
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_GET_CONCENTRATINGDATAENTRY_CASE_BPM;
		while (nextpage) {
			ResultCancelCaseBPM rp = new ResultCancelCaseBPM();
			try {
				bs = new BasedHttpClient();
				ApiResult result = bs.doGet(url.replace("{pageSize}",String.valueOf(pageSizeDSS)).replace("{rowBegin}",reSet.toString()));
	
				if (200 == result.getCode()) {
					ObjectMapper mapper = new ObjectMapper();
				try {
					rp =  mapper.readValue(result.getBodyContent().toString(), new TypeReference<ResultCancelCaseBPM>() {});
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
				List<CancelCaseObject> tmp  = rp.getResult();
				resoult.addAll(tmp);
					
					if(pageSizeDSS ==  tmp.size()){
						reSet = reSet + tmp.size() ;
					}else{
						nextpage = false ;
					}
				}else{
					nextpage = false ;
				}
			} catch (ISRestCoreException e) {
				e.printStackTrace();
				nextpage = false ;
			}
		}
		
		resoult.stream().forEach(elt -> elt.setTypeOfLoan(ProductGroupEnum.CONCENTRATINGDATAENTRY.value()));
		return resoult;
	}
	
	public List<CancelCaseObject> getCaseDONEConcentratingDataEntry() {
		
		List<CancelCaseObject> out = new ArrayList<>();
		Integer reSet = 0 ;
		boolean nextpage = true ;
		
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_GET_DONE_CONCENTRATINGDATAENTRY_CASE_BPM;
		while (nextpage) {
			
			ResultCancelCaseBPM rp = new ResultCancelCaseBPM();
			
			try {
				bs = new BasedHttpClient();
				ApiResult result = bs.doGet(url.replace("{pageSize}",String.valueOf(pageSizeDSS)).replace("{rowBegin}",reSet.toString()));
						
				if (result.getCode() == 200) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						rp =  mapper.readValue(result.getBodyContent().toString(), new TypeReference<ResultCancelCaseBPM>() {});
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(null == rp.getResult())
						return null ;
					
					out.addAll(getDisbursement(rp.getResult()));
					
					if(pageSizeDSS ==  rp.getResult().size()){
						reSet = reSet + rp.getResult().size() ;
					}else{
						nextpage = false ;
					}
				}else{
					nextpage = false ;
				}
			} catch (ISRestCoreException e) {
				e.printStackTrace();
				nextpage = false ;
			}
		}
		out.stream().forEach(elt -> elt.setTypeOfLoan(ProductGroupEnum.CONCENTRATINGDATAENTRY.value()));
		return out;
	}

	public List<CancelCaseObject> getDisbursement(List<CancelCaseObject> input) {
		System.out.println("getDisbursement:" + JSONConverter.toJSON(input));
		HashMap<String, CancelCaseObject> hashTMP  = new HashMap<>();
		for (CancelCaseObject cancelCaseObject : input) {
			hashTMP.put(cancelCaseObject.getContractNumber(), cancelCaseObject);
		}
		
		String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_GET_INTEGATION_SERVICES_DISBURSEMENT;
		
		List<String> ctList = input.stream().map(x -> x.getContractNumber()).collect(Collectors.toList());
		ArrayList<List<String>> ct = new ArrayList<List<String>>(PartitionListHelper.partition(ctList, pageSizegetDisbursement));
		
		for (int i = 0; i < ct.size(); i++) {
			
			bs = new BasedHttpClient(AuthorizationToken.BEARER.value(), tokenCancelCaseBPM);
			String conten = String.join(",", ct.get(i));
			
			DisbursementObject tmp = new DisbursementObject();
			try {
				ApiResult result = bs.doGet(url.replace("{contractNumbers}", conten));
				
				if (result.getCode() == 200) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						tmp =  mapper.readValue(result.getBodyContent(), new TypeReference<DisbursementObject>() {});
					} catch (Exception e) {
						e.printStackTrace();
					}
					tmp.getResult().forEach(item->{
						if(MsgStatus.DONE.value().equals( item.getStatus())){
							insertOrUPdateTableTMPCancelCaseDE(hashTMP.get(item.getContractNumber()), CommonStatus.CHECKED.value());
							CancelCaseObject o =	hashTMP.get(item.getContractNumber());
							input.remove(o);
						}
					});
				}
			} catch (ISRestCoreException e) {
				e.printStackTrace();
			}
		}
		return input;
	}
	
	public void insertOrUPdateTableTMPCancelCaseDE(CancelCaseObject input ,String status) {
		if(input!= null){
			String url = esbHost + BusinessConstant.BPM_AUTO_ABOUT_UDATE_TABLE_CANCELCASE_SERVICES;
			UpsertTmpCancelCaseDE upsert=  new UpsertTmpCancelCaseDE(input.getContractNumber(), input.getCaseId(), input.getCaseNumber(), status);
			System.out.println(" insertOrUPdateTableTMPCancelCaseDE: " + JSONConverter.toJSON(upsert));
			try {
				bs = new BasedHttpClient();
				bs.doPost(url, JSONConverter.toJSON(upsert), ContentType.Json, AcceptType.NotSet);				
				
			} catch (ISRestCoreException e) {
				e.printStackTrace();
			}
		}
	}
	
	public MessageTask createMessageTask(CancelCaseObject input ){
		MessageTask msgtsk = new MessageTask(new Timestamp(new Date().getTime()) , null , input.getCaseId() ,MsgStatus.NEW.value(),null, "CANCEL_CASE_BPM",JSONConverter.toJSON(input));
		unitOfWork.common.messageTaskRepo().upsert(msgtsk);
		return msgtsk ;
	}
	
	public MessageLog createMessageLog(Integer msgOrder ,String msgRequest , String caseId , String serviceName, long taskId ){
		MessageLog msglog = new MessageLog("MCP", msgOrder, msgRequest,MsgStatus.NEW.value(), "R", new Timestamp(new Date().getTime()), caseId, serviceName,"BMP", null, "CANCEL_CASE_BPM",  taskId) ;
		return msglog ;
	}
	
	public List<CancelCaseObject> buidSenEmail(List<CancelCaseObject> input) {	
		NotificationTemplate notiTemplate = this.unitOfWork.common.getNotificationTemplateRepository().findByCode(TemplateEnum.MAIL_AUTO_ABORT.toString());
		Map<String, CancelCaseObject> map = input.stream().collect(Collectors.toMap(x -> x.getCaseId(), x -> x ));
		List<String> caseId = input.stream().map(x -> x.getCaseId()).collect(Collectors.toList());
		
		List<CancelCaseInfoDTO> infor = unitOfWork.credit.creditApplicationRequestRepo().findInforByCaseId(caseId);
		if(infor == null || infor.isEmpty())
			return input ;
					
		for (CancelCaseInfoDTO caseInfo : infor) {
			CancelCaseObject caseTMP = map.get(caseInfo.getAppId().toString());
			if(null != caseTMP){
				caseTMP.setSaleEmail( caseInfo.getSaleEmail());
				caseTMP.setSubjectEmail(notiTemplate.getNotificationName());
				caseTMP.setContenEmail(notiTemplate.getNotificationTemplate()
						.replace("{0}", caseInfo.getCustName())
						.replace("{1}", caseTMP.getCaseNumber())
						.replace("{2}", null != setReasonforEmail(caseTMP.getCaseStatus()) ? setReasonforEmail(caseTMP.getCaseStatus()) : "")
						);
			}
			System.out.println(" =====> Send Mail to: " + caseTMP.getSaleEmail());
			map.put(caseInfo.getAppId().toString(), caseTMP);
		}
		
		return map.values().stream().collect(Collectors.toList());
	}

	private String setReasonforEmail(String caseStatus) {
		StatusCancelCaseBPMEnum statusCase = StatusCancelCaseBPMEnum.from(caseStatus);
		/*Case 1 POS_PENDING/ POS_OPEN*/
		if((StatusCancelCaseBPMEnum.POS_PENDING ==  statusCase || StatusCancelCaseBPMEnum.POS_OPEN ==  statusCase )){
			return ReasonCacelDetailEnum.APPROVAL_RESULTS_EXPIRE.value();
			
		}
		/*Case 2 POS_RETURN_PENDING/ POS_RETURN_OPEN*/
		if(StatusCancelCaseBPMEnum.POS_RETURN_PENDING ==  statusCase || StatusCancelCaseBPMEnum.POS_RETURN_OPEN ==  statusCase ){
			return ReasonCacelDetailEnum.EXPIRY_OF_ADDITIONAL_PAPER_TIME.value();
		}				
		/*Case 4 SALE_RETURN_PENDING/ SALE_RETURN_OPEN*/
		if(StatusCancelCaseBPMEnum.SALE_RETURN_PENDING ==  statusCase || StatusCancelCaseBPMEnum.SALE_RETURN_OPEN ==  statusCase ){
			return ReasonCacelDetailEnum.TIME_LIMIT_FOR_PROCESSING_DOCUMENTS.value();
		}
		/*Case 4 DE_RETURN_PENDING/ DE_RETURN_OPEN/ DE_ONLINE_RETURN_PENDING/ DE_ONLINE_RETURN_OPEN*/
		if (StatusCancelCaseBPMEnum.DE_RETURN_PENDING == statusCase
				|| StatusCancelCaseBPMEnum.DE_RETURN_OPEN == statusCase
				|| StatusCancelCaseBPMEnum.DE_ONLINE_RETURN_PENDING == statusCase
				|| StatusCancelCaseBPMEnum.DE_ONLINE_RETURN_OPEN == statusCase) { 
			
			return ReasonCacelDetailEnum.TIME_LIMIT_FOR_PROCESSING_DOCUMENTS.value();
		}
		
		/*Case 5 SALE_OPEN/ DE_OPEN*/
		if (StatusCancelCaseBPMEnum.SALE_OPEN == statusCase || StatusCancelCaseBPMEnum.DE_OPEN == statusCase) { 
			return ReasonCacelDetailEnum.EXPIRY_DATE_OF_DATA_ENTRY.value();
		}
		return  null;
	}

	
	public boolean  resultService(ApiResult in){
		if(null == in)
			return false;
		
		if( 200 == in.getCode())
			return true;
		return false;
	}
	
	public List<UplAppAutoAbort> getUplAppAutoAbortList() {
		List<UplAppAutoAbort> list = this.unitOfWork.uplAppAbort.uplAppAutoAbortDataRepository().getUplAppAutoAbortData();
		return list;
	}
	
	public UplAppAutoAbort getUplAppAutoAbortByAppuid(String appuid) {
		UplAppAutoAbort result = this.unitOfWork.uplAppAbort.uplAppAutoAbortDataRepository().getUplAppAutoAbortByAppuid(appuid);
		return result;
	}
	
	public void upsertUplAppAutoAbortByAppuid(UplAppAutoAbort uplAppAutoAbort) {
		this.unitOfWork.uplAppAbort.uplAppAutoAbortDataRepository().upsert(uplAppAutoAbort);
	}
	
}
