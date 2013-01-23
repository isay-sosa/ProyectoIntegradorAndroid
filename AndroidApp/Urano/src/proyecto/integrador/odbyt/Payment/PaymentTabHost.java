package proyecto.integrador.odbyt.Payment;

import proyecto.integrador.odbyt.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;

public class PaymentTabHost extends TabActivity {
	private TextView lblHeaderTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_tabhost);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent(this, TodayPaymentsList.class);
		spec = tabHost.newTabSpec("todayPayments");
		spec.setIndicator(getString(R.string.today_payments));
		spec.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent(this, NextPaymentsList.class);
		spec = tabHost.newTabSpec("nextPayments");
		spec.setIndicator(getString(R.string.next_payments));
		spec.setContent(intent);
		tabHost.addTab(spec);

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle.setText(getString(R.string.title_activity_payments));
	}
}
