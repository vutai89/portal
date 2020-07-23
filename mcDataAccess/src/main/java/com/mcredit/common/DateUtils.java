package com.mcredit.common;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtils {
public static Date getNextValidDate(List<com.mcredit.data.common.entity.Calendar> listCalendar) {
		
		for (com.mcredit.data.common.entity.Calendar car : listCalendar) if (car.getDay() == null) car.setDay("");
		
		Calendar today = Calendar.getInstance();
		Calendar validDate = null;
		Integer yearSearch = today.get(Calendar.YEAR);
		Integer monthSearch = today.get(Calendar.MONTH)+1;
		Integer thisDay = today.get(Calendar.DAY_OF_MONTH);
		boolean thisMonth = true;
		while(true) {
			boolean futureMonth = false;
			 
			for (com.mcredit.data.common.entity.Calendar cal:listCalendar) {
				if (cal.getYear().compareTo(yearSearch) > 0) futureMonth = true;
				if (cal.getYear().equals(yearSearch) && cal.getMonth().compareTo(monthSearch) > 0) futureMonth = true;
				if (cal.getMonth().equals(monthSearch) && cal.getYear().equals(yearSearch)) {
					if (!thisMonth && cal.getDay().contains("Y")) {
						validDate = Calendar.getInstance();
						validDate.set(Calendar.YEAR, yearSearch);
						validDate.set(Calendar.MONTH, monthSearch-1);
						int maxDays = validDate.getActualMaximum(Calendar.DAY_OF_MONTH);
						int date = cal.getDay().indexOf("Y")+1;
						if (maxDays<date) {
							validDate = null;
							continue;
						}
						validDate.set(Calendar.DAY_OF_MONTH, date );
						break;
					}
					if (thisMonth && cal.getDay().length()>=thisDay && cal.getDay().substring(thisDay).contains("Y")) {
						validDate = Calendar.getInstance();
						validDate.set(Calendar.YEAR, yearSearch);
						validDate.set(Calendar.MONTH, monthSearch-1);
						int maxDays = validDate.getActualMaximum(Calendar.DAY_OF_MONTH);
						int date = cal.getDay().substring(thisDay).indexOf("Y") + 1 + thisDay;
						if (maxDays<date) {
							validDate = null;
							continue;
						}
						validDate.set(Calendar.DAY_OF_MONTH, date);
						break;
					}
				}
			}
			monthSearch++;
			thisMonth = false;

			if (monthSearch==13) {
				monthSearch =1;
				yearSearch++;
			}

			if (validDate!=null) return validDate.getTime();
			if (!futureMonth) break;

		}
		return today.getTime();
	}
}
