package com.mcredit.business.customer;

import java.io.Closeable;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.business.customer.dto.CustomerDTO;
import com.mcredit.business.customer.dto.CustomerDisbursementDebtDTO;
import com.mcredit.business.customer.dto.CustomerIdentityDTO;
import com.mcredit.business.customer.dto.CustomerPersonalInfoDTO;
import com.mcredit.business.customer.validation.CustomerValidation;
import com.mcredit.common.Labels;
import com.mcredit.common.Messages;
import com.mcredit.data.UnitOfWork;
import com.mcredit.model.dto.CardInformationDTO;
import com.mcredit.model.dto.CodeTableDTO;
import com.mcredit.model.dto.IdDTO;
import com.mcredit.model.dto.ResponseIds;
import com.mcredit.model.dto.card.CustomerAccountLinkUpsertResultDTO;
import com.mcredit.model.dto.card.CustomerAddressInfoDTO;
import com.mcredit.model.dto.card.CustomerCommunicationInfoDTO;
import com.mcredit.model.dto.card.CustomerPersonalInfoUpsertResultDTO;
import com.mcredit.model.dto.check_cat.CompanyDTO;
import com.mcredit.model.dto.check_cat.CustCompanyCheckDTO;
import com.mcredit.model.dto.check_cat.CustCompanyInfoDTO;
import com.mcredit.model.dto.check_cat.ResponseCompCheckCat;
import com.mcredit.model.dto.check_cat.ResponseCompTypeDTO;
import com.mcredit.model.dto.check_cat.ResponseSearchCompany;
import com.mcredit.model.dto.check_cat.ResultRemoveCompDTO;
import com.mcredit.model.dto.check_cat.ResultUpdateListCompDTO;
import com.mcredit.model.dto.check_cat.SearchCompanyDTO;
import com.mcredit.model.dto.check_cat.UpdateTopCompDTO;
import com.mcredit.model.enums.CTCat;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.object.ContractCheck;
import com.mcredit.model.object.DataReport;
import com.mcredit.model.object.State;
import com.mcredit.model.object.TotalDebt;
import com.mcredit.model.object.warehouse.DocumentStateInfo;
import com.mcredit.model.telesale.ContractSearch;
import com.mcredit.restcore.enums.AcceptType;
import com.mcredit.restcore.enums.ContentType;
import com.mcredit.restcore.exception.ISRestCoreException;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.restcore.services.BasedHttpClient;
import com.mcredit.sharedbiz.BusinessConstant;
import com.mcredit.sharedbiz.aggregate.RedisAggregate;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.esb.EsbApi;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class CustomerManager implements Closeable {

	private UnitOfWork uok = null;
	private CustomerValidation _custValid = new CustomerValidation();
	
	private final String USER_ON_PAGE_REQUEST_CAT = "USER_ON_PAGE_REQUEST_CAT";
	
	private final String _esbHost = CacheManager.Parameters().findParamValueAsString(ParametersName.ESB_HOST);

	public List<DocumentStateInfo> findContractInfo(ContractSearch contractSearch) throws ValidationException {

		List<DocumentStateInfo> result = null;
		CustomerFactory.getContractDuplicateValidation().validate(contractSearch);

		try {

			uok = new UnitOfWork();

			this.uok.start();

			result = CustomerFactory.getInstance(this.uok.customer).findContractInfo(contractSearch);

			this.uok.commit();

		} catch (Throwable ex) {

			this.uok.rollback();

			throw new ValidationException(ex.toString());
		}
		return result;
	}

	public ContractCheck findContractDuplicate(String identityNumber) throws ValidationException {

		ContractCheck result = null;
		CustomerFactory.getCheckDuplicateValidation().validate(StringUtils.nullToEmpty(identityNumber));
		try {

			uok = new UnitOfWork();
			this.uok.start();

			result = CustomerFactory.getInstance(this.uok.customer).findContractDuplicate(identityNumber);

			this.uok.commit();

		} catch (Throwable ex) {

			this.uok.rollback();

			throw new ValidationException(ex.toString());
		}

		return result;
	}
	
	public TotalDebt findTotalDebt(String identityNumber, String oldIdentityNumber, String militaryNumber) throws ValidationException, Exception {

		TotalDebt result = new TotalDebt(new Long("-9999")); //Trả ra -9999 nếu gặp exception
		Long totalDebtT24 = new Long(0L);
		CustomerDisbursementDebtDTO cusDisbursemnetDebt = new CustomerDisbursementDebtDTO();
		
		try {
			
			if( "".equals(identityNumber) && "".equals(oldIdentityNumber) && "".equals(militaryNumber) )
				throw new ValidationException("Missing all parameters!");

			uok = new UnitOfWork();
			
			this.uok.start();

			totalDebtT24 = CustomerFactory.getInstance(this.uok.customer).findTotalDebt(identityNumber.trim(), oldIdentityNumber.trim(), militaryNumber.trim());
			
			this.uok.commit();
			
			// Get totalPayment ESB
			StringBuilder content = new StringBuilder();
			String lstIdentityNumber = new String();
			if(!StringUtils.isNullOrEmpty(identityNumber)) {
				content.append("'"+identityNumber.trim()+"'").append(",");
			}
			if(!StringUtils.isNullOrEmpty(oldIdentityNumber)) {
				content.append("'"+oldIdentityNumber.trim()+"'").append(",");
			}
			if(!StringUtils.isNullOrEmpty(militaryNumber)) {
				content.append("'"+militaryNumber.trim()+"'");
			}
			if(content.charAt(content.length() - 1) == ',') {
				lstIdentityNumber = content.substring(0, content.length()-1);
			} else {
				lstIdentityNumber = content.toString();
			}
//			content.append(identityNumber.trim()).append(",").append(oldIdentityNumber.trim()).append(",").append(militaryNumber.trim());
			String urlEsbPayment = this._esbHost + BusinessConstant.ESB_DISBURSEMENT_TOTAL_PAYMENT.replace("{identityNumber}", lstIdentityNumber);
			ApiResult apiResult = new ApiResult();
			@SuppressWarnings("resource")
			BasedHttpClient bs = new BasedHttpClient();
			apiResult = bs.doGet(urlEsbPayment);
			if(apiResult.getStatus()) {
				if(!StringUtils.isNullOrEmpty(apiResult.getBodyContent())) {
					ObjectMapper mapper = new ObjectMapper();
					cusDisbursemnetDebt = mapper.readValue(apiResult.getBodyContent().toString(), new TypeReference<CustomerDisbursementDebtDTO>() {});
				}
			}

		} catch (Throwable ex) {

			this.uok.rollback();
			ex.printStackTrace();
			throw new ValidationException(ex.toString());
		}
		
		result.setTotalDebt(totalDebtT24+cusDisbursemnetDebt.getTotalPaymentAmount());
		return result;
	}

	public CustomerPersonalInfoUpsertResultDTO upsertCustomer(CustomerDTO request, String updateId)
			throws ValidationException {

		CustomerPersonalInfoUpsertResultDTO result = null;
		_custValid.validateCustomer(request, updateId);

		try {
			uok = new UnitOfWork();
			this.uok.start();

			codeTableFieldProgress(request);

			CustomerAggregate item = CustomerFactory.getInstance(request, this.uok.customer);

			if (updateId != null) {
				Long id = Long.parseLong(updateId);
				if (item.findCustomerPersonalInfoById(id) == null)
					throw new ValidationException("Customer not found");

				item.getPersonalInfo().setId(id);
			}

			item.upsertCustomer();

			result = CustomerFactory.getInstance(item.getPersonalInfo().getId(),
					item.getPersonalInfo().getMcCustCode());

			this.uok.commit();

			System.out.println("upsertCustomer successfull, return ID[" + result.getMcCustId() + "]");

		} catch (Throwable ex) {

			this.uok.rollback();

			throw new ValidationException(ex.toString());
		}
		return result;
	}

	public CustomerPersonalInfoUpsertResultDTO removeCustomer(String ids) throws Exception {

		CustomerPersonalInfoUpsertResultDTO result = null;
		if (StringUtils.isNullOrEmpty(ids) || !StringUtils.isNumeric(ids))
			throw new ValidationException("Customer ID is invalid!");
		try {

			uok = new UnitOfWork();
			this.uok.start();

			CustomerAggregate item = CustomerFactory.getInstance(new CustomerDTO(new CustomerPersonalInfoDTO()),
					this.uok.customer);

			Long id = Long.parseLong(new DecimalFormat("###.#").format(Double.parseDouble(ids)));
			if (item.findCustomerPersonalInfoById(id) == null)
				throw new ValidationException("Customer not found!");

			item.getPersonalInfo().setId(id);

			item.removeCustomer();

			result = CustomerFactory.getInstance(item.getPersonalInfo().getId(),
					item.getPersonalInfo().getMcCustCode());

			this.uok.commit();

			System.out.println("Remove customer success, mcCustId[" + result.getMcCustId() + "] , mcCustCode["
					+ result.getMcCustCode() + "]");

		} catch (Throwable ex) {

			this.uok.rollback();

			throw new ValidationException(ex.toString());
		}
		return result;
	}

	public CardInformationDTO findCardInformationByCardId(String cardId) throws Exception {

		CardInformationDTO objResult = null;
		if (StringUtils.isNullOrEmpty(cardId))
			throw new ValidationException("Tham s\u1ED1 kh\u00F4ng h\u1EE3p l\u1EC7");

		try {

			uok = new UnitOfWork();
			this.uok.start();

			CustomerAggregate item = CustomerFactory.getInstance(this.uok.customer);

			objResult = item.findCardInformationByCardId(cardId.trim());

			this.uok.commit();

		} catch (Throwable ex) {

			this.uok.rollback();

			throw new ValidationException(ex.toString());
		}
		return objResult;
	}

	public Object queryInfo(String type, String contractNumbers, String identityNumber, String militaryId,
			String mobilePhone) throws ValidationException {

		Object objResult = null;
		CustomerFactory.getQueryInfoValidation().validate(type, contractNumbers, identityNumber, militaryId,
				mobilePhone);

		try {

			uok = new UnitOfWork();
			this.uok.start();

			CustomerAggregate item = CustomerFactory.getInstance(this.uok.customer);

			objResult = item.queryInfo(type.trim(), contractNumbers.trim(), identityNumber.trim(), militaryId.trim(),
					mobilePhone.trim());

			if (objResult == null) {

				if (!"".equals(contractNumbers) && !"".equals(identityNumber) && !"".equals(militaryId))
					throw new ValidationException("400.06", Messages.getString("validation.field.notFound", Labels.getString("label.customer.queryInfo.dataNotFound")));

				else if (!"".equals(identityNumber) && !"".equals(militaryId))
					throw new ValidationException("400.06", Messages.getString("validation.field.notFound", Labels.getString("label.customer.queryInfo.dataNotFound")));

				else if (!"".equals(contractNumbers) && !"".equals(identityNumber))
					throw new ValidationException("400.06", Messages.getString("validation.field.notFound", Labels.getString("label.customer.queryInfo.dataNotFound")));

				else if (!"".equals(contractNumbers) && !"".equals(militaryId))
					throw new ValidationException("400.06", Messages.getString("validation.field.notFound", Labels.getString("label.customer.queryInfo.dataNotFound")));

				else if (!"".equals(contractNumbers))
					throw new ValidationException("400.04",
							Messages.getString("validation.field.notFound", Labels.getString("label.customer.queryInfo.contractNumbersNotFound")));

				else if (!"".equals(identityNumber))
					throw new ValidationException("400.05",
							Messages.getString("validation.field.notFound", Labels.getString("label.customer.queryInfo.identityNumberNotFound")));

				else if (!"".equals(militaryId))
					throw new ValidationException("400.08",
							Messages.getString("validation.field.notFound", Labels.getString("label.customer.queryInfo.militaryIdNotFound")));
			}

			this.uok.commit();

		} catch (ValidationException ex) {

			this.uok.rollback();

			throw ex;
		} catch (Throwable ex) {

			this.uok.rollback();

			throw new ValidationException("500.00", ex.toString());
		}
		return objResult;
	}

	public State checkContractNumber(String contractNumber) throws Exception {

		State objResult = new State();
		if (StringUtils.isNullOrEmpty(contractNumber))
			throw new ValidationException("contractNumber is invalid!");

		try {
			uok = new UnitOfWork();
			this.uok.start();

			CustomerAggregate item = CustomerFactory.getInstance(this.uok.customer);

			objResult.setStatus(item.checkContractNumber(contractNumber.trim()));

		} catch (Throwable ex) {

			throw new ValidationException(ex.toString());
		}
		return objResult;
	}

	public Integer findIdCodeTable(String category, String value1) throws Exception {

		CodeTableDTO codeTable = CodeTableCacheManager.getInstance().getIdByCategoryCodeValue(category, value1.trim());

		if (codeTable == null)
			throw new ValidationException("category[" + category + "] - value1[" + value1 + "] in CodeTable not found");

		return codeTable.getId();
	}

	public void codeTableFieldProgress(CustomerDTO request) throws Exception {

		if (request.getPersonalInfo() != null) {
			if (!StringUtils.isNullOrEmpty(request.getPersonalInfo().getGenderValue()))
				request.getPersonalInfo()
						.setGender(findIdCodeTable(CTCat.GENDER.value(), request.getPersonalInfo().getGenderValue()));

			if (!StringUtils.isNullOrEmpty(request.getPersonalInfo().getHouseholdRegTypeIdValue()))
				request.getPersonalInfo().setHouseholdRegTypeId(findIdCodeTable(CTCat.HOUSE_HOLD_REG_TYPE_ID.value(),
						request.getPersonalInfo().getHouseholdRegTypeIdValue()));

			if (!StringUtils.isNullOrEmpty(request.getPersonalInfo().getMaritalStatusValue()))
				request.getPersonalInfo().setMaritalStatus(findIdCodeTable(CTCat.MARITAL_STATUS.value(),
						request.getPersonalInfo().getMaritalStatusValue()));
		}

		if (request.getAddlInfo() != null) {
			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getRelationSpouseValue()))
				request.getAddlInfo().setRelationSpouse(
						findIdCodeTable(CTCat.RELATION_SPOUSE.value(), request.getAddlInfo().getRelationSpouseValue()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getRelationRefPerson1Value()))
				request.getAddlInfo().setRelationRefPerson1(findIdCodeTable(CTCat.RELATION_REF_PERSON.value(),
						request.getAddlInfo().getRelationRefPerson1Value()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getRelationRefPerson2Value()))
				request.getAddlInfo().setRelationRefPerson2(findIdCodeTable(CTCat.RELATION_REF_PERSON.value(),
						request.getAddlInfo().getRelationRefPerson2Value()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getSpousePositionValue()))
				request.getAddlInfo().setSpousePosition(findIdCodeTable(CTCat.POSITION_IN_COMP.value(),
						request.getAddlInfo().getSpousePositionValue()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getEducationValue()))
				request.getAddlInfo().setEducation(
						findIdCodeTable(CTCat.EDUCATION.value(), request.getAddlInfo().getEducationValue()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getTempSamePermAddrValue()))
				request.getAddlInfo().setTempSamePermAddr(findIdCodeTable(CTCat.TEMP_SAME_PERM_ADDR.value(),
						request.getAddlInfo().getTempSamePermAddrValue()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getLabourContractTypeValue()))
				request.getAddlInfo().setLabourContractType(findIdCodeTable(CTCat.LABOUR_CONTRACT_TYPE.value(),
						request.getAddlInfo().getLabourContractTypeValue()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getPayrollMethodValue()))
				request.getAddlInfo().setPayrollMethod(
						findIdCodeTable(CTCat.PAYROOL_METHOD.value(), request.getAddlInfo().getPayrollMethodValue()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getAccommodationTypeValue()))
				request.getAddlInfo().setAccommodationType(findIdCodeTable(CTCat.ACCOMMODATION_TYPE.value(),
						request.getAddlInfo().getAccommodationTypeValue()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getIsBlackListValue()))
				request.getAddlInfo().setIsBlackList(
						findIdCodeTable(CTCat.IS_BLACK_LIST.value(), request.getAddlInfo().getIsBlackListValue()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getBlackListTypeValue()))
				request.getAddlInfo().setBlackListType(
						findIdCodeTable(CTCat.BLACK_LIST_TYPE.value(), request.getAddlInfo().getBlackListTypeValue()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getProfessionalValue()))
				request.getAddlInfo().setProfessional(
						findIdCodeTable(CTCat.PROFESSION.value(), request.getAddlInfo().getProfessionalValue()));

			if (!StringUtils.isNullOrEmpty(request.getAddlInfo().getPositionInCompValue()))
				request.getAddlInfo().setPositionInComp(findIdCodeTable(CTCat.POSITION_IN_COMP.value(),
						request.getAddlInfo().getPositionInCompValue()));
		}

		if (request.getFinancialInfo() != null) {
			if (!StringUtils.isNullOrEmpty(request.getFinancialInfo().getS37DataValue()))
				request.getFinancialInfo().setS37Data(
						findIdCodeTable(CTCat.S37_DATA.value(), request.getFinancialInfo().getS37DataValue()));

			if (!StringUtils.isNullOrEmpty(request.getFinancialInfo().getLifeInsuCompanyIdValue()))
				request.getFinancialInfo().setLifeInsuCompanyId(findIdCodeTable(CTCat.LIFE_INSU_COMPANY_ID.value(),
						request.getFinancialInfo().getLifeInsuCompanyIdValue()));

			if (!StringUtils.isNullOrEmpty(request.getFinancialInfo().getInsuTermValue()))
				request.getFinancialInfo().setInsuTerm(
						findIdCodeTable(CTCat.INSU_TERM.value(), request.getFinancialInfo().getInsuTermValue()));
		}

		if (request.getContactInfo() != null) {

			if (request.getContactInfo().getCommunicationInfo() != null
					&& request.getContactInfo().getCommunicationInfo().size() > 0) {
				for (CustomerCommunicationInfoDTO communication : request.getContactInfo().getCommunicationInfo()) {

					if (!StringUtils.isNullOrEmpty(communication.getType()))
						communication
								.setContactType(findIdCodeTable(CTCat.CONTAC_TYP.value(), communication.getType()));

					if (!StringUtils.isNullOrEmpty(communication.getCategory()))
						communication.setContactCategory(
								findIdCodeTable(CTCat.CONTAC_CAT.value(), communication.getCategory()));
				}
			}

			if (request.getContactInfo().getAddressInfo() != null
					&& request.getContactInfo().getAddressInfo().size() > 0) {
				for (CustomerAddressInfoDTO address : request.getContactInfo().getAddressInfo()) {

					if (!StringUtils.isNullOrEmpty(address.getType()))
						address.setAddrType(findIdCodeTable(CTCat.ADDR_TYPE.value(), address.getType()));

					if (!StringUtils.isNullOrEmpty(address.getAddrProvinceValue()))
						address.setProvince(findIdCodeTable(CTCat.PROVINCE.value(), address.getAddrProvinceValue()));

					if (!StringUtils.isNullOrEmpty(address.getAddrDistrictValue()))
						address.setDistrict(findIdCodeTable(CTCat.DISTRICT.value(), address.getAddrDistrictValue()));

					if (!StringUtils.isNullOrEmpty(address.getAddrWardValue()))
						address.setWard(findIdCodeTable(CTCat.WARD.value(), address.getAddrWardValue()));
				}
			}
		}

		if (request.getCompanyInfo() != null) {
			if (!StringUtils.isNullOrEmpty(request.getCompanyInfo().getCompAddrProvinceValue()))
				request.getCompanyInfo().setCompAddrProvince(
						findIdCodeTable(CTCat.PROVINCE.value(), request.getCompanyInfo().getCompAddrProvinceValue()));

			if (!StringUtils.isNullOrEmpty(request.getCompanyInfo().getCompAddrDistrictValue()))
				request.getCompanyInfo().setCompAddrDistrict(
						findIdCodeTable(CTCat.DISTRICT.value(), request.getCompanyInfo().getCompAddrDistrictValue()));

			if (!StringUtils.isNullOrEmpty(request.getCompanyInfo().getCompAddrWardValue()))
				request.getCompanyInfo().setCompAddrWard(
						findIdCodeTable(CTCat.WARD.value(), request.getCompanyInfo().getCompAddrWardValue()));
		}

		if (request.getCustomerIdentity() != null && request.getCustomerIdentity().size() > 0) {

			for (CustomerIdentityDTO obj : request.getCustomerIdentity()) {

				if (!StringUtils.isNullOrEmpty(obj.getIdentityIssuePlaceValue()))
					obj.setIdentityIssuePlace(
							findIdCodeTable(CTCat.IDENTITY_ISSUE_PLACE.value(), obj.getIdentityIssuePlaceValue()));

				if (!StringUtils.isNullOrEmpty(obj.getIdentityTypeIdValue()))
					obj.setIdentityTypeId(
							findIdCodeTable(CTCat.IDENTITY_TYPE_ID.value(), obj.getIdentityTypeIdValue()));
			}
		}
	}

	public CustomerAccountLinkUpsertResultDTO upsertAccountLink(CustomerDTO request, String updateId) throws Exception {
		CustomerAccountLinkUpsertResultDTO result = null;
		_custValid.validateCustomerAccountLink(request, updateId);

		try {
			uok = new UnitOfWork();
			this.uok.start();

			CustomerAggregate item = CustomerFactory.getInstance(request, this.uok.customer);

			if (updateId != null) {
				Long id = Long.parseLong(updateId);
				if (item.findCustomerAccountLinkById(id) == null)
					throw new ValidationException("Account Link not found");

				item.getAccountLink().setId(id);
			}

			item.upsertAccountLink();

			result = CustomerFactory.getInstance(item.getAccountLink().getId());

			this.uok.commit();
		} catch (Throwable ex) {

			this.uok.rollback();

			throw new ValidationException(ex.toString());
		}

		return result;
	}

	public IdDTO getCustIdByRelationIdMessageLog(String relationId) throws Exception {

		IdDTO obj = null;

		if (StringUtils.isNullOrEmpty(relationId))
			throw new ValidationException("relationId is invalid!");

		try {

			obj = new IdDTO();
			uok = new UnitOfWork();
			this.uok.start();

			CustomerAggregate item = CustomerFactory.getInstance(new CustomerDTO(), this.uok.customer);

			obj.setId(item.findCustIdByRelationIdMessageLog(relationId));

			this.uok.commit();

		} catch (Throwable ex) {

			this.uok.rollback();

			throw new ValidationException(ex.toString());
		}
		return obj;
	}

	public IdDTO getAppIdByRelationIdMessageLog(String relationId) throws Exception {

		IdDTO obj = null;

		if (StringUtils.isNullOrEmpty(relationId))
			throw new ValidationException("relationId is invalid!");

		try {

			obj = new IdDTO();
			uok = new UnitOfWork();
			this.uok.start();

			CustomerAggregate item = CustomerFactory.getInstance(new CustomerDTO(), this.uok.customer);

			obj.setId(item.findAppIdByRelationIdMessageLog(relationId));

			this.uok.commit();

		} catch (Throwable ex) {

			this.uok.rollback();

			throw new ValidationException(ex.toString());
		}
		return obj;
	}
	
	public void close() throws IOException {
	}
	
	/**
	 * Get total debt in MC: get data by DWH
	 * @param identityId
	 * @param oldIdentityId
	 * @param militaryId
	 * @return
	 * @throws ValidationException
	 * @throws Exception
	 */
	public TotalDebt findTotalDebtMC(String identityId, String oldIdentityId, String militaryId) throws ValidationException, Exception {
		if(StringUtils.isNullOrEmpty(identityId) && StringUtils.isNullOrEmpty(oldIdentityId) && StringUtils.isNullOrEmpty(militaryId)) {
			throw new ValidationException(Messages.getString("debt.total.mc.error"));
		}
		TotalDebt totalDebtMC = new TotalDebt(new Long("-9999"));
		EsbApi esb = new EsbApi();
		CustomerDisbursementDebtDTO debtDTO = new CustomerDisbursementDebtDTO();
		ApiResult apiResult = esb.findTotalDebtMC(identityId.trim(), oldIdentityId.trim(), militaryId.trim());
		if(apiResult == null || !apiResult.getStatus()) {
			throw new ValidationException("ESB.Disbursement/totalOSBalance error");
		}
		ObjectMapper mapper = new ObjectMapper();
		debtDTO = mapper.readValue(apiResult.getBodyContent(), CustomerDisbursementDebtDTO.class);
		totalDebtMC.setTotalDebt(Long.parseLong(debtDTO.getTotalOSBalance()));
		return totalDebtMC;
	}

}






