package com.mcredit.business.job.cancelCaseBPM;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mcredit.data.cancelcasebpm.entity.UplAppAutoAbort;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.cancelCaseBPM.CancelCaseObject;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.util.JSONConverter;

public class CancelCaseBPMManager extends BaseManager {
	protected CancellCaseBPMAggregate cancellCaseBPMAggregate = null;

	protected int numberParallel = ParametersCacheManager.getInstance() .findParamValueAsInteger(ParametersName.NUMBER_PARALLEL_CANCEL_CASE_BPM);
	protected int dssPageSize = ParametersCacheManager.getInstance() .findParamValueAsInteger(ParametersName.NUMBER_DSS_PAGESIZE);
	
	protected String tokenCancelCaseBPM = null;
	protected String refeshTokenCancelCaseBPM =  null ;
	
	protected String usrUidTarget = ParametersCacheManager.getInstance() .findParamValueAsString(ParametersName.USR_UID_TARGET);
	
	protected String esbHost =  CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST) ;
	//protected String esbHost =  CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST_MOCK) ;
	
	protected String clientID =  CacheManager.Parameters().findParamValueAsString(ParametersName.AUTO_ABORT_CLIENT_ID) ;
	protected String clientSecret =  CacheManager.Parameters().findParamValueAsString(ParametersName.AUTO_ABORT_CLIENT_SECRET) ;
	protected String uerAutoAbort =  CacheManager.Parameters().findParamValueAsString(ParametersName.AUTO_ABORT_USER) ;
	protected String passAutoAbort =  CacheManager.Parameters().findParamValueAsString(ParametersName.AUTO_ABORT_PASS) ;
	protected String emailAutoAbort =  CacheManager.Parameters().findParamValueAsString(ParametersName.AUTO_ABORT_EMAIL) ;
	protected String emailUserAutoAbort =  CacheManager.Parameters().findParamValueAsString(ParametersName.AUTO_ABORT_EMAIL_SMTP_USER) ;
	protected String emailPassAutoAbort =  CacheManager.Parameters().findParamValueAsString(ParametersName.AUTO_ABORT_EMAIL_SMP_PASS) ;
	
	public CancelCaseBPMManager() {
		this.cancellCaseBPMAggregate = new CancellCaseBPMAggregate(usrUidTarget, esbHost, clientID, dssPageSize, clientSecret, uerAutoAbort, passAutoAbort,
				tokenCancelCaseBPM, refeshTokenCancelCaseBPM, emailAutoAbort, emailUserAutoAbort, emailPassAutoAbort, this.uok);
		
		try {
			getToken();
			System.out.println( "Tokent for session: " + tokenCancelCaseBPM );
			this.cancellCaseBPMAggregate.tokenCancelCaseBPM = tokenCancelCaseBPM ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object runJob() throws Exception {
		return this.tryCatch( () -> {
			List<UplAppAutoAbort> uaaList = this.cancellCaseBPMAggregate.getUplAppAutoAbortList();
			System.out.println("AutoCancelCaseBPM start with: " + uaaList.size() + " cases.");
			
			List<String>  sucsessAppIdLst = new ArrayList<>();
			List<String>  errorAppIdLst = new ArrayList<>();
			List<String>  rejectAppIdLst = new ArrayList<>();
			List<String>  exceptionAppIdLst = new ArrayList<>();
				
			// Have no records for abort case
			if( 0 == uaaList.size()){
				return new CancelCaseOutPutDTO();
			}
			
			List<CancelCaseObject> cancelCaseObjLst = new ArrayList<>();
			
			// Convert UplAppAutoAbort to CancelCaseObject
			for(UplAppAutoAbort item: uaaList) {
				System.out.println("==> " + item);
				CancelCaseObject tmp = new CancelCaseObject();
				tmp.setCaseId(item.getAppuid());
				tmp.setCaseNumber(Long.toString(item.getAppnumber()));
				tmp.setCaseStatus(item.getCurrStatus()==null ? "" : item.getCurrStatus());
				tmp.setContractNumber(item.getConstractnumber()==null ? "" : item.getConstractnumber());
				tmp.setDelIndex(item.getDelIndex());
				tmp.setTypeOfLoan(item.getTypeOfLoan());
				tmp.setCasegroup(item.getCaseGroup());
				tmp.setErrorCount(item.getErrorCount());
				
				cancelCaseObjLst.add(tmp);
			}
			
			// Build email for object
			cancelCaseObjLst = cancellCaseBPMAggregate.buidSenEmail(cancelCaseObjLst);
			
			for (CancelCaseObject cases : cancelCaseObjLst) {
				int result = cancellCaseBPMAggregate.sendCase(cases);
				
				if (result == 0) {
					errorAppIdLst.add(cases.getCaseId());
				} else if ( result == 1) {
					sucsessAppIdLst.add(cases.getCaseId());
				} else if (result == 2 ){
					rejectAppIdLst.add(cases.getCaseId());
				} else {
					exceptionAppIdLst.add(cases.getCaseId());
				}
			}
			
			System.out.println("AutoCancelCaseBPM end.");
			
			return  new CancelCaseOutPutDTO(sucsessAppIdLst, errorAppIdLst, rejectAppIdLst, exceptionAppIdLst);
		});
	}
	
	

	public  void getToken() throws Exception{
		String out  = cancellCaseBPMAggregate.refeshAccessToken() ;
		this.tokenCancelCaseBPM = out ;
	}	
	
	@Override
	public void close() throws IOException {
	}
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		System.out.println( JSONConverter.toJSON(new CancelCaseBPMManager().runJob()));
	}

}
