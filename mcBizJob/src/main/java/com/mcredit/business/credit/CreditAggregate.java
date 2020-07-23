package com.mcredit.business.credit;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mcredit.data.credit.UnitOfWorkCredit;
import com.mcredit.data.credit.entity.CreditApplicationAdditional;
import com.mcredit.data.credit.entity.CreditApplicationAppraisal;
import com.mcredit.data.credit.entity.CreditApplicationBPM;
import com.mcredit.data.credit.entity.CreditApplicationLoanManagement;
import com.mcredit.data.credit.entity.CreditApplicationRequest;
import com.mcredit.model.dto.CardNotifyDTO;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.LongUtil;
import com.mcredit.util.StringUtils;

public class CreditAggregate {

	/**************** Begin Property ***************/
	private UnitOfWorkCredit unitOfWorkCredit = null;
	private CreditApplicationAppraisal applicationAppraisal;
	private CreditApplicationBPM applicationBPM;
	private CreditApplicationLoanManagement applicationLMN;
	private CreditApplicationRequest applicationRequest;
	private CreditApplicationAdditional applicationAdditional;
	private List<CardNotifyDTO> cardNotifyList;
	
	public CreditAggregate(UnitOfWorkCredit uok) {
		
		this.unitOfWorkCredit = uok;
	}

	@SuppressWarnings("rawtypes")
	public List<CardNotifyDTO> getCardNotifyMinList(){
		List cardNotifyList = this.unitOfWorkCredit.creditApplicationOutstandingRepo().findNativeCardUnderMinPayment();
		this.cardNotifyList = new ArrayList<CardNotifyDTO>();
		CardNotifyDTO cardNotify;
		for (Iterator iterator = cardNotifyList.iterator(); iterator.hasNext();) {
			Object[] record = (Object[]) iterator.next();
			cardNotify = new CardNotifyDTO();
			cardNotify.setCardId((String)record[0]);
			cardNotify.setDueBalance(((BigDecimal)record[1]).longValue());
			cardNotify.setRemainMinBalance(((BigDecimal)record[2]).longValue());
			cardNotify.setMobileNumber((String) record[3]);
			this.cardNotifyList.add(cardNotify);
		}
		return this.cardNotifyList;
	}
	
	@SuppressWarnings("rawtypes")
	public List<CardNotifyDTO> getCardNotifyDueList(){
		List cardNotifyList = this.unitOfWorkCredit.creditApplicationOutstandingRepo().findNativeCardUnderDuePayment();
		this.cardNotifyList = new ArrayList<CardNotifyDTO>();
		CardNotifyDTO cardNotify;
		for (Iterator iterator = cardNotifyList.iterator(); iterator.hasNext();) {
			Object[] record = (Object[]) iterator.next();
			cardNotify = new CardNotifyDTO();
			cardNotify.setCardId((String)record[0]);
			cardNotify.setDueBalance(((BigDecimal)record[1]).longValue());
			cardNotify.setRemainMinBalance(((BigDecimal)record[2]).longValue());
			cardNotify.setMobileNumber((String) record[3]);
			this.cardNotifyList.add(cardNotify);
		}
		return this.cardNotifyList;
	}

	
	public CreditApplicationAdditional getApplicationAdditional() {
		return applicationAdditional;
	}

	public void setApplicationAdditional(CreditApplicationAdditional applicationAdditional) {
		this.applicationAdditional = applicationAdditional;
	}

	public UnitOfWorkCredit getUnitOfWorkCredit() {
		return unitOfWorkCredit;
	}


	public void setUnitOfWorkCredit(UnitOfWorkCredit unitOfWorkCredit) {
		this.unitOfWorkCredit = unitOfWorkCredit;
	}


	public CreditApplicationAppraisal getApplicationAppraisal() {
		return applicationAppraisal;
	}


	public void setApplicationAppraisal(CreditApplicationAppraisal applicationAppraisal) {
		this.applicationAppraisal = applicationAppraisal;
	}


	public CreditApplicationBPM getApplicationBPM() {
		return applicationBPM;
	}


	public void setApplicationBPM(CreditApplicationBPM applicationBPM) {
		this.applicationBPM = applicationBPM;
	}


	public CreditApplicationLoanManagement getApplicationLMN() {
		return applicationLMN;
	}


	public void setApplicationLMN(CreditApplicationLoanManagement applicationLMN) {
		this.applicationLMN = applicationLMN;
	}


	public CreditApplicationRequest getApplicationRequest() {
		return applicationRequest;
	}


	public void setApplicationRequest(CreditApplicationRequest applicationRequest) {
		this.applicationRequest = applicationRequest;
	}

	
	/**************** End Property ***************/

	
	public List<CardNotifyDTO> getCardNotifyList() {
		return cardNotifyList;
	}

	/****************
	 * Begin Behavior
	 * 
	 * @throws Exception
	 ***************/
	public void removeCredit() throws Exception{
		if (this.applicationRequest == null) throw new Exception("CreditApplicationRequest does not exist!");
		if (this.applicationAppraisal == null) throw new Exception("CreditApplicationApprsail does not exist!");
		if (this.applicationBPM == null) throw new Exception("CreditApplicationBPM does not exist!");
		if (this.applicationLMN == null) throw new Exception("CreditApplicationLMN does not exist!");
		if (this.applicationAdditional == null) throw new Exception("CreditApplicationAdditional does not exist!");
		
		unitOfWorkCredit.creditApplicationAdditionalRepo().remove(this.applicationAdditional);
		unitOfWorkCredit.creditApplicationAppraisalRepo().remove(this.applicationAppraisal);
		unitOfWorkCredit.creditApplicationBPMRepo().remove(this.applicationBPM);
		unitOfWorkCredit.creditApplicationLoanManagementRepo().remove(this.applicationLMN);
		unitOfWorkCredit.creditApplicationRequestRepo().remove(this.applicationRequest);
		
	}
	
	public void checkUniqueKeys() throws ValidationException {
		
		CreditApplicationRequest CAR = unitOfWorkCredit.creditApplicationRequestRepo().getAppRequestBy(this.applicationRequest.getMcContractNumber());
		if (CAR != null) {
			this.applicationRequest.setId(CAR.getId());
			this.applicationRequest.setRecordStatus(CAR.getRecordStatus());
		}
		
		List<CreditApplicationLoanManagement> CALL = unitOfWorkCredit.creditApplicationLoanManagementRepo().getAppRequestBy(this.applicationLMN.getCoreLnAppId(),this.applicationRequest.getId());
		if (CALL.size()>=2) throw new ValidationException("Core Loan Id doesn't match with old record. Cannot upsert!");
		if (CALL.size() == 1 ) {
			this.applicationLMN.setId(CALL.get(0).getId());
			this.applicationLMN.setRecordStatus(CALL.get(0).getRecordStatus());
		}
		
		List<CreditApplicationBPM> CABL = unitOfWorkCredit.creditApplicationBPMRepo().getAppRequestBy(this.applicationBPM.getBpmAppId(),this.applicationRequest.getId());
		if (CABL.size()>=2) throw new ValidationException("BPM APP ID doesn't match with old record. Cannot upsert!");
		if (CABL.size() == 1 ) {
			this.applicationBPM.setId(CABL.get(0).getId());
			this.applicationBPM.setRecordStatus(CABL.get(0).getRecordStatus());
		}
		
		List<CreditApplicationAppraisal> CAAL = unitOfWorkCredit.creditApplicationAppraisalRepo().getAppRequestBy(this.applicationRequest.getId());
//		if (CAAL.size()>=2) throw new ValidationException("BPM APP ID doesn't match with old record. Cannot upsert!");
		if (CAAL.size() == 1 ) {
			this.applicationAppraisal.setId(CAAL.get(0).getId());
			this.applicationAppraisal.setRecordStatus(CAAL.get(0).getRecordStatus());
		}

		List<CreditApplicationAdditional> CAAdL = unitOfWorkCredit.creditApplicationAdditionalRepo().getAppRequestBy(this.applicationRequest.getId());
//		if (CAAdL.size()>=2) throw new ValidationException("BPM APP ID doesn't match with old record. Cannot upsert!");
		if (CAAdL.size() == 1 ) {
			this.applicationAdditional.setId(CAAdL.get(0).getId());
			this.applicationAdditional.setRecordStatus(CAAdL.get(0).getRecordStatus());
		}

		
		this.unitOfWorkCredit.flush();
	}
	
	
	public void upsertCredit() throws Exception {

//		private CreditApplicationAppraisal applicationAppraisal;
//		private CreditApplicationBPM applicationBPM;
//		private CreditApplicationLoanManagement applicationLMN;
//		private CreditApplicationRequest applicationRequest;
		
		checkUniqueKeys();
		
		if (this.applicationRequest == null)
			throw new Exception("Object applicationRequest is null!");
		
		
		if (this.applicationRequest.getId() == null || this.applicationRequest.getId() == 0)
			this.applicationRequest.setId(LongUtil.toId(unitOfWorkCredit.creditApplicationRequestRepo().findNextSeq()));
	
		unitOfWorkCredit.creditApplicationRequestRepo().upsert(this.applicationRequest);

		if (applicationLMN != null) {
			applicationLMN.setCreditAppId(this.applicationRequest.getId());
			if (this.applicationLMN.getId() == null || this.applicationLMN.getId() == 0)
				this.applicationLMN.setId(LongUtil.toId(unitOfWorkCredit.creditApplicationLoanManagementRepo().findNextSeq()));
			unitOfWorkCredit.creditApplicationLoanManagementRepo().upsert(this.applicationLMN);
		}

		if (applicationBPM != null) {
			applicationBPM.setCreditAppId(this.applicationRequest.getId());
			if (this.applicationBPM.getId() == null || this.applicationBPM.getId() == 0)
				this.applicationBPM.setId(LongUtil.toId(unitOfWorkCredit.creditApplicationBPMRepo().findNextSeq()));
			unitOfWorkCredit.creditApplicationBPMRepo().upsert(this.applicationBPM);
		}

		if (applicationAppraisal != null) {
			applicationAppraisal.setCreditAppId(this.applicationRequest.getId());
			if (this.applicationAppraisal.getId() == null || this.applicationAppraisal.getId() == 0)
				this.applicationAppraisal.setId(LongUtil.toId(unitOfWorkCredit.creditApplicationAppraisalRepo().findNextSeq()));
			unitOfWorkCredit.creditApplicationAppraisalRepo().upsert(this.applicationAppraisal);
		}
		
		if (applicationAdditional != null) {
			applicationAdditional.setCreditAppId(this.applicationRequest.getId());
			if (this.applicationAdditional.getId() == null || this.applicationAdditional.getId() == 0)
				this.applicationAdditional.setId(LongUtil.toId(unitOfWorkCredit.creditApplicationAdditionalRepo().findNextSeq()));
			unitOfWorkCredit.creditApplicationAdditionalRepo().upsert(this.applicationAdditional);
		}

	}
	
	public void updateCreditAppLmsWithCardId(String cardId, Long creditAppId) throws Exception {
		
		if (StringUtils.isNullOrEmpty(cardId))
			throw new ValidationException("CreditAggregate.updateCreditAppLmsWithCardId() ==> cardNumber is null!");
		
		if (creditAppId==null || creditAppId<=0)
			throw new ValidationException("CreditAggregate.updateCreditAppLmsWithCardId() ==> creditAppId is null!");

		unitOfWorkCredit.creditApplicationLoanManagementRepo().updateCreditAppLmsWithCardId(cardId, creditAppId);
	}
	/**************** End Behavior ***************/
}
