package com.mcredit.business.pcb.aggregate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;

import com.mcredit.business.pcb.callout.PcbEsbApi;
import com.mcredit.business.pcb.jsonobject.PcbDataDetail;
import com.mcredit.business.pcb.jsonobject.PcbDataDetailV2;
import com.mcredit.business.pcb.jsonobject.PcbInfo;
import com.mcredit.business.pcb.jsonobject.PcbResponse;
import com.mcredit.business.pcb.jsonobject.PcbResponseCommonInfo;
import com.mcredit.business.pcb.jsonobject.PcbResponseCreditInfo;
import com.mcredit.business.pcb.jsonobject.PcbResponseLoan;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.entity.ArchMessageLog;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.pcb.entity.CreditBureauData;
import com.mcredit.model.dto.pcb.CiReqInputDTO;
import com.mcredit.model.dto.pcb.IdCheckPcbDTO;
import com.mcredit.model.dto.pcb.request.RIReqInput;
import com.mcredit.model.dto.pcb.request.RIReqInputV2;
import com.mcredit.model.enums.pcb.PcbCallStatus;
import com.mcredit.model.enums.pcb.PcbRespCode;
import com.mcredit.model.enums.pcb.TransType;
import com.mcredit.model.enums.pcb.TypeChanel;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.model.EsbResult;
import com.mcredit.restcore.model.Result;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.aggregate.RedisAggregate;
import com.mcredit.util.JSONConverter;
import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

public class PCBAggregate {

	private UnitOfWork _uok = null;
	private PcbEsbApi _esbApi = null;

	public PCBAggregate(UnitOfWork _uok) {
		this._uok = _uok;
		_esbApi = new PcbEsbApi();
	}

	/**
	 * @author truongvd.ho
	 * @param payload : thong tin check PCB (BPM gui sang)
	 * @param appId : appId tuong ung voi ho so
	 * @return
	 * @throws Exception
	 */
	public  ArrayList<IdCheckPcbDTO> checkPCB(Object[] payload, String appNumber, String idCard, String idCard1 ,Date birthDay,String stepBpm, String type,Integer s37Result,Double loanAmount,String sourceReq) throws Exception {
		RedisAggregate redisAgg = new RedisAggregate();
		
		//Ngay request hien tai		
		Date requestTime = new Date();
		Date processTime = new Date();	
		
		//  String stepBpm = ""; //DC,CALL,APP
		//  String type = "";    // VIEW, CHECK   		
//		ObjectMapper mapper = new ObjectMapper();
		ArrayList<IdCheckPcbDTO> lstResultCheckPcb=new ArrayList<IdCheckPcbDTO>();	
		
		System.out.printf("Data from BPM call is payload=%s, appId=%s, idCard=%s, idCard1=%s, birthDay=%s,requestTime=%s,stepBpm=%s,type=%s,s37Result=%s,loanAmount=%s",JSONConverter.toJSON(payload).toString(),appNumber,idCard,idCard1,birthDay,requestTime,stepBpm,type,s37Result,loanAmount);
		String idCardRedis = redisAgg.get("cmnd_"+idCard);
		System.out.printf("Lay thong tin idCard tu Redis:%s",idCardRedis);
		IdCheckPcbDTO idCheckPcbDTO = new IdCheckPcbDTO();
		if(Strings.isNullOrEmpty(idCardRedis)) {
			// set thong tin vao redis
			redisAgg.set("cmnd_"+idCard, idCard,120);
			//query PCB by identity
			idCheckPcbDTO = getResultPcbByIdent(payload[0],appNumber,idCard,birthDay,stepBpm,type,requestTime,processTime,s37Result,loanAmount,sourceReq);
		}else {
			idCheckPcbDTO = new IdCheckPcbDTO(idCard, "CMND đang trong quá trình check PCB. Vui lòng thử lại sau !");
		}
		lstResultCheckPcb.add(idCheckPcbDTO);
		
		if(idCard.equals(idCard1)) {
			redisAgg.delete("cmnd_"+idCard);
		}
		
		if(!Strings.isNullOrEmpty(idCard1) && payload.length>1) {
			String idCard1Redis = redisAgg.get("cmnd_"+idCard1);
			System.out.printf("Lay thong tin idCard1 tu Redis:%s",idCard1Redis);
			IdCheckPcbDTO idCheckPcbDTO1 = new IdCheckPcbDTO();
			if(Strings.isNullOrEmpty(idCard1Redis)) {
				// set thong tin vao redis
				redisAgg.set("cmnd_"+idCard1, idCard1,120);
				idCheckPcbDTO1 = getResultPcbByIdent(payload[1],appNumber,idCard1,birthDay,stepBpm,type,requestTime,processTime,s37Result,loanAmount,sourceReq);	
			}else {
				idCheckPcbDTO1 = new IdCheckPcbDTO(idCard, "CMND đang trong quá trình check PCB. Vui lòng thử lại sau !");
			}
			
			lstResultCheckPcb.add(idCheckPcbDTO1);			
		}	
		redisAgg.delete("cmnd_"+idCard);
		redisAgg.delete("cmnd_"+idCard1);
		System.out.println("Result check PCB with idCard and idCard1:"+ idCard + "," + idCard1 + "----------" + JSONConverter.toJSON(lstResultCheckPcb).toString());
		return lstResultCheckPcb;
	}
	
	public IdCheckPcbDTO getResultPcbByIdent(Object payload, String appNumber, String idCard,Date birthDay,String stepBpm, String type,Date requestTime,Date processTime, Integer s37Result,Double loanAmount,String sourceReq) throws Exception {
//		ObjectMapper mapper = new ObjectMapper();
		MessageLog ObjectAppIdByStep = null;
		Object payloadMc = payload;
		Integer msgOrder = 2;    //2:thong tin cu   1: thong tin moi
		PcbDataDetail pcbDataDetail = null;
		PcbDataDetailV2 pcbDataDetailV2= null;
		PcbInfo pcbInfo1 = null;
		List<MessageLog> lstMessageLog = null;
		//Find list credit bureau data
		List<CreditBureauData> lstCreditBureauData = _uok.pcb.creditBureauDataRepository().getLstBureauDataByBirthDayAndIdent(idCard);			
		System.out.printf("Find lstCreditBureauData=%s \n",lstCreditBureauData);	 
				
		ApiResult resultApi = null;
		CreditBureauData maxLastUpdate = lstCreditBureauData.size() > 0 ? lstCreditBureauData.get(0) : null;
		if (maxLastUpdate!=null) {
			pcbInfo1 = JSONConverter.toObject(maxLastUpdate.getCbDataDetail(), PcbInfo.class);
            // lay thong tin request lan dau tien luu vao
		if(pcbInfo1.Result != null) {
			//tim kiem trong bang arch_message_log
			List<ArchMessageLog> lstArchMessageLog = _uok.common.archMessageLogRepo().getArchMessageLogFirstByTransId(maxLastUpdate.getId(),TypeChanel.PCB.value(),TransType.PCB_QUERY.value()); // get log theo buoc
			if(lstArchMessageLog.size()==0)
			//tim kiem trong bang message_log
			lstMessageLog = _uok.common.messageLogRepo().getMessageLogFirstByTransId(maxLastUpdate.getId(),TypeChanel.PCB.value(),TransType.PCB_QUERY.value()); // get log theo buoc	
			String cbDataDetail = maxLastUpdate.getCbDataDetail();
			
			RIReqInput rIReqInput = null;
			RIReqInputV2 rIReqInputV2 = null;
			pcbDataDetail= new PcbDataDetail();
			pcbDataDetailV2= new PcbDataDetailV2();
			
			if(lstMessageLog == null) {
				rIReqInput = JSONConverter.toObject(lstArchMessageLog.get(0).getMsgRequest(),RIReqInput.class);
				if(rIReqInput == null) {
				rIReqInputV2 = JSONConverter.toObject(lstArchMessageLog.get(0).getMsgRequest(),RIReqInputV2.class);	
				pcbDataDetailV2.setRequest(rIReqInputV2);
				pcbDataDetailV2.setResponse(pcbInfo1);
		    	cbDataDetail= JSONConverter.toJSON(pcbDataDetailV2).toString();
				}else {
				pcbDataDetail.setRequest(rIReqInput);
				pcbDataDetail.setResponse(pcbInfo1);
		    	cbDataDetail= JSONConverter.toJSON(pcbDataDetail).toString();
				}
		    }else {
		        rIReqInput = JSONConverter.toObject(lstMessageLog.get(0).getMsgRequest(),RIReqInput.class);	
		        if(rIReqInput == null) {
				rIReqInputV2 = JSONConverter.toObject(lstMessageLog.get(0).getMsgRequest(),RIReqInputV2.class);	
				pcbDataDetailV2.setRequest(rIReqInputV2);
				pcbDataDetailV2.setResponse(pcbInfo1);
		    	cbDataDetail= JSONConverter.toJSON(pcbDataDetailV2).toString();
				}else {
				pcbDataDetail.setRequest(rIReqInput);
				pcbDataDetail.setResponse(pcbInfo1);
		    	cbDataDetail= JSONConverter.toJSON(pcbDataDetail).toString();
				}
			}				
			
		    //update credit bureau data
			maxLastUpdate.setCbDataDetail(cbDataDetail);
		   _uok.pcb.creditBureauDataRepository().update(maxLastUpdate);
		}
		else{
			RIReqInput rIReqInput = null;
			RIReqInputV2 rIReqInputV2 = null;
			String cbDataDetail = maxLastUpdate.getCbDataDetail();
			pcbDataDetail = JSONConverter.toObject(maxLastUpdate.getCbDataDetail(), PcbDataDetail.class);				
			if(pcbDataDetail==null)	{
				pcbDataDetailV2 = JSONConverter.toObject(maxLastUpdate.getCbDataDetail(), PcbDataDetailV2.class);	
			}else {
				if(pcbDataDetail.getRequest()==null) {
					//tim kiem trong bang arch_message_log
					List<ArchMessageLog> lstArchMessageLog = _uok.common.archMessageLogRepo().getArchMessageLogFirstByTransId(maxLastUpdate.getId(),TypeChanel.PCB.value(),TransType.PCB_QUERY.value()); // get log theo buoc
					if(lstArchMessageLog.size()==0)
					//tim kiem trong bang message_log
					lstMessageLog = _uok.common.messageLogRepo().getMessageLogFirstByTransId(maxLastUpdate.getId(),TypeChanel.PCB.value(),TransType.PCB_QUERY.value()); // get log theo buoc	
					if(lstMessageLog == null) {
						rIReqInput = JSONConverter.toObject(lstArchMessageLog.get(0).getMsgRequest(),RIReqInput.class);
						if(rIReqInput==null) {
							rIReqInputV2 = JSONConverter.toObject(lstArchMessageLog.get(0).getMsgRequest(),RIReqInputV2.class);
							pcbDataDetailV2 = JSONConverter.toObject(maxLastUpdate.getCbDataDetail(), PcbDataDetailV2.class);
							pcbDataDetailV2.setRequest(rIReqInputV2);
							cbDataDetail= JSONConverter.toJSON(pcbDataDetailV2).toString();
						}else {
							pcbDataDetail.setRequest(rIReqInput);
							cbDataDetail= JSONConverter.toJSON(pcbDataDetail).toString();
						}
					}else{
						rIReqInput = JSONConverter.toObject(lstMessageLog.get(0).getMsgRequest(),RIReqInput.class);
						if(rIReqInput==null) {
							rIReqInputV2 = JSONConverter.toObject(lstMessageLog.get(0).getMsgRequest(),RIReqInputV2.class);
							pcbDataDetailV2 = JSONConverter.toObject(maxLastUpdate.getCbDataDetail(), PcbDataDetailV2.class);
							pcbDataDetailV2.setRequest(rIReqInputV2);
							cbDataDetail= JSONConverter.toJSON(pcbDataDetailV2).toString();
						}else {
							pcbDataDetail.setRequest(rIReqInput);
							cbDataDetail= JSONConverter.toJSON(pcbDataDetail).toString();
						}
					}
				   //update credit bureau data
				   maxLastUpdate.setCbDataDetail(cbDataDetail);
				   _uok.pcb.creditBureauDataRepository().update(maxLastUpdate);
				}
			}			
		 }	
		}
		
		if(maxLastUpdate != null && !Strings.isNullOrEmpty(appNumber)) {
			List<MessageLog> lstObjectAppIdByStep = _uok.common.messageLogRepo().getMessageLogByIdAndStepBmp(maxLastUpdate.getId(),appNumber,  stepBpm); // get log theo buoc
				ObjectAppIdByStep = lstObjectAppIdByStep.size() >0 ? lstObjectAppIdByStep.get(0):null;				
			}
				
			if (type.equals("VIEW")) {
				if(maxLastUpdate == null || ObjectAppIdByStep == null) {	    	   
					return new IdCheckPcbDTO(idCard, "");    // khong tim thay khach hang  
							   
			    }else {	    	  
			    	PcbInfo pcbInfo = JSONConverter.toObject(ObjectAppIdByStep.getMsgResponse(), PcbInfo.class);	 
			   		return new IdCheckPcbDTO(ObjectAppIdByStep.getId().toString(),idCard, pcbInfo.getHighest12MonthLoan(), pcbInfo.getHighest3YearLoan(), pcbInfo.getFinalcialCompanyAmount(), pcbInfo.getFinalComLoanTotal(), pcbInfo.getCountBank(),
			   				pcbInfo.getLoanMainTotal(),"");
			    }	
			}else if (type.equals("CHECK")) {
				//neu khong co thi la khach hang moi
				if (maxLastUpdate == null || (maxLastUpdate != null && Strings.isNullOrEmpty(maxLastUpdate.getCbkey()))) {
				//call PCB
				System.out.println("New Customer");	
				try {
					if(!checkCallPcbByS37LoanAmount(s37Result,loanAmount) && "B".equals(sourceReq)) {
						return new IdCheckPcbDTO(idCard,"Không truy vấn PCB");
					}
					System.out.println("Begin check RI PCB with idCard:"+idCard);
					resultApi =  _esbApi.pcbCheck(payload, BusinessConstant.PCB_CHECK_RI_REQ);
					System.out.println("End check RI PCB with idCard:"+idCard);
					// truong hop tao moi
					msgOrder= 1;					
				}catch(Exception ex) {
					Date responseTime = new Date();
					MessageLog messLog = new MessageLog(TypeChanel.MCP.value(), JSONConverter.toJSON(payload).toString(), null, new java.sql.Timestamp(processTime.getTime()), appNumber,
						PcbRespCode.PCBFAILCODE.value(), new java.sql.Timestamp(responseTime.getTime()), TypeChanel.PCB.value(), null,TransType.PCB_QUERY.value(),null, PcbCallStatus.PCBFAILCHECK.value(),stepBpm,"R",null);
						_uok.common.messageLogRepo().add(messLog);
						System.out.printf("Call api failed at 1 Exception:%s \n",ex.getMessage());
						return new IdCheckPcbDTO(idCard, "Có lỗi trong quá trình xử lý PCB");
				}
				} else {
					System.out.println("Old Customer");
					//ngay 11 thang T-1
					Date date11LastMonth = DateUtils.setDays(DateUtils.addMonths(requestTime, -1), 11);
					//ngay 11 thang T
					Date date11CurertMonth = DateUtils.setDays(DateUtils.addMonths(requestTime, 0), 11);
					//ngay 01 thang T
					Date date01CurrentMonth = DateUtils.setDays(DateUtils.addMonths(requestTime, 0), 1);
					//ngay 10 thang T
					Date date10CurrentMonth = DateUtils.setDays(DateUtils.addMonths(requestTime, 0), 10);	
						
					//test
//						requestTime = DateUtils.setDays(DateUtils.addMonths(requestTime, 0), 1);	//ngay 01
//						requestTime = DateUtils.setDays(DateUtils.addMonths(requestTime, 0), 5);	//ngay 05
//						requestTime = DateUtils.setDays(DateUtils.addMonths(requestTime, 0), 10);	//ngay 10
				        
					Date lastQuery = maxLastUpdate.getLastUpdatedDate();
				    if (getDecisionCallPcb(requestTime,lastQuery,date11LastMonth,date11CurertMonth)) {
				    	CiReqInputDTO ciReqInputDTO = new CiReqInputDTO(); 
						ciReqInputDTO.setCBSubjectCode(maxLastUpdate.getCbkey());
						payload = ciReqInputDTO;
						try {
							if(!checkCallPcbByS37LoanAmount(s37Result,loanAmount) && "B".equals(sourceReq)) {
								return new IdCheckPcbDTO(idCard,"Không truy vấn PCB");
							}
							System.out.println("Begin check CI PCB with idCard:"+idCard);
							resultApi =  _esbApi.pcbCheck(payload, BusinessConstant.PCB_CHECK_CI_REQ);
							// truong hop goi lai
							msgOrder= 1;	
							System.out.println("End check CI PCB with idCard:"+idCard);
						}catch(Exception ex) {
						    Date responseTime = new Date();
						    MessageLog messLog = new MessageLog(TypeChanel.MCP.value(), JSONConverter.toJSON(payload).toString(), null, new java.sql.Timestamp(processTime.getTime()), appNumber,
							PcbRespCode.PCBFAILCODE.value(), new java.sql.Timestamp(responseTime.getTime()), TypeChanel.PCB.value(), null,TransType.PCB_QUERY.value(),null, PcbCallStatus.PCBFAILCHECK.value(),stepBpm,"R",null);
							_uok.common.messageLogRepo().add(messLog);
							System.out.printf("Call api failed at 4 Exception:%s\n",ex.getMessage());
							return new IdCheckPcbDTO(idCard, "Có lỗi trong quá trình xử lý PCB");
						}
					} else if (getDicisionQueryMC(requestTime,lastQuery,date11CurertMonth,date11LastMonth,date01CurrentMonth,date10CurrentMonth)) {	
						 //thong tin MC : tim kiem trong mesage_log va tra ve				       
				         if(Strings.isNullOrEmpty(appNumber) && Strings.isNullOrEmpty(stepBpm)) {
				        	 PcbInfo pcbInfo = JSONConverter.toObject(maxLastUpdate.getCbDataDetail(), PcbInfo.class);				        
				        	 
				        	 return new IdCheckPcbDTO(maxLastUpdate.getId().toString(),idCard, pcbInfo.getHighest12MonthLoan(), pcbInfo.getHighest3YearLoan(), pcbInfo.getFinalcialCompanyAmount(), pcbInfo.getFinalComLoanTotal(), pcbInfo.getCountBank(),
				        	 pcbInfo.getLoanMainTotal(),"");		                	
				         }
				             Date responseTime = new Date();	
						     if (ObjectAppIdByStep == null)// neu null
						     {
						    	 PcbInfo response=null;
						    	 if(pcbDataDetail!=null) {
						    		 if(pcbDataDetail.getResponse()!=null) {
						    			 response=pcbDataDetail.getResponse();
						    		 }else {
						    		     response=pcbDataDetailV2.getResponse();
						    		 }
						    	 }else {
					    		     response=pcbDataDetailV2.getResponse();
					    		 }
						    	 MessageLog messageLog = new MessageLog(TypeChanel.MCP.value(), JSONConverter.toJSON(payloadMc).toString(), JSONConverter.toJSON(response).toString(), new java.sql.Timestamp(processTime.getTime()), appNumber,
						    	 PcbRespCode.PCBSUCCESSCODE.value(), new java.sql.Timestamp(responseTime.getTime()), TypeChanel.PCB.value(), maxLastUpdate.getId(),TransType.MC_QUERY.value(),maxLastUpdate.getCbkey(), PcbCallStatus.PCBSUCCESSCHECK.value(),stepBpm,"R",msgOrder);
						    	 _uok.common.messageLogRepo().add(messageLog);
								 PcbInfo pcbInfo = JSONConverter.toObject(messageLog.getMsgResponse(), PcbInfo.class);
						         return new IdCheckPcbDTO(messageLog.getId().toString(),idCard, pcbInfo.getHighest12MonthLoan(), pcbInfo.getHighest3YearLoan(), pcbInfo.getFinalcialCompanyAmount(), pcbInfo.getFinalComLoanTotal(), pcbInfo.getCountBank(),
						         pcbInfo.getLoanMainTotal(),"");
						     }else { //neu co roi thi get va tra ve
						    	 PcbInfo response=null;
						    	 if(pcbDataDetail!=null) {
						    		 if(pcbDataDetail.getResponse()!=null) {
						    			 response=pcbDataDetail.getResponse();
						    		 }else {
						    		     response=pcbDataDetailV2.getResponse();
						    		 }
						    	 }else {
					    		     response=pcbDataDetailV2.getResponse();
					    		 }
						        ObjectAppIdByStep.setResponseTime(new java.sql.Timestamp(processTime.getTime()));
						        ObjectAppIdByStep.setMsgResponse(JSONConverter.toJSON(response).toString());
						        ObjectAppIdByStep.setMsgRequest(JSONConverter.toJSON(payloadMc).toString());
						        ObjectAppIdByStep.setMsgOrder(msgOrder);
						        _uok.common.messageLogRepo().update(ObjectAppIdByStep);
						        PcbInfo pcbInfo = JSONConverter.toObject(ObjectAppIdByStep.getMsgResponse(), PcbInfo.class);
						        return new IdCheckPcbDTO(ObjectAppIdByStep.getId().toString(),idCard, pcbInfo.getHighest12MonthLoan(), pcbInfo.getHighest3YearLoan(), pcbInfo.getFinalcialCompanyAmount(), pcbInfo.getFinalComLoanTotal(), pcbInfo.getCountBank(),
						        pcbInfo.getLoanMainTotal(),"");
						}
					}
				}			
					       
					if(resultApi != null) {
						Date responseTime = new Date();	
						String bodyContent = resultApi.getBodyContent();
						Long creditBureauDataId = null;
						String	cbkey ="";
						
						if (resultApi.getStatus() && StringUtils.isNotEmpty(bodyContent)) {				

							// result = bodyContent;
							JSONObject jsonBodyContent = new JSONObject(bodyContent);
							try {
								cbkey = String.valueOf(jsonBodyContent.getJSONObject("Result").getJSONObject("Subject").getJSONObject("Inquired").get("CBSubjectCode"));
							}catch(Exception ex) {
								MessageLog messLog = new MessageLog(TypeChanel.MCP.value(), JSONConverter.toJSON(payload).toString(), bodyContent, new java.sql.Timestamp(processTime.getTime()), appNumber,
										String.valueOf(resultApi.getCode()), new java.sql.Timestamp(responseTime.getTime()), TypeChanel.PCB.value(), null,TransType.PCB_QUERY.value(),null, PcbCallStatus.PCBSUCCESSCHECK.value(),stepBpm,"R",null);
								_uok.common.messageLogRepo().add(messLog);
								System.out.printf("Get Cbkey Failed with jsonBodyContent :%s \n",jsonBodyContent);
								return new IdCheckPcbDTO(idCard, "Có lỗi trong quá trình xử lý PCB");
							}					
						
							// Lay cusId trong DB portal
							Long cusId = _uok.customer.customerPersonalInfoRepo().getCusIdByIdentityAndBirthDay(idCard,birthDay);
							// tim cbkey trong bang credit_bureau_data : neu co thi update , chua co thi insert					
							if (maxLastUpdate != null ) {
								//update
								String dataDetail = "";
								if(pcbDataDetail.getRequest()!=null) {
									pcbDataDetail.setResponse(JSONConverter.toObject(bodyContent, PcbInfo.class));
									if(Strings.isNullOrEmpty(maxLastUpdate.getCbkey())) pcbDataDetail.setRequest(JSONConverter.toObject(JSONConverter.toJSON(payloadMc), RIReqInput.class));
									dataDetail = JSONConverter.toJSON(pcbDataDetail).toString();
								}else {
									if(Strings.isNullOrEmpty(maxLastUpdate.getCbkey())) 
									{   PcbDataDetail pcbDataConvert = new PcbDataDetail();
										pcbDataConvert.setRequest(JSONConverter.toObject(JSONConverter.toJSON(payloadMc), RIReqInput.class));
										pcbDataConvert.setResponse(pcbDataDetailV2.getResponse());
										dataDetail = JSONConverter.toJSON(pcbDataConvert).toString();
									}else {
										pcbDataDetailV2.setResponse(JSONConverter.toObject(bodyContent, PcbInfo.class));
										dataDetail = JSONConverter.toJSON(pcbDataDetailV2).toString();
									}
								}
								maxLastUpdate.setCbDataDetail(dataDetail);
								maxLastUpdate.setLastUpdatedDate(requestTime);
								if(Strings.isNullOrEmpty(maxLastUpdate.getCbkey())) // truong hop khong tim thay ma kh/hang can xoa cb_key
								{
									maxLastUpdate.setCbkey(cbkey);
//									maxLastUpdate.setCreatedDate( new java.sql.Timestamp(new Date().getTime()));
								}       
								_uok.pcb.creditBureauDataRepository().update(maxLastUpdate);
								creditBureauDataId = maxLastUpdate.getId();
								System.out.printf("Update CreditBureauData=%s \n",creditBureauDataId);
							} else {
								//create	
								pcbDataDetail = new PcbDataDetail();
								pcbDataDetail.setRequest(JSONConverter.toObject(JSONConverter.toJSON(payload).toString(), RIReqInput.class));
								pcbDataDetail.setResponse(JSONConverter.toObject(bodyContent, PcbInfo.class));
								String dataDetail=JSONConverter.toJSON(pcbDataDetail).toString();
								CreditBureauData creditBureauDataSave = new CreditBureauData(requestTime, requestTime, cusId, cbkey, dataDetail, idCard, null,null);
								_uok.pcb.creditBureauDataRepository().add(creditBureauDataSave);
								creditBureauDataId = creditBureauDataSave.getId();
								maxLastUpdate = creditBureauDataSave;
								System.out.printf("Create CreditBureauData=%s \n",creditBureauDataId);
							}
						
						// Luu lai lich su call PCB vao Messelog (call success)
					    if(Strings.isNullOrEmpty(appNumber)) {
					    	stepBpm="SALE";
					    }
						MessageLog messLog = new MessageLog(TypeChanel.MCP.value(), JSONConverter.toJSON(payload).toString(), bodyContent, new java.sql.Timestamp(processTime.getTime()), appNumber,
								String.valueOf(resultApi.getCode()), new java.sql.Timestamp(responseTime.getTime()), TypeChanel.PCB.value(), creditBureauDataId,TransType.PCB_QUERY.value(),cbkey, PcbCallStatus.PCBSUCCESSCHECK.value(),stepBpm,"R",null);
						_uok.common.messageLogRepo().add(messLog);			
						
						// tao moi hoac update MC_QUERY
						if(!Strings.isNullOrEmpty(appNumber)) {
							//get MC_query cho 
							if(ObjectAppIdByStep == null) {//tao moi
								MessageLog messageLog = new MessageLog(TypeChanel.MCP.value(), JSONConverter.toJSON(payloadMc).toString(), bodyContent, new java.sql.Timestamp(processTime.getTime()), appNumber,
										String.valueOf(resultApi.getCode()), new java.sql.Timestamp(responseTime.getTime()), TypeChanel.PCB.value(), creditBureauDataId,TransType.MC_QUERY.value(),cbkey, PcbCallStatus.PCBSUCCESSCHECK.value(),stepBpm,"R",msgOrder);
								_uok.common.messageLogRepo().add(messageLog);
								ObjectAppIdByStep = messageLog;
							}else {//update
								ObjectAppIdByStep.setResponseTime(new java.sql.Timestamp(processTime.getTime()));	
								ObjectAppIdByStep.setMsgRequest(JSONConverter.toJSON(payloadMc).toString());
								ObjectAppIdByStep.setMsgOrder(msgOrder);
								ObjectAppIdByStep.setMsgResponse(bodyContent);
								_uok.common.messageLogRepo().update(ObjectAppIdByStep);
							}
						}
						
						System.out.println("Call PCB Success");		
					} else {
						if (maxLastUpdate != null ) {
							creditBureauDataId = maxLastUpdate.getId();
							cbkey = maxLastUpdate.getCbkey();
						}
						// Luu lai lich su call PCB vao Messelog (call fail)
						MessageLog messLog = new MessageLog(TypeChanel.MCP.value(), JSONConverter.toJSON(payload).toString(), bodyContent, new java.sql.Timestamp(processTime.getTime()), appNumber,
								String.valueOf(resultApi.getCode()), new java.sql.Timestamp(responseTime.getTime()), TypeChanel.PCB.value(), creditBureauDataId,TransType.PCB_QUERY.value(),cbkey, PcbCallStatus.PCBFAILCHECK.value(),stepBpm,"R",null);
						_uok.common.messageLogRepo().add(messLog);
						System.out.printf("Call PCB failed code:%s,message:%s",resultApi.getCode(),resultApi.getBodyContent());
						EsbResult<Result> objRsp = JSONConverter.toObject(resultApi.getBodyContent(),  new TypeToken<EsbResult<Result>>() {}.getType());
						String messageLog = objRsp.getResult().getReturnMes();
						String codeReturn ="";
						if(!Strings.isNullOrEmpty(messageLog)) {
							codeReturn= messageLog.substring(0,messageLog.indexOf("_"));
						}
						
						if("30".equals(codeReturn)) {
							maxLastUpdate.setCbkey("");
							_uok.pcb.creditBureauDataRepository().update(maxLastUpdate);
						}
						return new IdCheckPcbDTO(idCard,objRsp.getResult().getReturnMes());
						}		
					}			
				} 
			 
				
				PcbInfo pcbInfo = null;
				 if(Strings.isNullOrEmpty(appNumber)) {
					pcbInfo = JSONConverter.toObject(maxLastUpdate.getCbDataDetail(), PcbInfo.class);
					return new IdCheckPcbDTO(maxLastUpdate.getId().toString(),idCard, pcbInfo.getHighest12MonthLoan(), pcbInfo.getHighest3YearLoan(), pcbInfo.getFinalcialCompanyAmount(), pcbInfo.getFinalComLoanTotal(), pcbInfo.getCountBank(),
							pcbInfo.getLoanMainTotal(),"");
				}else  {
					pcbInfo = JSONConverter.toObject(ObjectAppIdByStep.getMsgResponse(), PcbInfo.class);
					return new IdCheckPcbDTO(ObjectAppIdByStep.getId().toString(),idCard, pcbInfo.getHighest12MonthLoan(), pcbInfo.getHighest3YearLoan(), pcbInfo.getFinalcialCompanyAmount(), pcbInfo.getFinalComLoanTotal(), pcbInfo.getCountBank(),
							pcbInfo.getLoanMainTotal(),"");
				}
	}	
	
	public Boolean checkCallPcbByS37LoanAmount(Integer s37Result,Double loanAmount) {
		if(s37Result==null || loanAmount==null) {
			return false;
		}
//		if((s37Result==6) || (s37Result==2) || (s37Result==3) || (s37Result==4 && loanAmount<5000000)) {
//			return false;   // khong call PCB
//		}else
		if((s37Result==1 && loanAmount<5000000) || (s37Result==5) || (s37Result==4 && loanAmount>=5000000) || (s37Result==1 && loanAmount>=5000000) || (s37Result==5 && loanAmount<5000000))  {
			return true;    // call PCB
		}		
		return false;
	}
	
	
	/*
	 * quyet dinh goi sang PCB
	 */
	public Boolean getDecisionCallPcb(Date requestTime, Date lastQuery, Date date11LastMonth,Date date11CurertMonth) {
		if(compareDateIgnoreTime(lastQuery,date11LastMonth) || (
    			compareDateIgnoreTime(lastQuery,date11LastMonth) == false  						//Ngày 11 tháng T -1  <= Last Query < 11 tháng T va Date_Query >= Ngày 11 tháng T
				&& compareDateIgnoreTime(lastQuery,date11CurertMonth) 
				&& compareDateIgnoreTime(requestTime,date11CurertMonth) == false  
    	)) { 			
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * quyet dinh truy van trong DB cua MC
	 */
	public Boolean getDicisionQueryMC(Date requestTime,Date lastQuery,Date date11CurertMonth,Date date11LastMonth,Date date01CurrentMonth,Date date10CurrentMonth) {
		 if (compareDateIgnoreTime(lastQuery,date11CurertMonth)== false || (
     			compareDateIgnoreTime(lastQuery,date11LastMonth) == false 						//Ngày 11 tháng T -1 <= Last Query < 11 tháng T va Ngày 01 tháng T <= Date_Query <= Ngày 10 tháng T
					&& compareDateIgnoreTime(lastQuery,date11CurertMonth) 
					&& compareDateIgnoreTime(requestTime,date01CurrentMonth) == false 
					&&  compareDateIgnoreTime(date10CurrentMonth,requestTime) == false
     			)) {
			 return true;
		 }else {
			 return false;
		 }
	}

	/**
	 * @author sonhv.ho
	 * @param id : id cua bang CREDIT_BUREAU_DATA
	 * @return PcbResponse : Thong tin check PCB
	 * @throws Exception
	 */
	public PcbResponse getPcbInfo(String id,String channel) throws Exception {
		PcbInfo pcbInfo = null;
		PcbDataDetail pcbDataDetail = null;
		PcbDataDetailV2 pcbDataDetailV2= null;
		CreditBureauData  creditBureauData=null;
		Integer orderMsg = null;
		if("P".equals(channel)) {
			creditBureauData = _uok.pcb.creditBureauDataRepository().getById(Long.valueOf(id));
		}else {
			MessageLog messageLog = _uok.common.messageLogRepo().getMessageByMessId(Long.valueOf(id));
			orderMsg= messageLog.getMsgOrder();
			pcbInfo = JSONConverter.toObject(messageLog.getMsgResponse(), PcbInfo.class);
			creditBureauData = _uok.pcb.creditBureauDataRepository().getById(Long.valueOf(messageLog.getTransId()));
		}
		// convert data detail
		pcbDataDetail = JSONConverter.toObject(creditBureauData.getCbDataDetail(), PcbDataDetail.class);
		if(pcbDataDetail==null){
			pcbDataDetailV2 = JSONConverter.toObject(creditBureauData.getCbDataDetail(), PcbDataDetailV2.class);
		}
		
		System.out.println("Begin get info PCB with id, channel" + id +","+channel);
		if("P".equals(channel)) {
			System.out.println("Begin check nguon Portal ");		
			if(pcbDataDetail!=null) {
				return convertPcbResponseFromPcbInfo(pcbDataDetail,null,creditBureauData.getLastUpdatedDate());
			}else {
				return convertPcbResponseFromPcbInfoV2(pcbDataDetailV2,null,creditBureauData.getLastUpdatedDate());
			}
		}else {
			System.out.println("Begin check nguon BPM ");			
			if(pcbDataDetail!=null) {
				pcbDataDetail.setResponse(pcbInfo);
				return convertPcbResponseFromPcbInfo(pcbDataDetail,orderMsg,creditBureauData.getLastUpdatedDate());
			}else {
				pcbDataDetailV2.setResponse(pcbInfo);
				return convertPcbResponseFromPcbInfoV2(pcbDataDetailV2,orderMsg,creditBureauData.getLastUpdatedDate());
			}
		}		
	}
	
	/**
	 * @author truongvd.ho
	 * @param id : id cua bang CREDIT_BUREAU_DATA
	 * @return 
	 * @throws Exception
	 */
	
//	private PcbResponse getByFirstMsgRequest(Long id,PcbInfo pcbInfo,Integer msgOrder) throws Exception {
//		Gson g = new Gson();
//		//tim request cua trans_id lan dau tien sang PCB
//		List<MessageLog> lstObjectAppIdByStep = _uok.common.messageLogRepo().getMessageLogFirstByTransId(Long.valueOf(id),TypeChanel.PCB.value(),TransType.PCB_QUERY.value()); // get log theo buoc	
//		MessageLog  messageLog2 = lstObjectAppIdByStep.size() >0 ? lstObjectAppIdByStep.get(0):null;
//		PcbInfo msgFirstRsp = g.fromJson(messageLog2.getMsgResponse(),  PcbInfo.class);
//		return convertPcbResponseFromPcbInfo(pcbInfo,msgFirstRsp,msgOrder);		
//	}
	
	
	/**
	 * Lay thong tin PCB goc
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PcbInfo getPcbRawInfo(String id) throws Exception {
		PcbDataDetail pcbDataDetail = null;
		PcbDataDetailV2 pcbDataDetailV2= null;
		CreditBureauData creditBureauData = _uok.pcb.creditBureauDataRepository().getById(Long.valueOf(id));
		try {
			pcbDataDetail = JSONConverter.toObject(creditBureauData.getCbDataDetail(), PcbDataDetail.class);
			return pcbDataDetail.getResponse();
		}catch(Exception ex) {
			System.out.println("Convert CbDataDetail exception:" + ex.getMessage());	
			pcbDataDetailV2 = JSONConverter.toObject(creditBureauData.getCbDataDetail(), PcbDataDetailV2.class);
			return pcbDataDetailV2.getResponse();
		}		
	}

	private Boolean compareDateIgnoreTime(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		calendar1.set(Calendar.HOUR_OF_DAY, 0);
		calendar1.set(Calendar.MINUTE, 0);
		calendar1.set(Calendar.SECOND, 0);
		calendar1.set(Calendar.MILLISECOND, 0);

		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		calendar2.set(Calendar.HOUR_OF_DAY, 0);
		calendar2.set(Calendar.MINUTE, 0);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MILLISECOND, 0);
		return calendar2.after(calendar1);
	}
	
//	private PcbResponse convertPcbResponseFromPcbInfo(PcbInfo pcbInfo,PcbInfo msgFirstRsp,Integer msgOrder) throws Exception {
//
//		PcbResponseCommonInfo pcbResponseCommonInfo;
//		pcbResponseCommonInfo = new PcbResponseCommonInfo(msgFirstRsp.getCusNameRequest(),msgFirstRsp.getIdentityRequest(),msgFirstRsp.getBirthDateRequest(),msgFirstRsp.getMainAddressRequest(),msgFirstRsp.getGenderRequest(), pcbInfo.getPcbCode(),pcbInfo.getCusName(), pcbInfo.getIdentityCode(),pcbInfo.getIdentityCode2(), pcbInfo.getBirthDate(), pcbInfo.getMainAddress(),
//				pcbInfo.getAdditionalAddress(), pcbInfo.getDocumentList(), pcbInfo.getReferenceNumber(), pcbInfo.getCountBank(), pcbInfo.getLoanMainTotal(), pcbInfo.getLoanGuaranteeTotal(),
//				pcbInfo.getFinalcialCompanyAmount(), pcbInfo.getFinalComLoanTotal(), pcbInfo.getCreditComRequestAmount(), pcbInfo.getCreditComRejectAmount(), pcbInfo.getHighest12MonthLoan(),
//				pcbInfo.getHighest3YearLoan(), pcbInfo.getOutOfDateMax());
//		PcbResponseLoan pcbResponseLoan = new PcbResponseLoan(pcbInfo.getInstalmentsNumberOfRequested(), pcbInfo.getInstalmentsNumberOfRefused(), pcbInfo.getInstalmentsNumberOfRenounced(),
//				pcbInfo.getInstalmentsNumberOfLiving(), pcbInfo.getInstalmentsNumberOfTerminated(), pcbInfo.getCardsNumberOfRequested(), pcbInfo.getCardsNumberOfRefused(),
//				pcbInfo.getCardsNumberOfRenounced(), pcbInfo.getCardsNumberOfLiving(), pcbInfo.getCardsNumberOfTerminated(), pcbInfo.getNonInstalmentsNumberOfRequested(),
//				pcbInfo.getNonInstalmentsNumberOfRefused(), pcbInfo.getNonInstalmentsNumberOfRenounced(), pcbInfo.getNonInstalmentsNumberOfLiving(), pcbInfo.getNonInstalmentsNumberOfTerminated(),
//				pcbInfo.getMainMonthlyInstalmentsAmount(), pcbInfo.getGuardMonthlyInstalmentsAmount(), pcbInfo.getMainRemainingInstalmentsAmount(), pcbInfo.getGuradRemainingInstalmentsAmount(),
//				pcbInfo.getMainUnpaidDueInstalmentsAmount(), pcbInfo.getGuardUnpaidDueInstalmentsAmount(), pcbInfo.getMainLimitOfCredit(), pcbInfo.getGuradLimitOfCredit(),
//				pcbInfo.getMainResidualAmount(), pcbInfo.getGuardResidualAmount(), pcbInfo.getMainOverDueAmount(), pcbInfo.getGuardOverDueAmount(), pcbInfo.getMainCreditLimit(),
//				pcbInfo.getGuardCreditLimit(), pcbInfo.getMainUtilization(), pcbInfo.getGuardUtilization(), pcbInfo.getMainOverdraft(), pcbInfo.getGuardOverdraft());
//
//		PcbResponseCreditInfo pcbResponseCreditInfo = new PcbResponseCreditInfo(pcbInfo.getCreditInstitutionsAmount(), pcbInfo.getTypeCurrency(), pcbInfo.getMaxWorstStatus(),
//				pcbInfo.getScoreRangeCode(), pcbInfo.getScoreScoreRaw(), pcbInfo.getScoreDescription(), pcbResponseLoan, pcbInfo.getInstalmentsGrantedContractLiving(),
//				pcbInfo.getCardsGrantedContractLiving(), pcbInfo.getNonInstalmentsGrantedContractLiving(), pcbInfo.getInstalmentsGrantedContractEnd(), pcbInfo.getCardsGrantedContractEnd(),
//				pcbInfo.getNonInstalmentsGrantedContractEnd(), pcbInfo.getNotGrantedContract());
//
//		PcbResponse pcbResponse = new PcbResponse(pcbResponseCommonInfo, pcbResponseCreditInfo,msgOrder);
//		System.out.println("End get info PCB");
//		return pcbResponse;
//
//	}

	private PcbResponse convertPcbResponseFromPcbInfo(PcbDataDetail pcbDataDetail,Integer msgOrder, Date createDate) throws Exception {

		PcbResponseCommonInfo pcbResponseCommonInfo;
		PcbInfo pcbInfo= pcbDataDetail.getResponse();
		pcbResponseCommonInfo = new PcbResponseCommonInfo(pcbDataDetail.getRequest().getSubject().getPerson().getName(),pcbDataDetail.getRequest().getSubject().getPerson().getIDCard() ,
				pcbDataDetail.getRequest().getSubject().getPerson().getDateOfBirth(),pcbDataDetail.getRequest().getSubject().getPerson().getAddress().getMain().getFullAddress(),pcbDataDetail.getRequest().getSubject().getPerson().getGender(),
				pcbInfo.getCusName(), pcbInfo.getPcbCode(), pcbInfo.getIdentityCode(),pcbInfo.getIdentityCode2(), pcbInfo.getBirthDate(), pcbInfo.getMainAddress(),
				pcbInfo.getAdditionalAddress(), pcbInfo.getDocumentList(), pcbInfo.getReferenceNumber(), pcbInfo.getCountBank(), pcbInfo.getLoanMainTotal(), pcbInfo.getLoanGuaranteeTotal(),
				pcbInfo.getFinalcialCompanyAmount(), pcbInfo.getFinalComLoanTotal(), pcbInfo.getCreditComRequestAmount(), pcbInfo.getCreditComRejectAmount(), pcbInfo.getHighest12MonthLoan(),
				pcbInfo.getHighest3YearLoan(), pcbInfo.getOutOfDateMax());
		PcbResponseLoan pcbResponseLoan = new PcbResponseLoan(pcbInfo.getInstalmentsNumberOfRequested(), pcbInfo.getInstalmentsNumberOfRefused(), pcbInfo.getInstalmentsNumberOfRenounced(),
				pcbInfo.getInstalmentsNumberOfLiving(), pcbInfo.getInstalmentsNumberOfTerminated(), pcbInfo.getCardsNumberOfRequested(), pcbInfo.getCardsNumberOfRefused(),
				pcbInfo.getCardsNumberOfRenounced(), pcbInfo.getCardsNumberOfLiving(), pcbInfo.getCardsNumberOfTerminated(), pcbInfo.getNonInstalmentsNumberOfRequested(),
				pcbInfo.getNonInstalmentsNumberOfRefused(), pcbInfo.getNonInstalmentsNumberOfRenounced(), pcbInfo.getNonInstalmentsNumberOfLiving(), pcbInfo.getNonInstalmentsNumberOfTerminated(),
				pcbInfo.getMainMonthlyInstalmentsAmount(), pcbInfo.getGuardMonthlyInstalmentsAmount(), pcbInfo.getMainRemainingInstalmentsAmount(), pcbInfo.getGuradRemainingInstalmentsAmount(),
				pcbInfo.getMainUnpaidDueInstalmentsAmount(), pcbInfo.getGuardUnpaidDueInstalmentsAmount(), pcbInfo.getMainLimitOfCredit(), pcbInfo.getGuradLimitOfCredit(),
				pcbInfo.getMainResidualAmount(), pcbInfo.getGuardResidualAmount(), pcbInfo.getMainOverDueAmount(), pcbInfo.getGuardOverDueAmount(), pcbInfo.getMainCreditLimit(),
				pcbInfo.getGuardCreditLimit(), pcbInfo.getMainUtilization(), pcbInfo.getGuardUtilization(), pcbInfo.getMainOverdraft(), pcbInfo.getGuardOverdraft());

		PcbResponseCreditInfo pcbResponseCreditInfo = new PcbResponseCreditInfo(pcbInfo.getCreditInstitutionsAmount(), pcbInfo.getTypeCurrency(), pcbInfo.getMaxWorstStatus(),
				pcbInfo.getScoreRangeCode(), pcbInfo.getScoreScoreRaw(), pcbInfo.getScoreDescription(), pcbResponseLoan, pcbInfo.getInstalmentsGrantedContractLiving(),
				pcbInfo.getCardsGrantedContractLiving(), pcbInfo.getNonInstalmentsGrantedContractLiving(), pcbInfo.getInstalmentsGrantedContractEnd(), pcbInfo.getCardsGrantedContractEnd(),
				pcbInfo.getNonInstalmentsGrantedContractEnd(), pcbInfo.getNotGrantedContract());

		PcbResponse pcbResponse = new PcbResponse(pcbResponseCommonInfo, pcbResponseCreditInfo,msgOrder,createDate);
		System.out.println("End get info PCB");
		return pcbResponse;

	}
	
	private PcbResponse convertPcbResponseFromPcbInfoV2(PcbDataDetailV2 pcbDataDetailV2,Integer msgOrder,Date createDate) throws Exception {

		PcbResponseCommonInfo pcbResponseCommonInfo;
		PcbInfo pcbInfo= pcbDataDetailV2.getResponse();
		pcbResponseCommonInfo = new PcbResponseCommonInfo(pcbDataDetailV2.getRequest().getSubject().getPerson().getName(),pcbDataDetailV2.getRequest().getSubject().getPerson().getIDCard(),
				pcbDataDetailV2.getRequest().getSubject().getPerson().getDateOfBirth(),pcbDataDetailV2.getRequest().getSubject().getPerson().getAddress(),pcbDataDetailV2.getRequest().getSubject().getPerson().getGender(),
				pcbInfo.getCusName(), pcbInfo.getPcbCode(), pcbInfo.getIdentityCode(),pcbInfo.getIdentityCode2(),pcbInfo.getBirthDate(), pcbInfo.getMainAddress(),
				pcbInfo.getAdditionalAddress(), pcbInfo.getDocumentList(), pcbInfo.getReferenceNumber(), pcbInfo.getCountBank(), pcbInfo.getLoanMainTotal(), pcbInfo.getLoanGuaranteeTotal(),
				pcbInfo.getFinalcialCompanyAmount(), pcbInfo.getFinalComLoanTotal(), pcbInfo.getCreditComRequestAmount(), pcbInfo.getCreditComRejectAmount(), pcbInfo.getHighest12MonthLoan(),
				pcbInfo.getHighest3YearLoan(), pcbInfo.getOutOfDateMax());
		PcbResponseLoan pcbResponseLoan = new PcbResponseLoan(pcbInfo.getInstalmentsNumberOfRequested(), pcbInfo.getInstalmentsNumberOfRefused(), pcbInfo.getInstalmentsNumberOfRenounced(),
				pcbInfo.getInstalmentsNumberOfLiving(), pcbInfo.getInstalmentsNumberOfTerminated(), pcbInfo.getCardsNumberOfRequested(), pcbInfo.getCardsNumberOfRefused(),
				pcbInfo.getCardsNumberOfRenounced(), pcbInfo.getCardsNumberOfLiving(), pcbInfo.getCardsNumberOfTerminated(), pcbInfo.getNonInstalmentsNumberOfRequested(),
				pcbInfo.getNonInstalmentsNumberOfRefused(), pcbInfo.getNonInstalmentsNumberOfRenounced(), pcbInfo.getNonInstalmentsNumberOfLiving(), pcbInfo.getNonInstalmentsNumberOfTerminated(),
				pcbInfo.getMainMonthlyInstalmentsAmount(), pcbInfo.getGuardMonthlyInstalmentsAmount(), pcbInfo.getMainRemainingInstalmentsAmount(), pcbInfo.getGuradRemainingInstalmentsAmount(),
				pcbInfo.getMainUnpaidDueInstalmentsAmount(), pcbInfo.getGuardUnpaidDueInstalmentsAmount(), pcbInfo.getMainLimitOfCredit(), pcbInfo.getGuradLimitOfCredit(),
				pcbInfo.getMainResidualAmount(), pcbInfo.getGuardResidualAmount(), pcbInfo.getMainOverDueAmount(), pcbInfo.getGuardOverDueAmount(), pcbInfo.getMainCreditLimit(),
				pcbInfo.getGuardCreditLimit(), pcbInfo.getMainUtilization(), pcbInfo.getGuardUtilization(), pcbInfo.getMainOverdraft(), pcbInfo.getGuardOverdraft());

		PcbResponseCreditInfo pcbResponseCreditInfo = new PcbResponseCreditInfo(pcbInfo.getCreditInstitutionsAmount(), pcbInfo.getTypeCurrency(), pcbInfo.getMaxWorstStatus(),
				pcbInfo.getScoreRangeCode(), pcbInfo.getScoreScoreRaw(), pcbInfo.getScoreDescription(), pcbResponseLoan, pcbInfo.getInstalmentsGrantedContractLiving(),
				pcbInfo.getCardsGrantedContractLiving(), pcbInfo.getNonInstalmentsGrantedContractLiving(), pcbInfo.getInstalmentsGrantedContractEnd(), pcbInfo.getCardsGrantedContractEnd(),
				pcbInfo.getNonInstalmentsGrantedContractEnd(), pcbInfo.getNotGrantedContract());

		PcbResponse pcbResponse = new PcbResponse(pcbResponseCommonInfo, pcbResponseCreditInfo,msgOrder,createDate);
		System.out.println("End get info PCB");
		return pcbResponse;

	}
}
