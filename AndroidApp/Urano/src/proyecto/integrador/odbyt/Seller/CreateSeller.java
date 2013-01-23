package proyecto.integrador.odbyt.Seller;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.SellerTable;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateSeller extends Activity implements OnClickListener {
	private TextView lblHeaderTitle;
	private EditText txtUsername, txtPassword, txtName, txtLastName;
	private Button btnPositive, btnNegative;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_seller);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle
				.setText(getString(R.string.title_activity_create_seller));

		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtName = (EditText) findViewById(R.id.txtName);
		txtLastName = (EditText) findViewById(R.id.txtLastName);

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
		if (txtUsername.getText().toString().trim().length() > 0
				&& txtPassword.getText().toString().trim().length() > 0
				&& txtName.getText().toString().trim().length() > 0
				&& txtLastName.getText().toString().trim().length() > 0) {

			SellerTable seller = new SellerTable(this);
			if (seller.insertSeller(0, txtUsername.getText().toString(),
					txtPassword.getText().toString(), txtName.getText()
							.toString(), txtLastName.getText().toString(), true) > 0) {

				Intent data = new Intent();
				Bundle extras = new Bundle();

				extras.putString("USERNAME_KEY", txtUsername.getText()
						.toString().trim());
				extras.putString("PASSWORD_KEY", txtPassword.getText()
						.toString().trim());

				data.putExtras(extras);
				setResult(RESULT_OK, data);

				extras = null;
				data = null;
				finish();
			}
		} else
			Toast.makeText(this,
					getString(R.string.error_fill_requiered_fields),
					Toast.LENGTH_LONG).show();
	}
}
