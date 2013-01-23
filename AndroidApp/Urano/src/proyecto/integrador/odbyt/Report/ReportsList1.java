package proyecto.integrador.odbyt.Report;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.PaymentTable;
import proyecto.integrador.odbyt.Utils.Convert;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ReportsList1 extends ListActivity implements OnItemClickListener {
	private TextView lblHeaderTitle, lblHeaderSection;
	private ListView mList;
	private String report_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reports_list);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		Bundle extras = getIntent().getExtras();
		report_type = extras.getString("TYPE_OF_REPORT");

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle.setText(getString(R.string.title_activity_reports_by)
				+ " " + report_type);

		lblHeaderSection = (TextView) findViewById(R.id.header_section);
		mList = getListView();

		if (report_type.equals(getString(R.string.day_Text))) {
			lblHeaderSection.setText(getString(R.string.days_Text));
			 mList.setAdapter(new ArrayAdapter<String>(this,
			 android.R.layout.simple_list_item_1, Utils.getDaysList()));

		} else if (report_type.equals(getString(R.string.month_Text))) {
			lblHeaderSection.setText(getString(R.string.months_Text));
			mList.setAdapter(new ArrayAdapter<Month>(this,
					android.R.layout.simple_list_item_1, Utils.getMonthsList()));

		} else if (report_type.equals(getString(R.string.week_Text))) {
			lblHeaderSection.setText(getString(R.string.weeks_Text));
			mList.setAdapter(new ArrayAdapter<Week>(this,
					android.R.layout.simple_list_item_1, Utils.getWeeksList()));
		}

		mList.setOnItemClickListener(this);
	}

	/**
	 * Callback method to be invoked when an item in this AdapterView has been
	 * clicked.
	 * 
	 * @param parent
	 *            The AdapterView where the click happened.
	 * @param view
	 *            The view within the AdapterView that was clicked (this will be
	 *            a view provided by the adapter).
	 * @param position
	 *            The position of the view in the adapter.
	 * @param id
	 *            The row id of the item that was clicked.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Object row = parent.getItemAtPosition(position);

		if (row instanceof String) {
			Utils.setReportssList(null);
			Utils.setReportssList(new PaymentTable(this)
					.getReportsByDay(Convert.fromDateStringtoLong(row
							.toString())));
		} else if (row instanceof Month) {
			Month m = (Month) row;

			Utils.setReportssList(null);
			Utils.setReportssList(new PaymentTable(this).getReportsByRange(
					m.getStart(), m.getEnd()));
		} else if (row instanceof Week) {
			Week w = (Week) row;

			Utils.setReportssList(null);
			Utils.setReportssList(new PaymentTable(this).getReportsByRange(
					w.getStart(), w.getEnd()));
		}

		Intent i = new Intent(this, ReportsList2.class);
		Bundle extras = new Bundle();

		extras.putString("TYPE_OF_REPORT", report_type);
		extras.putString("ROW_KEY", row.toString());
		i.putExtras(extras);

		startActivity(i);
	}
}
