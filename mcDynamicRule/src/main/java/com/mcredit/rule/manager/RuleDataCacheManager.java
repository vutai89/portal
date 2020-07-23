package com.mcredit.rule.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcredit.data.UnitOfWork;
import com.mcredit.data.factory.UnitOfWorkFactory;
import com.mcredit.data.rule.entity.RuleDetail;
import com.mcredit.data.rule.entity.Rules;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.RuleParameterType;
import com.mcredit.model.enums.SessionType;
import com.mcredit.model.object.RuleCacheData;
import com.mcredit.model.object.RuleCacheInitObject;
import com.mcredit.model.object.RuleCacheMap;
import com.mcredit.model.object.RuleCacheObject;
import com.mcredit.model.object.RuleObject;
import com.mcredit.model.object.RuleParameterDetail;
import com.mcredit.model.object.RuleResult;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.RuleUtils;
import com.mcredit.util.StringUtils;

public class RuleDataCacheManager {
	private UnitOfWork uok = null;
	private List<Rules> rules = null;
	private List<RuleDetail> ruleDetails = null;
	private List<Rules> childrenRules = null;
	private List<RuleParameterDetail> ruleParams = null;
	private List<RuleCacheInitObject> ruleCacheParams = null;
	private Map<Integer, List<Rules>> childrenRuleList = null;
	private Map<Integer, List<RuleParameterDetail>> ruleParamList = null;
	private Map<Integer, List<RuleCacheInitObject>> ruleCacheParamList = null;
	private Map<String, RuleCacheMap> ruleOutputCache = null;

	private static RuleDataCacheManager instance;

	public static synchronized RuleDataCacheManager getInstance() {
		if (instance == null) {
			synchronized (RuleDataCacheManager.class) {
				if (null == instance) {
					instance = new RuleDataCacheManager();
				}
			}
		}
		return instance;
	}

	public Rules findRule(String ruleCode) {
		if(ruleCode == null) {
			return null;
		}
		initCache();
		for(Rules r : rules) {
			if(ruleCode.equals(r.getRuleCode())) {
				System.out.println("findRule: Rule code found in cache="+ruleCode);
				return r;
			}
		}
		return null;
	}
	
	public Rules findRule(Integer ruleId) {
		if(ruleId == null) {
			return null;
		}
		initCache();
		for(Rules r : rules) {
			if(ruleId.equals(r.getId())) {
				System.out.println("findRule: Rule id found in cache="+r.getRuleCode()+"; id="+ruleId.intValue());
				return r;
			}
		}
		return null;
	}
	
	public List<Rules> findChildrenRule(Integer ruleId) {
		if(ruleId == null) {
			return null;
		}
		initCache();
		return childrenRuleList.get(ruleId);
	}

	public RuleDetail findRuleDetail(Integer ruleId) {
		if(ruleId == null) {
			return null;
		}
		initCache();
		for(RuleDetail rd : ruleDetails) {
			if(ruleId.equals(rd.getRuleId())) {
				System.out.println("findRuleDetail: Rule detail id found in cache="+rd.getRuleId().intValue());
				return rd;
			}
		}
		return null;
	}
	
	public RuleDetail findRuleDetail(String ruleCode) {
		if(ruleCode == null) {
			return null;
		}
		initCache();
		Rules r = findRule(ruleCode);
		if(r != null) {
			for(RuleDetail rd : ruleDetails) {
				if(r.getId().equals(rd.getRuleId())) {
					System.out.println("findRuleDetail: Rule detail id found in cache="+rd.getRuleId().intValue());
					return rd;
				}
			}
		}
		return null;
	}

	public List<RuleParameterDetail> findRuleParameter(Integer ruleId) {
		if(ruleId == null) {
			return null;
		}
		initCache();
		System.out.println("findRuleParameter: Rule parameter found in cache="+ruleId);
		return ruleParamList.get(ruleId);
	}

	public List<RuleCacheInitObject> findRuleCacheInitParameter(Integer ruleId) {
		if(ruleId == null) {
			return null;
		}
		initCache();
		System.out.println("findRuleCacheInitParameter: Rule cache init parameter found in cache="+ruleId);
		return ruleCacheParamList.get(ruleId);
	}
	
	public RuleCacheMap getRuleCache(String ruleKey) {
		return this.ruleOutputCache.get(ruleKey);
	}

	public Map<String, RuleCacheMap> getRuleCacheMap() {
		return this.ruleOutputCache;
	}

	public synchronized void initCache() {
		// TODO Auto-generated method stub
		if(rules != null) {
			return;
		}
		System.out.println("Rule initCache starting ...");
		try {
			uok = UnitOfWorkFactory.getInstance(SessionType.JTA);
			uok.start();
			rules = uok.rule.ruleDetailRepo().getAllRules();
			ruleDetails = uok.rule.ruleDetailRepo().getAllRuleDetails();
			childrenRules = uok.rule.ruleDetailRepo().getAllChildrenRules();
			ruleParams = uok.rule.ruleDetailRepo().getAllRulesParameterList();
			ruleCacheParams = uok.rule.ruleDetailRepo().getRuleCacheParameterList();
			// Create children rules list
			childrenRuleList = new HashMap<Integer, List<Rules>>();
			Integer pr = new Integer(-1);
			List<Rules> childList = null;
			for(Rules cr : childrenRules) {
				if(!pr.equals(cr.getParentRule())) {
					if(pr.intValue() != -1) {
						childrenRuleList.put(pr, childList);
					}
					childList = new ArrayList<Rules>();
					pr = new Integer(cr.getParentRule().intValue());
				}
				childList.add(cr);
			}
			childrenRuleList.put(pr, childList);
			// Create parameter rules list
			ruleParamList = new HashMap<Integer, List<RuleParameterDetail>>();
			Integer rpd = new Integer(-1);
			List<RuleParameterDetail> paramList = null;
			for(RuleParameterDetail rp : ruleParams) {
				if(!rpd.equals(rp.getRuleId())) {
					if(rpd.intValue() != -1) {
						ruleParamList.put(rpd, paramList);
					}
					paramList = new ArrayList<RuleParameterDetail>();
					rpd = new Integer(rp.getRuleId().intValue());
				}
				paramList.add(rp);
			}
			ruleParamList.put(rpd, paramList);
			// Create cache parameter rules list
			ruleCacheParamList = new HashMap<Integer, List<RuleCacheInitObject>>();
			Integer rcio = new Integer(-1);
			List<RuleCacheInitObject> pcList = null;
			String[] paramNames = null;
			for(RuleCacheInitObject rc : ruleCacheParams) {
				if(!rcio.equals(rc.getRuleId())) {
					if(rcio.intValue() != -1) {
						ruleCacheParamList.put(rcio, pcList);
					}
					pcList = new ArrayList<RuleCacheInitObject>();
					rcio = new Integer(rc.getRuleId().intValue());
				}
				rc.setVarName(RuleUtils.convert2VariableName(rc.getBindVar(), rc.getParamType()));
				rc.setMethodGetName(RuleUtils.convert2GetMethodName(rc.getBindVar(), rc.getParamType()));
				if(!RuleParameterType.CONSTANT.value().equals(rc.getParamType())) {
					rc.setMethodName("set" + rc.getVarName().substring(0, 1).toUpperCase() + rc.getVarName().substring(1));
				}
				pcList.add(rc);
			}
			ruleCacheParamList.put(rcio, pcList);
			// Init rule data cache
			initRuleDataCache();
			uok.commit();
			System.out.println("Rule initCache ended");
		} catch (Throwable e) {
			uok.rollback();
			e.printStackTrace();
			throw e;
		} finally {
			uok.close();
		}

	}
	
	private void initRuleDataCache() {
		List<RuleCacheData> ruleCacheData = new ArrayList<RuleCacheData>();
		List<RuleCacheObject> ruleCache = uok.rule.ruleDetailRepo().getRuleCacheList();
		Integer rId = new Integer(-1);
		String sqlCache = null;
		boolean noCache = false;
		for(RuleCacheObject rco : ruleCache) {
			if(!rId.equals(rco.getRuleId())) {
				RuleDetail rd = findRuleDetail(rco.getRuleId());
				sqlCache = rd.getRuleSQL2InitCache();
				if(StringUtils.isNullOrEmpty(sqlCache)) {
					noCache = true;
				} else {
					noCache = false;
				}
				rId = new Integer(rco.getRuleId().intValue());
			}
			// Execute SQL
			if(!noCache) {
				List<RuleCacheData> rcdList = uok.rule.ruleDetailRepo()
						.findRuleCacheOutputList(sqlCache, rco, ruleCacheParamList.get(rco.getRuleId()));
				ruleCacheData.addAll(rcdList);
			}
		}
		// Get rule result
		RuleAggregate ruleAgg = RuleFactory.getInstance(this.uok);
		ruleOutputCache = new HashMap<String, RuleCacheMap>();
		for(RuleCacheData rcd : ruleCacheData) {
			System.out.println("Execute rule to add cache list. Rule code="+rcd.getRuleCode()+"; combination key="+rcd.getRuleCombinationKey());
			RuleObject ro = JSONConverter.toObject(rcd.getRuleMessage(), RuleObject.class);
			ruleAgg.resetRuleParameterDetailList();
			RuleResult rr = ruleAgg.executeRuleOnDB(rcd.getRuleCode(), ro);
			if(rr != null) {
				RuleCacheMap rcm = new RuleCacheMap();
				rcm.setRuleKey(rcd.getRuleKey());
				rcm.setDateParams(rcd.getDateParams());
				rcm.setRuleOutput(rr);
				ruleOutputCache.put(rcd.getRuleKey(), rcm);
			}
		}
	}

	public void refresh() {
		// TODO Auto-generated method stub
		rules = null;
		initCache();

	}

}
