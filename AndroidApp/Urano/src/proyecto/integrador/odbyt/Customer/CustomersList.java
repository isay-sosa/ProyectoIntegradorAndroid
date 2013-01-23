package proyecto.integrador.odbyt.Customer;

import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.CustomerTable;
import proyecto.integrador.odbyt.DataBase.ProductTable;
import proyecto.integrador.odbyt.ListAdapters.CustomerAdapter;
import proyecto.integrador.odbyt.Sell.MakeSell_Select_Customer_Products;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author  Isay
 */
public class CustomersList extends ListActivity implements OnItemClickListener,
		OnClickListener, View.OnClickListener {
	private ListView mList;
	private AutoCompleteTextView txtSearch;
	private CustomerAdapter mListAdapter, mSearchAdapter;
	private ImageView btnSearch, btnCloseSearch;
	private TextView lblMakeSell;
	private List<Customer> customers;
	private int mListIndex = -1;
	private View header1, header2;
	private final int CREATE_CUSTOMER_REQUEST_CODE = 20;
	private final int UPDATE_CUSTOMER_REQUEST_CODE = 30;
	private final int DELETE_CUSTOMERS_REQUEST_CODE = 40;
	private final int CUSTOMER_DETAILS_REQUEST_CODE = 50;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customers_list);
		initComponents();
	}

	@Override
	protected void onResume() {
		super.onResume();
		txtSearch.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.customer, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case R.id.menu_addCustomer:
			addCustomerMenu_onClick();
			return true;
		case R.id.menu_deleteCustomers:
			deleteCustomersMenu_onClick();
			return true;
		}

		return false;
	}

	/**
	 * Called when <b>Add Customer</b> option from the menu has been clicked.
	 */
	private void addCustomerMenu_onClick() {
		startActivityForResult(new Intent(this, CreateCustomer.class),
				CREATE_CUSTOMER_REQUEST_CODE);
	}

	/**
	 * Called when <b>Delete Customers</b> option from the menu has been clicked.
	 */
	private void deleteCustomersMenu_onClick() {
		startActivityForResult(new Intent(this, DeleteCustomers.class),
				DELETE_CUSTOMERS_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK)
			if (customers.size() == 0) {
				startActivity(new Intent(this, NoCustomers.class));
				finish();
			} else {
				mListAdapter.notifyDataSetChanged();
				updateSearchAdapter();
			}
	}

	/**
	 * Method that updates the Adapter of the Search input.
	 */
	private void updateSearchAdapter() {
		mSearchAdapter = null;
		mSearchAdapter = new CustomerAdapter(this, customers);
		txtSearch.setAdapter(mSearchAdapter);
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		header1 = findViewById(R.id.header1);

		lblMakeSell = (TextView) findViewById(R.id.lblMakeSell);
		lblMakeSell.setOnClickListener(this);

		btnSearch = (ImageView) findViewById(R.id.imgSearch);
		btnSearch.setOnClickListener(this);

		btnCloseSearch = (ImageView) findViewById(R.id.imgCloseSearch);
		btnCloseSearch.setOnClickListener(this);

		header2 = findViewById(R.id.header2);
		header2.setVisibility(8);

		customers = Utils.getCustomersList();
		mList = getListView();
		mListAdapter = new CustomerAdapter(this, customers);
		mList.setAdapter(mListAdapter);
		mList.setOnItemClickListener(this);
		mList.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				return mList_onLongClick(position);
			}
		});

		txtSearch = (AutoCompleteTextView) findViewById(R.id.search_box);
		txtSearch.setHint(R.string.txtSearchCustomers_Hint);
		txtSearch.setThreshold(1);
		mSearchAdapter = new CustomerAdapter(this, customers);
		txtSearch.setAdapter(mSearchAdapter);
		txtSearch.setOnItemClickListener(this);
	}

	/**
	 * Called when an item from the ListView has been long clicked.
	 */
	private boolean mList_onLongClick(final int position) {
		mListIndex = position;

		String title = customers.get(position).toString();
		String[] options = getResources().getStringArray(
				R.array.customers_list_on_item_long_click_dialog);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setItems(options, new OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case 0:
					// Delete
					DeleteOption_onClick();
					break;
				case 1:
					// Modify
					ModifyOption_onClick();
					break;
				}
			}
		});

		builder.create().show();
		return true;
	}

	/**
	 * Called when <b>Delete</b> <i>Customer</i> has been clicked.
	 */
	private void DeleteOption_onClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.delete_sigle_customer_title);
		builder.setMessage(R.string.delete_single_customer_message);
		builder.setIcon(android.R.drawable.ic_dialog_alert);

		builder.setPositiveButton(android.R.string.yes, this);
		builder.setNegativeButton(android.R.string.no, this);

		builder.create().show();
	}

	/**
	 * Called when <b>Modify</b> <i>Customer</i> has been clicked.
	 */
	private void ModifyOption_onClick() {
		Utils.setCustomer(customers.get(mListIndex));
		startActivityForResult(new Intent(this, ModifyCustomer.class),
				UPDATE_CUSTOMER_REQUEST_CODE);
	}

	/**
	 * Callback method to be invoked when an item in this AdapterView has been
	 * clicked.
	 * 
	 * @param parent
	 *            The AdapterView where the click happened.
	 * @param view
	 *            The view within the AdapterView that was clicked (this will be
	 *            a view provided by the adapter).
	 * @param position
	 *            The position of the view in the adapter.
	 * @param id
	 *            The row id of the item that was clicked.
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Start customer detail activity
		Customer c = (Customer) parent.getItemAtPosition(position);
		Utils.setCustomer(null);
		Utils.setCustomer(c);
		startActivityForResult(new Intent(this, CustomerDetail.class),
				CUSTOMER_DETAILS_REQUEST_CODE);
	}

	public void onClick(DialogInterface dialog, int which) {
		// -1 = positive
		// -2 = negative
		if (which == -1) {
			CustomerTable customer = new CustomerTable(this);

			if (customer.deleteCustomerById(customers.get(mListIndex).getId(),
					true)) {
				customers.remove(mListIndex);

				if (customers.size() == 0) {
					startActivity(new Intent(this, NoCustomers.class));
					finish();
				}

				mListAdapter.notifyDataSetChanged();
				updateSearchAdapter();
			} else
				Toast.makeText(this,
						getString(R.string.error_deleting_single_customer),
						Toast.LENGTH_LONG).show();

			customer = null;
		}
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.lblMakeSell:
			lblMakeSell_onClick();
			break;
		case R.id.imgSearch:
			btnSearch_onClick();
			break;
		case R.id.imgCloseSearch:
			btnCloseSearch_onClick();
			break;
		}
	}

	/**
	 * Called when <b>Make Sell</b> from the navigation bar has been clicked.
	 */
	private void lblMakeSell_onClick() {
		if (Utils.getProductsList() == null) {
			Utils.setProductsList(new ProductTable(this).getAllProductsOnList());

			if (Utils.getProductsList() == null)
				// Don't start MakeSell Activity
				Toast.makeText(this, "", Toast.LENGTH_LONG).show();
			else
				// Start MakeSell Activity
				startActivity(new Intent(this,
						MakeSell_Select_Customer_Products.class));
		} else
			// Start MakeSell Activity
			startActivity(new Intent(this,
					MakeSell_Select_Customer_Products.class));
	}

	/**
	 * Called when the Search Image from the navigation bar has been clicked.
	 */
	private void btnSearch_onClick() {
		header1.setVisibility(8);
		header2.setVisibility(0);
		txtSearch.requestFocus();
	}

	/**
	 * Called when the Close Search Image from the navigation bar has been clicked.
	 */
	private void btnCloseSearch_onClick() {
		txtSearch.setText("");
		header2.setVisibility(8);
		header1.setVisibility(0);
	}
}
