package proyecto.integrador.odbyt.Customer;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.ListAdapters.CustomerPaymentsHistoryAdapter;
import proyecto.integrador.odbyt.Sell.Sell;
import proyecto.integrador.odbyt.Utils.*;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class CustomerPaymentsHistoryList extends ListActivity {
	private ListView mList;
	private CustomerPaymentsHistoryAdapter mListAdapter;
	private View nextPayment;
	private TextView lblHeaderTitle, lblNextPaymentDate, lblNextPaymentNumber;
	private Sell sell;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_detail_sells_payments_list);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		sell = Utils.getSell();

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle.setText(getString(R.string.title_activity_payments));

		nextPayment = (View) findViewById(R.id.next_payment_info);
		lblNextPaymentDate = (TextView) findViewById(R.id.lblNextPaymentDate);
		lblNextPaymentDate.setText(Convert.fromLongtoDateString(sell
				.getNextPayment())
				+ " ("
				+ sell.getChargeType()
				+ ")\n"
				+ sell.getHourToCharge());

		lblNextPaymentNumber = (TextView) findViewById(R.id.lblNextPaymentNumber);
		lblNextPaymentNumber.setText(getString(R.string.simbol_$)
				+ sell.getAgreedPayment());

		mList = getListView();
		mListAdapter = new CustomerPaymentsHistoryAdapter(this,
				sell.getPayments());
		mList.setAdapter(mListAdapter);
		
		Bundle extras = getIntent().getExtras();
		if (extras.getInt("STATE") == 0)
			nextPayment.setVisibility(8);
		else
			nextPayment.setVisibility(0);
	}
}
