package com.mtstream.shelve.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateProvider {
	
	public static boolean day(int m,int d) {
		Date date = new Date();
		LocalDate locdate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int month = locdate.getMonthValue();
		int day  = locdate.getDayOfMonth();
		return m==month&&d==day;
	}
}
