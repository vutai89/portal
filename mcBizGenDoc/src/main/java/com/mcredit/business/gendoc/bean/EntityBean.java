package com.mcredit.business.gendoc.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import pl.jsolve.templ4docx.variable.Variable;

public class EntityBean {
	String tableName;
	List<String> fieldList = new ArrayList<String>();
	Map<String, String> mapVariableName = new HashMap<String, String>();
	Map<String, String> mapVariableValue= new HashMap<String, String>();
	Map<String, List<Variable>> mapVariableColum= new HashMap<String, List<Variable>>();
	String fieldCondition;
	String fieldConditionValue;
	boolean isRepeatTable;

	public String buildQuery() {
		String strField = StringUtils.join(fieldList, ',');
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT ").append(strField).append(" FROM ").append(tableName).append(" WHERE ")
				.append(fieldCondition).append(" = '").append(fieldConditionValue).append("' ");
		if(!isRepeatTable){
			sb.append(" LIMIT 1 ");
		}
		return sb.toString();

	}
        public String fieldQuery() {
            String strField="";
            if (tableName.equalsIgnoreCase("v_data_entry")) {
                for (String field : fieldList) {
                    strField = strField + "REPLACE(" + field + ",\"\\\"\",\"'\")" + field + ",";
                }
            } else if (tableName.equalsIgnoreCase("v_data_entry_gendoc")) {
                for (String field : fieldList) {
                    strField = strField + "CONCAT(''," + field + ",'+')" + field + ",";
                }
            } else if(tableName.equalsIgnoreCase("v_repayment_schedule")) {
            	return strField;
            } else if(tableName.equalsIgnoreCase("v_data_entry_gendoc_edit") || tableName.equalsIgnoreCase("v_repayment_date")) {
            	return strField;
            } else if(tableName.equalsIgnoreCase("v_data_entry_de")) {
            	for(String field: fieldList) {
            		strField = strField + ",";
            	}
            }
            return strField.substring(0, strField.length() - 1);
	}
         public String clauseWhere() {		
		StringBuilder sb = new StringBuilder();
		sb.append(fieldCondition).append("='").append(fieldConditionValue).append("'");		
		return sb.toString();
	}

	public void addField(String str) {

		fieldList.add(str);
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldCondition() {
		return fieldCondition;
	}

	public void setFieldCondition(String fieldCondition) {
		this.fieldCondition = fieldCondition;
	}

	public String getFieldConditionValue() {
		return fieldConditionValue;
	}

	public void setFieldConditionValue(String fieldConditionValue) {
		this.fieldConditionValue = fieldConditionValue;
	}

	public List<String> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}


	public boolean isRepeatTable() {
		return isRepeatTable;
	}

	public void setRepeatTable(boolean isRepeatTable) {
		this.isRepeatTable = isRepeatTable;
	}

	public Map<String, String> getMapVariableName() {
		return mapVariableName;
	}

	public void setMapVariableName(Map<String, String> mapVariableName) {
		this.mapVariableName = mapVariableName;
	}

	public Map<String, List<Variable>> getMapVariableColum() {
		return mapVariableColum;
	}

	public void setMapVariableColum(Map<String, List<Variable>> mapVariableColum) {
		this.mapVariableColum = mapVariableColum;
	}

	public Map<String, String> getMapVariableValue() {
		return mapVariableValue;
	}

	public void setMapVariableValue(Map<String, String> mapVariableValue) {
		this.mapVariableValue = mapVariableValue;
	}
	

}
