package com.mcredit.model.object;

import java.util.Set;

public class ScoreCardResult {
	/**
	 * 
	 */
	private double totalScore;
	private String cutOff;
	private String rating;
	/** goi y cho Call **/
	private String proposalForCall;
	/** goi y cho Approve **/
	private String proposalForApprove;
	
	private String proposalForDC;
//	private String messageForApprove;
	private Set<String> messageForApprove;

	public double getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(double totalScore) {
		this.totalScore = totalScore;
	}

	public String getCutOff() {
		return cutOff;
	}

	public void setCutOff(String cutOff) {
		this.cutOff = cutOff;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
	

	public String getProposalForCall() {
		return proposalForCall;
	}

	public void setProposalForCall(String proposalForCall) {
		this.proposalForCall = proposalForCall;
	}

	public String getProposalForApprove() {
		return proposalForApprove;
	}

	public void setProposalForApprove(String proposalForApprove) {
		this.proposalForApprove = proposalForApprove;
	}

	public String getProposalForDC() {
		return proposalForDC;
	}

	public void setProposalForDC(String proposalForDC) {
		this.proposalForDC = proposalForDC;
	}

//	public String getMessageForApprove() {
//		return messageForApprove;
//	}
//
//	public void setMessageForApprove(String messageForApprove) {
//		this.messageForApprove = messageForApprove;
//	}
	
	public Set<String> getMessageForApprove() {
		return messageForApprove;
	}

	public void setMessageForApprove(Set<String> messageForApprove) {
		this.messageForApprove = messageForApprove;
	}

	@Override
	public String toString() {
		return "ScoreCardResult [totalScore=" + totalScore + ", cutOff=" + cutOff + ", rating=" + rating
				+ ", proposalForCall=" + proposalForCall + ", proposalForApprove=" + proposalForApprove +", proposalForDC=" + proposalForDC + ", messageForApprove=" + messageForApprove + "]";
	}

}
