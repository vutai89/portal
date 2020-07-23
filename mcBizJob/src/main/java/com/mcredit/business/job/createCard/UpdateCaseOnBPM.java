package com.mcredit.business.job.createCard;

import java.sql.Timestamp;
import java.util.Date;

import com.mcredit.business.job.CallBack;
import com.mcredit.business.job.createCard.dto.CreateCardDTO;
import com.mcredit.business.job.createCard.dto.CreateCaseOnBPMResultDTO;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.cache.CacheManager;

public class UpdateCaseOnBPM extends CallBack{
	GetPropertiesValues prop = new GetPropertiesValues();
	BasedHttpClient bs = new BasedHttpClient();

//	public void execute( CreateCardDTO createcardDTO) throws Exception{
//		GetPropertiesValues prop = new GetPropertiesValues();
//		String uri = prop.getPropValues("application.properties", "url.esb.link") + BusinessConstant.URI_UPDATE_BPM + createcardDTO.getMessageLog().getRelationId().trim().toString();
//		URL url = new URL(uri) ;
//		UpDateBPMVO updateBPMVO = new UpDateBPMVO();
//		
//		updateBPMVO.setCoreCustCode(createcardDTO.getCoreCustCode());
//		updateBPMVO.setCardIdNumber(String.valueOf(createcardDTO.getCardId()));
//		updateBPMVO.setIssueId(String.valueOf(createcardDTO.getIssueId()));
//		
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setRequestMethod(HttpMethod.PUT.getDescription());
//		conn.setRequestProperty("Content-Type", "application/json; charset=utf8");
//		conn.setDoOutput(true);			
//		
//		OutputStream os = conn.getOutputStream();
//		os.write(updateBPMVO.toJson().getBytes());
//		os.flush();
//		os.close();
//		if(conn.getResponseCode()!= BusinessConstant.HTTP_RESPONSE_200){
//			throw new Exception();
//		}		
//	}	
	@Override
	public void execute(CreateCardDTO createcardDTO) throws Exception {
		createcardDTO.getMessageLog().setProcessTime(new Timestamp(new Date().getTime()));
//		String[] path = {"CustomerInformation.Person.Gender"};
//		String[] categories = {"GENDER"};
		String translatedRequest = createcardDTO.getMessageLog().getMsgRequest();

		if (null != createcardDTO && !createcardDTO.getMessageLog().getRelationId().trim().isEmpty()) {
			try {				
				String url = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST) + BusinessConstant.BPM_UPDATE_CARD_INFO +createcardDTO.getMessageLog().getRelationId().trim().toString();				
				CreateCaseOnBPMResultDTO createCaseOnBPMResultDTO = bs.doPut(url, translatedRequest, ContentType.Json,CreateCaseOnBPMResultDTO.class);				
				if (createCaseOnBPMResultDTO.getCode().equals(BusinessConstant.BPM_RESPONSE_OK+"")) {
					createcardDTO.getMessageLog().setResponseCode(BusinessConstant.RESPONSE_OK+"");
					createcardDTO.getMessageLog().setMsgStatus(MsgStatus.SUCCESS.value());
					createcardDTO.getMessageLog().setMsgResponse(createCaseOnBPMResultDTO.toJson());
					createcardDTO.getMessageLog().setResponseTime(new Timestamp(new Date().getTime()));
					createcardDTO.setMessageTaskStatus(MsgStatus.SUCCESS);
				}
			} catch (ISRestCoreException e) {
				exceptionHandler(bs, createcardDTO.getMessageLog(), e);
				System.out.println(e.getMessage());
				createcardDTO.setMessageTaskStatus(MsgStatus.ERROR);
				//createMessgeLog(createcardDTO.getMessageLog());
				throw new Exception(e.getMessage());
			}
			catch (Exception e) {
				exceptionHandler(bs, createcardDTO.getMessageLog(), e);
				System.out.println(e.getMessage());
				createcardDTO.setMessageTaskStatus(MsgStatus.ERROR);
				//createMessgeLog(createcardDTO.getMessageLog());
				throw new Exception(e.getMessage());
			}
		}
	}
}
