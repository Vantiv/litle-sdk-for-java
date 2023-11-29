package io.github.vantiv.sdk;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarPrinter {
	private static final SimpleDateFormat XS_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static String printDate(Calendar val) {
		return XS_DATE_FORMAT.format(val.getTime());
	}
}
