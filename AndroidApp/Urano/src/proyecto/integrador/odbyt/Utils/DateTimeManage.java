package proyecto.integrador.odbyt.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTimeManage {

	/**
	 * Function that returns Today's date.
	 * 
	 * @return Today's date as long format (yyyymmdd).
	 */
	public static long getTodayDateAsLong() {
		Calendar c = new GregorianCalendar();
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH) + 1;

		String date = c.get(Calendar.YEAR) + ""
				+ (month < 10 ? "0" + month : month) + ""
				+ (day < 10 ? "0" + day : day);

		return Long.valueOf(date);
	}

	/**
	 * Function that returns Today's date.
	 * 
	 * @return Today's date as String format (dd/mm/yyyy).
	 */
	public static String getTodayDateAsString() {
		return Convert.fromLongtoDateString(getTodayDateAsLong());
	}
	
	public static long getTodayTimeAsLong() {
		Calendar c = new GregorianCalendar();
		int h = c.get(Calendar.HOUR_OF_DAY);
		int m = c.get(Calendar.MINUTE);
		
		return (h * 100) + m;
	}
	
	public static String getTodayTimeAsStirng() {
		return Convert.fromLongtoTimeString(getTodayTimeAsLong());
	}
	
	public static long every_week(int day_of_week, Calendar c) {
		day_of_week++;
		
		if (c.get(Calendar.DAY_OF_WEEK) < day_of_week) {
            c.set(Calendar.DATE, c.get(Calendar.DATE) + (day_of_week - c.get(Calendar.DAY_OF_WEEK) + 7));
        } else if (c.get(Calendar.DAY_OF_WEEK) > day_of_week) {
            c.set(Calendar.DATE, c.get(Calendar.DATE) + (7 - (c.get(Calendar.DAY_OF_WEEK) - day_of_week)));
        } else {
            c.set(Calendar.DATE, c.get(Calendar.DATE) + 7);
        }
		
		return Convert.fromCalendartoLongDate(c);
	}
	
	public static long every_2_weeks(int day_of_week, Calendar c) {
		day_of_week++;
		
		if (c.get(Calendar.DAY_OF_WEEK) < day_of_week) {
            c.set(Calendar.DATE, c.get(Calendar.DATE) + (day_of_week - c.get(Calendar.DAY_OF_WEEK) + 14));
        } else if (c.get(Calendar.DAY_OF_WEEK) > day_of_week) {
            c.set(Calendar.DATE, c.get(Calendar.DATE) + (14 - (c.get(Calendar.DAY_OF_WEEK) - day_of_week)));
        } else {
            c.set(Calendar.DATE, c.get(Calendar.DATE) + 14);
        }
		
		return Convert.fromCalendartoLongDate(c);
	}
	
	public static long every_month(int day_of_week, Calendar c) {
		day_of_week++;
		
		if (c.get(Calendar.DAY_OF_WEEK) < day_of_week) {
            c.set(Calendar.DATE, c.get(Calendar.DATE) + (day_of_week - c.get(Calendar.DAY_OF_WEEK) + 28));
        } else if (c.get(Calendar.DAY_OF_WEEK) > day_of_week) {
            c.set(Calendar.DATE, c.get(Calendar.DATE) + (28 - (c.get(Calendar.DAY_OF_WEEK) - day_of_week)));
        } else {
            c.set(Calendar.DATE, c.get(Calendar.DATE) + 28);
        }
		
		return Convert.fromCalendartoLongDate(c);
	}
}
