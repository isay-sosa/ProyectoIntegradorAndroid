package proyecto.integrador.odbyt;

import proyecto.integrador.odbyt.DataBase.SellerTable;
import proyecto.integrador.odbyt.Preferences.OdbytPreferences;
import proyecto.integrador.odbyt.Seller.CreateSeller;
import proyecto.integrador.odbyt.Sync.Sync;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {
	private EditText txtUsername, txtPassword;
	private TextView lblCreateAccount, lblImportData;
	private Button btnLogin;
	private final int CREATE_SELLER_REQUEST_CODE = 10;
	private ProgressDialog pd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);
		lblCreateAccount = (TextView) findViewById(R.id.lblCreateAccount);
		lblCreateAccount.setOnClickListener(this);
		lblImportData = (TextView) findViewById(R.id.lblImportData);
		lblImportData.setOnClickListener(this);
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnLogin:
			btnLogin_onClick();
			break;
		case R.id.lblCreateAccount:
			lblCreateAccount_onClick();
			break;
		case R.id.lblImportData:
			lblImportData_onClick();
			break;
		}
	}

	/**
	 * Called when the Login button has been clicked.
	 */
	private void btnLogin_onClick() {
		SellerTable seller = new SellerTable(this);

		if (seller.LogIn(txtUsername.getText().toString(), txtPassword
				.getText().toString())) {
			// Save username and password to a SharePreference file
			OdbytPreferences prefs = new OdbytPreferences(this);
			prefs.setUsername(txtUsername.getText().toString());
			prefs.setPassword(txtPassword.getText().toString());

			startActivity(new Intent(this, Home.class));
			finish();
		} else
			Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Called when the Create Account label has been clicked.
	 */
	private void lblCreateAccount_onClick() {
		startActivityForResult(new Intent(this, CreateSeller.class),
				CREATE_SELLER_REQUEST_CODE);
	}

	/**
	 * Called when the Import Data label has been clicked.
	 */
	private void lblImportData_onClick() {
		pd = new ProgressDialog(this);
		pd.setTitle("Importando los datos...");
		pd.setMessage("Por favor espere.");
		pd.show();
		new AsyncSyncTask(this).execute((Void[]) null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CREATE_SELLER_REQUEST_CODE
				&& resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			txtUsername.setText(extras.getString("USERNAME_KEY"));
			txtPassword.setText(extras.getString("PASSWORD_KEY"));

			btnLogin_onClick();
			extras = null;
		}
	}

	/**
	 * Asynchronous class which makes the synchronization in background.
	 * 
	 * @author Isay
	 * 
	 */
	class AsyncSyncTask extends AsyncTask<Void, Integer, Void> {
		private Sync sync;
		private Context context;

		public AsyncSyncTask(Context context) {
			sync = new Sync();
			this.context = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			sync.initialTableObjects(context);
			sync.import_db_from_server(context);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pd.dismiss();
		}
	}
}