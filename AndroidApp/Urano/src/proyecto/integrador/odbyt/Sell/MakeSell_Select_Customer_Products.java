package proyecto.integrador.odbyt.Sell;

import java.text.DecimalFormat;
import java.util.ArrayList;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.Customer.Customer;
import proyecto.integrador.odbyt.Product.Product;
import proyecto.integrador.odbyt.Utils.SellData;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MakeSell_Select_Customer_Products extends ListActivity implements
		OnClickListener {
	private Spinner spnCustomer;
	private ListView listProducts;
	private Button btnPositive, btnNegative;
	private TextView lblTotal, lblHeaderTitle;
	DecimalFormat df;
	private double total = 0.0;
	public final int MAKE_SELL_REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sell_select_customer_product);
		initComponents();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (data != null) {
				Bundle extras = data.getExtras();
				boolean isCompleted = extras.getBoolean("COMPLETED");
				if (isCompleted) {
					Toast.makeText(this, getString(R.string.sale_made_message),
							Toast.LENGTH_LONG).show();
					btnNegative_onClick();
				} else {
					Toast.makeText(this, getString(R.string.error_sale_made),
							Toast.LENGTH_LONG).show();
					btnNegative_onClick();
				}
			}
		}
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		df = new DecimalFormat("#.##");

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle.setText(R.string.make_sell_Text);

		spnCustomer = (Spinner) findViewById(R.id.spnCustomers);
		spnCustomer.setAdapter(new ArrayAdapter<Customer>(this,
				android.R.layout.simple_spinner_dropdown_item, Utils
						.getCustomersList()));
		spnCustomer.setOnItemSelectedListener(spinner_item_selected);

		listProducts = getListView();
		listProducts.setChoiceMode(2);
		setListAdapter(new ArrayAdapter<Product>(this,
				android.R.layout.simple_list_item_checked,
				Utils.getProductsList()));

		lblTotal = (TextView) findViewById(R.id.lblTotal);
		lblTotal.setText(lblTotal.getText() + " " + total);

		btnPositive = (Button) findViewById(R.id.btnPositive);
		btnPositive.setText(R.string.btnNext_Text);
		btnPositive.setOnClickListener(this);
		setEnableButton(btnPositive, total > 0.0);

		btnNegative = (Button) findViewById(R.id.btnNegative);
		btnNegative.setText(R.string.btnCancel_Text);
		btnNegative.setOnClickListener(this);
	}

	OnItemSelectedListener spinner_item_selected = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			Utils.setCustomer(null);
			Utils.setCustomer((Customer) parent.getItemAtPosition(position));
		}

		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

	protected void onListItemClick(ListView l, View v, int position, long id) {
		Product p = (Product) l.getItemAtPosition(position);

		if (p.isChecked()) {
			total = Double.valueOf(df.format(total - p.getPrice()).replace(",",
					"."));
			p.setChecked(false);
		} else {
			total = Double.valueOf(df.format(total + p.getPrice()).replace(",",
					"."));
			p.setChecked(true);
		}

		lblTotal.setText(getString(R.string.simbol_$) + " " + total);
		setEnableButton(btnPositive, total > 0.0);
	};

	private void setEnableButton(Button btn, boolean isEnabled) {
		if (isEnabled)
			btn.setTextColor(getResources().getColor(R.color.Black));
		else
			btn.setTextColor(getResources().getColor(R.color.Gray_Footer));

		btn.setEnabled(isEnabled);
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
		SellData.clear_sell_data();

		SellData.selected_customer = Utils.getCustomer();
		SellData.selected_products = new ArrayList<Product>();
		for (int i = 0; i < Utils.getProductsList().size(); i++)
			if (Utils.getProductsList().get(i).isChecked())
				SellData.selected_products.add(Utils.getProductsList().get(i));
		SellData.total = total;

		startActivityForResult(new Intent(this, MakeSell_Select_Date.class),
				MAKE_SELL_REQUEST_CODE);
	}

	/**
	 * Called when the Negative button from the footer has been clicked.
	 */
	private void btnNegative_onClick() {
		SellData.clear_sell_data();

		for (int i = 0; i < Utils.getProductsList().size(); i++)
			Utils.getProductsList().get(i).setChecked(false);

		finish();
	}
}
