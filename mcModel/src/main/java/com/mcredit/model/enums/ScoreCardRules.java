package com.mcredit.model.enums;

public enum ScoreCardRules {
	
	CL_RATING_DRL("CL_RATING.drl"),
	CL_SCORECARD_DRL("CL_SCORECARD.drl"),
	IL_RATING_DRL("IL_RATING.drl"),
	IL_SCORECARD_DRL("IL_SCORECARD.drl"),
	IL_RATING_DRL_NEW("IL_RATING_NEW.drl"),
	IL_SCORECARD_DRL_NEW("IL_SCORECARD_NEW.drl");
	
	private final String value;

	ScoreCardRules(String value) {
		this.value = value;
	}

	public String value() {
		return this.value;
	}
}
