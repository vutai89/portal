package com.mcredit.business.telesales.validation;

import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.telesale.entity.UplDetail;
import com.mcredit.model.dto.telesales.UploadFileXsellDTO;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateTimeFormat;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;
import java.text.ParseException;

public class XSellValidation extends AbstractValidation {
	public void validateFileID(Long idFile) throws ValidationException {
		if (idFile == null || idFile == 0) {
			throw new ValidationException(Messages.getString("validation.field.empty", Labels.getString("label.xsell.file.id")));
		}
	}

	public void validateUpl(UplDetail upl) throws ValidationException {
		exitsUpl(upl);
		if(upl.getServerFileName().equals(""))
			getMessageDes().add(Messages.getString("validation.field.empty",Labels.getString("label.xsell.file.url.empty")));
		
		if (!isValid()) throw new ValidationException(this.buildValidationMessage());
	}
	
	public void validateUplStatus(boolean canApprove) throws ValidationException {
		if(!canApprove)
			throw new ValidationException(Messages.getString("validation.field.empty",Labels.getString("label.xsell.file.url.status")));
		
		if (!isValid()) throw new ValidationException(this.buildValidationMessage());
	}
	
	public void exitsUpl(UplDetail upl)throws ValidationException{
		if(upl==null)
			throw new ValidationException(Messages.getString("validation.field.empty",Labels.getString("label.xsell.file.idExits")));
	}
        
      public void validateImport(UploadFileXsellDTO dTO)
            throws ValidationException, ParseException {

        if (dTO != null) {

            if (StringUtils.isNullOrEmpty(dTO.getUserFileName())) {
                getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.upload.fileName")));
            }

            if (dTO.getFileContent() == null) {
                getMessageDes().add(Messages.getString("validation.field.madatory", Labels.getString("label.telesale.upload.file")));
            }

            if (StringUtils.isNullOrEmpty(dTO.getDateFrom())) {
                getMessageDes().add(Messages.getString("validation.field.empty",
                        Labels.getString("label.warehouse.datetime.valid")));
            }
            if (!StringUtils.isNullOrEmpty(dTO.getDateFrom())
                    && !DateUtil.validateFormat(dTO.getDateFrom(), "dd/MM/yyyy")) {
                getMessageDes().add(Messages.getString("validation.field.empty",
                        Labels.getString("label.warehouse.datetime.valid")));
            }
            if (StringUtils.isNullOrEmpty(dTO.getDateTo())) {
                getMessageDes().add(Messages.getString("validation.field.empty",
                        Labels.getString("label.warehouse.datetime.valid")));
            }
            if (!StringUtils.isNullOrEmpty(dTO.getDateTo())
                    && !DateUtil.validateFormat(dTO.getDateTo(), "dd/MM/yyyy")) {
                getMessageDes().add(Messages.getString("validation.field.empty",
                        Labels.getString("label.warehouse.datetime.valid")));
            }
            if (!StringUtils.isNullOrEmpty(dTO.getDateFrom()) && !StringUtils.isNullOrEmpty(dTO.getDateTo()) &&
                    DateUtil.toDate(dTO.getDateFrom(), "dd/MM/yyyy").compareTo(DateUtil.toDate(dTO.getDateTo(), "dd/MM/yyyy")) > 0) {
                getMessageDes().add(Messages.getString("validation.field.notcorect",
                        Labels.getString("label.xsell.datetime.compare")));
            }
        }
        if (!isValid()) {
            throw new ValidationException(this.buildValidationMessage());
        }
    }
      public void exitsUplDetailId(Long uplDetailId)throws ValidationException{
		if(uplDetailId == null){
                    	throw new ValidationException(Messages.getString("validation.field.empty",Labels.getString("label.xsell.file.id")));
                }
	}
}
