package proyecto.integrador.odbyt.Report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.PaymentTable;
import proyecto.integrador.odbyt.Utils.Convert;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SelectReportType extends Activity implements OnClickListener {
	private TextView lblHeaderTitle;
	private Button btnByDay, btnByWeek, btnByMonth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_report_type);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle.setText(getString(R.string.title_activity_reports));

		btnByDay = (Button) findViewById(R.id.btnByDay);
		btnByDay.setOnClickListener(this);

		btnByMonth = (Button) findViewById(R.id.btnByMonth);
		btnByMonth.setOnClickListener(this);

		btnByWeek = (Button) findViewById(R.id.btnByWeek);
		btnByWeek.setOnClickListener(this);
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnByDay:
			btnByDay_onClick();
			break;

		case R.id.btnByMonth:
			btnByMonth_onClick();
			break;

		case R.id.btnByWeek:
			btnByWeek_onClick();
			break;
		}
	}

	/**
	 * Called when By Day button has been clicked.
	 */
	private void btnByDay_onClick() {
		long[] dates = new PaymentTable(this).getAllDistinctDates();
		List<String> days = null;

		if (dates != null) {
			days = new ArrayList<String>();
			for (int i = 0; i < dates.length; i++)
				days.add(Convert.fromLongtoDateString(dates[i]));

			Utils.setDaysList(null);
			Utils.setDaysList(days);

			startReportActivity(getString(R.string.day_Text));
		}
	}

	/**
	 * Called when By Week button has been clicked.
	 */
	private void btnByWeek_onClick() {
		long[] dates = new PaymentTable(this).getAllDistinctDates();
		List<Week> weeks = null;

		if (dates != null) {
			Week week = null;

			for (int i = 0; i < dates.length; i++) {
				if (weeks == null) {
					weeks = new ArrayList<Week>();
					week = getWeek(Convert.fromLongDatetoCalendar(dates[i]));

					weeks.add(week);
					week = null;
				} else {
					if (dates[i] >= weeks.get(weeks.size() - 1).getStart()
							&& dates[i] <= weeks.get(weeks.size() - 1).getEnd())
						continue;
					else {
						week = getWeek(Convert.fromLongDatetoCalendar(dates[i]));
						weeks.add(week);
						week = null;
					}
				}
			}

			Utils.setWeeksList(null);
			Utils.setWeeksList(weeks);

			startReportActivity(getString(R.string.week_Text));
		}
	}

	/**
	 * Function that returns a Week object through a Calendar object.
	 * 
	 * @param c
	 *            Calendar object with the date.
	 * @return Week object. If an error occurs <i>null</i> is returned.
	 */
	private Week getWeek(Calendar c) {
		Week week = null;

		if (c != null) {
			week = new Week();

			c.set(Calendar.WEEK_OF_YEAR, c.get(Calendar.WEEK_OF_YEAR));
			c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			week.setStart(Convert.fromCalendartoLongDate(c));

			c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			week.setEnd(Convert.fromCalendartoLongDate(c));
		}

		return week;
	}

	/**
	 * Called when By Month button has been clicked.
	 */
	private void btnByMonth_onClick() {
		long dates[] = new PaymentTable(this).getAllDistinctDates();
		List<Month> months = null;

		if (dates != null) {
			Month month = null;

			for (int i = 0; i < dates.length; i++) {
				if (months == null) {
					months = new ArrayList<Month>();
					month = getMonth(Convert.fromLongDatetoCalendar(dates[i]));

					months.add(month);
					month = null;
				} else {
					if (dates[i] >= months.get(months.size() - 1).getStart()
							&& dates[i] <= months.get(months.size() - 1)
									.getEnd())
						continue;
					else {
						month = getMonth(Convert
								.fromLongDatetoCalendar(dates[i]));
						months.add(month);
						month = null;
					}
				}
			}

			Utils.setMonthsList(null);
			Utils.setMonthsList(months);

			startReportActivity(getString(R.string.month_Text));
		}
	}

	/**
	 * Function that returns a Month object through a Calendar object.
	 * 
	 * @param c
	 *            Calendar object with the date.
	 * @return Month object. If an error occurs <i>null</i> is returned.
	 */
	private Month getMonth(Calendar c) {
		Month month = null;

		if (c != null) {
			month = new Month(this);

			c.set(Calendar.MONTH, c.get(Calendar.MONTH));
			c.set(Calendar.DAY_OF_MONTH, 1);
			month.setStart(Convert.fromCalendartoLongDate(c));

			c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
			month.setEnd(Convert.fromCalendartoLongDate(c));
		}

		return month;
	}

	/**
	 * Method that starts the Report Activity.
	 * 
	 * @param report_type
	 *            It could be <b>By Day</b>, <b>By Week</b> or <b>By Month</b>.
	 */
	private void startReportActivity(String report_type) {
		Intent i = new Intent(this, ReportsList1.class);
		Bundle extras = new Bundle();
		extras.putString("TYPE_OF_REPORT", report_type);

		i.putExtras(extras);
		startActivity(i);
	}
}
