package fr.cavalier.netcheck.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author malika 
 * @date 27 janv. 2013
 * net-check
 * fr.cavalier.netcheck.util DateFormatter.java 
 */
public class DateFormatter {
	
	private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	
	public static String convertDateToString(Date date) {
		return format.format(date);
	}
	
	public static Date convertStringToDate(String date) throws ParseException {
		return format.parse(date);
	}

}
