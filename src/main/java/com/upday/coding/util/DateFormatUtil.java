package com.upday.coding.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.upday.coding.exceptions.InvalidDateFormatException;

import static com.upday.coding.validation.ValidationPatterns.VALID_DATE_FORMAT;

public class DateFormatUtil {

	
	public static String formatUsingDefaultFormat(Date date) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(VALID_DATE_FORMAT);
		return dateFormatter.format(date);
	}
	
	public static Date parseUsingDefaultFormat(String formattedDate) throws InvalidDateFormatException {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(VALID_DATE_FORMAT);
			return dateFormatter.parse(formattedDate);			
		} catch (ParseException e) {
			throw new InvalidDateFormatException(e);
		}
	}
	
}
