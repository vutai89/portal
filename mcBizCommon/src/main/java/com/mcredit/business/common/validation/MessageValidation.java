package com.mcredit.business.common.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.common.MessageLogDTO;
import com.mcredit.model.dto.common.MessageTaskDTO;
import com.mcredit.model.enums.CommonValidationLength;
import com.mcredit.model.enums.CreditValidationLength;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.MsgType;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;


public class MessageValidation extends AbstractValidation {

	public void validateMessageLog(MessageLogDTO item, String updateId) throws ValidationException {
		if (item != null) {
			if (updateId != null && (!StringUtils.isNumberic(updateId) || Long.parseLong(updateId) == 0))
				getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.common.queryParam.isNumber")));
			
			
			if(!StringUtils.isNullOrEmpty(item.getMsgType()) && item.getMsgType().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_MSGTYPE.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isMsgType"), CreditValidationLength.MAX_LEN_MESSAGELOG_MSGTYPE.value()));
				
			if( !StringUtils.isNullOrEmpty(item.getMsgType()) && 
					!(item.getMsgType().equals(MsgType.REQUEST.value())
					||item.getMsgType().equals(MsgType.REVERT.value())))
				getMessageDes().add(Messages.getString("validation.field.type", Labels.getString("label.common.messagelog.isMsgType"),MsgType.REQUEST.value() + " : " + MsgType.REVERT.value()));
						
			if( !StringUtils.isNullOrEmpty(item.getFromChannel()) && item.getFromChannel().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_FROMCHANNEL.value() )
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isFromChannel"), CreditValidationLength.MAX_LEN_MESSAGELOG_FROMCHANNEL.value()));
			
			if( !StringUtils.isNullOrEmpty(item.getToChannel()) && item.getToChannel().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_TOCHANNEL.value() )
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isToChannel"), CreditValidationLength.MAX_LEN_MESSAGELOG_TOCHANNEL.value()));
			
			/*if( !StringUtils.isNullOrEmpty(item.getRelationId()) && item.getRelationId().length() > IValidationConstants.MAX_LEN_MESSAGELOG_RELATIONID )
				getMessageDescCollection().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isRelationId"), IValidationConstants.MAX_LEN_MESSAGELOG_RELATIONID));*/
						
			if( item.getTransId() != null && item.getTransId().toString().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_TRANSID.value() )
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isTransId"), CreditValidationLength.MAX_LEN_MESSAGELOG_TRANSID.value()));
			
			if( !StringUtils.isNullOrEmpty(item.getTransType()) && item.getTransType().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_TRANSTYPE.value() )
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isTransType"), CreditValidationLength.MAX_LEN_MESSAGELOG_TRANSTYPE.value()));
			
			if( !StringUtils.isNullOrEmpty(item.getServiceName()) && item.getServiceName().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_GETSERVICENAME.value() )
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isServiceName"), CreditValidationLength.MAX_LEN_MESSAGELOG_GETSERVICENAME.value()));
			
			if( !StringUtils.isNullOrEmpty(item.getResponseCode()) && item.getResponseCode().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_RESPONSECODE.value() )
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isResponseCode"), CreditValidationLength.MAX_LEN_MESSAGELOG_RESPONSECODE.value()));
			
			if( !StringUtils.isNullOrEmpty(item.getResponseErrorDesc()) && item.getResponseErrorDesc().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_RESPONSEERRORDESC.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isResponseErrorDesc"), CreditValidationLength.MAX_LEN_MESSAGELOG_RESPONSEERRORDESC.value()));
			
			if( (item.getMsgOrder()!= null)  && item.getMsgOrder().toString().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_MSGORDER.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isMsgOrder"), CreditValidationLength.MAX_LEN_MESSAGELOG_MSGORDER.value()));
			
			if( !StringUtils.isNullOrEmpty(item.getMsgRequest())  && item.getMsgRequest().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_MSGREQUEST.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isMsgRequest"), CreditValidationLength.MAX_LEN_MESSAGELOG_MSGREQUEST.value()));
			
			if( !StringUtils.isNullOrEmpty(item.getMsgResponse())  && item.getMsgResponse().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_MSGRESPONSE.value())
				getMessageDes().add(Messages.getString("validation.field.length", Labels.getString("label.common.messagelog.isMsgResponse"), CreditValidationLength.MAX_LEN_MESSAGELOG_MSGRESPONSE.value()));
			
			if((!StringUtils.isNullOrEmpty(item.getMsgStatus()) &&
					!(item.getMsgStatus().equals(MsgStatus.NEW.value())
					||item.getMsgStatus().equals(MsgStatus.SUCCESS.value())
					||item.getMsgStatus().equals(MsgStatus.ERROR.value())
					||item.getMsgStatus().equals(MsgStatus.TIME_OUT.value())
					||item.getMsgStatus().equals(MsgStatus.IGNORE.value())))){
				getMessageDes().add(Messages.getString("validation.field.type", Labels.getString("label.common.messagelog.isMsgStatus"),MsgStatus.NEW.value() + " : " + MsgStatus.SUCCESS.value() +" : " +MsgStatus.TIME_OUT.value() + " : " + MsgStatus.ERROR.value()  + " : " + MsgStatus.IGNORE.value()));
			}			
			
		}
		if(item  == null)
			getMessageDes().add(Messages.getString("validation.field.invalidFormat", Labels.getString("label.common.queryParam.isNumber")));
			
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
		
	}

	public void validateMessageTask(MessageTaskDTO request, String updateId) throws ValidationException {
		if (request != null) {
			/*
			 * if(!StringUtils.isNullOrEmpty(request.getTaskType()) &&
			 * request.getTaskType().length() >
			 * IValidationConstants.MAX_LEN_MESSAGETASK_TASKTYPE){
			 * getMessageDescCollection
			 * ().add(Messages.getString("validation.field.length",
			 * Labels.getString("label.customer.messageTask.isTaskType"),
			 * IValidationConstants.MAX_LEN_MESSAGETASK_TASKTYPE)); }
			 */
			if (!StringUtils.isNullOrEmpty(request.getTransType())
					&& request.getTransType().length() > CommonValidationLength.MAX_LEN_MESSAGETASK_TRANSTYPE.value()) {
				getMessageDes()
						.add(Messages.getString( "validation.field.length", Labels.getString("label.customer.messageTask.isTransType"), CommonValidationLength.MAX_LEN_MESSAGETASK_TRANSTYPE.value()));
			}
			/*
			 * if(!StringUtils.isNullOrEmpty(request.getDtoObject()) &&
			 * request.getDtoObject().length() >
			 * IValidationConstants.MAX_LEN_MESSAGETASK_DTOOBJECT){
			 * getMessageDescCollection
			 * ().add(Messages.getString("validation.field.length",
			 * Labels.getString("label.customer.messageTask.isDtoObject"),
			 * IValidationConstants.MAX_LEN_MESSAGETASK_DTOOBJECT)); }
			 * if(!StringUtils.isNullOrEmpty(request.getStatus()) &&
			 * request.getStatus().length() >
			 * IValidationConstants.MAX_LEN_MESSAGETASK_STATUS){
			 * getMessageDescCollection
			 * ().add(Messages.getString("validation.field.length",
			 * Labels.getString("label.customer.messageTask.isStatus"),
			 * IValidationConstants.MAX_LEN_MESSAGETASK_STATUS)); }
			 */
			/*if (!StringUtils.isNullOrEmpty(request.getRelationId())
					&& request.getRelationId().length() > IValidationConstants.MAX_LEN_MESSAGETASK_RELATIONID) {
				getMessageDescCollection() .add(Messages.getString( "validation.field.length", Labels.getString("label.customer.messageTask.isRelationId"), IValidationConstants.MAX_LEN_MESSAGETASK_RELATIONID));
			}*/

			if (request.getListMessage().size() < 1) {
				getMessageDes().add(
						Messages.getString("validation.field.madatory",
								Labels.getString("label.customer.messageLog")));

			} else {
				for (MessageLogDTO messageLogDTO : request.getListMessage()) {
					validateMessageLog(messageLogDTO, null);
				}
			}
		}
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
		

}
