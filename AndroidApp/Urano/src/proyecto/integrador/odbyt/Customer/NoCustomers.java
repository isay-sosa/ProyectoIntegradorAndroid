package proyecto.integrador.odbyt.Customer;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.CustomerTable;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoCustomers extends Activity {
	private Button btnCreateCustomer;
	private final int CREATE_CUSTOMER_REQUEST_CODE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LookForCustomers();
		setContentView(R.layout.no_customers);
		initComponents();
	}

	/**
	 * Method that looks and gets all the elements on the Customer Table and
	 * starts the correct Activity.
	 */
	private void LookForCustomers() {		
		Utils.setCustomersList(new CustomerTable(this).getAllCustomersOnList());

		if (Utils.getCustomersList() != null) {
			if (Utils.getCustomersList().size() > 0) {
				startActivity(new Intent(this, CustomersList.class));
				finish();
			}
		}
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		btnCreateCustomer = (Button) findViewById(R.id.btnCreateCustomer);
		btnCreateCustomer.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startActivityForResult(new Intent(NoCustomers.this,
						CreateCustomer.class), CREATE_CUSTOMER_REQUEST_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CREATE_CUSTOMER_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			startActivity(new Intent(this, CustomersList.class));
			finish();
		}
	}
}
