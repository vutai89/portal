package com.mcredit.business.customer;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mcredit.business.customer.dto.CustomerDisbursementDebtDTO;
import com.mcredit.data.customer.UnitOfWorkCustomer;
import com.mcredit.data.customer.entity.CustomerAccountLink;
import com.mcredit.data.customer.entity.CustomerAddlInfo;
import com.mcredit.data.customer.entity.CustomerAddressInfo;
import com.mcredit.data.customer.entity.CustomerCompanyInfo;
import com.mcredit.data.customer.entity.CustomerContactInfo;
import com.mcredit.data.customer.entity.CustomerFinancialInfo;
import com.mcredit.data.customer.entity.CustomerIdentity;
import com.mcredit.data.customer.entity.CustomerPersonalInfo;
import com.mcredit.model.dto.CardInformationDTO;
import com.mcredit.model.enums.CardState;
import com.mcredit.model.enums.Commons;
import com.mcredit.model.enums.ContractState;
import com.mcredit.model.enums.IdentityType;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.QueyInfo;
import com.mcredit.model.object.CardInformation;
import com.mcredit.model.object.ContractCheck;
import com.mcredit.model.object.TotalDebt;
import com.mcredit.model.object.warehouse.DocumentStateInfo;
import com.mcredit.model.telesale.ContractSearch;
import com.mcredit.restcore.model.ApiResult;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.esb.EsbApi;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.StringUtils;

public class CustomerAggregate {

	/**************** Begin Property ***************/
	private CustomerPersonalInfo personalInfo;
	private CustomerAccountLink accountLink;
	private CustomerAddlInfo addlInfo;
	private CustomerFinancialInfo financialInfo;
	private List<CustomerContactInfo> contactInfo;
	private List<CustomerAddressInfo> addressInfo;
	private CustomerCompanyInfo companyInfo;
	private List<CustomerIdentity> lstIdentity;
	private UnitOfWorkCustomer unitOfWorkCustomer = null;
			
	private static ModelMapper modelMapper = new ModelMapper();
	
	private final static Lock lock = new ReentrantLock();
	/**************** End Property ***************/

	/****************
	 * Begin Constructor
	 ***************/
	public CustomerAggregate(UnitOfWorkCustomer uok) {

		this.unitOfWorkCustomer = uok;
	}

	/****************
	 * End Constructor
	 ***************/

	public List<CustomerAddressInfo> getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(List<CustomerAddressInfo> addressInfo) {
		this.addressInfo = addressInfo;
	}
	
	public CustomerPersonalInfo getPersonalInfo() {
		return personalInfo;
	}

	public void setPersonalInfo(CustomerPersonalInfo personalInfo) {
		this.personalInfo = personalInfo;
	}

	public CustomerAddlInfo getAddlInfo() {
		return addlInfo;
	}

	public void setAddlInfo(CustomerAddlInfo addlInfo) {
		this.addlInfo = addlInfo;
	}

	public CustomerFinancialInfo getFinancialInfo() {
		return financialInfo;
	}

	public void setFinancialInfo(CustomerFinancialInfo financialInfo) {
		this.financialInfo = financialInfo;
	}

	public List<CustomerContactInfo> getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(List<CustomerContactInfo> contactInfo) {
		this.contactInfo = contactInfo;
	}

	public CustomerCompanyInfo getCompanyInfo() {
		return companyInfo;
	}

	public void setCompanyInfo(CustomerCompanyInfo companyInfo) {
		this.companyInfo = companyInfo;
	}

	public CustomerAccountLink getAccountLink() {
		return accountLink;
	}

	public void setAccountLink(CustomerAccountLink accountLink) {
		this.accountLink = accountLink;
	}

	public List<CustomerIdentity> getLstIdentity() {
		return lstIdentity;
	}

	public void setLstIdentity(List<CustomerIdentity> lstIdentity) {
		this.lstIdentity = lstIdentity;
	}

	/****************
	 * Begin Behavior
	 * 
	 * @throws Exception
	 ***************/
	public void upsertCustomer() throws ValidationException {

		if (this.personalInfo == null)
			throw new ValidationException("Object personalInfo is null!");

		if (this.personalInfo.getId() == null || this.personalInfo.getId() == 0) {
			
			if (this.personalInfo.getCoreCustCode()!=null && unitOfWorkCustomer.customerPersonalInfoRepo().findCustomerByCoreCustCode(this.personalInfo.getCoreCustCode()) != null)
				throw new ValidationException("CoreCustCode is existed!");

			if(this.personalInfo.getMcCustCode()==null) {
				
				String identityNumber = "";
				if (lstIdentity != null && lstIdentity.size() > 0) {
					for (CustomerIdentity identity : lstIdentity) {
						if(identity.getIdentityTypeIdValue().equals(IdentityType.CMND.value())) {
							identityNumber = identity.getIdentityNumber();
							//System.out.println("==> identityNumber: " + identityNumber);
							break;
						}
					}
					
					if( "".equals(identityNumber) ) //Truong hop khach hang khong co so CMND
						identityNumber = lstIdentity.get(0).getIdentityNumber();
				}
				
				this.personalInfo.setMcCustCode(StringUtils.generateMcCustCode((!"".equals(identityNumber)?identityNumber:"") + "-" + this.personalInfo.getBirthDate().getTime()));
				if (unitOfWorkCustomer.customerPersonalInfoRepo().findCustomerByMcCustCode(this.personalInfo.getMcCustCode()) != null) {
					//throw new ValidationException("McCustCode is existed!");
					System.out.println("McCustCode is existed!");
					Long custId = unitOfWorkCustomer.customerPersonalInfoRepo().findCustomerByMcCustCode(this.personalInfo.getMcCustCode());
					this.personalInfo.setId(custId);
					//this.companyInfo.setId(unitOfWorkCustomer.customerPersonalInfoRepository().findValueByField("findCustomerCompanyInfoIdByCustId", "custId", custId));
					this.addlInfo.setId(unitOfWorkCustomer.customerPersonalInfoRepo().findValueByField("findCustomerAddlInfoIdByCustId", "custId", custId));
					this.financialInfo.setId(unitOfWorkCustomer.customerPersonalInfoRepo().findValueByField("findCustomerFinancialInfoIdByCustId", "custId", custId));
					
					if( this.contactInfo!=null && this.contactInfo.size()>0 ) {
						for( CustomerContactInfo item : this.contactInfo ) {
							if( !StringUtils.isNullOrEmpty(item.getContactValue()) )
								item.setId(unitOfWorkCustomer.customerPersonalInfoRepo()
														 .findCustContactInfoIdBy(custId, item.getContactType(), item.getContactCategory(), item.getContactValue()));
						}
					}
					
					if( this.addressInfo!=null && this.addressInfo.size()>0 ) {
						for( CustomerAddressInfo item : this.addressInfo ) {
							if( !StringUtils.isNullOrEmpty(item.getAddress()) )
								item.setId(unitOfWorkCustomer.customerPersonalInfoRepo()
										 .findCustAddressInfoIdBy(custId, item.getAddrType(), item.getAddress()));
						}
					}
					
					if (this.lstIdentity != null && this.lstIdentity.size() > 0) {
						for (CustomerIdentity identity : this.lstIdentity) {
							identity.setId(unitOfWorkCustomer.customerPersonalInfoRepo().findIdentityIdBy(custId, identity.getIdentityTypeId()));
						}
					}
				}
			}
		}

		this.personalInfo.setIdentityId(new Long("0"));
		unitOfWorkCustomer.customerPersonalInfoRepo().upsert(this.personalInfo);

		/*
		 * if (this.companyInfo != null) { //this.companyInfo.setCustId((Long)
		 * this.personalInfo.getId());
		 * 
		 * unitOfWorkCustomer.customerCompanyInfoRepo().upsert(this.companyInfo); }
		 */

		if (this.addlInfo != null) {
			this.addlInfo.setCustId(this.personalInfo.getId().longValue());
			
			if (this.companyInfo != null)
				this.addlInfo.setCustCompanyId(this.companyInfo.getId());

			unitOfWorkCustomer.customerAddlInfoRepo().upsert(this.addlInfo);
		}

		if (this.financialInfo != null) {
			this.financialInfo.setCustId(this.personalInfo.getId().longValue());

			unitOfWorkCustomer.customerFinancialInfoRepo().upsert(this.financialInfo);
		}

		if (this.contactInfo != null && this.contactInfo.size()>0) {
			for( CustomerContactInfo item : this.contactInfo ) {
				if( !StringUtils.isNullOrEmpty(item.getContactValue()) ) {
					item.setCustId(this.personalInfo.getId().longValue());
					
					if( item.getId()==null || item.getId()==0 ) {
						Integer currPriority = unitOfWorkCustomer.customerContactInfoRepo()
																 .findNextContactPriority(item.getCustId(), item.getContactType(), item.getContactCategory());
						if (currPriority == null || currPriority == 0)
							item.setContactPriority(1);
						else
							item.setContactPriority(currPriority + 1);
					}
					unitOfWorkCustomer.customerContactInfoRepo().upsert(item);
				}
			}
		}

		if (this.addressInfo != null && this.addressInfo.size()>0) {
			for( CustomerAddressInfo item : this.addressInfo ) {
				if( !StringUtils.isNullOrEmpty(item.getAddress()) ) {
					item.setCustId(this.personalInfo.getId().longValue());
					
					if( item.getId()==null || item.getId()==0 ) {
						Integer addressOrder = unitOfWorkCustomer.customerAddressInfoRepo()
																 .findNextAddressOrder(item.getCustId(), item.getAddrType());
						if (addressOrder == null || addressOrder == 0)
							item.setAddrOrder(1);
						else
							item.setAddrOrder(addressOrder + 1);
					}
					unitOfWorkCustomer.customerAddressInfoRepo().upsert(item);
				}
			}
		}
		
		if (this.lstIdentity != null && this.lstIdentity.size() > 0) {
			
			for (CustomerIdentity identity : this.lstIdentity) {
				identity.setCustId(this.personalInfo.getId().longValue());

				unitOfWorkCustomer.customerIdentityRepo().upsert(identity);
				
				//set identityId for CustPersonalInfo
				if(identity.getIdentityTypeIdValue().equals(IdentityType.CMND.value()))
					this.personalInfo.setIdentityId(identity.getId());
			}
		}
	}

	public List<DocumentStateInfo> findContractInfo(ContractSearch contractSearch) {
		return unitOfWorkCustomer.customerPersonalInfoRepo().findContractInfo(contractSearch);
	}
	
	public ContractCheck findContractDuplicate(String identityNumber) throws ValidationException, Exception {
		
		ContractCheck item = unitOfWorkCustomer.customerPersonalInfoRepo().findContractDuplicate(identityNumber, "");
		
		if( item == null || item.getCustId().equals(0L) ) { //Check tiep dieu kien tongDuNo >= 100000000
//			Long tongDuNo = unitOfWorkCustomer.customerPersonalInfoRepo().findTotalDebtPaymentNext(identityNumber, "");
			CustomerManager cusManager = new CustomerManager();
			Long tongDuNo = cusManager.findTotalDebtMC(identityNumber, identityNumber, identityNumber).getTotalDebt();
			if( tongDuNo >= Commons.TONG_DU_NO.intValue() )
				item.setCustId(new Long("1"));//Set custId co gia tri, danh dau la bi duplicate
		}
		
		return item;
	}
	
	public Long findTotalDebt(String identityNumber, String oldIdentityNumber, String militaryNumber) {
		
		return unitOfWorkCustomer.customerPersonalInfoRepo().findTotalDebtForBPM(identityNumber, oldIdentityNumber, militaryNumber);
	}
	
	public void upsertAccountLink() throws Exception {
		
		//Xu ly lock nhung xu ly song song
		lock.lock();
		try {
			if (this.accountLink == null)
				throw new ValidationException("Object accountLink is null!");

			if(this.accountLink.getId() == null || this.accountLink.getId() == 0) {
				 
				 Integer currSeqLink = unitOfWorkCustomer.customerAccountLinkRepo().findNextLinkSeq(this.accountLink.getCustId(), this.accountLink.getLinkType());
				 if (currSeqLink == null || currSeqLink == 0)
					 this.accountLink.setLinkSeq(1);
				 else
					 this.accountLink.setLinkSeq(currSeqLink + 1);
			}

			unitOfWorkCustomer.customerAccountLinkRepo().upsert(this.accountLink);
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}finally{
			lock.unlock();
		}
	}

	public void updateCoreCustCode() throws Exception {
		if (this.personalInfo == null)
			throw new ValidationException("Object personalInfo is null!");

		/*if (this.personalInfo.getCoreCustCode().indexOf(Commons.CUSTOMER_CODE_PREFIX.value()) == -1)
			this.personalInfo.setCoreCustCode(Commons.CUSTOMER_CODE_PREFIX.value() + this.personalInfo.getCoreCustCode());*/

		if (unitOfWorkCustomer.customerPersonalInfoRepo().findCustomerPersonalInfoById(this.personalInfo.getId()) == null)
			throw new ValidationException("Customer not found");

		unitOfWorkCustomer.customerPersonalInfoRepo().updateCoreCustCode(this.personalInfo);
	}

	public Long findCustIdByRelationIdMessageLog(String relationId) throws Exception {
		Long custId = null;
		if (!StringUtils.isNullOrEmpty(relationId))
			custId = unitOfWorkCustomer.customerPersonalInfoRepo().findCustIdByRelationIdMessageLog(relationId);
		return custId;
	}
	
	public Long findAppIdByRelationIdMessageLog(String relationId) throws Exception {
		Long custId = null;
		if (!StringUtils.isNullOrEmpty(relationId))
			custId = unitOfWorkCustomer.customerPersonalInfoRepo().findAppIdByRelationIdMessageLog(relationId);
		return custId;
	}

	public CustomerPersonalInfo findCustomerPersonalInfoById(Long id) {
		return unitOfWorkCustomer.customerPersonalInfoRepo().findCustomerPersonalInfoById(id);
	}

	public void removeCustomer() throws Exception {
		unitOfWorkCustomer.customerPersonalInfoRepo().remove(this.personalInfo);
	}
	
	public CardInformationDTO findCardInformationByCardId(String cardId) throws Exception {
		
		CardInformation result = unitOfWorkCustomer.customerAccountLinkRepo().findCardInformationByCardId(cardId
											, CacheManager.Parameters().findParamValueAsBigDecimal(ParametersName.FEE_COLLECT_AMT)
											, new String[]{
												CardState.CARD_OK.value(),
												CardState.CARD_DO_NOT_HONOR.value(),
												CardState.CARD_NO_RENEWAL_BADEBT.value(),
												CardState.PIN_TRIES_EXECEEDED.value(),
												CardState.CARD_EXPIRED_ZCODE_76.value(),
												CardState.PICKUP_04.value(),
												CardState.PickUp_L_41.value()
											});
		
		if( result==null )
			throw new ValidationException("Kh\u00F4ng t\u00ECm th\u1EA5y th\u1EBB");
		if( result.getPaymentAmount()==null )
			throw new ValidationException("Kh\u00F4ng t\u00ECm th\u1EA5y th\u00F4ng tin sao k\u00EA th\u1EBB");
		if( result.getPaymentAmount().equals(new BigDecimal(-1)) )
			throw new ValidationException("Tr\u1EA1ng th\u00E1i th\u1EBB kh\u00F4ng h\u1EE3p l\u1EC7");
			
		return modelMapper.map(result, CardInformationDTO.class);
	}
	
	public Object queryInfo(String type, String contractNumbers, String identityNumber, String militaryId, String mobilePhone) throws ValidationException {
		
		Object obj = null;
		
		if( QueyInfo.CREDIT_CONTRACT.value().equals(type) )
			obj = unitOfWorkCustomer.customerAccountLinkRepo().findCreditContractInfo(contractNumbers, identityNumber, militaryId, mobilePhone);
		
		if( QueyInfo.PAYMENT_ON_WEB.value().equals(type) )
			obj = unitOfWorkCustomer.customerAccountLinkRepo().findPaymentOnWeb(contractNumbers, identityNumber, militaryId);
		
		return obj;
	}
	
	public int checkContractNumber(String contractNumber) throws Exception {
		
		String status = unitOfWorkCustomer.customerAccountLinkRepo().checkContractNumber(contractNumber);
		
		if( !"".equals(status) ) {
			if( status.equalsIgnoreCase(ContractState.EARLY_PAYMENT.value()) || status.equalsIgnoreCase(ContractState.CLOSE.value()) )
				return 1;
			else if( status.equalsIgnoreCase(ContractState.BACK_DATE.value()) || status.equalsIgnoreCase(ContractState.OPEN.value()) )
				return 2;
			else if( status.equalsIgnoreCase(ContractState.CANCEL.value()) )
				return 3;
		}
		
		return 0;
	}
	
	public CustomerAccountLink findCustomerAccountLinkById(Long id) {
		CustomerAccountLink accountLink = unitOfWorkCustomer.customerAccountLinkRepo().findCustomerAccountLinkById(id);
		return accountLink;
	}
	
}