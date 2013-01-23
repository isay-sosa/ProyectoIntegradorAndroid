package proyecto.integrador.odbyt.Report;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.ListAdapters.ReportsAdapter;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ReportsList2 extends ListActivity {
	private TextView lblHeaderTitle, lblHeaderSection;

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
		String report_type = extras.getString("TYPE_OF_REPORT");

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle.setText(getString(R.string.title_activity_reports_by)
				+ " " + report_type);

		lblHeaderSection = (TextView) findViewById(R.id.header_section);
		lblHeaderSection.setText(extras.getString("ROW_KEY"));

		setListAdapter(new ReportsAdapter(this, Utils.getReportsList()));
	}
}
