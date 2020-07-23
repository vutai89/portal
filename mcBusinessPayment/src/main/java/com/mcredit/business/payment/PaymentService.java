package com.mcredit.business.payment;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.modelmapper.ModelMapper;

import com.mcredit.common.Messages;
import com.mcredit.data.credit.UnitOfWorkCredit;
import com.mcredit.data.credit.entity.CreditApplicationOutstanding;
import com.mcredit.data.payment.UnitOfWorkPayment;
import com.mcredit.data.payment.entity.PostingInstance;
import com.mcredit.model.dto.PartnerDTO;
import com.mcredit.model.dto.PostingConfigurationDTO;
import com.mcredit.model.dto.PostingInstanceDTO;
import com.mcredit.model.dto.card.Amount;
import com.mcredit.model.dto.card.CreditAccountDTO;
import com.mcredit.model.dto.card.DebitAccountDTO;
import com.mcredit.model.dto.card.MakePaymentCreditCard4DTO;
import com.mcredit.model.dto.card.OtherDTO;
import com.mcredit.model.dto.card.PaymentCreditCardDTO;
import com.mcredit.model.dto.card.PaymentSMSDTO;
import com.mcredit.model.dto.card.PaymentSendSMSBody;
import com.mcredit.model.enums.AmountFormat;
import com.mcredit.model.enums.FeeAmount;
import com.mcredit.model.enums.MsgChannel;
import com.mcredit.model.enums.MsgConcurency;
import com.mcredit.model.enums.MsgStatus;
import com.mcredit.model.enums.OwnerTag;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.PaymentAmountTag;
import com.mcredit.model.enums.PostingConfigurationDebitOwner;
import com.mcredit.model.enums.SMSType;
import com.mcredit.model.enums.T24OtherTag;
import com.mcredit.sharedbiz.cache.CacheManager;
import com.mcredit.sharedbiz.cache.CodeTableCacheManager;
import com.mcredit.sharedbiz.cache.PartnerCacheManager;
import com.mcredit.sharedbiz.validation.ValidationException;
import com.mcredit.util.DateUtil;
import com.mcredit.util.JSONConverter;

public class PaymentService {	
	private UnitOfWorkCredit unitOfWorkCredit = null;
	private UnitOfWorkPayment unitOfWorkPayment = null;
	private static ModelMapper modelMapper = new ModelMapper();
	

	/**************** End Property ***************/

	public PaymentService(UnitOfWorkPayment uokPayment , UnitOfWorkCredit unitOfWorkCredit) {
		this.unitOfWorkPayment = uokPayment;
		this.unitOfWorkCredit = unitOfWorkCredit;
	}
	
	public List<PostingInstanceDTO> pInstances;
		
	public List<PostingInstanceDTO> getpInstances() {
		return pInstances;
	}

	public void setpInstances(List<PostingInstanceDTO> pInstances) {
		this.pInstances = pInstances;
	}

	/****************
	 * Begin Behavior
	 * 
	 * @throws Exception
	 ***************/
	public List<PostingConfigurationDTO> createPayment(String partnerRefId ,Long custId,String partnerCode,String postingGroup,BigDecimal amount,String cardId) throws Exception {
		
		PartnerDTO partner = PartnerCacheManager.getInstance().getPartnerByPartnerCode(partnerCode);
		if(partner == null )
			throw new ValidationException("Payment Channel Code is invalid");
		
		List<PostingConfigurationDTO> postingConfigurationDTOs = CacheManager.PostingConfiguration().getPostConfigByPartnerIdAndPostGroup(partner.getId().intValue(), postingGroup);
		
		if(postingConfigurationDTOs != null && postingConfigurationDTOs.size()>0) {
			
			PostingInstance pInstance = null;
			BigDecimal  feeCollectATM = null ; 
			List<PostingInstanceDTO> lsPInstance = new ArrayList<PostingInstanceDTO>();
			for (PostingConfigurationDTO pscDTO : postingConfigurationDTOs) {
				if(pscDTO.getAmountTag().equals(PaymentAmountTag.FEE_COLLECT_AMT.value())){
					feeCollectATM = pscDTO.getAmount();
				}
					
				pInstance = new PostingInstance();
				Long StringpInstanceID = pInstance.getId();
				pInstance  = modelMapper.map(pscDTO, PostingInstance.class);
				
				pInstance.setId(StringpInstanceID);
				pInstance.setPartnerRefId(partnerRefId);
				
				PostingConfigurationDebitOwner enums = PostingConfigurationDebitOwner.fromString(pscDTO.getDebitOwner());
				
				switch (enums) {
					case CUSTOMER:
						if(pscDTO.getDebitAccount().equals(OwnerTag.OWNERTAG_ACCOUNT.value())
								|| pscDTO.getDebitAccount().equals(OwnerTag.OWNERTAG_CARDID.value())
								|| pscDTO.getDebitAccount().equals(OwnerTag.OWNERTAG_LOANLD.value())){
							pInstance.setDebitAccount(cardId);
						}else{
							pInstance.setDebitAccount(null);
						}
						break;
					case PARTNER:
						if(pscDTO.getDebitAccount().equals(OwnerTag.OWNERTAG_ACCOUNT.value())
								|| pscDTO.getDebitAccount().equals(OwnerTag.OWNERTAG_CARDID.value())
								|| pscDTO.getDebitAccount().equals(OwnerTag.OWNERTAG_LOANLD.value())){
							pInstance.setDebitAccount(getAccountFromPartner(pscDTO.getPartnerId()));
						}else{
							pInstance.setDebitAccount(null);
						}
						
						break;
					case INTERNAL:
						pInstance.setDebitAccount(pscDTO.getDebitAccount());
						break;
					case UNUSED:
						pInstance.setDebitAccount("");
						break;
					default:
						throw new Exception();
				}
				
				enums = PostingConfigurationDebitOwner.fromString(pscDTO.getCreditOwner());
				switch (enums) {
					case CUSTOMER:
						if(pscDTO.getCreditAccount().equals(OwnerTag.OWNERTAG_ACCOUNT.value())
								|| pscDTO.getCreditAccount().equals(OwnerTag.OWNERTAG_CARDID.value())
								|| pscDTO.getCreditAccount().equals(OwnerTag.OWNERTAG_LOANLD.value())){
							pInstance.setCreditAccount(cardId);
						}else{
							pInstance.setCreditAccount(null);
						}
						
						break;
					case PARTNER:
						if(pscDTO.getCreditAccount().equals(OwnerTag.OWNERTAG_ACCOUNT.value())
								|| pscDTO.getCreditAccount().equals(OwnerTag.OWNERTAG_CARDID.value())
								|| pscDTO.getCreditAccount().equals(OwnerTag.OWNERTAG_LOANLD.value())){
							pInstance.setCreditAccount(getAccountFromPartner(pscDTO.getPartnerId()));
						}else{
							pInstance.setCreditAccount(null);
						}
						break;
					case INTERNAL:
						pInstance.setCreditAccount(pscDTO.getCreditAccount());
						break;
					case UNUSED:
						pInstance.setCreditAccount("");
						break;
					default:
						throw new Exception();
				}
				
				if(pInstance.getAmount() == null) {					
					if(pscDTO.getAmountTag() == null)
						throw new Exception();
						pInstance.setAmount(buidAmount(feeCollectATM,PaymentAmountTag.fromString(pscDTO.getAmountTag()), pscDTO,amount));
				}
				
				pInstance.setPostingConfigurationId(pscDTO.getId());
				
				pInstance.setPartnerId(partner.getId().intValue());
				
				pInstance.setStatus(MsgStatus.NEW.value());
				
				if(custId!=null)
					pInstance.setCustId(custId);				
				unitOfWorkPayment.postingInstanceRepo().upsert(pInstance);
				lsPInstance.add(modelMapper.map(pInstance, PostingInstanceDTO.class));
			}
			setpInstances(lsPInstance);
			return postingConfigurationDTOs;
		}
		
		return null;
	}
	
	public String getAccountFromPartner(long partnerId ){
		 return CacheManager.Partner().getPartnerByPartnerId(partnerId).getPartnerAcctInCore();		
	}
	
	public String buidMessageLogBody(String customerId,BigDecimal feeCollectATM, PostingConfigurationDTO pscDTO, String partnerRefId, String cardId, BigDecimal _amount , String custName , PostingInstanceDTO pInstanceDTO) throws Exception {
		String msgbody = "";
		try {
		switch (MsgChannel.fromString(pscDTO.getPostingSystem())) {
		
			case WAY4_APPLICATION:
				PaymentCreditCardDTO paymentCreditCardDTO = new PaymentCreditCardDTO();
				if(pscDTO.getAmountTag().equals(PaymentAmountTag.TRANS_AMT_MINUS_FEE.value()))
					paymentCreditCardDTO.setFeeAmount(FeeAmount.FREE_FEE_AMOUNT.stringValue());	
					/*TODO chua co truong hop khac */
				paymentCreditCardDTO.setCreditCardNo(pInstanceDTO.getDebitAccount());
				//paymentCreditCardDTO.setAmount(String.valueOf(buidAmount(feeCollectATM,PaymentAmountTag.fromString(pscDTO.getAmountTag()), pscDTO,_amount)));
				paymentCreditCardDTO.setAmount(pInstanceDTO.getAmount().toString());
				paymentCreditCardDTO.setRefId(partnerRefId);
				msgbody = paymentCreditCardDTO.toJson();
				CreditApplicationOutstanding creditApplicationOutstanding = unitOfWorkCredit.creditApplicationOutstandingRepo().findOutstandingByCard(cardId);
				creditApplicationOutstanding.setPaymentAmount(creditApplicationOutstanding.getPaymentAmount().add(pInstanceDTO.getAmount()));
				unitOfWorkCredit.creditApplicationOutstandingRepo().upsert(creditApplicationOutstanding);
				break;
				
			case T24_APPLICATION:
				MakePaymentCreditCard4DTO makePaymentCreditCard4DTO = new MakePaymentCreditCard4DTO() ;
				
				makePaymentCreditCard4DTO.setTransactionType(CodeTableCacheManager.getInstance().getCodeById(pscDTO.getTransactionTypeId()).getCodeValue2());
				
				DebitAccountDTO debitAccount = new DebitAccountDTO();
				debitAccount.setAccountNumber(pInstanceDTO.getDebitAccount() != null ? pInstanceDTO.getDebitAccount() : "");
				CreditAccountDTO creditAccount = new CreditAccountDTO();
				creditAccount.setAccountNumber(pInstanceDTO.getCreditAccount() != null ? pInstanceDTO.getCreditAccount() : "");
				Amount amount = new Amount();
				amount.setAmount(pInstanceDTO.getAmount() != null ? pInstanceDTO.getAmount().toString() : "");
				List<OtherDTO>  otherDTOs = new ArrayList<OtherDTO>();
					OtherDTO other1 = new OtherDTO();
					other1.setName(T24OtherTag.OTHER_CustomerID.value());
					other1.setValue(customerId != null ? customerId : "");					
					otherDTOs.add(other1);
					
					OtherDTO other2 = new OtherDTO();
					other2.setName(T24OtherTag.OTHER_CARDID.value());
					other2.setValue(cardId);					
					otherDTOs.add(other2);
					
					OtherDTO other3 = new OtherDTO();				
					other3.setName(T24OtherTag.OTHER_RECORDID.value());
					other3.setValue(partnerRefId != null ? partnerRefId : "");					
					otherDTOs.add(other3);
					
				makePaymentCreditCard4DTO.setDebitAccount(debitAccount);
				makePaymentCreditCard4DTO.setCreditAccount(creditAccount);
				makePaymentCreditCard4DTO.setAmount(amount);
				makePaymentCreditCard4DTO.setOthers(otherDTOs);
				msgbody = JSONConverter.toJSON(makePaymentCreditCard4DTO);
				break;
				
			default:
				throw new Exception();
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msgbody;
		
	}
	
	public String buidMessageLogBodyForSMS(String partnerRefId, String amount, String cardId) throws Exception {
		NumberFormat f = NumberFormat.getInstance(Locale.ENGLISH);
		if (f instanceof DecimalFormat) {
		    ((DecimalFormat) f).applyPattern(AmountFormat.PATTERN_AMOUNTFORMAT_2.stringValue());
		}		
		String msgbody = "";
		PaymentSMSDTO  pSmsdto = new PaymentSMSDTO();
		PaymentSendSMSBody paymentSendSMSBody = new PaymentSendSMSBody();
		paymentSendSMSBody.setReceiverID(partnerRefId);
		paymentSendSMSBody.setSenderSystem(MsgChannel.MC_PORTAL_APPLICATION.value());
		paymentSendSMSBody.setSmsType(SMSType.SMSTYPE_NOTIFICATION.value());
		paymentSendSMSBody.setSendImmidiately(true);
		//MCREDIT thong bao da nhan duoc khoan thanh toan sao ke cua The {0}, So tien thanh toan {1}, Ngay thanh toan {2}.Chi tiet xin LH: 1900636769
		paymentSendSMSBody.setContent(Messages.getString("sms.payment.content",cardId,f.format(new BigDecimal(amount)) +  MsgConcurency.VND.value(),DateUtil.toString(new Date(),"dd/MM/yyyy")));		
		pSmsdto.setPaymentSendSMSBody(paymentSendSMSBody);
		msgbody = JSONConverter.toJSON(pSmsdto);
		return msgbody;
	}
	
	public BigDecimal buidAmount(BigDecimal feeCollectATM , PaymentAmountTag amountTag, PostingConfigurationDTO pscDTO,BigDecimal amount) throws Exception {
		System.out.println(amount);
		
		if(pscDTO.getAmount()== null){
			switch (amountTag) {		
			case CRED_AMT:
				//TODO Tam thoi chua co truong hop nay
				break;
			case CRED_AMT_MINUS_INSU:
				//TODO Tam thoi chua co truong hop nay
				break;
			case FEE_COLLECT_AMT:
				if(feeCollectATM == null){
					return CacheManager.Parameters().findParamValueAsBigDecimal(ParametersName.FEE_COLLECT_AMT);
				}else {
					return pscDTO.getAmount();
				}
					
			case TRANS_AMT:
				if(pscDTO.getAmount() == null){
					return amount;
				}else {
					return pscDTO.getAmount();
				}
											
			case TRANS_AMT_MINUS_FEE:
				if(feeCollectATM == null){
					return amount.subtract(CacheManager.Parameters().findParamValueAsBigDecimal(ParametersName.FEE_COLLECT_AMT));
				}else {
					return amount.subtract(feeCollectATM);
				}					
			default:
				throw new Exception();
			}
		}else{
			return pscDTO.getAmount();
		}
		
		
		
		return null;
	}
	
	public void upDateCreditApplicationOutstanding(String as ){
		
	}
	/**************** End Behavior 
	 * @throws Exception ***************/
}
