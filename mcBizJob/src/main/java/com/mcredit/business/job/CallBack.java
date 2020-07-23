package com.mcredit.business.job;

import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.mcredit.business.job.cardPayment.dto.CardPaymentDTO;
import com.mcredit.business.job.createCard.dto.CreateCardDTO;
import com.mcredit.business.job.dto.JobDTO;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.model.dto.ErrorResultDTO;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;

public abstract class CallBack {
	
    public void execute(CreateCardDTO createcardDTO) throws Exception{};
    
    public void execute(CreateCardDTO createcardDTO, List<MessageLog> messageLogList) {};
    
    public void execute(CardPaymentDTO cardPaymentDTO) throws Exception{};
    
    public void execute(CardPaymentDTO cardPaymentDTO, List<MessageLog> messageLogList) {};
    
    public void execute(JobDTO jobDTO) throws Exception {};
    
    public void transformMessage(JobDTO jobDTO, List<MessageLog> messageLogList) {};
    
    public void exceptionHandler(BasedHttpClient bs, MessageLog msgLog, Exception e) {
    	
    	ErrorResultDTO rs = bs.toJSONObject(ErrorResultDTO.class,e.getMessage().toString(), BusinessConstant.ESB_RESULT_JSON);
		msgLog.setMsgResponse(e.getMessage().toString());
		msgLog.setResponseTime(new Timestamp(new Date().getTime()));
		msgLog.setProcessTime( new Timestamp(new Date().getTime() - new Date().getTime()));
		
		if(e instanceof SocketTimeoutException )
			msgLog.setMsgStatus(MsgStatus.TIME_OUT.value());
		else
			msgLog.setMsgStatus(MsgStatus.ERROR.value());
		
		if(e instanceof ISRestCoreException) {
			msgLog.setResponseCode(((ISRestCoreException) e).statusCode + "");
			msgLog.setResponseErrorDesc("REST EXCEPTION!");
		}else { 
			msgLog.setResponseCode(rs.getReturnCode());
			msgLog.setResponseErrorDesc(rs.getReturnMes());
		}
    }

}
