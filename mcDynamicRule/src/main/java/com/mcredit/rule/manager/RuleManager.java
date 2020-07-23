package com.mcredit.rule.manager;

import java.util.HashMap;
import java.util.Map;

import com.mcredit.model.object.RuleObject;
import com.mcredit.model.object.RuleResult;
import com.mcredit.restcore.model.Result;
import com.mcredit.sharedbiz.manager.BaseManager;
import com.mcredit.util.JSONConverter;

public class RuleManager extends BaseManager {
	
	public RuleResult checkRule(String ruleCode, Object... objects) throws Exception {

		return this.tryCatch(()->{	
			RuleAggregate ruleAgg = RuleFactory.getInstance(this.uok);
			return ruleAgg.executeRule(ruleCode, objects);
		});
		
	}
	
	public Object checkRule(RuleObject input) throws Exception {

		return this.tryCatch(()->{
			RuleAggregate ruleAgg = RuleFactory.getInstance(this.uok);
			RuleResult  output = null ;
			  output= ruleAgg.executeRule(input.getRuleCode(), input);
			if(output == null){
				//throw new ValidationException(" kh\u00F4ng c\u00F3 ch\u01B0\u01A1ng tr\u00ECnh n\u00E0o h\u1EE3p l\u1EC7 ");
				Result rs = new Result();
				rs.setReturnCode("400");
				rs.setReturnMes(" kh\u00F4ng c\u00F3 Output h\u1EE3p l\u1EC7 ");	
				return rs ;
			}
			return output ;
		});
		
	}
	
	public Object checkRules(Map<String, RuleObject> input) throws Exception {

		return this.tryCatch(()->{
			Map<String, Object> result = new HashMap<>();
			RuleAggregate ruleAgg = RuleFactory.getInstance(this.uok);
			RuleResult  output = null ;
			System.out.println("checkRules: " + JSONConverter.toJSON(input));
			for (Map.Entry<String, RuleObject> ro : input.entrySet()) {
				output= ruleAgg.executeRule(ro.getValue().getRuleCode(), ro.getValue());
				if(output == null){
	//				throw new ValidationException(" kh\u00F4ng c\u00F3 ch\u01B0\u01A1ng tr\u00ECnh n\u00E0o h\u1EE3p l\u1EC7 ");
					Result rs = new Result();
					rs.setReturnCode("400");
					rs.setReturnMes(" kh\u00F4ng c\u00F3 Output h\u1EE3p l\u1EC7 ");	
					result.put(ro.getKey(), rs);
				} else {
					result.put(ro.getKey(), output);
				}
			}
			return result;
		});
		
	}
	
	
	public static void main(String[] args) {
		
		RuleManager rm = new RuleManager();
		
		 RuleObject ageValidation =  new RuleObject(); 
		 ageValidation.setRuleCode("AGE_VALIDATION");
		 ageValidation.setProductCode("M0000015");
		 ageValidation.setLoanTenor(12);
		 ageValidation.setBirthDate("1960-06-20");
		 ageValidation.setProductGroup("InstallmentLoan");
		 ageValidation.setCreatedDate("2019-05-22 10:01:15");
		 
		try {
			System.out.println( " Out " + JSONConverter.toJSON(rm.checkRule(ageValidation)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
