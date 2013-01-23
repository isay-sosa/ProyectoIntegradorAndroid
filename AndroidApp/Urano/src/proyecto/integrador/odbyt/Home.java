package proyecto.integrador.odbyt;

import proyecto.integrador.odbyt.Customer.NoCustomers;
import proyecto.integrador.odbyt.Payment.PaymentTabHost;
import proyecto.integrador.odbyt.Preferences.OdbytPreferences;
import proyecto.integrador.odbyt.Product.NoProducts;
import proyecto.integrador.odbyt.Report.SelectReportType;
import proyecto.integrador.odbyt.Seller.ModifySeller;
import proyecto.integrador.odbyt.Sync.Sync;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Home extends Activity implements OnClickListener {
	private ImageView imgCustomers, imgProducts, imgPayments, imgReports,
			imgSync;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		initComponents();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.account, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_updateInfoAccount:
			updateInfoAccountMenu_onClick();
			return true;
		case R.id.menu_closeSession:
			closeSessionMenu_onClick();
			return true;
		}

		return false;
	}

	/**
	 * Called when <b>Update Info Account</b> option from the menu has been
	 * clicked.
	 */
	private void updateInfoAccountMenu_onClick() {
		startActivity(new Intent(this, ModifySeller.class));
	}

	/**
	 * Called when <b>Close Session</b> option from the menu has been clicked.
	 */
	private void closeSessionMenu_onClick() {
		OdbytPreferences prefs = new OdbytPreferences(this);
		prefs.setId(0);
		prefs.setUsername("");
		prefs.setPassword("");
		prefs.setName("");
		prefs.setLastName("");

		finish();
		startActivity(new Intent(this, Login.class));
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		imgCustomers = (ImageView) findViewById(R.id.imgCustomers);
		imgCustomers.setOnClickListener(this);

		imgProducts = (ImageView) findViewById(R.id.imgProducts);
		imgProducts.setOnClickListener(this);

		imgPayments = (ImageView) findViewById(R.id.imgPayments);
		imgPayments.setOnClickListener(this);

		imgReports = (ImageView) findViewById(R.id.imgReports);
		imgReports.setOnClickListener(this);

		imgSync = (ImageView) findViewById(R.id.imgSync);
		imgSync.setOnClickListener(this);
	}

	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.imgCustomers:
			imgCustomers_onClick();
			break;

		case R.id.imgProducts:
			imgProducts_onClick();
			break;

		case R.id.imgPayments:
			imgPayments_onClick();
			break;

		case R.id.imgReports:
			imgReports_onClick();
			break;

		case R.id.imgSync:
			imgSync_onClick();
			break;
		}
	}

	/**
	 * Called when the Customer's Image has been clicked.
	 */
	private void imgCustomers_onClick() {
		startActivity(new Intent(this, NoCustomers.class));
	}

	/**
	 * Called when the Product's Image has been clicked.
	 */
	private void imgProducts_onClick() {
		startActivity(new Intent(this, NoProducts.class));
	}

	/**
	 * Called when the Payment's Image has been clicked.
	 */
	private void imgPayments_onClick() {
		startActivity(new Intent(this, PaymentTabHost.class));
	}

	/**
	 * Called when the Report's Image has been clicked.
	 */
	private void imgReports_onClick() {
		startActivity(new Intent(this, SelectReportType.class));
	}

	/**
	 * Called when the Sync's Image has been clicked.
	 */
	private void imgSync_onClick() {
		pd = new ProgressDialog(this);
		pd.setTitle("Sincronizando...");
		pd.setMessage("Esto puede tardar algunos minutos, por favor espere.");
		pd.show();
		new AsyncSyncTask(this).execute((Void[]) null);
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
			sync.export_db_from_local(context);
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
