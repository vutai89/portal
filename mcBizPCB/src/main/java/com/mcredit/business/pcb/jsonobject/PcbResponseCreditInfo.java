package com.mcredit.business.pcb.jsonobject;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PcbResponseCreditInfo {

	public String creditInstitutionsAmount;
	public String typeCurrency;
	public String maxWorstStatus;
	public String scoreRangeCode;
	public String scoreScoreRaw;
	public String scoreDescription;
	public PcbResponseLoan pcbResponseLoan;

	public List<GrantedContract> instalmentsGrantedContractLiving;
	public List<GrantedContract> cardsGrantedContractLiving;
	public List<GrantedContract> nonInstalmentsGrantedContractLiving;

	public List<GrantedContract> instalmentsGrantedContractEnd;
	public List<GrantedContract> cardsGrantedContractEnd;
	public List<GrantedContract> nonInstalmentsGrantedContractEnd;

	public List<NotGrantedContract> notGrantedContract;

	public PcbResponseCreditInfo(String creditInstitutionsAmount, String typeCurrency, String maxWorstStatus, String scoreRangeCode, String scoreScoreRaw, String scoreDescription,
			PcbResponseLoan pcbResponseLoan, List<GrantedContract> instalmentsGrantedContractLiving, List<GrantedContract> cardsGrantedContractLiving,
			List<GrantedContract> nonInstalmentsGrantedContractLiving, List<GrantedContract> instalmentsGrantedContractEnd, List<GrantedContract> cardsGrantedContractEnd,
			List<GrantedContract> nonInstalmentsGrantedContractEnd, List<NotGrantedContract> notGrantedContract) {
		super();
		this.creditInstitutionsAmount = creditInstitutionsAmount;
		this.typeCurrency = typeCurrency;
		this.maxWorstStatus = maxWorstStatus;
		this.scoreRangeCode = scoreRangeCode;
		this.scoreScoreRaw = scoreScoreRaw;
		this.scoreDescription = scoreDescription;
		this.pcbResponseLoan = pcbResponseLoan;
		this.instalmentsGrantedContractLiving = instalmentsGrantedContractLiving;
		this.cardsGrantedContractLiving = cardsGrantedContractLiving;
		this.nonInstalmentsGrantedContractLiving = nonInstalmentsGrantedContractLiving;
		this.instalmentsGrantedContractEnd = instalmentsGrantedContractEnd;
		this.cardsGrantedContractEnd = cardsGrantedContractEnd;
		this.nonInstalmentsGrantedContractEnd = nonInstalmentsGrantedContractEnd;
		this.notGrantedContract = notGrantedContract;
	}
	
	
	
}
