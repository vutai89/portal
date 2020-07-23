package com.mcredit.rule.manager;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcredit.common.DataUtils;
import com.mcredit.data.DataException;
import com.mcredit.data.rule.UnitOfWorkRule;
import com.mcredit.data.rule.entity.RuleDetail;
import com.mcredit.data.rule.entity.RuleOutput;
import com.mcredit.data.rule.entity.RuleOutputDetail;
import com.mcredit.data.rule.entity.Rules;
import com.mcredit.model.enums.Constant;
import com.mcredit.model.enums.ParametersName;
import com.mcredit.model.enums.RuleCombinationType;
import com.mcredit.model.enums.RuleMasterType;
import com.mcredit.model.enums.RuleParameterDataType;
import com.mcredit.model.enums.RuleParameterListType;
import com.mcredit.model.enums.RuleParameterType;
import com.mcredit.model.object.DebugInfor;
import com.mcredit.model.object.RuleCacheInitObject;
import com.mcredit.model.object.RuleCacheKeyInput;
import com.mcredit.model.object.RuleCacheMap;
import com.mcredit.model.object.RuleDateType;
import com.mcredit.model.object.RuleObject;
import com.mcredit.model.object.RuleOutputList;
import com.mcredit.model.object.RuleParameterDetail;
import com.mcredit.model.object.RuleResult;
import com.mcredit.rule.ExecuteFormula;
import com.mcredit.rule.FormulaResult;
import com.mcredit.sharedbiz.cache.ParametersCacheManager;
import com.mcredit.util.JSONConverter;
import com.mcredit.util.StringUtils;

public class RuleAggregate {
	
	//private ModelMapper modelMapper = new ModelMapper();
	private UnitOfWorkRule _uok  = null ;
	private List<RuleParameterDetail> rpdList = null;
	private List<String> rKeyList = null;
	private List<RuleOutputDetail> rodList = null;

	public RuleAggregate() {
	}

	public RuleAggregate(UnitOfWorkRule uok) {
		this._uok = uok;
	}

	public void resetRuleParameterDetailList() {
		this.rpdList = null;
	}

	public RuleResult executeRule(String ruleCode, Object... objects) {
		RuleResult rr = null;
		this.rpdList = null;
		boolean hasCache = false;
		boolean hasUpdateCache = false;
		long timeStamp = ZonedDateTime.now().toInstant().toEpochMilli();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		DebugInfor di = new DebugInfor(timeStamp, sdf.format(Calendar.getInstance().getTime()));
		System.out.println("executeRule input data: "+ di.getCurrentDateTime() + " :"+di.getTimeStamp()+": " + JSONConverter.toJSON(objects[0]));
		/*
		RuleDetail rd = RuleDataCacheManager.getInstance().findRuleDetail(ruleCode);
		if(!StringUtils.isNullOrEmpty(rd.getRuleSQL2InitCache())) {
			hasCache = true;
		}
		if(!StringUtils.isNullOrEmpty(rd.getRuleSQL2GetCacheInputKey())) {
			hasUpdateCache = true;
		}
		// Get rule from cache
		if(hasCache) {
			rr = findRuleInCache(ruleCode, (RuleObject) objects[0]);
		}
		*/
		if(rr != null) {
			System.out.println("Rule code="+ruleCode+" was found in rule data cache.");
			System.out.println("executeRule ended: "+ sdf.format(Calendar.getInstance().getTime()) + " :"+di.getTimeStamp()+":");
			return rr;
		} else {
			System.out.println("Rule code="+ruleCode+" cannot be found in rule data cache. Try a full execution");
			Object[] objs = new Object[objects.length+1];
			int i = 0;
			for(Object obj : objects) {
				objs[i] = objects[i];
				i++;
			}
			objs[i] = di;
			rr = executeRuleOnDB(ruleCode, objs);
			/*
			if((rr != null) && hasUpdateCache) {
				try {
					addToRuleCache(ruleCode, (RuleObject) objects[0], rr);
				} catch (Throwable th) {
					System.out.println("addToRuleCache error. Rule code="+ruleCode);
					th.printStackTrace();
				}
			}
			*/
		}
		System.out.println("executeRule ended: "+ sdf.format(Calendar.getInstance().getTime()) + " :"+di.getTimeStamp()+":");
		return rr;
	}

	public RuleResult executeRuleOnDB(String ruleCode, Object... objects) {
		long timeStamp = ZonedDateTime.now().toInstant().toEpochMilli();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		DebugInfor di = null;
		Object obj = DataUtils.findObject("com.mcredit.model.object.DebugInfor", objects);
		if((obj != null) && (obj instanceof DebugInfor)) {
			di = (DebugInfor) obj;
			timeStamp = di.getTimeStamp();
		}
		System.out.println("executeRuleOnDB "+sdf.format(Calendar.getInstance().getTime()) + " :"+timeStamp+": " + JSONConverter.toJSON(objects));

		RuleResult rr = null;
		Rules masterRule = null;
		Rules rule = null;
		List<RuleOutput> roList = null;
		List<Map<String, String>> dropList = null;
		int listLength = 0;
		List list = null;
		rodList = null;
		try {
			this._uok.clearCache();
			masterRule = (Rules) RuleDataCacheManager.getInstance().findRule(ruleCode); // this._uok.ruleDetailRepo().findRule(ruleCode);
			if(masterRule != null) {
				// Rule type is NOT Parent
				if(RuleMasterType.PARENT.value().equals(masterRule.getRuleType())) {
					List<Rules> ruleList = RuleDataCacheManager.getInstance().findChildrenRule(masterRule.getId()); //this._uok.ruleDetailRepo().findAllChildrenRuleById(masterRule.getId());
					if(ruleList != null && ruleList.size() > 0) {
						list = new ArrayList<RuleOutputDetail>();
						for(Rules r : ruleList) {
							System.out.println("Rule id="+ r.getId() + "; rule code="+r.getRuleCode());
							List<RuleOutputDetail> lst = fireRule(r.getRuleCode(), rule, objects);
							if(lst != null && lst.size() > 0) {
								list.addAll(lst);
							}
						}
					}
				} else {
					list = fireRule(ruleCode, rule, objects);
				}
				if(list != null) {
					System.out.println(sdf.format(Calendar.getInstance().getTime()) + " :"+timeStamp+": Rule code=" + ruleCode + " - Output="+list.size());
					if(RuleMasterType.DROP_LIST.value().equals(masterRule.getRuleType())) {
						
					} else {
						rodList = list;
						for(RuleOutputDetail rod : rodList) {
							System.out.println("Raw output detail: id="+rod.getId()+"; value="+rod.getOutputValue());
						}
					}
				} else {
					System.out.println(sdf.format(Calendar.getInstance().getTime()) + " :"+timeStamp+": Rule code=" + ruleCode + " return no output");
				}
				
				List<RuleOutputList> outList = new ArrayList<RuleOutputList>();
				// Convert rule output to result
				if(list != null && list.size() > 0) {
					rr = new RuleResult();
					rr.setRuleType(masterRule.getRuleType());
					if(RuleMasterType.DROP_LIST.value().equals(masterRule.getRuleType())) {
						dropList = list;
						rr.setDropList(dropList);
					} else {
						rodList = list;
						if(RuleMasterType.SINGLE_VALUE.value().equals(masterRule.getRuleType())
								|| RuleMasterType.MULTI_VALUE.value().equals(masterRule.getRuleType())) {
							if(masterRule.getNumOfResult() == -1) {
								listLength = rodList.size();
							} else {
								listLength = masterRule.getNumOfResult();
							}
							if(listLength > 1) {
								rr.setRuleType(RuleMasterType.LIST_VALUE.value());
								for(int i=0;i<listLength;i++) {
									RuleOutputList outVal = new RuleOutputList();
									outVal.setOutputType(rodList.get(i).getOutputType());
									outVal.setOutputValue(rodList.get(i).getOutputValue());
									outList.add(outVal);
								}
								rr.setListValue(outList);
							} else if(listLength == 1) {
								if(RuleMasterType.SINGLE_VALUE.value().equals(masterRule.getRuleType())) {
									rr.setScalarValue(rodList.get(0).getOutputValue());
								} else if(RuleMasterType.MULTI_VALUE.value().equals(masterRule.getRuleType())) {
									if(StringUtils.isNullOrEmpty(rodList.get(0).getOutputType())) {
										rr.setMultiValue(rodList.get(0).getOutputValue());
									} else {
										String mValue = "[ \"" + rodList.get(0).getOutputType() + "\", \"" + rodList.get(0).getOutputValue() + "\" ]";
										rr.setMultiValue(mValue);
									}
								}
							}
						} else if(RuleMasterType.VALIDATION.value().equals(masterRule.getRuleType()) ||
								RuleMasterType.LIST_VALUE.value().equals(masterRule.getRuleType()) ||
								RuleMasterType.PARENT.value().equals(masterRule.getRuleType())) {
							int outputId = -1;
							int count = 0;
							if(masterRule.getNumOfResult() == -1) {
								listLength = rodList.size();
							} else {
								listLength = masterRule.getNumOfResult();
							}
							for(int i=0;i<rodList.size() && listLength>0;i++) {
								// Init var
								if(outputId == -1) {
									outputId = rodList.get(i).getRuleOutputId();
								}
								// Check change output
								if(outputId != rodList.get(i).getRuleOutputId()) {
									count++;
								}
								// Check length of list
								if(count == listLength) {
									break;
								}
								RuleOutputList outVal = new RuleOutputList();
								outVal.setOutputType(rodList.get(i).getOutputType());
								outVal.setOutputValue(rodList.get(i).getOutputValue());
								outList.add(outVal);
								outputId = rodList.get(i).getRuleOutputId();
							}
							rr.setListValue(outList);
						}
					}
				}
			}
		} catch (Throwable ex) {
			System.out.println("executeRuleOnDB got exception="+ex.getMessage());
			ex.printStackTrace();
			throw new DataException(ex.getMessage());
		}

		if(rr != null) {
			System.out.println("Final Output : type="+rr.getRuleType());
			System.out.println("Final Output : scalar="+rr.getScalarValue());
			System.out.println("Final Output : multivalue="+rr.getMultiValue());
			if(rr.getListValue() != null) {
				for(RuleOutputList rol : rr.getListValue()) {
					System.out.println("Final Output : output type="+rol.getOutputType()+"; output value="+rol.getOutputValue());
				}
			}
		}
		return rr;
	}
	
	private String getOutputKey(Integer id, List<RuleOutput> ros) {
		for(RuleOutput ro : ros) {
			if(ro.getId() == id) {
				return ro.getOutputKey();
			}
		}

		return "";
	}

	public List fireRule(String ruleCode, Rules rule, Object... objects) {
		//UnitOfWorkRule uor = new UnitOfWorkRule();
		RuleDetail ruleDetail = null;
		List<RuleOutputDetail> roList = null;
		List<Map<String, String>> valueList = null;
		List<RuleOutputDTO> rodList = new ArrayList<RuleOutputDTO>();
		RuleOutputDTO rod = null;
		try {
			//uor.start();
			ruleDetail = RuleDataCacheManager.getInstance().findRuleDetail(ruleCode); //this._uok.ruleDetailRepo().findRuleDetail(ruleCode);
			if(ruleDetail != null) {
				RuleDetail rd = ruleDetail;
				rule = (Rules) RuleDataCacheManager.getInstance().findRule(rd.getRuleId()); //this._uok.ruleDetailRepo().get(Rules.class, rd.getRuleId());
				if(this.rpdList == null) {
					this.rpdList = RuleDataCacheManager.getInstance().findRuleParameter(rd.getRuleId()); //this._uok.ruleDetailRepo().findRuleParameterList(rd.getRuleId());
					//Set value for parameters
					for(RuleParameterDetail rpd : this.rpdList) {
						rpd.resetValues();
						setParameterValue(rpd, objects);
					}
				}
				//Execute dynamic query
				if(RuleCombinationType.QUERY.value().equals(rd.getRuleCombinationType())) {
					roList = this._uok.ruleDetailRepo().findRuleOutputList(rd.getRuleCombinationDefinition(), this.rpdList);
					if(roList != null && roList.size() > 0) {
						return roList;
					}
				} else if(RuleCombinationType.DROP_LIST.value().equals(rd.getRuleCombinationType())) {
					valueList = this._uok.ruleDetailRepo().getRuleResultList(rd.getRuleCombinationDefinition(),rd.getMappingColumnName(), this.rpdList);
					if(valueList != null && valueList.size() > 0) {
						return valueList;
					}
				}
			}
		} catch (Throwable th) {
			System.out.println("fireRule got exception="+th.getMessage());
			th.printStackTrace();
			throw new DataException(th.getMessage());
		}
		return null;
	}
	
	private void setParameterValue(RuleParameterDetail rParamDetail, Object... objects) {
		System.out.println("setParameterValue: param Id="+rParamDetail.getId()
				+"; param data type="+rParamDetail.getParamDataType()
				+"; param type="+rParamDetail.getParamType());
		String[] name = null;
		String regEx = ":";
		if(RuleParameterType.CONSTANT.value().equals(rParamDetail.getParamType()) ||
				RuleParameterType.QUERY_SINGLE_VALUE.value().equals(rParamDetail.getParamType()) ||
				RuleParameterType.QUERY_TABLE_VALUE.value().equals(rParamDetail.getParamType()) ||
				RuleParameterType.QUERY_LIST_VALUE.value().equals(rParamDetail.getParamType()) ||
				RuleParameterType.QUERY_ROW_VALUE.value().equals(rParamDetail.getParamType())) {
			if(!StringUtils.isNullOrEmpty(rParamDetail.getJavaClassName())) {
				name = rParamDetail.getJavaClassName().split(regEx);
			}
			if(name != null) {
				Object instance = DataUtils.findObject(name[0], objects);
				if(instance != null) {
					invokeMethod(rParamDetail, instance, name[1]);
				}
			}
		} else if(RuleParameterType.PREDEFINE_LIST_VALUE.value().equals(rParamDetail.getParamType()) ||
					RuleParameterType.JAVA_VARIABLE.value().equals(rParamDetail.getParamType())) {
			if(RuleParameterListType.REFERENCE.value().equals(rParamDetail.getParamListType())) {
				String[] params = rParamDetail.getParamValue().split(regEx);
				RuleParameterDetail rpd = findRuleDetailById(Integer.parseInt(params[0]));
				invokeMethod(rParamDetail, rpd, params[1]);
			} else {
				if(!StringUtils.isNullOrEmpty(rParamDetail.getParamValue())) {
					name = rParamDetail.getParamValue().split(regEx);
				}
				if(name != null) {
					rParamDetail.setParamDetailName(name[0]);
					rParamDetail.setJavaClassName(name[1]+regEx+name[2]);
					Object instance = DataUtils.findObject(name[1], objects);
					if(instance != null) {
						invokeMethod(rParamDetail, instance, name[2]);
					}
				}
			}
		} else if(RuleParameterType.FORMULA.value().equals(rParamDetail.getParamType())) {
			FormulaResult fr = ExecuteFormula.execFormula(rParamDetail.getParamValue(), objects);
			if(fr != null) {
				System.out.println("String value="+fr.getStringValue());
				System.out.println("Double value="+fr.getDoubleValue());
				System.out.println("Float value="+fr.getFloatValue());
				System.out.println("Int value="+fr.getIntValue());
				System.out.println("Long value="+fr.getLongValue());
				System.out.println("Bool value="+fr.getBoolValue());
				System.out.println("Date value="+fr.getDateValue());
				System.out.println("Decimal value="+fr.getDecimalValue());
			}
			if(rParamDetail.getJavaClassName() != null && !"".equals(rParamDetail.getJavaClassName())) {
				name = rParamDetail.getJavaClassName().split(regEx);
			}
			if(name != null) {
				if(fr != null) {
					invokeMethod(rParamDetail, fr, name[1]);
				}
			}
		}
		System.out.println("setParameterValue: Rule detail: param name="+rParamDetail.getParamDetailName()+
				"; param value="+rParamDetail.getParamValue()+
				"; sql parameter="+rParamDetail.getParamDetailName()+
				"; java class="+rParamDetail.getJavaClassName()+
				"; input value="+rParamDetail.toString()+
				"; param Id="+rParamDetail.getId());
	}
	
	private RuleParameterDetail findRuleDetailById(int rdId) {
		if(this.rpdList == null) {
			throw new RuntimeException("Rule detail list is empty");
		}
		for(RuleParameterDetail r : this.rpdList) {
			if(r.getId().intValue() == rdId) {
				return r;
			}
		}
		throw new DataException("Rule detail with Id="+rdId+" cannot be found");
	}

/*
	private Object getParameterObject(String clsName, Object... objects) {
		if(clsName == null || "".equals(clsName)) {
			throw new DataException("getParameterObject: No class found");
		}
		// Go through to find object instance
		for(Object instance : objects) {
			if(instance == null) {
				throw new DataException("getParameterObject: Object instance is null!");
			}
			Class cls = instance.getClass();
			if(clsName.equals(cls.getName())) {
				return instance;
			}
		}
		System.out.println("Cannot find object instance of class="+clsName);
		throw new DataException("Cannot find object instance of class="+clsName);
	}
*/

	private void invokeMethod(RuleParameterDetail rParamDetail, Object object, String methodName) {
		System.out.println("invokeMethod="+methodName+"; param name="+rParamDetail.getParamName());
		if(RuleParameterListType.ARRAY_INPUT_TYPE.value().equals(rParamDetail.getParamListType())) {
			rParamDetail.setListValue((List) DataUtils.getMethodResult(object, methodName));
		} else if(RuleParameterDataType.STRING.value().equals(rParamDetail.getParamDataType())) {
			rParamDetail.setStringValue((String) DataUtils.getMethodResult(object, methodName));
		} else if(RuleParameterDataType.INTEGER.value().equals(rParamDetail.getParamDataType())) {
			rParamDetail.setIntValue((Integer) DataUtils.getMethodResult(object, methodName));
		} else if(RuleParameterDataType.LONG.value().equals(rParamDetail.getParamDataType())) {
			rParamDetail.setLongValue((Long) DataUtils.getMethodResult(object, methodName));
		} else if(RuleParameterDataType.DECIMAL.value().equals(rParamDetail.getParamDataType())) {
			rParamDetail.setDecimalValue((BigDecimal) DataUtils.getMethodResult(object, methodName));
		} else if(RuleParameterDataType.FLOAT.value().equals(rParamDetail.getParamDataType())) {
			rParamDetail.setFloatValue((Float) DataUtils.getMethodResult(object, methodName));
		} else if(RuleParameterDataType.DOUBLE.value().equals(rParamDetail.getParamDataType())) {
			rParamDetail.setDoubleValue((Double) DataUtils.getMethodResult(object, methodName));
		} else if(RuleParameterDataType.BOOLEAN.value().equals(rParamDetail.getParamDataType())) {
			rParamDetail.setBoolValue((Boolean) DataUtils.getMethodResult(object, methodName));
		} else if(RuleParameterDataType.DATE.value().equals(rParamDetail.getParamDataType()) ||
				RuleParameterDataType.TIME.value().equals(rParamDetail.getParamDataType()) ||
				RuleParameterDataType.DATETIME.value().equals(rParamDetail.getParamDataType())) {
			rParamDetail.setDateValue((String) DataUtils.getMethodResult(object, methodName));
		} else if(RuleParameterDataType.LIST.value().equals(rParamDetail.getParamDataType())) {
			rParamDetail.setListValue((List) DataUtils.getMethodResult(object, methodName));
		}
	}
	
	private RuleParameterDetail findParameterDetailByName(String pName) {
		if(StringUtils.isNullOrEmpty(pName)) {
			return null;
		}
		
		for(RuleParameterDetail rpd : this.rpdList) {
			if(pName.equals(rpd.getParamName())) {
				return rpd;
			}
		}
		return null;
	}
	
	private void addToRuleCache(String ruleCode, RuleObject ruleObj, RuleResult rr) {
		Rules r = (Rules) RuleDataCacheManager.getInstance().findRule(ruleCode);
		List<RuleCacheInitObject> rcioList = RuleDataCacheManager.getInstance().findRuleCacheInitParameter(r.getId());
		RuleDetail rd = RuleDataCacheManager.getInstance().findRuleDetail(r.getId());
		List<RuleCacheKeyInput> rckiList = this._uok.ruleDetailRepo().getRuleInputKey(rd.getRuleSQL2GetCacheInputKey(), this.rpdList, rcioList);
		for(RuleCacheKeyInput rcki : rckiList) {
			boolean ok = true;
			for(RuleCacheInitObject rcio : rcioList) {
				if(RuleParameterType.FORMULA.value().equals(rcio.getParamType())) {
					Object obj = DataUtils.getMethodResult(ruleObj, rcio.getMethodGetName());
					if((obj == null) || ((obj instanceof String) && StringUtils.isNullOrEmpty((String) obj))) {
						if(!Constant.RULE_DEFAULT_VALUE.equals(rcki.getParamValue().get(rcio.getParamName()))) {
							ok = false;
							break;
						}
					} else {
						if(Constant.RULE_DEFAULT_VALUE.equals(rcki.getParamValue().get(rcio.getParamName()))) {
							ok = false;
							break;
						}
					}
				}
			}
			if(ok) {
				// add to cache
				if(RuleDataCacheManager.getInstance().getRuleCache(rcki.getRuleKey()) == null) {
					RuleCacheMap rcm = new RuleCacheMap();
					rcm.setRuleKey(rcki.getRuleKey());
					rcm.setDateParams(rcki.getDateList());
					rcm.setRuleOutput(rr);
					RuleDataCacheManager.getInstance().getRuleCacheMap().put(rcki.getRuleKey(), rcm);
				}
			}
		}
		
	}
	
	public RuleResult findRuleInCache(String ruleCode, RuleObject obj) {
		RuleResult rr = null;
		boolean isFirstParam = true;
		Rules r = (Rules) RuleDataCacheManager.getInstance().findRule(ruleCode);
		List<RuleCacheInitObject> rcioList = RuleDataCacheManager.getInstance().findRuleCacheInitParameter(r.getId());
		Map<String, String> rDate = new HashMap<String, String>();
		rKeyList = new ArrayList<String>();
		List<String> tmpList = new ArrayList<String>();
		// Get parameter detail and set value
		if(this.rpdList == null) {
			this.rpdList = RuleDataCacheManager.getInstance().findRuleParameter(r.getId()); //this._uok.ruleDetailRepo().findRuleParameterList(rd.getRuleId());
			//Set value for parameters
			for(RuleParameterDetail rpd : this.rpdList) {
				setParameterValue(rpd, obj);
			}
		}
		// Get all parameters
		String rKey = ruleCode;
		for(RuleCacheInitObject rcio : rcioList) {
			RuleParameterDetail rpd = this.findParameterDetailByName(rcio.getParamName());
			if(!RuleParameterType.CONSTANT.value().equals(rcio.getParamType())
					&& !RuleParameterType.FORMULA.value().equals(rcio.getParamType())
					&& RuleParameterDataType.DATE.value().equals(rcio.getDataType())) {
				SimpleDateFormat sdf = new SimpleDateFormat(Constant.RULE_DEFAULT_DATE_FORMAT);
				rDate.put(rpd.getParamName(), sdf.format(rpd.getDateValue()));
			} else if(RuleParameterType.FORMULA.value().equals(rcio.getParamType())
					&& !StringUtils.isNullOrEmpty(rpd.toString())) {
				String fr = rpd.toString();
				if(!isFirstParam) {
					tmpList.clear();
					tmpList.addAll(rKeyList);
					rKeyList.clear();
				}
				for(int i=0;i<fr.length();i++) {
					if("Y".equals(fr.substring(i, i+1))) {
						if(isFirstParam) {
							rKeyList.add(rKey + Integer.toString(i+1));
						} else {
							for(int j=0;j<tmpList.size();j++) {
								rKeyList.add(tmpList.get(j) + Integer.toString(i+1));
							}
						}
					}
				}
				isFirstParam = false;
			} else {
				String value = rpd.toString();
				if(StringUtils.isNullOrEmpty(rpd.toString())) {
					value = Constant.RULE_DEFAULT_VALUE;
				}
				rKey = rKey + value;
				int k = 0;
				for(String s : rKeyList) {
					s = s + value;
					rKeyList.set(k, s);
					k++;
				}
			}
		}
		// Verify rule
		RuleCacheMap rcm = null;
		if(rKeyList.size() == 0) {
			rKeyList.add(rKey);
		}
		for(String s : rKeyList) {
			rcm = RuleDataCacheManager.getInstance().getRuleCache(s);
			if(rcm != null) {
				for(RuleDateType rdt : rcm.getDateParams()) {
					String d = rDate.get(rdt.getParamName());
					if(!StringUtils.isNullOrEmpty(d)) {
						if((d.compareTo(rdt.getStartDate()) >= 0)
								&& (d.compareTo(rdt.getEndDate()) <= 0)) {
							return rcm.getRuleOutput();
						}
					}
				}
			}
		}
		return rr;
	}
	
}
