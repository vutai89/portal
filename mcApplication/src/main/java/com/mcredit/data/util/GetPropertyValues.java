package com.mcredit.data.util;

import java.util.ResourceBundle;


public class GetPropertyValues {	
	 public static String ReadProperties(String filename, String properties_name) {
	        String str = "";
	        try {
	            ResourceBundle bun = ResourceBundle.getBundle(filename);
	            str = bun.getString(properties_name);
	        } catch (Exception e) {
	           e.printStackTrace();	        	
	        }
	        System.out.println("STR : "+str);
	        return str;
	    }
}
