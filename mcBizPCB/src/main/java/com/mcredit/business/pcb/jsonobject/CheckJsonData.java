package com.mcredit.business.pcb.jsonobject;

import com.mcredit.util.JSONConverter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CheckJsonData {

	public static void main(String[] args) throws Exception {

		String str = "";

		PcbInfo ex = JSONConverter.toObject(str, PcbInfo.class);
		System.out.println("----------->" + "ex.getCusName()" + ex.getCusName());
		System.out.println("----------->" + "ex.getPcbCode()" + ex.getPcbCode());
		System.out.println("----------->" + "ex.getIdentityCode()" + ex.getIdentityCode());
		System.out.println("----------->" + "ex.getBirthDate()" + ex.getBirthDate());
		System.out.println("----------->" + "ex.getMainAddress()" + ex.getMainAddress());
		System.out.println("----------->" + "ex.getAdditionalAddress()" + ex.getAdditionalAddress());
		System.out.println("----------->" + "ex.getDocumentList()" + ex.getDocumentList());
		System.out.println("----------->" + "ex.getReferenceNumber()" + ex.getReferenceNumber());
		System.out.println("----------->" + "ex.getCountBank()" + ex.getCountBank());
		System.out.println("----------->" + "ex.getLoanMainTotal()" + ex.getLoanMainTotal());
		System.out.println("----------->" + "ex.getLoanGuaranteeTotal()" + ex.getLoanGuaranteeTotal());
		System.out.println("----------->" + "ex.getFinalcialCompanyAmount()" + ex.getFinalcialCompanyAmount());
		System.out.println("----------->" + "ex.getFinalComLoanTotal()" + ex.getFinalComLoanTotal());
		System.out.println("----------->" + "ex.getCreditComRequestAmount()" + ex.getCreditComRequestAmount());
		System.out.println("----------->" + "ex.getCreditComRejectAmount()" + ex.getCreditComRejectAmount());
		System.out.println("----------->" + "ex.getHighest12MonthLoan()" + ex.getHighest12MonthLoan());
		System.out.println("----------->" + "ex.getHighest3YearLoan()" + ex.getHighest3YearLoan());
		System.out.println("----------->" + "ex.getOutOfDateMax()" + ex.getOutOfDateMax());
		System.out.println("----------->" + "ex.getCreditInstitutionsAmount()" + ex.getCreditInstitutionsAmount());
		System.out.println("----------->" + "ex.getTypeCurrency()" + ex.getTypeCurrency());
		System.out.println("----------->" + "ex.getMaxWorstStatus()" + ex.getMaxWorstStatus());
		System.out.println("----------->" + "ex.getScoreRangeCode()" + ex.getScoreRangeCode());
		System.out.println("----------->" + "ex.getScoreScoreRaw()" + ex.getScoreScoreRaw());
		System.out.println("----------->" + "ex.getScoreDescription()" + ex.getScoreDescription());
		System.out.println("----------->" + "ex.getInstalmentsNumberOfRequested()" + ex.getInstalmentsNumberOfRequested());
		System.out.println("----------->" + "ex.getInstalmentsNumberOfRefused()" + ex.getInstalmentsNumberOfRefused());
		System.out.println("----------->" + "ex.getInstalmentsNumberOfRenounced()" + ex.getInstalmentsNumberOfRenounced());
		System.out.println("----------->" + "ex.getInstalmentsNumberOfLiving()" + ex.getInstalmentsNumberOfLiving());
		System.out.println("----------->" + "ex.getInstalmentsNumberOfTerminated()" + ex.getInstalmentsNumberOfTerminated());
		System.out.println("----------->" + "ex.getCardsNumberOfRequested()" + ex.getCardsNumberOfRequested());
		System.out.println("----------->" + "ex.getCardsNumberOfRefused()" + ex.getCardsNumberOfRefused());
		System.out.println("----------->" + "ex.getCardsNumberOfRenounced()" + ex.getCardsNumberOfRenounced());
		System.out.println("----------->" + "ex.getCardsNumberOfLiving()" + ex.getCardsNumberOfLiving());
		System.out.println("----------->" + "ex.getCardsNumberOfTerminated()" + ex.getCardsNumberOfTerminated());
		System.out.println("----------->" + "ex.getNonInstalmentsNumberOfRequested()" + ex.getNonInstalmentsNumberOfRequested());
		System.out.println("----------->" + "ex.getNonInstalmentsNumberOfRefused()" + ex.getNonInstalmentsNumberOfRefused());
		System.out.println("----------->" + "ex.getNonInstalmentsNumberOfRenounced()" + ex.getNonInstalmentsNumberOfRenounced());
		System.out.println("----------->" + "ex.getNonInstalmentsNumberOfLiving()" + ex.getNonInstalmentsNumberOfLiving());
		System.out.println("----------->" + "ex.getNonInstalmentsNumberOfTerminated()" + ex.getNonInstalmentsNumberOfTerminated());
		System.out.println("----------->" + "ex.getMainMonthlyInstalmentsAmount()" + ex.getMainMonthlyInstalmentsAmount());
		System.out.println("----------->" + "ex.getGuardMonthlyInstalmentsAmount()" + ex.getGuardMonthlyInstalmentsAmount());
		System.out.println("----------->" + "ex.getMainRemainingInstalmentsAmount()" + ex.getMainRemainingInstalmentsAmount());
		System.out.println("----------->" + "ex.getGuradRemainingInstalmentsAmount()" + ex.getGuradRemainingInstalmentsAmount());
		System.out.println("----------->" + "ex.getMainUnpaidDueInstalmentsAmount()" + ex.getMainUnpaidDueInstalmentsAmount());
		System.out.println("----------->" + "ex.getGuardUnpaidDueInstalmentsAmount()" + ex.getGuardUnpaidDueInstalmentsAmount());
		System.out.println("----------->" + "ex.getMainLimitOfCredit()" + ex.getMainLimitOfCredit());
		System.out.println("----------->" + "ex.getGuradLimitOfCredit()" + ex.getGuradLimitOfCredit());
		System.out.println("----------->" + "ex.getMainResidualAmount()" + ex.getMainResidualAmount());
		System.out.println("----------->" + "ex.getGuardResidualAmount()" + ex.getGuardResidualAmount());
		System.out.println("----------->" + "ex.getMainOverDueAmount()" + ex.getMainOverDueAmount());
		System.out.println("----------->" + "ex.getGuardOverDueAmount()" + ex.getGuardOverDueAmount());
		System.out.println("----------->" + "ex.getMainCreditLimit()" + ex.getMainCreditLimit());
		System.out.println("----------->" + "ex.getGuardCreditLimit()" + ex.getGuardCreditLimit());
		System.out.println("----------->" + "ex.getMainUtilization()" + ex.getMainUtilization());
		System.out.println("----------->" + "ex.getGuardUtilization()" + ex.getGuardUtilization());
		System.out.println("----------->" + "ex.getMainOverdraft()" + ex.getMainOverdraft());
		System.out.println("----------->" + "ex.getGuardOverdraft()" + ex.getGuardOverdraft());
		System.out.println("----------->" + "ex.getInstalmentsGrantedContractLiving()" + ex.getInstalmentsGrantedContractLiving());
		System.out.println("----------->" + "ex.getCardsGrantedContractLiving()" + ex.getCardsGrantedContractLiving());
		System.out.println("----------->" + "ex.getNonInstalmentsGrantedContractLiving()" + ex.getNonInstalmentsGrantedContractLiving());
		System.out.println("----------->" + "ex.getInstalmentsGrantedContractEnd()" + ex.getInstalmentsGrantedContractEnd());
		System.out.println("----------->" + "ex.getCardsGrantedContractEnd()" + ex.getCardsGrantedContractEnd());
		System.out.println("----------->" + "ex.getNonInstalmentsGrantedContractEnd()" + ex.getNonInstalmentsGrantedContractEnd());
		System.out.println("----------->" + "ex.getNotGrantedContract()" + ex.getNotGrantedContract());

	}

}
