package com.mglj.totorial.framework.tools.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日历工具
 * 
 * @date 2016-11-04
 * @author yj
 */
public class CalendarUtils {

	public final static  String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public final static  String DEFAULT_SHORT_DATE_FORMAT = "yyyy-MM-dd";

    private final static Map<String, DateTimeFormatter> formatterMap = new HashMap<String, DateTimeFormatter>();
    private final static DateTimeFormatter defaultFormatter = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT);
    
    static {
    	formatterMap.put(DEFAULT_DATE_FORMAT, defaultFormatter);
    	formatterMap.put(DEFAULT_SHORT_DATE_FORMAT, DateTimeFormat.forPattern(DEFAULT_SHORT_DATE_FORMAT));
    	formatterMap.put("yyyy/MM/dd HH:mm:ss", DateTimeFormat.forPattern("yyyy/MM/dd HH:mm:ss"));
    	formatterMap.put("yyyy/MM/dd", DateTimeFormat.forPattern("yyyy/MM/dd"));
    }
    
    /**
     * 分析字符串，转为时间。
     * 
     * @param dateStr
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr) 
    		throws ParseException {
    	if(dateStr == null || dateStr.isEmpty() || dateStr.trim().isEmpty()) {
    		return null;
    	}
		if(dateStr.indexOf("T") > 0) {
			return DateTime.parse(dateStr).toDate();
		}
    	return parse(dateStr, defaultFormatter);
    }
    
    /**
     * 分析字符串，转为时间。
     * 
     * @param dateStr
     * @param format
     * @return
     * @throws ParseException
     */
    public static Date parse(String dateStr, String format) 
    		throws ParseException {
    	if(dateStr == null || dateStr.isEmpty() || dateStr.trim().isEmpty()) {
    		return null;
    	}
		if(dateStr.indexOf("T") > 0) {
			return DateTime.parse(dateStr).toDate();
		}
    	return parse(dateStr, getDateTimeFormatter(format));
    }
   
    /**
     * 把时间转成字符串
     * 
     * @param date
     * @return
     */
    public static String format(Date date) {
    	if(date == null) {
    		return null;
    	}
        return new DateTime(date).toString(defaultFormatter);
    }
    
    /**
     * 把时间转成字符串
     * 
     * @param date
     * @param format
     * @return
     */
    public static String format(Date date, String format) {
    	if(date == null) {
    		return null;
    	}
    	return new DateTime(date).toString(getDateTimeFormatter(format));
    }
    
    private static DateTimeFormatter getDateTimeFormatter(String format) {
    	if(format == null || format.isEmpty() || format.trim().isEmpty()) {
    		return defaultFormatter;
    	}
    	DateTimeFormatter dateTimeFormatter = formatterMap.get(format);
    	if(dateTimeFormatter == null) {
    		if(formatterMap.size() > 100) {
    			throw new IllegalArgumentException("The formatters supported is excced 100, format "+ format + "is forbbiden.");
    		}
    		synchronized (CalendarUtils.class) {
    			dateTimeFormatter = formatterMap.get(format);{
    				if(dateTimeFormatter == null) {
    					dateTimeFormatter = DateTimeFormat.forPattern(format);  
    		    		formatterMap.put(format, dateTimeFormatter);
    				}
    			}
			}
    	}
    	return dateTimeFormatter;
    }
    
    private static Date parse(String dateStr, DateTimeFormatter dateTimeFormatter)
    		throws ParseException {
    	try {
    		return DateTime.parse(dateStr, dateTimeFormatter).toDate();
    	} catch(Exception e) {
    		return parseByOtherPattern(dateStr, dateTimeFormatter);
    	}
    }
    
    private static Date parseByOtherPattern(String dateStr, DateTimeFormatter exclude) 
    		throws ParseException {
    	Date result = null;
    	for(String key : formatterMap.keySet()){
    		DateTimeFormatter dateTimeFormatter = formatterMap.get(key);
    		if(dateTimeFormatter != exclude) {
    			try {
					result = DateTime.parse(dateStr, dateTimeFormatter).toDate();
				} catch (Exception e) {
    				//do nothing
				}
    		}
    	}
    	if(result == null){
    		throw new IllegalArgumentException("Error date format: " + dateStr);
    	}
    	return result;
    }
    
}