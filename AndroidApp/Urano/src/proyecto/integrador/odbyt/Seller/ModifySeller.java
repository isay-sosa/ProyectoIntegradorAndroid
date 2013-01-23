package proyecto.integrador.odbyt.Seller;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.SellerTable;
import proyecto.integrador.odbyt.Preferences.OdbytPreferences;
import proyecto.integrador.odbyt.Security.Encrypt;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifySeller extends Activity implements OnClickListener {
	private TextView lblHeaderTitle;
	private EditText txtUsername, txtPassword, txtName, txtLastName;
	private Button btnPositive, btnNegative;
	private OdbytPreferences prefs;

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
		prefs = new OdbytPreferences(this);

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle
				.setText(getString(R.string.title_activity_modify_seller));

		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtUsername.setText(prefs.getUsername());

		txtPassword = (EditText) findViewById(R.id.txtPassword);
		txtPassword.setText(prefs.getPassword());

		txtName = (EditText) findViewById(R.id.txtName);
		txtName.setText(prefs.getName());

		txtLastName = (EditText) findViewById(R.id.txtLastName);
		txtLastName.setText(prefs.getLastName());

		btnPositive = (Button) findViewById(R.id.btnPositive);
		btnPositive.setText(getString(R.string.btnSave_Text));
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
			if (seller
					.updateSellerById(prefs.getId(), prefs.getIdServer(),
							txtUsername.getText().toString(), Encrypt
									.MD5(txtPassword.getText().toString()),
							txtName.getText().toString(), txtLastName.getText()
									.toString(), true)) {

				Toast.makeText(this, "Cuenta actualizada", Toast.LENGTH_LONG)
						.show();
				finish();
			}
		} else
			Toast.makeText(this,
					getString(R.string.error_fill_requiered_fields),
					Toast.LENGTH_LONG).show();
	}
}
