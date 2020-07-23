package com.mcredit.rule;

import java.io.Closeable;
import java.io.IOException;

import org.kie.api.runtime.KieSession;

import com.mcredit.model.enums.ScoreCardRules;
import com.mcredit.model.object.Applicant;
import com.mcredit.model.object.ScoreCardResult;

public class RuleScoreCardManager implements Closeable{

	public ScoreCardResult getScoreCardResult(Applicant a) {
		KieSession kieSession = RuleCacheManager.getInstance().getSession(getDrlFileScoreCard(a));

		// tinh diem khach hang
		kieSession.insert(a);
		kieSession.fireAllRules();
		kieSession.dispose();


		kieSession = RuleCacheManager.getInstance().getSession(getDrlFileRating(a));

		// tinh hang va cutOff
		kieSession.insert(a);
		kieSession.fireAllRules();
		kieSession.dispose();


		ScoreCardResult scoreCardResult = new ScoreCardResult();

		scoreCardResult.setTotalScore(a.getTotalScore());
		scoreCardResult.setCutOff(a.getCutOff());
		scoreCardResult.setRating(a.getRating());
		scoreCardResult.setProposalForCall(a.getProposalForCall());
		scoreCardResult.setProposalForDC(a.getProposalForDataChecker());
		scoreCardResult.setProposalForApprove(a.getProposalForApprove());
		scoreCardResult.setMessageForApprove(a.getMessageForApprove());
//		if(a.getIsNewScore()!=null && a.getIsNewScore()== 1) {
//			String sProposalForDC = "";
//			// Kiem tra va tra ve them thong so : proposalForDC
//			/*Number($("#loanAmount").getValue()) < 7500000 && $('#policyRegulationAppraisal').getValue() == 'Pass' && $('#fraudAppraisal').getValue() == 'No' && result >= 451*/
//			if(a.getLoanAmount() < 7500000 && a.getPolicyRegulation().equals("Pass") && a.getFraud().equals("No") && a.getTotalScore() >=451)
//				sProposalForDC = "Move to Approve";
//			scoreCardResult.setProposalForDC(sProposalForDC);
//		}
		return scoreCardResult;
	}
	
	private String getDrlFileScoreCard(Applicant a) {
		if (a == null) {
			return null;
		}
		if (Applicant.CASH_LOAN.equals(a.getTypeOfLoan())) {
			return ScoreCardRules.CL_SCORECARD_DRL.value();
		} else {
			// ntmanh : Kiem tra neu file sau golive thi su dung file tinh diem ( theo dau vao isNewScore BPM truyen vao )
			return a.getIsNewScore()!=null && a.getIsNewScore()== 1 ? ScoreCardRules.IL_SCORECARD_DRL_NEW.value() : ScoreCardRules.IL_SCORECARD_DRL.value();
		}
	}

	private String getDrlFileRating(Applicant a) {
		if (a == null) {
			return null;
		}
		if (Applicant.CASH_LOAN.equals(a.getTypeOfLoan())) {
			return ScoreCardRules.CL_RATING_DRL.value();
		} else {
			return a.getIsNewScore()!=null && a.getIsNewScore()== 1 ? ScoreCardRules.IL_RATING_DRL_NEW.value() : ScoreCardRules.IL_RATING_DRL.value();
		}
	}

	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
}
