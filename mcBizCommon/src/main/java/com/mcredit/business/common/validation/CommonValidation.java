package com.mcredit.business.common.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.common.CommoditiesDTO;
import com.mcredit.model.dto.common.CommonDTO;
import com.mcredit.model.dto.common.MessageLogDTO;
import com.mcredit.model.enums.CreditValidationLength;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.MsgType;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CommonValidation extends AbstractValidation {

	public void validateCommodities(CommoditiesDTO item, String updateId) throws Exception {

		if (item != null) {
			if (updateId != null && (!StringUtils.isNumberic(updateId) || Long.parseLong(updateId) == 0))
				getMessageDes().add(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.common.queryParam.isNumber")));

			if (StringUtils.isNullOrEmpty(item.getRecordStatus()))
				getMessageDes().add(Messages.getString("validation.field.madatory",
						Labels.getString("label.customer.personal.corecode")));

			if (!StringUtils.isNullOrEmpty(item.getCommName()) || Long.parseLong(item.getCommName()) == 0)
				getMessageDes().add(Messages.getString("validation.field.madatory",
						Labels.getString("label.customer.personal.corecode")));

			if (!StringUtils.isNullOrEmpty(item.getStatus()))
				getMessageDes().add(Messages.getString("validation.field.madatory",
						Labels.getString("label.customer.personal.corecode")));
		}
	}

	public void validateMessageLog(MessageLogDTO item, String updateId) throws ValidationException {
		if (item != null) {
			if (updateId != null && (!StringUtils.isNumberic(updateId) || Long.parseLong(updateId) == 0))
				getMessageDes().add(Messages.getString("validation.field.invalidFormat",
						Labels.getString("label.common.queryParam.isNumber")));

			if (!StringUtils.isNullOrEmpty(item.getMsgType())
					&& item.getMsgType().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_MSGTYPE.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isMsgType"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_MSGTYPE.value()));

			if (!StringUtils.isNullOrEmpty(item.getMsgType()) && !(item.getMsgType().equals(MsgType.REQUEST.value())
					|| item.getMsgType().equals(MsgType.REVERT.value())))
				getMessageDes().add(Messages.getString("validation.field.type",
						Labels.getString("label.common.messagelog.isMsgType"),
						MsgType.REQUEST.value() + " : " + MsgType.REVERT.value()));

			if (!StringUtils.isNullOrEmpty(item.getFromChannel())
					&& item.getFromChannel().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_FROMCHANNEL.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isFromChannel"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_FROMCHANNEL.value()));

			if (!StringUtils.isNullOrEmpty(item.getToChannel())
					&& item.getToChannel().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_TOCHANNEL.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isToChannel"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_TOCHANNEL.value()));

			if (!StringUtils.isNullOrEmpty(item.getRelationId())
					&& item.getRelationId().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_RELATIONID.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isRelationId"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_RELATIONID.value()));

			if (item.getTransId() != null && item.getTransId().toString()
					.length() > CreditValidationLength.MAX_LEN_MESSAGELOG_TRANSID.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isTransId"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_TRANSID.value()));

			if (!StringUtils.isNullOrEmpty(item.getTransType())
					&& item.getTransType().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_TRANSTYPE.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isTransType"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_TRANSTYPE.value()));

			if (!StringUtils.isNullOrEmpty(item.getServiceName()) && item.getServiceName()
					.length() > CreditValidationLength.MAX_LEN_MESSAGELOG_GETSERVICENAME.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isServiceName"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_GETSERVICENAME.value()));

			if (!StringUtils.isNullOrEmpty(item.getResponseCode())
					&& item.getResponseCode().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_RESPONSECODE.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isResponseCode"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_RESPONSECODE.value()));

			if (!StringUtils.isNullOrEmpty(item.getResponseErrorDesc()) && item.getResponseErrorDesc()
					.length() > CreditValidationLength.MAX_LEN_MESSAGELOG_RESPONSEERRORDESC.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isResponseErrorDesc"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_RESPONSEERRORDESC.value()));

			if ((item.getMsgOrder() != null) && item.getMsgOrder().toString()
					.length() > CreditValidationLength.MAX_LEN_MESSAGELOG_MSGORDER.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isMsgOrder"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_MSGORDER.value()));

			if (!StringUtils.isNullOrEmpty(item.getMsgRequest())
					&& item.getMsgRequest().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_MSGREQUEST.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isMsgRequest"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_MSGREQUEST.value()));

			if (!StringUtils.isNullOrEmpty(item.getMsgResponse())
					&& item.getMsgResponse().length() > CreditValidationLength.MAX_LEN_MESSAGELOG_MSGRESPONSE.value())
				getMessageDes().add(Messages.getString("validation.field.length",
						Labels.getString("label.common.messagelog.isMsgResponse"),
						CreditValidationLength.MAX_LEN_MESSAGELOG_MSGRESPONSE.value()));

			if ((!StringUtils.isNullOrEmpty(item.getMsgStatus()) && !(item.getMsgStatus().equals(MsgStatus.NEW.value())
					|| item.getMsgStatus().equals(MsgStatus.SUCCESS.value())
					|| item.getMsgStatus().equals(MsgStatus.ERROR.value())
					|| item.getMsgStatus().equals(MsgStatus.TIME_OUT.value())
					|| item.getMsgStatus().equals(MsgStatus.IGNORE.value())))) {
				getMessageDes().add(Messages.getString("validation.field.type",
						Labels.getString("label.common.messagelog.isMsgStatus"),
						MsgStatus.NEW.value() + " : " + MsgStatus.SUCCESS.value() + " : " + MsgStatus.TIME_OUT.value()
								+ " : " + MsgStatus.ERROR.value() + " : " + MsgStatus.IGNORE.value()));
			}

		}
		if (item == null)
			getMessageDes().add(Messages.getString("validation.field.invalidFormat",
					Labels.getString("label.common.queryParam.isNumber")));

		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());

	}

	public void validateCommon(CommonDTO request, String updateId) throws Exception {
		if (request != null) {
			validateCommodities(request.getCommoditiesDTO(), updateId);
			validateMessageLog(request.getMessageLogDTO(), updateId);
		}

		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());

	}

	public void validate(String appId, String doctype) throws ValidationException {
		if (StringUtils.isNullOrEmpty(appId))
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.bpm.appid")));

		if (StringUtils.isNullOrEmpty(doctype))
			getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.bpm.doctype")));

		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
}
