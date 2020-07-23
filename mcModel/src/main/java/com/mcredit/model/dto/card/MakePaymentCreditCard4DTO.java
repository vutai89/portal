
package com.mcredit.model.dto.card;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MakePaymentCreditCard4DTO {

	@SerializedName("DebitAccount")
	@Expose
	private DebitAccountDTO debitAccount;
	@SerializedName("CreditAccount")
	@Expose
	private CreditAccountDTO creditAccount;
	@SerializedName("CreditBank")
	@Expose
	private CreditBankDTO creditBank;
	@SerializedName("Amount")
	@Expose
	private Amount amount;
	@SerializedName("TransactionType")
	@Expose
	private String transactionType;
	@SerializedName("Others")
	@Expose
	private List<OtherDTO> others;

	public DebitAccountDTO getDebitAccount() {
		return debitAccount;
	}

	public void setDebitAccount(DebitAccountDTO debitAccount) {
		this.debitAccount = debitAccount;
	}

	public CreditAccountDTO getCreditAccount() {
		return creditAccount;
	}

	public void setCreditAccount(CreditAccountDTO creditAccount) {
		this.creditAccount = creditAccount;
	}

	public CreditBankDTO getCreditBank() {
		return creditBank;
	}

	public void setCreditBank(CreditBankDTO creditBank) {
		this.creditBank = creditBank;
	}

	public Amount getAmount() {
		return amount;
	}

	public void setAmount(Amount amount) {
		this.amount = amount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public List<OtherDTO> getOthers() {
		return others;
	}

	public void setOthers(List<OtherDTO> others) {
		this.others = others;
	}
 
	/*public static void main(String[] args) throws Exception {
		MakePaymentCreditCard4DTO mpCard4dto = new MakePaymentCreditCard4DTO();
		List<OtherDTO>  otherDTOs = new ArrayList<OtherDTO>();
		OtherDTO other1 = new OtherDTO();
		other1.setName(T24OtherTag.OTHER_CustomerID.value());
		other1.setValue("23123");					
		otherDTOs.add(other1);
		
		OtherDTO other2 = new OtherDTO();
		other2.setName(T24OtherTag.OTHER_CARDID.value());
		other2.setValue("233123");					
		otherDTOs.add(other2);
		
		OtherDTO other3 = new OtherDTO();				
		other3.setName(T24OtherTag.OTHER_RECORDID.value());
		other3.setValue("23123");					
		otherDTOs.add(other3);
		
		mpCard4dto.setOthers(otherDTOs);
		System.out.println(JSONConverter.toJSON(mpCard4dto));
	}*/
	
}
