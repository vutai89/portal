package com.mcredit.business.transaction.validation;

import java.text.ParseException;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.model.dto.transaction.TransactionDTO;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;

public class TransactionValidation extends AbstractValidation {

    public void validateaAddScheduleProduct(TransactionDTO transactionDTO) throws ValidationException, ParseException {
        if (StringUtils.isNullOrEmpty(transactionDTO.getTicketCode())) {
            getMessageDes().add(Messages.getString("validation.field.mainMessage",
                    Labels.getString("label.schedule.product.required.TicketCode")));
        }
        if (!isValid()) {
            throw new ValidationException(this.buildValidationMessage());
        }
    }

    public void validateaSeachTransactions(TransactionDTO transactionDTO) throws ValidationException, ParseException {
        if (!StringUtils.isNullOrEmpty(transactionDTO.getEffectDateFrom())
                && !DateUtil.validateFormat(transactionDTO.getEffectDateFrom(), "dd/MM/yyyy")) {
            getMessageDes().add(Messages.getString("validation.field.mainMessage",
                    Labels.getString("label.schedule.product.required.EffectDate")));
        }
        if (!StringUtils.isNullOrEmpty(transactionDTO.getEffectDateTo())
                && !DateUtil.validateFormat(transactionDTO.getEffectDateTo(), "dd/MM/yyyy")) {
            getMessageDes().add(Messages.getString("validation.field.mainMessage",
                    Labels.getString("label.schedule.product.required.EffectDate")));
        }

        if (!isValid()) {
            throw new ValidationException(this.buildValidationMessage());
        }
    }

}
