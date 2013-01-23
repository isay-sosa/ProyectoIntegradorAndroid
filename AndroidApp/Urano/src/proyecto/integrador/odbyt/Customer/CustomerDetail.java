package proyecto.integrador.odbyt.Customer;

import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.CustomerTable;
import proyecto.integrador.odbyt.DataBase.SellProductTable;
import proyecto.integrador.odbyt.DataBase.SellTable;
import proyecto.integrador.odbyt.Sell.Sell;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerDetail extends Activity implements OnClickListener,
		android.content.DialogInterface.OnClickListener {
	private TextView lblCustomerName, lblCustomerAddress, lblCustomerPhone,
			lblCustomerCellPhone, lblCustomerEmail, lblPurachaseState,
			lblPurachaseDate, lblPurachaseTotal, lblHeaderTitle;
	private Button btnModify, btnDelete, btnPaying, btnFinished;
	private LinearLayout parentProducts;
	private LayoutParams params;
	private Customer customer;
	private final int UPDATE_CUSTOMER_REQUEST_CODE = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_detail);
		initComponents();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			setCustomerInfo();
			setResult(RESULT_OK);
		}
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		customer = Utils.getCustomer();

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle.setText(R.string.title_activity_customer_details);

		lblCustomerName = (TextView) findViewById(R.id.lblCustomerName);
		lblCustomerAddress = (TextView) findViewById(R.id.lblCustomerAddress);
		lblCustomerPhone = (TextView) findViewById(R.id.lblCustomerPhone);
		lblCustomerCellPhone = (TextView) findViewById(R.id.lblCustomerCellPhone);
		lblCustomerEmail = (TextView) findViewById(R.id.lblCustomerEmail);

		btnModify = (Button) findViewById(R.id.btnModify);
		btnModify.setOnClickListener(this);

		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);

		parentProducts = (LinearLayout) findViewById(R.id.parentProducts);
		params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);

		lblPurachaseDate = (TextView) findViewById(R.id.lblDate);
		lblPurachaseState = (TextView) findViewById(R.id.lblState);
		lblPurachaseTotal = (TextView) findViewById(R.id.lblTotal);

		btnPaying = (Button) findViewById(R.id.btnPaying);
		btnPaying.setOnClickListener(this);

		btnFinished = (Button) findViewById(R.id.btnFinished);
		btnFinished.setOnClickListener(this);

		setCustomerInfo();
		setPurchaseInfo();
	}

	/**
	 * Method that sets the Customer Information.
	 */
	private void setCustomerInfo() {
		if (customer != null) {
			lblCustomerName.setText(customer.toString());
			lblCustomerAddress.setText(customer.getAddress() + ".\n"
					+ customer.getCity() + ", " + customer.getState());
			lblCustomerPhone.setText(customer.getPhone());
			lblCustomerCellPhone.setText(customer.getCellPhone());
			lblCustomerEmail.setText(customer.getEmail());
		}
	}

	/**
	 * Method that sets the Last Purchase Information of the Customer.
	 */
	private void setPurchaseInfo() {
		Sell sell_info = new SellTable(this).getLastSellByCustomerId(customer
				.getId());

		if (sell_info != null) {
			SellProductTable sp = new SellProductTable(this);
			List<String> products = sp.getProductsBySellId(sell_info.getId());

			if (products != null) {
				sell_info.setProducts(products);

				for (int i = 0; i < products.size(); i++)
					addProductToParentProducts(products.get(i));
			}

			lblPurachaseTotal.setText(getString(R.string.simbol_$) + " "
					+ sell_info.getTotalPayment());
			lblPurachaseState.setText(sell_info.getState());
			lblPurachaseDate.setText(sell_info.getDate() + "\n"
					+ sell_info.getTime());
		} else {
			addProductToParentProducts("S/N");
			lblPurachaseDate.setText("S/N");
			lblPurachaseState.setText("S/N");
			lblPurachaseTotal.setText(getString(R.string.simbol_$) + " 0.0");
		}
	}

	private void addProductToParentProducts(String productName) {
		TextView product = new TextView(this);

		product.setLayoutParams(params);
		product.setText(productName);
		product.setTextColor(getResources().getColor(R.color.Gray_Footer));

		parentProducts.addView(product);
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnModify:
			btnModify_onClick();
			break;
		case R.id.btnDelete:
			btnDelete_onClick();
			break;
		case R.id.btnPaying:
			btnPaying_onClick();
			break;
		case R.id.btnFinished:
			btnFinished_onClick();
			break;
		}
	}

	/**
	 * Called when the Modify button from the Customer Info has been clicked.
	 */
	private void btnModify_onClick() {
		startActivityForResult(new Intent(this, ModifyCustomer.class),
				UPDATE_CUSTOMER_REQUEST_CODE);
	}

	/**
	 * Called when the Delete button from the Customer Info has been clicked.
	 */
	private void btnDelete_onClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.delete_sigle_customer_title);
		builder.setMessage(R.string.delete_single_customer_message);
		builder.setIcon(android.R.drawable.ic_dialog_alert);

		builder.setPositiveButton(android.R.string.yes, this);
		builder.setNegativeButton(android.R.string.no, this);

		builder.create().show();
	}

	public void onClick(DialogInterface dialog, int which) {
		// -1 = positive
		// -2 = negative
		if (which == -1) {
			CustomerTable c = new CustomerTable(this);

			if (c.deleteCustomerById(customer.getId(), true)) {
				Utils.getCustomersList().remove(customer);
				setResult(RESULT_OK);
				finish();

			} else
				Toast.makeText(this,
						getString(R.string.error_deleting_single_customer),
						Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Called when the Paying button from the Last Purchase Info has been clicked.
	 */
	private void btnPaying_onClick() {
		SellTable sell = new SellTable(this);
		Utils.setSellsList(null);
		Utils.setSellsList(sell.getSellsByCustomerIdAndState(customer.getId(),
				1));

		if (Utils.getSellsList() != null) {
			Bundle extras = new Bundle();
			extras.putInt("STATE", 1);

			Intent i = new Intent(this, CustomerDetailSellsList.class);
			i.putExtras(extras);

			startActivity(i);
		}
	}

	/**
	 * Called when the Finished button from the Last Purchase Info has been clicked.
	 */
	private void btnFinished_onClick() {
		SellTable sell = new SellTable(this);
		Utils.setSellsList(null);
		Utils.setSellsList(sell.getSellsByCustomerIdAndState(customer.getId(),
				0));

		if (Utils.getSellsList() != null) {
			Bundle extras = new Bundle();
			extras.putInt("STATE", 0);

			Intent i = new Intent(this, CustomerDetailSellsList.class);
			i.putExtras(extras);

			startActivity(i);
		}
	}

}
