package com.mcredit.business.debt_home.validation;

import java.util.List;

import com.mcredit.business.debt_home.factory.MainFactory;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.debt_home.DebtHomeAssignDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class DebtHomeValidation {

	UnitOfWork uok = null;
	
	private static final int MAX_LENGTH_MOBILE_PHONE = 10;
	private static final int MIN_LENTH_IDENTITY_NUMBER = 9;
	private static final int MAX_LENGTH_IDENTITY_NUMBER = 12;

	public DebtHomeValidation(UnitOfWork uok) {
		this.uok = uok;
	}

	public void validateLookup(String contractNumber, String loginId, boolean isCheckPermission)
			throws ValidationException {
		boolean isContractNumberExist = this.uok.debtHome.debtHomeAssignRepository()
				.checkExistContractNumber(contractNumber);
		if (!isContractNumberExist) {
			throw new ValidationException(Messages.getString("debt_home.contract_number_exist"));
		}
		if (isCheckPermission)
			this.validatePermision(contractNumber, loginId);
	}

	public void validateAssign(List<DebtHomeAssignDTO> debtHomeAssigns) throws ValidationException {
		if (debtHomeAssigns != null && debtHomeAssigns.size() == 0) {
			throw new ValidationException(Messages.getString("debt_home.assign"));
		}
	}

	public void validateGetFilePath(Long fileId, String loginId) throws Exception {
		if (fileId == null) {
			throw new ValidationException(Messages.getString("debt_home.id.null"));
		}

		Long appNumber = MainFactory.searchFilesAggreate(this.uok).getAppNumber(fileId);
		String contractNumber = this.uok.debtHome.debtHomeAssignRepository().getContractNumberByAppNumber(appNumber);

		if (contractNumber == null) {
			throw new ValidationException(Messages.getString("debt_home.contract_number.null"));
		}

		this.validatePermision(contractNumber, loginId);
	}

	private void validatePermision(String contractNumber, String loginId) throws ValidationException {
		boolean isHasPermision = this.uok.debtHome.debtHomeAssignRepository().checkPermision(contractNumber, loginId);
		if (!isHasPermision) {
			throw new ValidationException(Messages.getString("debt_home.lookup"));
		}
	}

	public void validateContractNumForDebtColection(String contractNumber) throws ValidationException {

		if (contractNumber.trim().isEmpty() || contractNumber.trim().length() > 20) {
			throw new ValidationException(Messages.getString("debt_home.invalidContractNum"));
		}

		if (!StringUtils.isDigit(contractNumber.trim())) {
			throw new ValidationException(Messages.getString("debt_home.invalidContractNum"));
		}

		/*
		 * boolean checkContractNum =
		 * this.uok.credit.creditApplicationRequestRepo().checkContractNumberDebt(
		 * contractNumber); if (!checkContractNum) { throw new
		 * ValidationException(Messages.getString("debt_home.notExitsdContractNum")); }
		 */

	}
	
	public void validateInputLookupContract(String contractNumber, String appNumber, String custMobilePhone, String identityNumber) throws ValidationException {
		if(contractNumber.trim().isEmpty() && appNumber.trim().isEmpty() && custMobilePhone.trim().isEmpty() && identityNumber.trim().isEmpty()) {
			throw new ValidationException(Messages.getString("debt.invalidInput"));
		}
		
		if(!StringUtils.isNullOrEmpty(appNumber) && !StringUtils.isDigit(appNumber.trim())) {
			throw new ValidationException(Messages.getString("debt.invalid.appNumber"));
		}
		
		if(!StringUtils.isNullOrEmpty(custMobilePhone.trim()) && !StringUtils.checkMobilePhoneNumberNew(custMobilePhone.trim())) {
			throw new ValidationException(Messages.getString("debt.invalid.mobilePhone"));
		};
		
		if(!StringUtils.isNullOrEmpty(identityNumber) && !(identityNumber.trim().length() == 12 || identityNumber.trim().length() == 9)) {
			throw new ValidationException(Messages.getString("debt.invalid.identityNumber"));
		}
	}
	
	public void validateFindFile(String appNumber) throws ValidationException {
		if(StringUtils.isNullOrEmpty(appNumber.trim())) {
			throw new ValidationException(Messages.getString("debt.appNumber.required"));
		}
	}
	
	public void validateViewFileContract(Long fileId) throws Exception {
		if (fileId == null) {
			throw new ValidationException(Messages.getString("debt_home.id.null"));
		}
	}
}