package com.mcredit.rule.manager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import org.apache.commons.lang3.time.DateUtils;

import com.mcredit.model.enums.DateFormatTag;

public class DateHelper {
	
	public static void main(String[] args) {
		String createdDate = "2019-05-22 00:00:00" ;
		String birthDate = "1994-05-23" ;		
		int loanTenor = 12;
		//System.out.println( " -- ^^ 11--- :  " + plusYear(plusDate(plusMonths(toDate(birthDateStr), loanTenor ),30),60).before(toDate(createDateStr))); // 1994,11,22
		
		System.out.println( " a :" +  plusDate(plusMonths(toDate(createdDate), loanTenor),30));
		System.out.println(" b + c :" + plusYear(toDate(birthDate),60));
		System.out.println("TEST ="+test60(createdDate,birthDate ,loanTenor));
		
	}
	
	public static boolean  test60(String createdDate, String  birthDate, int loanTenor){
		
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");  
		
		String date = dateFormat.format( plusDate(plusMonths(toDate(createdDate), loanTenor),30));  
		
		String date2 = dateFormat.format(  plusYear(toDate(birthDate),60));  
		
		System.out.println("date1=" + date + "; date2="+date2);
		
		System.out.println( " aaa " + date.compareTo(date2));
		
		System.out.println( " 122 " + plusDate(plusMonths(toDate(createdDate), loanTenor),30).compareTo(plusYear(toDate(birthDate),60)));
		
		System.out.println( "  ^^ " + plusYear(toDate(birthDate),20).after(toDate(createdDate)));
		
		return  plusDate(plusMonths(toDate(createdDate), loanTenor),30).compareTo(plusYear(toDate(birthDate),60)) > 0 ;
	}
	
	public static Date plusMonths(Date input , int month){
		
		Calendar c = Calendar.getInstance();
		c.setTime(input);
		c.add(Calendar.MONTH, month);
		return c.getTime();
		
	}
	
	public static Date plusDate(Date input , int days){
		Calendar c = Calendar.getInstance();
		c.setTime(input);
		c.add(Calendar.DATE, days);
		return c.getTime();
		
	}
	public static Date plusYear(Date input , int years){
		Calendar c = Calendar.getInstance();
		c.setTime(input);
		c.add(Calendar.YEAR, years);
		return c.getTime();
		
	}
	
	public static Date toDate(String input){
		Date date = null ;
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormatTag.DATEFORMAT_yyyy_MM_dd_HH_mm_ss.value());
		SimpleDateFormat sdf1 = new SimpleDateFormat(DateFormatTag.DATEFORMAT_yyyy_MM_dd_.value());
		
		try {
			date = sdf.parse(input);
		} catch (Exception e) {
			try {
				 date = sdf1.parse(input);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return date;
		
	}
	
	
}
