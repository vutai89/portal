package com.mcredit.data.rule.repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.type.StandardBasicTypes;

import com.mcredit.common.DataUtils;
import com.mcredit.data.BaseRepository;
import com.mcredit.data.DataException;
import com.mcredit.data.repository.IUpdateRepository;
import com.mcredit.data.rule.entity.RuleDetail;
import com.mcredit.data.rule.entity.RuleOutputDetail;
import com.mcredit.data.rule.entity.Rules;
import com.mcredit.model.enums.Constant;
import com.mcredit.model.enums.RuleParameterDataType;
import com.mcredit.model.enums.RuleParameterListType;
import com.mcredit.model.enums.RuleParameterType;
import com.mcredit.model.object.RuleCacheData;
import com.mcredit.model.object.RuleCacheInitObject;
import com.mcredit.model.object.RuleCacheKeyInput;
import com.mcredit.model.object.RuleCacheObject;
import com.mcredit.model.object.RuleCacheParameter;
import com.mcredit.model.object.RuleDateType;
import com.mcredit.model.object.RuleParameterDetail;
import com.mcredit.util.RuleUtils;
import com.mcredit.util.StringUtils;

public class RuleDetailRepository extends BaseRepository implements IUpdateRepository<RuleDetail> {

	public RuleDetailRepository(Session session) {
		super(session);
	}

	public void update(RuleDetail item) throws DataException {
		this.session.update("RuleDetail", item);
	}

	public void upsert(RuleDetail item) throws DataException {
		if( item.getId() != null )
			updateEntity(item, "RuleDetail");
		else
			this.session.save("RuleDetail", item);
	}
	
	public void remove(RuleDetail item) throws DataException {
		this.session.delete("RuleDetail", item);
	}
	
	public RuleDetail findRuleDetail(String ruleCode) {
		List<RuleDetail> lst = this.session.createNamedQuery("findRuleDetail", RuleDetail.class)
				.setHibernateFlushMode(FlushMode.ALWAYS)
				.setParameter("ruleCode", ruleCode)
				.list();
		
		if(lst != null && lst.size() > 0) {
			return lst.get(0);
		} else {
			return null;
		}
	}

	public List<RuleDetail> getAllRuleDetails() {
		List<RuleDetail> lst = this.session.createNamedQuery("getAllRuleDetails", RuleDetail.class)
				.setHibernateFlushMode(FlushMode.ALWAYS)
				.list();

		return lst;
	}
	
	public Rules findRule(String ruleCode) {
		Rules rule = this.session.createNamedQuery("findRule", Rules.class)
				.setHibernateFlushMode(FlushMode.ALWAYS)
				.setParameter("ruleCode", ruleCode)
				.getSingleResult();

		return rule;
	}
	
	public List<Rules> getAllRules() {
		List<Rules> lst = this.session.createNamedQuery("getAllRules", Rules.class)
				.setHibernateFlushMode(FlushMode.ALWAYS)
				.list();

		return lst;
	}
	
	public List<Rules> findAllChildrenRuleById(Integer ruleId) {
		List<Rules> ruleList = this.session.createNamedQuery("findAllChildrenRuleById", Rules.class)
				.setHibernateFlushMode(FlushMode.ALWAYS)
				.setParameter("ruleId", ruleId)
				.list();

		return ruleList;
	}

	public List<Rules> findAllChildrenRuleByCode(String ruleCode) {
		List<Rules> ruleList = this.session.createNamedQuery("findAllChildrenRuleByCode", Rules.class)
				.setParameter("ruleCode", ruleCode)
				.list();

		return ruleList;
	}

	public List<Rules> getAllChildrenRules() {
		List<Rules> lst = this.session.createNamedQuery("getAllChildrenRules", Rules.class)
				.setHibernateFlushMode(FlushMode.ALWAYS)
				.list();

		return lst;
	}

	public List<RuleParameterDetail> findRuleParameterList(Integer ruleId) {
		List<Object[]> objList = this.session.getNamedNativeQuery("findRuleParameterList")
				.setFlushMode(FlushMode.ALWAYS)
				.setHibernateFlushMode(FlushMode.ALWAYS)
                .setParameter("ruleId", ruleId)
                .getResultList();
		List<RuleParameterDetail> retList = new ArrayList<RuleParameterDetail>();
		for(Object[] objects : objList) {
			RuleParameterDetail rpd = new RuleParameterDetail();
			rpd = (RuleParameterDetail) DataUtils.bindingData(rpd, objects);
			retList.add(rpd);
		}
		return retList;
	}
	
	public List<RuleParameterDetail> getAllRulesParameterList() {
		List<Object[]> objList = this.session.getNamedNativeQuery("getAllRulesParameterList")
				.setFlushMode(FlushMode.ALWAYS)
				.setHibernateFlushMode(FlushMode.ALWAYS)
                .getResultList();
		List<RuleParameterDetail> retList = new ArrayList<RuleParameterDetail>();
		for(Object[] objects : objList) {
			RuleParameterDetail rpd = new RuleParameterDetail();
			rpd = (RuleParameterDetail) DataUtils.bindingData(rpd, objects);
			retList.add(rpd);
		}
		return retList;
	}
	
	private void setSqlParameter(NativeQuery hnq, RuleParameterDetail rpdObj, String paramName) {
		if(RuleParameterDataType.INTEGER.value().equals(rpdObj.getParamDataType())) {
			hnq.setParameter(((paramName == null) ? rpdObj.getParamDetailName() : paramName), rpdObj.getIntValue(), StandardBasicTypes.INTEGER);
		} else if(RuleParameterDataType.LONG.value().equals(rpdObj.getParamDataType())) {
			hnq.setParameter(((paramName == null) ? rpdObj.getParamDetailName() : paramName), rpdObj.getLongValue(), StandardBasicTypes.LONG);
		} else if(RuleParameterDataType.DOUBLE.value().equals(rpdObj.getParamDataType())) {
			hnq.setParameter(((paramName == null) ? rpdObj.getParamDetailName() : paramName), rpdObj.getDoubleValue(), StandardBasicTypes.DOUBLE);
		} else if(RuleParameterDataType.FLOAT.value().equals(rpdObj.getParamDataType())) {
			hnq.setParameter(((paramName == null) ? rpdObj.getParamDetailName() : paramName), rpdObj.getFloatValue(), StandardBasicTypes.FLOAT);
		} else if(RuleParameterDataType.DECIMAL.value().equals(rpdObj.getParamDataType())) {
			hnq.setParameter(((paramName == null) ? rpdObj.getParamDetailName() : paramName), rpdObj.getDecimalValue(), StandardBasicTypes.BIG_DECIMAL);
		} else if(RuleParameterDataType.STRING.value().equals(rpdObj.getParamDataType()) ||
				RuleParameterDataType.BOOLEAN.value().equals(rpdObj.getParamDataType()) ||
				RuleParameterDataType.DATE.value().equals(rpdObj.getParamDataType()) ||
				RuleParameterDataType.TIME.value().equals(rpdObj.getParamDataType()) ||
				RuleParameterDataType.DATETIME.value().equals(rpdObj.getParamDataType())) {
			hnq.setParameter(((paramName == null) ? rpdObj.getParamDetailName() : paramName), rpdObj.toString(), StandardBasicTypes.STRING);
		} else if(RuleParameterDataType.LIST.value().equals(rpdObj.getParamDataType())) {
			hnq.setParameterList(((paramName == null) ? rpdObj.getParamDetailName() : paramName), (rpdObj.getListValue()==null||rpdObj.getListValue().isEmpty())?new ArrayList<String>(){{add("###");}}:rpdObj.getListValue());
			// Neu list rong add 1 phan tu ### => se khong bao gio thoa man dieu kien ### nay => khong anh huong ket qua cuoi
		}
	}
	
	public List<RuleOutputDetail> findRuleOutputList(String sqlQuery, List<RuleParameterDetail> paramList) {
		String strSql = sqlQuery;
		String[] name = null;
		String delim = ":";
		String strParam = "";
		int i = 1;
		for(RuleParameterDetail rpd : paramList) {
			if(RuleParameterListType.ARRAY_INPUT_TYPE.value().equals(rpd.getParamListType())) {
				name = rpd.getParamValue().split(delim);
				List<?> valList = rpd.getListValue();
				if(valList != null) {
					i = 1;
					for(Object obj : valList) {
						strParam = strParam + ((i == 1) ? "" : ":") + name[0].replaceAll(Constant.RULE_ARRAY_SEQ, Integer.toString(i)) + ((i < valList.size()) ? "," : "");
						i++;
					}
					strSql = strSql.replaceAll(name[0], strParam);
				} else {
					strParam = name[0].replaceAll(Constant.RULE_ARRAY_SEQ, "1");
					strSql = strSql.replaceAll(name[0], strParam);
				}
			}
		}
		NativeQuery nq = this.session.createNativeQuery(strSql, RuleOutputDetail.class);
		System.out.println("findRuleOutputList starting ...");
		for(RuleParameterDetail rpd : paramList) {
			if(RuleParameterType.CONSTANT.value().equals(rpd.getParamType())) {
				nq.setParameter(rpd.getParamDetailName(), rpd.toString());
			} else if(RuleParameterListType.ARRAY_INPUT_TYPE.value().equals(rpd.getParamListType())) {
				i = 1;
				String paramName = rpd.getParamValue().split(delim)[0];
				List<?> valList = rpd.getListValue();
				if(valList != null) {
					for(Object obj : valList) {
						if(RuleParameterDataType.INTEGER.value().equals(rpd.getParamDataType())) {
							rpd.setIntValue((Integer) obj);
							setSqlParameter(nq, rpd, paramName.replaceAll(Constant.RULE_ARRAY_SEQ, Integer.toString(i)));
						} else if(RuleParameterDataType.LONG.value().equals(rpd.getParamDataType())) {
							rpd.setLongValue((Long) obj);
							setSqlParameter(nq, rpd, paramName.replaceAll(Constant.RULE_ARRAY_SEQ, Integer.toString(i)));
						} else if(RuleParameterDataType.DOUBLE.value().equals(rpd.getParamDataType())) {
							rpd.setDoubleValue((Double) obj);
							setSqlParameter(nq, rpd, paramName.replaceAll(Constant.RULE_ARRAY_SEQ, Integer.toString(i)));
						} else if(RuleParameterDataType.FLOAT.value().equals(rpd.getParamDataType())) {
							rpd.setFloatValue((Float) obj);
							setSqlParameter(nq, rpd, paramName.replaceAll(Constant.RULE_ARRAY_SEQ, Integer.toString(i)));
						} else if(RuleParameterDataType.DECIMAL.value().equals(rpd.getParamDataType())) {
							rpd.setDecimalValue((BigDecimal) obj);
							setSqlParameter(nq, rpd, paramName.replaceAll(Constant.RULE_ARRAY_SEQ, Integer.toString(i)));
						} else if(RuleParameterDataType.STRING.value().equals(rpd.getParamDataType()) ||
								RuleParameterDataType.BOOLEAN.value().equals(rpd.getParamDataType()) ||
								RuleParameterDataType.DATE.value().equals(rpd.getParamDataType()) ||
								RuleParameterDataType.TIME.value().equals(rpd.getParamDataType()) ||
								RuleParameterDataType.DATETIME.value().equals(rpd.getParamDataType())) {
							rpd.setStringValue((String) obj);
							setSqlParameter(nq, rpd, paramName.replaceAll(Constant.RULE_ARRAY_SEQ, Integer.toString(i)));
						}
						i++;
					}
				} else {
					paramName = rpd.getParamValue().split(delim)[0].replaceAll(Constant.RULE_ARRAY_SEQ, "1");
					setSqlParameter(nq, rpd, paramName);
				}
			} else {
				setSqlParameter(nq, rpd, null);
				/*
				if(RuleParameterDataType.INTEGER.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getIntValue(), StandardBasicTypes.INTEGER);
				} else if(RuleParameterDataType.LONG.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getLongValue(), StandardBasicTypes.LONG);
				} else if(RuleParameterDataType.DOUBLE.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getDoubleValue(), StandardBasicTypes.DOUBLE);
				} else if(RuleParameterDataType.FLOAT.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getFloatValue(), StandardBasicTypes.FLOAT);
				} else if(RuleParameterDataType.DECIMAL.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getDecimalValue(), StandardBasicTypes.BIG_DECIMAL);
				} else if(RuleParameterDataType.STRING.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.BOOLEAN.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.DATE.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.TIME.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.DATETIME.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.toString(), StandardBasicTypes.STRING);
				}
				*/
			}
		}
		List<RuleOutputDetail> retList = nq
				.setFlushMode(FlushMode.ALWAYS)
				.setHibernateFlushMode(FlushMode.ALWAYS).list();
		return retList;
	}
	
	public List<Map<String, String>> getRuleResultList(String sqlQuery, String mapColumn, List<RuleParameterDetail> paramList) {
		//List<Object[]> objList = this.session.createNativeQuery(sqlQuery)
		String strValue = null;
		NativeQuery nq = this.session.createNativeQuery(sqlQuery);
		System.out.println("getRuleResultList starting ...");
		for(RuleParameterDetail rpd : paramList) {
			if(RuleParameterType.CONSTANT.value().equals(rpd.getParamType())) {
				nq.setParameter(rpd.getParamDetailName(), rpd.toString());
			} else {
				if(RuleParameterDataType.INTEGER.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getIntValue(), StandardBasicTypes.INTEGER);
				} else if(RuleParameterDataType.LONG.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getLongValue(), StandardBasicTypes.LONG);
				} else if(RuleParameterDataType.DOUBLE.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getDoubleValue(), StandardBasicTypes.DOUBLE);
				} else if(RuleParameterDataType.FLOAT.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getFloatValue(), StandardBasicTypes.FLOAT);
				} else if(RuleParameterDataType.DECIMAL.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getDecimalValue(), StandardBasicTypes.BIG_DECIMAL);
				} else if(RuleParameterDataType.STRING.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.BOOLEAN.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.DATE.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.TIME.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.DATETIME.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.toString(), StandardBasicTypes.STRING);
				}
			}
		}
		
		String[] columnList = mapColumn.split(",");
		int i = 0;
		String tmpValue = "";
		ArrayList<Map<String, String>> valueList = new ArrayList<Map<String, String>>();
		List<Object[]> retList = (List<Object[]>) nq
				.setFlushMode(FlushMode.ALWAYS)
				.setHibernateFlushMode(FlushMode.ALWAYS).getResultList();
		for(Object[] objs : retList) {
			Map<String, String> item = new HashMap<String, String>();
			i = 0;
			for(Object obj : objs) {
				if(StringUtils.isNullOrEmpty(columnList[i]) || (i == columnList.length)) {
					break;
				}
				tmpValue = RuleUtils.convert2String(obj);
				item.put(columnList[i], tmpValue);
				i++;
			}
			valueList.add(item);
		}
		return valueList;
	}
	
	public List<RuleCacheParameter> findRuleCacheParameterList(Integer ruleId) {
		List<Object[]> objList = this.session.getNamedNativeQuery("findRuleCacheParameterList")
                .setParameter("ruleId", ruleId)
                .getResultList();
		List<RuleCacheParameter> retList = new ArrayList<RuleCacheParameter>();
		for(Object[] objects : objList) {
			RuleCacheParameter rcp = new RuleCacheParameter();
			rcp = (RuleCacheParameter) DataUtils.bindingData(rcp, objects);
			retList.add(rcp);
		}
		return retList;
	}

	public List<RuleCacheInitObject> getRuleCacheParameterList() {
		List<Object[]> objList = this.session.getNamedNativeQuery("getRuleCacheParameterList")
                .getResultList();
		List<RuleCacheInitObject> retList = new ArrayList<RuleCacheInitObject>();
		for(Object[] objects : objList) {
			RuleCacheInitObject rco = new RuleCacheInitObject();
			rco = (RuleCacheInitObject) DataUtils.bindingData(rco, objects);
			retList.add(rco);
		}
		return retList;
	}

	public List<RuleCacheObject> getRuleCacheList() {
		List<Object[]> objList = this.session.getNamedNativeQuery("getRuleCacheList")
                .getResultList();
		List<RuleCacheObject> retList = new ArrayList<RuleCacheObject>();
		for(Object[] objects : objList) {
			RuleCacheObject rco = new RuleCacheObject();
			rco = (RuleCacheObject) DataUtils.bindingData(rco, objects);
			retList.add(rco);
		}
		return retList;
	}
	
	private RuleCacheInitObject findRuleCacheInitObject(String pName, List<RuleCacheInitObject> list) {
		if(StringUtils.isNullOrEmpty(pName)) {
			return null;
		}

		for(RuleCacheInitObject rco : list) {
			if(pName.equals(rco.getVarName())) {
				return rco;
			}
		}
		return null;
	}

	public List<RuleCacheKeyInput> getRuleInputKey(String sqlQuery, List<RuleParameterDetail> paramList, 
			List<RuleCacheInitObject> rcioList) {
		//List<Object[]> objList = this.session.createNativeQuery(sqlQuery)
		String strValue = null;
		NativeQuery nq = this.session.createNativeQuery(sqlQuery);
		System.out.println("getRuleInputKey starting ...");
		for(RuleParameterDetail rpd : paramList) {
			if(RuleParameterType.CONSTANT.value().equals(rpd.getParamType())) {
				nq.setParameter(rpd.getParamDetailName(), rpd.toString());
			} else {
				if(RuleParameterDataType.INTEGER.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getIntValue(), StandardBasicTypes.INTEGER);
				} else if(RuleParameterDataType.LONG.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getLongValue(), StandardBasicTypes.LONG);
				} else if(RuleParameterDataType.DOUBLE.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getDoubleValue(), StandardBasicTypes.DOUBLE);
				} else if(RuleParameterDataType.FLOAT.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getFloatValue(), StandardBasicTypes.FLOAT);
				} else if(RuleParameterDataType.DECIMAL.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.getDecimalValue(), StandardBasicTypes.BIG_DECIMAL);
				} else if(RuleParameterDataType.STRING.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.BOOLEAN.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.DATE.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.TIME.value().equals(rpd.getParamDataType()) ||
						RuleParameterDataType.DATETIME.value().equals(rpd.getParamDataType())) {
					nq.setParameter(rpd.getParamDetailName(), rpd.toString(), StandardBasicTypes.STRING);
				}
			}
		}

		List<Object[]> objList = (List<Object[]>) nq
				.setFlushMode(FlushMode.ALWAYS)
				.setHibernateFlushMode(FlushMode.ALWAYS).getResultList();

		List<RuleCacheKeyInput> retList = new ArrayList<RuleCacheKeyInput>();
		for(Object[] objects : objList) {
			int i = 3;
			List<RuleDateType> dtList = new ArrayList<RuleDateType>();
			Map<String, String> mapParam = new HashMap<String, String>();
			RuleCacheKeyInput rcki = new RuleCacheKeyInput();
			rcki = (RuleCacheKeyInput) DataUtils.bindingData(rcki, objects);
			for(RuleCacheInitObject robj : rcioList) {
				// Date type
				if(!RuleParameterType.CONSTANT.value().equals(robj.getParamType())
						&& !RuleParameterType.FORMULA.value().equals(robj.getParamType())
						&& RuleParameterDataType.DATE.value().equals(robj.getDataType())
						&& (RuleParameterListType.DATE_EFFECTIVE_TEST.value().equals(robj.getParamListType()))) {
					RuleDateType rdt = new RuleDateType();
					rdt.setParamName(robj.getParamName());
					rdt.setVarName(robj.getVarName());
					rdt.setStartDate(rcki.getStartDate());
					rdt.setEndDate(rcki.getEndDate());
					SimpleDateFormat sdf = new SimpleDateFormat(robj.getDataFormat());
					SimpleDateFormat cdf = new SimpleDateFormat(Constant.RULE_DEFAULT_DATE_FORMAT);
					try {
						rdt.setDtStartDate(sdf.parse(rdt.getStartDate()));
						rdt.setDtEndDate(sdf.parse(rdt.getEndDate()));
						if(rdt.getDtStartDate() != null) {
							rdt.setStartDate(cdf.format(rdt.getDtStartDate()));
						}
						if(rdt.getDtEndDate() != null) {
							rdt.setEndDate(cdf.format(rdt.getDtEndDate()));
						}
					} catch	(Exception ex) {
						System.out.println("set Date error="+ex.getMessage());
						rdt.setDtStartDate(null);
						rdt.setDtEndDate(null);
					}
					dtList.add(rdt);
				} else if(RuleParameterType.FORMULA.value().equals(robj.getParamType())) {
					mapParam.put(robj.getParamName(), RuleUtils.convert2String(objects[i]));
					i++;
				}
			}
			rcki.setDateList(dtList);
			rcki.setParamValue(mapParam);
			retList.add(rcki);
		}

		return retList;
	}
		
	public List<RuleCacheData> findRuleCacheOutputList(String sqlQuery, RuleCacheObject rco, 
			List<RuleCacheInitObject> rcioList) {
		String tmpValue = null;
		String paramType = null;
		String varName = null;
		boolean isInit = true;
		int i = 0;
		NativeQuery nq = this.session.createNativeQuery(sqlQuery);
		nq.setParameter("ruleId", rco.getRuleId(), StandardBasicTypes.INTEGER);
		nq.setParameter("combKey", rco.getRuleCombinationKey(), StandardBasicTypes.INTEGER);
		List<Object[]> retList = (List<Object[]>) nq
				.setFlushMode(FlushMode.ALWAYS)
				.setHibernateFlushMode(FlushMode.ALWAYS).getResultList();
		List<RuleCacheData> rcdList = new ArrayList<RuleCacheData>();
		for(RuleCacheInitObject robj : rcioList) {
			if(robj.getParamIndex() == -1) {
				isInit = false;
				break;
			}
		}

		for(Object[] objs : retList) {
			if(!isInit) {
				i = 0;
				for(Object obj : objs) {
					tmpValue = RuleUtils.convert2String(obj);
					if(i > 0) {
						if(((i-1)%4) == 0) {
							paramType = tmpValue;
						} else if(((i-2)%4) == 0) {
							varName = RuleUtils.convert2VariableName(tmpValue, paramType);
							RuleCacheInitObject rcio = findRuleCacheInitObject(varName, rcioList);
							rcio.setParamIndex(i);
						}
					}
					i++;
				}
				isInit = true;
			}
			// Build message and key
			List<RuleDateType> dtList = new ArrayList<RuleDateType>();
			String ruleKey = rco.getRuleCode();
			String msg = "{\r\n\"ruleCode\": \"" + rco.getRuleCode() + "\"";
			for(RuleCacheInitObject robj : rcioList) {
				int index = robj.getParamIndex();
				String pValue = RuleUtils.convert2String(objs[index+2]);
				String pType = (String) objs[index-1];
				// Date type
				if(!RuleParameterType.CONSTANT.value().equals(robj.getParamType())
						&& !RuleParameterType.FORMULA.value().equals(robj.getParamType())
						&& RuleParameterDataType.DATE.value().equals(robj.getDataType())
						&& (RuleParameterListType.DATE_EFFECTIVE_TEST.value().equals(robj.getParamListType()))) {
					RuleDateType rdt = new RuleDateType();
					rdt.setParamName(robj.getParamName());
					rdt.setVarName(robj.getVarName());
					rdt.setStartDate(RuleUtils.convert2String(objs[index+1]));
					rdt.setEndDate(RuleUtils.convert2String(objs[index+2]));
					SimpleDateFormat sdf = new SimpleDateFormat(robj.getDataFormat());
					SimpleDateFormat cdf = new SimpleDateFormat(Constant.RULE_DEFAULT_DATE_FORMAT);
					try {
						rdt.setDtStartDate(sdf.parse(rdt.getStartDate()));
						rdt.setDtEndDate(sdf.parse(rdt.getEndDate()));
						if(rdt.getDtStartDate() != null) {
							rdt.setStartDate(cdf.format(rdt.getDtStartDate()));
						}
						if(rdt.getDtEndDate() != null) {
							rdt.setEndDate(cdf.format(rdt.getDtEndDate()));
						}
					} catch	(Exception ex) {
						System.out.println("set Date error="+ex.getMessage());
						rdt.setDtStartDate(null);
						rdt.setDtEndDate(null);
					}
					dtList.add(rdt);
				} else {
					ruleKey = ruleKey + pValue;
				}
				msg = msg + ",\r\n" + "\""+ robj.getVarName() +"\": " + (Constant.RULE_DEFAULT_VALUE.equals(pValue) ? "null" : "\"" + pValue + "\"");
			}
			// Set data
			msg = msg + "\r\n}";
			RuleCacheData rcd = new RuleCacheData();
			rcd.setRuleCode(rco.getRuleCode());
			rcd.setRuleCombinationKey(rco.getRuleCombinationKey().intValue());
			rcd.setRuleKey(ruleKey);
			rcd.setRuleMessage(msg);
			rcd.setDateParams(dtList);
			rcdList.add(rcd);
		}
		return rcdList;
	}
	
}