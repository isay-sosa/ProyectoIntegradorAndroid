package proyecto.integrador.odbyt.Customer;

import java.util.ArrayList;
import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.CustomerTable;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateCustomer extends Activity implements OnClickListener {
	private TextView lblHeaderTitle;
	private EditText txtName, txtLastName, txtAddress, txtCity, txtState,
			txtPhone, txtCellphone, txtEmail;
	private Button btnPositive, btnNegative;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_customer);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle
				.setText(getString(R.string.title_activity_create_customer));

		txtName = (EditText) findViewById(R.id.txtName);
		txtLastName = (EditText) findViewById(R.id.txtLastName);
		txtAddress = (EditText) findViewById(R.id.txtAddress);
		txtCity = (EditText) findViewById(R.id.txtCity);
		txtState = (EditText) findViewById(R.id.txtState);
		txtPhone = (EditText) findViewById(R.id.txtPhone);
		txtCellphone = (EditText) findViewById(R.id.txtCellPhone);
		txtEmail = (EditText) findViewById(R.id.txtEmail);

		btnPositive = (Button) findViewById(R.id.btnPositive);
		btnPositive.setText(getString(R.string.btnCreate_Text));
		btnPositive.setOnClickListener(this);

		btnNegative = (Button) findViewById(R.id.btnNegative);
		btnNegative.setText(getString(R.string.btnCancel_Text));
		btnNegative.setOnClickListener(this);
	}

	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.btnNegative:
			btnNegative_onClick();
			break;
		case R.id.btnPositive:
			btnPositive_onClick();
			break;
		}
	}

	/**
	 * Called when the Negative button from the footer has been clicked.
	 */
	private void btnNegative_onClick() {
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * Called when the Positive button from the footer has been clicked.
	 */
	private void btnPositive_onClick() {
		if (txtName.getText().toString().trim().length() > 0
				&& txtLastName.getText().toString().trim().length() > 0
				&& txtAddress.getText().toString().trim().length() > 0
				&& txtCity.getText().toString().trim().length() > 0
				&& txtState.getText().toString().trim().length() > 0) {

			CustomerTable customer = new CustomerTable(this);
			long idInserted = customer.insertCustomer(0, txtName.getText()
					.toString().trim(),
					txtLastName.getText().toString().trim(), txtAddress
							.getText().toString().trim(), txtCity.getText()
							.toString().trim(), txtState.getText().toString()
							.trim(), txtPhone.getText().toString().trim(),
					txtCellphone.getText().toString().trim(), txtEmail
							.getText().toString().trim(), true);
			if (idInserted > 0) {
				Customer c = new Customer();
				c.setId(idInserted);
				c.setName(txtName.getText().toString().trim());
				c.setLastName(txtLastName.getText().toString().trim());
				c.setAddress(txtAddress.getText().toString().trim());
				c.setCity(txtCity.getText().toString().trim());
				c.setState(txtState.getText().toString().trim());
				c.setPhone(txtPhone.getText().toString().trim());
				c.setCellPhone(txtCellphone.getText().toString().trim());
				c.setEmail(txtEmail.getText().toString().trim());

				if (Utils.getCustomersList() == null) {
					List<Customer> customers = new ArrayList<Customer>();
					customers.add(c);
					Utils.setCustomersList(customers);
					customers = null;
				} else {
					Utils.getCustomersList().add(c);
				}

				c = null;

				setResult(RESULT_OK);
				finish();
			} else
				// Error occurred.
				Toast.makeText(this, getString(R.string.error_create_customer),
						Toast.LENGTH_LONG).show();
		} else
			// Fill the * fields.
			Toast.makeText(this,
					getString(R.string.error_fill_requiered_fields),
					Toast.LENGTH_LONG).show();
	}
}
