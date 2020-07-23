package com.mcredit.model.object;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.ParseException;

import com.mcredit.model.enums.RuleParameterDataType;

public class RuleParameterDetail {

	@ColumnIndex(index=0)
	private Integer id;
	@ColumnIndex(index=1)
	private String paramName;
	@ColumnIndex(index=2)
	private String paramType;
	@ColumnIndex(index=3)
	private String paramDataType;
	@ColumnIndex(index=4)
	private String paramListType;
	@ColumnIndex(index=5)
	private String paramValue;
	@ColumnIndex(index=6)
	private String status;
	@ColumnIndex(index=7)
	private String dataFormat;
	@ColumnIndex(index=8)
	private String paramDetailName;
	@ColumnIndex(index=9)
	private String javaClassName;
	@ColumnIndex(index=10)
	private BigDecimal paramDetailDecimal;
	@ColumnIndex(index=11)
	private Integer ruleId;

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	private Integer intValue;
	private Long longValue;
	private String stringValue;
	private BigDecimal decimalValue;
	private Boolean boolValue;
	private Float floatValue;
	private Double doubleValue;
	private Date dateValue;
	private List<?> listValue;		// list param when PARAM_LIST_TYPE = AIT; list value when PARAM_DATA_TYPE = A

	@Override
	public String toString() {
		NumberFormat nf = null;
		SimpleDateFormat df = null;
		if(RuleParameterDataType.FLOAT.value().equals(paramDataType)) {
			nf = new DecimalFormat(dataFormat);
			if(floatValue == null) {
				return null;
			} else {
				return nf.format(floatValue);
			}
		} else if(RuleParameterDataType.DOUBLE.value().equals(paramDataType)) {
			nf = new DecimalFormat(dataFormat);
			if(doubleValue == null) {
				return null;
			} else {
				return nf.format(doubleValue);
			}
		} else if(RuleParameterDataType.DECIMAL.value().equals(paramDataType)) {
			nf = new DecimalFormat(dataFormat);
			if(decimalValue == null) {
				return null;
			} else {
				return nf.format(decimalValue);
			}
		} else if(RuleParameterDataType.STRING.value().equals(paramDataType)) {
			return stringValue;
		} else if(RuleParameterDataType.INTEGER.value().equals(paramDataType)) {
			if(intValue == null) {
				return null;
			} else {
				return intValue.toString();
			}
		} else if(RuleParameterDataType.LONG.value().equals(paramDataType)) {
			if(longValue == null) {
				return null;
			} else {
				return longValue.toString();
			}
		} else if(RuleParameterDataType.BOOLEAN.value().equals(paramDataType)) {
			if(boolValue == null) {
				return null;
			} else {
				if(boolValue.booleanValue()) {
					return "true";
				} else {
					return "false";
				}
			}
		} else if(RuleParameterDataType.DATE.value().equals(paramDataType) ||
				RuleParameterDataType.DATETIME.value().equals(paramDataType) ||
				RuleParameterDataType.TIME.value().equals(paramDataType)) {
			//yyyyMMdd
			//hh:mm:ss
			df = new SimpleDateFormat(dataFormat);
			if(dateValue == null) {
				return null;
			} else {
				return df.format(dateValue);
			}
		} else if(RuleParameterDataType.LIST.value().equals(paramDataType)) {
			if(listValue == null) {
				return null;
			} else {
				return listValue.toString();
			}
		}

		return null;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getParamName() {
		return paramName;
	}
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getParamDataType() {
		return paramDataType;
	}
	public void setParamDataType(String paramDataType) {
		this.paramDataType = paramDataType;
	}
	public String getParamListType() {
		return paramListType;
	}
	public void setParamListType(String paramListType) {
		this.paramListType = paramListType;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getParamDetailName() {
		return paramDetailName;
	}
	public void setParamDetailName(String paramDetailName) {
		this.paramDetailName = paramDetailName;
	}
	public String getJavaClassName() {
		return javaClassName;
	}
	public void setJavaClassName(String javaClassName) {
		this.javaClassName = javaClassName;
	}
	public Integer getIntValue() {
		return intValue;
	}
	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}
	public Long getLongValue() {
		return longValue;
	}
	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public BigDecimal getDecimalValue() {
		return decimalValue;
	}
	public void setDecimalValue(BigDecimal decimalValue) {
		this.decimalValue = decimalValue;
	}
	public Boolean getBoolValue() {
		return boolValue;
	}
	public void setBoolValue(Boolean boolValue) {
		this.boolValue = boolValue;
	}
	public Float getFloatValue() {
		return floatValue;
	}
	public void setFloatValue(Float floatValue) {
		this.floatValue = floatValue;
	}
	public Double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}
	public String getDataFormat() {
		return dataFormat;
	}
	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}

	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(String dateValue) {
		//yyyyMMdd
		//hh:mm:ss
		if(isNullOrEmpty(dateValue)) {
			this.dateValue = null;
			return;
		}
		SimpleDateFormat df = new SimpleDateFormat(dataFormat);
		try {
			df.setLenient(false);
			this.dateValue = df.parse(dateValue);
		} catch	(Throwable th) {
			System.out.println("setDateValue error="+th.getMessage());
			th.printStackTrace();
			this.dateValue = null;
			throw new ParseException(th.getMessage());
		}
	}

	public BigDecimal getParamDetailDecimal() {
		return paramDetailDecimal;
	}

	public void setParamDetailDecimal(BigDecimal paramDetailDecimal) {
		this.paramDetailDecimal = paramDetailDecimal;
	}

	public List<?> getListValue() {
		return listValue;
	}

	public void setListValue(List<?> listValue) {
		this.listValue = listValue;
	}

	private boolean isNullOrEmpty(String input) {

		return input == null || input.trim().isEmpty();
	}
	
	public void resetValues() {
		this.intValue = null;
		this.longValue = null;
		this.stringValue = null;
		this.decimalValue = null;
		this.boolValue = null;
		this.floatValue = null;
		this.doubleValue = null;
		this.dateValue = null;
		this.listValue = null;
	}
}
