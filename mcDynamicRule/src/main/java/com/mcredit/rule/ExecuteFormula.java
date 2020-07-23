package com.mcredit.rule;

import org.kie.api.runtime.KieSession;

import com.mcredit.common.DataUtils;

public class ExecuteFormula {

	public static FormulaResult execFormula(String ruleConf, Object... objects) {
		RuleCacheManager rcm = RuleCacheManager.getInstance();
		FormulaResult fr = new FormulaResult();
		String[] name = ruleConf.split(":");
		if(name.length > 0) {
			KieSession ks = rcm.getSession(name[0]);
			//ks.insert(fr);
			ks.setGlobal("formulaResult", fr);
			if(name.length > 1) {
				for(int i=1;i<name.length;i++) {
					Object obj = DataUtils.findObject(name[i], objects);
					if(obj != null) {
						ks.insert(obj);
					}
				}
			}
			ks.fireAllRules();
			ks.dispose();
		}
		return fr;
	}
	
}
