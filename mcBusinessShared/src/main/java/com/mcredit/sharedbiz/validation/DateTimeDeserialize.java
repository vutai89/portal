/**
 * 
 */
package com.mcredit.sharedbiz.validation;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

import com.mcredit.util.StringUtils;

/**
 * @author anhdv.ho
 *
 */
public class DateTimeDeserialize extends JsonDeserializer<Date> {

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	@Override
    public Date deserialize(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext) throws IOException, JsonProcessingException {
		
        String date = paramJsonParser.getText().trim();
        
        if( !isDate(date) )
        	throw new IOException("Date format is invalid (yyyy-MM-dd)");
        
        if( date.indexOf("-")!=-1 ) {
        	String[] arrDate = date.split("-");
            
            if( arrDate == null || arrDate.length != 3 || Integer.parseInt(arrDate[1]) > 12 )
            	//date = arrDate[0] + "-" + arrDate[2] + "-" + arrDate[1];
            	throw new IOException("Date format is invalid (yyyy-MM-dd)");
            
            try {
            	
                return dateFormat.parse(date);
                
            } catch (ParseException e) {
            	
                System.out.println("CustomerDateAndTimeDeserialize.deserialize.ex: " + e.toString());
            }
            
            return paramDeserializationContext.parseDate(date);
        }
        
        return null;
    }
	
	private boolean isDate(String input) {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			if( !StringUtils.isNullOrEmpty(input) )
				df.parse(input);
		    return true;
		}
		catch(Exception e){}
		
		return false;
	}
}
