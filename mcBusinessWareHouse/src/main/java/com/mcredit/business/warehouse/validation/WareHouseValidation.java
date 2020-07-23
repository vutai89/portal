package com.mcredit.business.warehouse.validation;

import java.util.Arrays;
import java.util.List;
import java.lang.reflect.Field;
import java.text.ParseException;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.common.entity.AllocationDetail;
import com.mcredit.model.dto.warehouse.CavetDTO;
import com.mcredit.model.dto.warehouse.ContractNumberScannerDTO;
import com.mcredit.model.dto.warehouse.DocumentsDTO;
import com.mcredit.model.dto.warehouse.DocumentsErrorsDTO;
import com.mcredit.model.dto.warehouse.QRCodeCheckDTO;
import com.mcredit.model.dto.warehouse.QRCodeDTO;
import com.mcredit.model.dto.warehouse.WareHousePayBackCavetDTO;
import com.mcredit.model.dto.warehouse.WhBorrowedDocumentDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.CTCodeValue1;
import com.mcredit.model.enums.CTGroup;
import com.mcredit.model.enums.WHStep;
import com.mcredit.model.object.SearchCaseInput;
import com.mcredit.model.object.warehouse.Document;
import com.mcredit.model.object.warehouse.LodgeDocumentDTO;
import com.mcredit.model.object.warehouse.RenewalDocumentDTO;
import com.mcredit.model.object.warehouse.WHAllocationDocInput;
import com.mcredit.sharedbiz.validation.AbstractValidation;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.StringUtils;

public class WareHouseValidation extends AbstractValidation {
	public void validateAddWHBrrowedDocument(WhBorrowedDocumentDTO whBorrowedDocumentDTO)
			throws ValidationException, ParseException {
		if (whBorrowedDocumentDTO == null)
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.borrowed.Empty")));
		if (!StringUtils.isNullOrEmpty(whBorrowedDocumentDTO.getAppointmentDate())
				&& !DateUtil.validateFormat(whBorrowedDocumentDTO.getAppointmentDate(), "dd/MM/yyyy"))
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.datetime.valid")));
		if (whBorrowedDocumentDTO.getWhDocId() == null)
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.borrowed.Empty")));
		if (whBorrowedDocumentDTO.getObjectTo() == null)
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.object.Empty")));
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}

	public void validateRenewalWHBrrowedDocument(Integer idDocument) throws ValidationException, ParseException {
		if (idDocument == null)
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.whDocID.Empty")));
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}

	public void validateValidList(List<?> listDocument) throws ValidationException, ParseException {
		if (listDocument == null || listDocument.size() == 0)
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.listDoc.Empty")));
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}

	public void validateSearchPayBackCavet(WareHousePayBackCavetDTO backCavet)
			throws ValidationException, ParseException {
		if (backCavet.getTypeScreen() != 1 && backCavet.getTypeScreen() != 2) {
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.typeScreen.valid")));
		}
		if (!StringUtils.isNullOrEmpty(backCavet.getAppPayBackDateFrom())
				&& !DateUtil.validateFormat(backCavet.getAppPayBackDateFrom(), "dd/MM/yyyy")) {
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.datetime.valid")));
		}
		if (!StringUtils.isNullOrEmpty(backCavet.getAppPayBackDateTo())
				&& !DateUtil.validateFormat(backCavet.getAppPayBackDateTo(), "dd/MM/yyyy")) {
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.datetime.valid")));
		}
		if (!StringUtils.isNullOrEmpty(backCavet.getFwAppPayBackDateFrom())
				&& !DateUtil.validateFormat(backCavet.getFwAppPayBackDateFrom(), "dd/MM/yyyy")) {
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.datetime.valid")));
		}
		if (!StringUtils.isNullOrEmpty(backCavet.getFwAppPayBackDateTo())
				&& !DateUtil.validateFormat(backCavet.getFwAppPayBackDateTo(), "dd/MM/yyyy")) {
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.datetime.valid")));
		}
		if (!isValid()) {
			throw new ValidationException(this.buildValidationMessage());
		}
	}

	public void validateExportContract(String contractNumber) throws ValidationException, ParseException {
		if (StringUtils.isNullOrEmpty(contractNumber)) {
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.required.contractNumber")));
		}
		if (!isValid()) {
			throw new ValidationException(this.buildValidationMessage());
		}
	}
	
	public void validateSeachCaseInput(SearchCaseInput input, String step) throws ValidationException, IllegalArgumentException, IllegalAccessException {		
		if (step == null || "".equals(step.trim())) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.input")));
		}
		
		WHStep _step = WHStep.from(step.toUpperCase());
		
		if (_step == null) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.input")));
		}

		if (input == null) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.input")));
		}
		
		List<String> listErr = Arrays.asList("NaN","--","\'",";","AND 1 = 1","SELECT","CONCAT","UNION","CONSOLE","DELETE","UPDATE","INSERT","TRUNCATE","CREATE","ALTER","DROP","LIKE","WHERE","GRANT","SCR","FRAME","SCRIPT","JAVASCRIPT","VBSCRIPT","ALERT","FUCTION","ONCLICK","ONCHANGE","MOUSE","ONMOUSEOVER");
		
		Field[] fields = input.getClass().getDeclaredFields();
		
		for (Field field : fields) {
			if ("class java.lang.String".equals(field.getType().toString())) {
				if(field.get(input) != null){
					 for (String str : listErr) {
						 if(field.get(input).toString().toLowerCase().contains(str.toLowerCase())){
							 getMessageDes().add(Messages.getString("validation.field.invalidFormat",field.getName())); 
						 };
					}
					 
				}
			}
		}
		
		if(!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void validateCheckRecordsCavet() throws ValidationException, ParseException {
		
	}
	
	public void insertQRCode(QRCodeCheckDTO qrCode) throws ValidationException {
		if(qrCode == null)
			getMessageDes().add(Messages.getString("validation.field.madatory", "Input"));	
		
		if(StringUtils.isNullOrEmpty(qrCode.getAppID()))
			getMessageDes().add(Messages.getString("validation.field.madatory", "APP ID"));	
	
		if(StringUtils.isNullOrEmpty(qrCode.getAppNumber()))
			getMessageDes().add(Messages.getString("validation.field.madatory", "App Number"));
		
		if(StringUtils.isNullOrEmpty(qrCode.getContractNumber()))
			getMessageDes().add(Messages.getString("validation.field.madatory", "Contract Number"));
		
		if(StringUtils.isNullOrEmpty(qrCode.getSignature()))
			getMessageDes().add(Messages.getString("validation.field.madatory", "Signature"));
		
		if(qrCode.getVersion() == null)
			getMessageDes().add(Messages.getString("validation.field.madatory", "Version"));
		
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void scanContractNumber(ContractNumberScannerDTO input) throws ValidationException {
		if(input == null)
			getMessageDes().add(Messages.getString("validation.field.madatory", "Input"));	
		
		if(StringUtils.isNullOrEmpty(input.getContractNumber()))
			getMessageDes().add(Messages.getString("validation.field.madatory", "Contract Number"));
		
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void createQRCode(QRCodeDTO data) throws ValidationException {
		if(data == null)
			getMessageDes().add(Messages.getString("validation.field.madatory", "Input"));	
		
		if(StringUtils.isNullOrEmpty(data.getSoHopDong()))
			getMessageDes().add(Messages.getString("validation.field.madatory", "So Hop Dong"));	
	
		if(StringUtils.isNullOrEmpty(data.getAppId()))
			getMessageDes().add(Messages.getString("validation.field.madatory", "App Id"));
		
		if(StringUtils.isNullOrEmpty(data.getAppNumber()))
			getMessageDes().add(Messages.getString("validation.field.madatory", "App Number"));
			
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void validateDocumentsDTO(DocumentsDTO payloadSaveDocuments) throws ValidationException {
		if ((payloadSaveDocuments == null) || (payloadSaveDocuments.getDocuments().size() == 0)) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.payload.Empty")));
		}
	}
	
	public void updateDocumentErrors(DocumentsErrorsDTO input) throws ValidationException {
		if (input == null) 
			getMessageDes().add(Messages.getString("validation.field.madatory", "DocumentsErrorsDTO"));			
		
		if(input.getWhDocumentId() == null  ||input.getWhDocumentId().equals(0l))
			getMessageDes().add(Messages.getString("validation.field.madatory", "WhDocumentId"));
		
		if(input.getErrorTypes() == null)
			getMessageDes().add(Messages.getString("validation.field.madatory", "List<ErrorType>"));
	
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void validateCavet(CavetDTO cavet) throws ValidationException {
		if (cavet.getWhDocId() == null) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.whDocID.Empty")));
		}

		// Todo: Check whdocId doesn't exists db
	}
	
	public void validateDocument(Document DDTO) throws ValidationException, ParseException {

		if (DDTO == null) {
			getMessageDes().add(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.whDoc.Empty")));
		} else {
			if (DDTO.getCreditAppId() == null)
				getMessageDes().add(Messages.getString("validation.field.mainMessage",
						Labels.getString("label.warehouse.creditAppId.Empty")));
			if (DDTO.getDocType() == null)
				getMessageDes().add(Messages.getString("validation.field.mainMessage",
						Labels.getString("label.warehouse.doctype.Empty")));
			if (DDTO.getOrderBy() == null)
				getMessageDes().add(Messages.getString("validation.field.mainMessage",
						Labels.getString("label.warehouse.orderBy.Empty")));
		}
		if (!isValid())
			throw new ValidationException(this.buildValidationMessage());
	}
	
	public void validateRenewalDocument(List<RenewalDocumentDTO> renewaldocumentInputs, String typeDocument) throws ValidationException, ParseException {
		if (typeDocument == null || typeDocument.isEmpty() == true) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.check.typedocument")));
		}

		if (CTCodeValue1.WH_CAVET != CTCodeValue1.valueOf(typeDocument.toUpperCase())
				&& CTCodeValue1.WH_LOAN_DOC != CTCodeValue1.valueOf(typeDocument.toUpperCase())) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.invalid.typedocument")));
		}

		if (renewaldocumentInputs == null || renewaldocumentInputs.size() == 0) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.require.renewdoc")));
		}
	}
	
	public void validateLodgeDocument(LodgeDocumentDTO lodgeDocument) throws ValidationException, ParseException {
		if (lodgeDocument == null || StringUtils.isNullOrEmpty(lodgeDocument.getType())
				|| !Arrays
						.asList(new String[] { CTCodeValue1.WH_LOAN_DOC.value(), CTCodeValue1.WH_CAVET.value(),
								CTCodeValue1.WH_THANKS_LETTER.value() })
						.contains(lodgeDocument.getType().toUpperCase())
				|| lodgeDocument.getLstData() == null || lodgeDocument.getLstData().size() == 0) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.invalid.lodgeDocument")));
		}
	}
	
	public void validateFindRemainDocument(String step, List<Long> caseReturn) throws ValidationException, ParseException {
		if (step == null || "".equals(step.trim())) {
			throw new ValidationException(
					Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.input")));
		}
		
		if (caseReturn == null || caseReturn.size() == 0) {
			throw new ValidationException(
					Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.input")));
		}
	}
	
	public void validateAllocationDocument(String allocation, WHAllocationDocInput allocationDocumnetInputs) throws ValidationException, ParseException {
		if (allocation == null || "".equals(allocation.trim())) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.check.step")));
		}

		if (allocationDocumnetInputs == null || allocationDocumnetInputs.getLstwhDocId().size() == 0) {
			throw new ValidationException(
					Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.input")));
		}
	}
	
	public void validateListDocumentType(String codeGroup, String category) throws ValidationException, ParseException {
		if (StringUtils.isNullOrEmpty(category)) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.require.category")));
		}

		if (StringUtils.isNullOrEmpty(codeGroup)) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.require.codevalue1")));
		}
		
		CTGroup group = CTGroup.from(codeGroup);
		CTCat cat = CTCat.from(category);

		if (group == null) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.invalid.group")));
		}

		if (cat == null) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage",
					Labels.getString("label.warehouse.invalid.category")));
		}
	}
	
	public void validateGetCheckRecordsCavet(Long whId) throws ValidationException, ParseException {
		if (whId == null) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.check.whid")));
		}
	}
	
	public void validateDeleteDocument(String whIds) throws ValidationException, ParseException {
		if (StringUtils.isNullOrEmpty(whIds)) {
			throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.check.whid")));
		}
	}
	
	public void validateListAssign(String allocation, List<AllocationDetail> listAssign, WHAllocationDocInput allocationDocumnetInputs) throws ValidationException, ParseException {
		if (allocation.equalsIgnoreCase("assign")) {
			if (!listAssign.isEmpty()) {
				throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.allocation.assign")));
			}
		}
		
		if (allocation.equalsIgnoreCase("reassign")) {
			if (listAssign == null || listAssign.size() != allocationDocumnetInputs.lstwhDocId.size()) {
				throw new ValidationException(Messages.getString("validation.field.mainMessage", Labels.getString("label.warehouse.allocation.reassign")));
			}
		}
	}
	
}
