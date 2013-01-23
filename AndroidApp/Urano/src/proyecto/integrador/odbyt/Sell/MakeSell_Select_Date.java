package proyecto.integrador.odbyt.Sell;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.Utils.SellData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

public class MakeSell_Select_Date extends Activity implements OnClickListener {
	private Spinner spnDays, spnGoToPass;
	private TimePicker tmTime;
	private String[] days, go_to_pass;
	private TextView lblHeaderTitle;
	private Button btnPositive, btnNegative;
	public final int MAKE_SELL_REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sell_select_dates);
		initComponents();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK, data);
			finish();
		}
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle.setText(R.string.make_sell_Text);

		spnDays = (Spinner) findViewById(R.id.spnDayToPass);
		days = getResources().getStringArray(R.array.days_of_the_week);
		spnDays.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, days));
		spnDays.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				SellData.day = position + 1;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		tmTime = (TimePicker) findViewById(R.id.tpTime);
		tmTime.setIs24HourView(true);

		spnGoToPass = (Spinner) findViewById(R.id.spnGoToPass);
		go_to_pass = getResources().getStringArray(
				R.array.how_to_pass_to_charge_the_payment);
		spnGoToPass.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, go_to_pass));
		spnGoToPass.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				SellData.go_to_charge = position + 1;
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		btnPositive = (Button) findViewById(R.id.btnPositive);
		btnPositive.setText(R.string.btnNext_Text);
		btnPositive.setOnClickListener(this);

		btnNegative = (Button) findViewById(R.id.btnNegative);
		btnNegative.setText(R.string.btnGoBack_Text);
		btnNegative.setOnClickListener(this);
	}

	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.btnPositive:
			btnPositive_onClick();
			break;
		case R.id.btnNegative:
			btnNegative_onClick();
			break;
		}
	}

	/**
	 * Called when the Positive button from the footer has been clicked.
	 */
	private void btnPositive_onClick() {
		SellData.time = (tmTime.getCurrentHour() * 100)
				+ tmTime.getCurrentMinute();
		
		SellData.setNextPayment(SellData.day, SellData.go_to_charge);

		startActivityForResult(new Intent(this, MakeSell_Payment_Data.class),
				MAKE_SELL_REQUEST_CODE);
	}

	/**
	 * Called when the Negative button from the footer has been clicked.
	 */
	private void btnNegative_onClick() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
