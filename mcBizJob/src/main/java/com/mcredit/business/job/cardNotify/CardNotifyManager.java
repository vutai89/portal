package com.mcredit.business.job.cardNotify;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.mcredit.business.common.MessageAggregate;
import com.mcredit.business.credit.CreditAggregate;
import com.mcredit.business.job.JobManagerBase;
import com.mcredit.business.job.dto.CardNotifyRespondDTO;
import com.mcredit.common.DateUtils;
import com.mcredit.common.UnitOfWorkHelper;
import com.mcredit.data.UnitOfWork;
import com.mcredit.data.common.UnitOfWorkCommon;
import com.mcredit.data.common.entity.MessageLog;
import com.mcredit.data.common.entity.NotificationTemplate;
import com.mcredit.data.common.entity.Parameters;
import com.mcredit.data.credit.UnitOfWorkCredit;
import com.mcredit.model.dto.CardNotifyDTO;
import com.mcredit.model.dto.ErrorResultDTO;
import com.mcredit.model.dto.SendSMSDTO;
import com.mcredit.model.dto.TemplatePanttern;
import com.mcredit.model.enums.MsgOrder;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.MsgTransType;
import com.mcredit.model.enums.MsgType;
import com.mcredit.model.enums.TemplatePantternEnum;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.util.TemplatePantternUtil;
import com.mcredit.util.DateUtil;

public class CardNotifyManager extends JobManagerBase {
	
	
	@Override
	public Object runJob() throws Exception {
		
		UnitOfWork uok = new  UnitOfWork();
		
		return UnitOfWorkHelper.tryCatch(uok, ()->{
			CardNotifyRespondDTO CRD = new CardNotifyRespondDTO();
			
			Parameters cardNotifyDate = uok.common.parametersRepo().findParameter(BusinessConstant.PARAM_CARD_NOTIFY_DATE);
			Date notifyDate = DateUtil.toDate(cardNotifyDate.getParamValue(), "yyyyMMdd");
			if (!DateUtil.isToday(notifyDate)) {
				return new ErrorResultDTO("200", "Today is not the date");
				
			}/**/
			
			CreditAggregate creditAgg = new CreditAggregate(uok.credit);
			
			List<MessageLog> MsgLogList = new ArrayList<MessageLog>();
			
			creditAgg.getCardNotifyMinList();
			NotificationTemplate nt = uok.common.notificationTemplateRepo().findByCode(BusinessConstant.NOTIFY_TEMPLATE_CARD_MIN_SMS);
			MsgLogList.addAll(genMSGLog(nt.getNotificationTemplate(),creditAgg.getCardNotifyList()));
			CRD.setTotalMin(MsgLogList.size());
			
			creditAgg.getCardNotifyDueList();
			nt = uok.common.notificationTemplateRepo().findByCode(BusinessConstant.NOTIFY_TEMPLATE_CARD_DUE_SMS);
			MsgLogList.addAll(genMSGLog(nt.getNotificationTemplate(), creditAgg.getCardNotifyList()));
			CRD.setTotalDue(MsgLogList.size()-CRD.getTotalMin());
			
			MessageAggregate msgAgg = new MessageAggregate(uok.common);
			if (MsgLogList.size() != 0) {
				msgAgg.setMessageLog(MsgLogList);
				msgAgg.upsertBatchMessageLog();
			}
			
			List<com.mcredit.data.common.entity.Calendar> calendarList = uok.common.calendarRepo().findCalendarByCategory(BusinessConstant.CALENDAR_CARD_NOTIFY_DATE);
			Date validDate = DateUtils.getNextValidDate(calendarList);
			String validDateString = DateUtil.toString(validDate, "yyyyMMdd");
			cardNotifyDate.setParamValue(validDateString);
			
			//lam cai gi o day nhi trong khi ham update khong lam gi trong day 
			//uok.parametersRepository().update(cardNotifyDate);
			
			uok.common.parametersRepo().merge(cardNotifyDate);
			
			return CRD;	
		});
			
	}
	
	private List<MessageLog> genMSGLog(String notificationTemplate, List<CardNotifyDTO> cndList) {
		// TODO Auto-generated method stub
        String panttern = "<\\w\\|\\w+\\|[A-Za-z0-9#.,-/]*>";
        Pattern r = Pattern.compile(panttern);
        Matcher m = r.matcher(notificationTemplate);
        final List<String> matches = new ArrayList<String>();
        while (m.find()) {
            matches.add(m.group(0));
        }
        
        
        List<TemplatePanttern> tpList = new ArrayList<TemplatePanttern>();
        for (String match : matches) {
        	TemplatePanttern tp = TemplatePantternUtil.toPanttern(match);
        	if (tp!=null) tpList.add(tp);
        }
                
        List<MessageLog> result = new ArrayList<MessageLog>();
        
        for (CardNotifyDTO cnd: cndList) {
        	PantternTranslate(tpList, cnd);
        	SendSMSDTO sms = new SendSMSDTO();
        	sms.setContent(ContentGenerate(notificationTemplate, tpList));
        	sms.setSenderSystem(BusinessConstant.MCP);
        	sms.setSendImmidiately(true);
        	sms.setReceiverID(cnd.getMobileNumber());
        	sms.setSmsType(BusinessConstant.SMS_TYPE_CARD_NOTIFY);
        	JSONObject msgPayload = new JSONObject();
        	msgPayload.put(BusinessConstant.SMS_JSON_ROOT, new JSONObject(sms));
			MessageLog msgLog = new MessageLog();
			msgLog.setFromChannel(BusinessConstant.MCP);
			msgLog.setMsgOrder(MsgOrder.ONE.value());
			msgLog.setMsgRequest(msgPayload.toString());
			msgLog.setMsgStatus(MsgStatus.NEW.value());
			msgLog.setRelationId(cnd.getCardId());
			msgLog.setTransType(MsgTransType.SMS.value());
			msgLog.setToChannel(BusinessConstant.MSG_TO_CHANNEL_SMS_GATEWAY);
			msgLog.setServiceName(BusinessConstant.MSG_SERVICE_NAME_CARD_NOTIFY);
			msgLog.setMsgType(MsgType.REQUEST.value());

        	result.add(msgLog);
        }
        
		return result;
	}
	
	private String ContentGenerate(String notificationTemplate, List<TemplatePanttern> tpList) {
		String result = notificationTemplate;
		for (TemplatePanttern tp: tpList) {
			if (tp.getOriginal()==null || tp.getReplacer() == null) continue;
			result = result.replace(tp.getOriginal(), tp.getReplacer());
		}
		return result;
	}
	
	private void PantternTranslate(List<TemplatePanttern> tpList, CardNotifyDTO cnd) {
        Date dueDate = DateUtil.getDayOfThisMonth(25);
        DecimalFormat df;
		for (TemplatePanttern tp: tpList) {
			switch (TemplatePantternEnum.valueOf(tp.getName())) {
				case DUE_DATE:
					SimpleDateFormat dateFormat = new SimpleDateFormat(tp.getFormat());
					tp.setReplacer(dateFormat.format(dueDate));
					break;
				case CARD_ID:
					tp.setReplacer(cnd.getCardId());
					break;
				case DUE_BALANCE:
					df = new DecimalFormat(tp.getFormat());
					tp.setReplacer(df.format( - cnd.getDueBalance()));
					break;
				case REMAIN_MIN_AMOUNT:
					df = new DecimalFormat(tp.getFormat());
					tp.setReplacer(df.format( - cnd.getRemainMinBalance()));
					break;
			}
		}
	}
	
	public void testRun() throws ParseException {
		UnitOfWorkCommon uok = new  UnitOfWorkCommon();
		uok.start();

		List<com.mcredit.data.common.entity.Calendar> calendarList = uok.calendarRepo().findCalendarByCategory(BusinessConstant.CALENDAR_CARD_NOTIFY_DATE);
		Date validDate = DateUtils.getNextValidDate(calendarList);
		String validDateString = DateUtil.toString(validDate, "yyyyMMdd");
		System.out.println(validDateString);
	}
	
	public static void main(final String[] args) throws Exception {
		CardNotifyManager manager = new CardNotifyManager();
		manager.testRun();
//		manager.runJob();
		manager.close();
		

	}

	@Override
	public void close() throws IOException {
	
	}
}
