package proyecto.integrador.odbyt.Utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

import proyecto.integrador.odbyt.R;
import android.content.Context;

public class Convert {
	/**
	 * Function that converts a date in long format to a date in String format.
	 * 
	 * @param date
	 *            In long format (yyyymmdd).
	 * @return Date in String format (dd/mm/yyyy).
	 */
	public static String fromLongtoDateString(long date) {
		return StringManage.Right(date + "", 2) + "/"
				+ StringManage.Mid(date + "", 5, 2) + "/"
				+ StringManage.Left(date + "", 4);
	}

	/**
	 * Function that converts a date in String format to a date in long format.
	 * 
	 * @param date
	 *            In String format (dd/mm/yyyy).
	 * @return Date in long format (yyyymmdd).
	 */
	public static long fromDateStringtoLong(String date) {
		String temp = StringManage.Right(date, 4);
		temp += StringManage.Mid(date, 4, 2);
		temp += StringManage.Left(date, 2);

		return Long.valueOf(temp);
	}

	/**
	 * Function that converts the time in long format to time in String format.
	 * 
	 * @param time
	 *            In long format (hhmm).
	 * @return Time in String format (hh:mm).
	 */
	public static String fromLongtoTimeString(long time) {
		String time_to_string = time + "";

		return StringManage.Left(time_to_string,
				(time_to_string.length() == 4 ? 2 : 1))
				+ ":" + StringManage.Right(time_to_string, 2);
	}

	/**
	 * Function that convert the State of a sell in integer format to a String
	 * format.
	 * 
	 * @param context
	 * @param state
	 *            1 if is paying, 0 if is finished.
	 * @return State of a sell in a String format.
	 */
	public static String fromIntStatetoString(Context context, int state) {
		return (state > 0 ? context.getString(R.string.paying_Text) : context
				.getString(R.string.finished_Text));
	}

	/**
	 * Function that converts the ChargeType of a sell in integer format to a
	 * String format.
	 * 
	 * @param context
	 * @param chargeType
	 *            1 if is every week, 2 if is every two weeks, 3 if is every
	 *            month.
	 * @return The ChargeType of a sell in String format.
	 */
	public static String fromIntChargeTypetoString(Context context,
			int chargeType) {
		String[] types = context.getResources().getStringArray(
				R.array.how_to_pass_to_charge_the_payment);
		return types[chargeType - 1];
	}

	/**
	 * Function that converts the ChargeType of a sell in String format to a
	 * integer format.
	 * 
	 * @param context
	 * @param chargeType
	 *            In String format.
	 * @return The ChargeType in integer format.
	 */
	public static int fromStringChargeTypetoIng(Context context,
			String chargeType) {
		String[] types = context.getResources().getStringArray(
				R.array.how_to_pass_to_charge_the_payment);

		for (int i = 0; i < types.length; i++)
			if (types[i].equals(chargeType))
				return ++i;

		return -1;
	}

	/**
	 * Function that converts a day in integer format to day in String format.
	 * 
	 * @param context
	 * @param day
	 *            1 if is Monday, 2 if is Tuesday, 3 if is Wednesday, 4 if is
	 *            Thursday, 5 if is Friday, 6 if is Saturday.
	 * @return Day in String format.
	 */
	public static String fromIntDaytoString(Context context, int day) {
		String[] days = context.getResources().getStringArray(
				R.array.days_of_the_week);
		return days[day - 1];
	}

	/**
	 * Function that converts a day in String format to day in integer format.
	 * 
	 * @param context
	 * @param day
	 *            In String format.
	 * @return Day in integer format.
	 */
	public static int fromStringDaytoInt(Context context, String day) {
		String[] days = context.getResources().getStringArray(
				R.array.days_of_the_week);

		for (int i = 0; i < days.length; i++)
			if (days[i].equals(day))
				return ++i;

		return -1;
	}

	/**
	 * Function that converts a date in long format to a Calendar object. It
	 * sets these properties:
	 * <ul>
	 * <li>Calendar.YEAR</li>
	 * <li>Calendar.MONTH</li>
	 * <li>Calendar.DAY_OF_MONTH</li>
	 * </ul>
	 * 
	 * @param date
	 *            In long format (yyyymmdd).
	 * @return Date in a Calendar object.
	 */
	public static Calendar fromLongDatetoCalendar(long date) {
		int y = Integer.parseInt(StringManage.Left(date + "", 4));
		int m = Integer.parseInt(StringManage.Mid(date + "", 5, 2)) - 1;
		int d = Integer.parseInt(StringManage.Right(date + "", 2));

		return new GregorianCalendar(y, m, d);
	}

	/**
	 * Function that converts a date in a Calendar object to a long format.
	 * 
	 * @param c
	 *            Date in a Calendar object. Takes these properties:
	 *            <ul>
	 *            <li>Calendar.YEAR</li>
	 *            <li>Calendar.MONTH</li>
	 *            <li>Calendar.DAY_OF_MONTH</li>
	 *            </ul>
	 * @return Date in long format (yyyymmdd).
	 */
	public static long fromCalendartoLongDate(Calendar c) {
		int y = c.get(Calendar.YEAR);
		int m = c.get(Calendar.MONTH) + 1;
		int d = c.get(Calendar.DAY_OF_MONTH);

		String date = y + "" + (m < 10 ? "0" + m : m) + ""
				+ (d < 10 ? "0" + d : d);

		return Long.valueOf(date);
	}

	/**
	 * Function that converts the HTML characters to special characters.
	 * 
	 * @param text
	 *            String with the HTML characters to be converted.
	 * 
	 *            <pre>
	 *            <b>e.g.</b>
	 *            {@code"&aacute;"} would be "á".
	 *            {@code"&eacute;"} would be "é".
	 *            And so on.
	 * </pre>
	 * 
	 * @return The transformed <b>text</b>.
	 */
	public static String __(String text) {
		if (text.length() > 0) {
			text = text.replace("&aacute;", "á");
			text = text.replace("&eacute;", "é");
			text = text.replace("&iacute;", "í");
			text = text.replace("&oacute;", "ó");
			text = text.replace("&uacute;", "ú");
			text = text.replace("&Aacute;", "Á");
			text = text.replace("&Eacute;", "É");
			text = text.replace("&Iacute;", "Í");
			text = text.replace("&Oacute;", "Ó");
			text = text.replace("&Uacute;", "Ú");
			text = text.replace("&ntilde;", "ñ");
			text = text.replace("&Ñtilde;", "Ñ");
			text = text.replace("&auml;", "ä");
			text = text.replace("&euml;", "ë");
			text = text.replace("&iuml;", "ï");
			text = text.replace("&ouml;", "ö");
			text = text.replace("&uuml;", "ü");
			text = text.replace("&Auml;", "Ä");
			text = text.replace("&Euml;", "Ë");
			text = text.replace("&Iuml;", "Ï");
			text = text.replace("&Ouml;", "Ö");
			text = text.replace("&Uuml;", "Ü");
		}

		return text;
	}
}
