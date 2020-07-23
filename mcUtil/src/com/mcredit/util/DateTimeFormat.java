package com.mcredit.util;

public enum DateTimeFormat
{
	yyyyMMdd("yyyy-MM-dd"),//example value: 2001-07-04
	yyyyMMddHHmmss("yyyy-MM-dd HH:mm:ss"),//example value: 2001-07-04 12:08:56
	yyyyMMdd_T_HHmmssSSSXXX("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"),//example value: 2001-07-04T12:08:56.235-07:00
	yyyyMMdd_T_HHmmssSSSZ("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),//example value: 2001-07-04T12:08:56.235-0700
	ddMMyyyHHmmss("dd-MM-yyyy HH:mm:ss"),//example value: 28-08-2019 01:02:03
	dd_MM_yyyy("dd-MM-yyyy"), //example value: 28-08-2019
	ddMMyyyy("ddMMyyyy"); //example value: 28082019
	
	String _value;
	DateTimeFormat(String v) {
		_value = v;
    }
    
	public String getDescription() {
      return _value;
    }
}