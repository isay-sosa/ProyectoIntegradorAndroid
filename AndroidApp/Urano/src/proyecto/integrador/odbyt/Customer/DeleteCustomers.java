package proyecto.integrador.odbyt.Customer;

import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.CustomerTable;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteCustomers extends ListActivity implements OnClickListener,
		android.content.DialogInterface.OnClickListener {
	private TextView lblHeaderTitle;
	private ListView mList;
	private List<Customer> customers;
	private CheckBox chkboxAllItems;
	private Button btnPositive, btnNegative;
	private static int select_item_count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_selected_items);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		customers = Utils.getCustomersList();

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle
				.setText(getString(R.string.title_activity_delete_customers));

		mList = getListView();
		mList.setChoiceMode(2); // Multi choice
		setListAdapter(new ArrayAdapter<Customer>(this,
				android.R.layout.simple_list_item_checked, customers));

		chkboxAllItems = (CheckBox) findViewById(R.id.select_all_items);
		chkboxAllItems.setOnClickListener(this);

		btnPositive = (Button) findViewById(R.id.btnPositive);
		btnPositive.setText(getString(R.string.btnDelete_Text));
		btnPositive.setOnClickListener(this);
		setEnableButton(btnPositive, select_item_count > 0);

		btnNegative = (Button) findViewById(R.id.btnNegative);
		btnNegative.setText(getString(R.string.btnCancel_Text));
		btnNegative.setOnClickListener(this);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		boolean isChecked = l.isItemChecked(position);

		customers.get(position).setChecked(isChecked);
		if (isChecked)
			select_item_count++;
		else
			select_item_count--;

		chkboxAllItems.setChecked(select_item_count == customers.size());
		setEnableButton(btnPositive, select_item_count > 0);
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.select_all_items:
			chkboxAllItems_onClick();
			break;
		case R.id.btnNegative:
			btnNegative_onClick();
			break;
		case R.id.btnPositive:
			btnPositive_onClick();
			break;
		}
	}

	/**
	 * Called when All Items (CheckBox) has been clicked.
	 */
	private void chkboxAllItems_onClick() {
		boolean isChecked = chkboxAllItems.isChecked();

		for (int i = 0; i < customers.size(); i++) {
			mList.setItemChecked(i, isChecked);
			customers.get(i).setChecked(isChecked);
		}

		if (isChecked)
			select_item_count = customers.size();
		else
			select_item_count = 0;

		setEnableButton(btnPositive, select_item_count > 0);
	}

	/**
	 * Called when the Negative button from the footer has been clicked.
	 */
	private void btnNegative_onClick() {
		for (int i = 0; i < customers.size(); i++)
			customers.get(i).setChecked(false);

		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * Called when the Positive button from the footer has been clicked.
	 */
	private void btnPositive_onClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.delete_selected_customers_title);
		builder.setMessage(R.string.delete_selected_customers_message);
		builder.setPositiveButton(android.R.string.yes, this);
		builder.setNegativeButton(android.R.string.no, this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);

		builder.create().show();
	}

	public void onClick(DialogInterface dialog, int which) {
		if (which == -1) {
			CustomerTable customer = new CustomerTable(this);

			for (int i = customers.size() - 1; i > -1; i--) {
				if (customers.get(i).isChecked()) {
					if (customer.deleteCustomerById(customers.get(i).getId(),
							true)) {
						customers.remove(i);
					} else {
						Toast.makeText(
								this,
								getString(R.string.error_deleting_selected_customers),
								Toast.LENGTH_LONG).show();
						break;
					}
				}
			}

			setResult(RESULT_OK);
			finish();
		}
	}

	private void setEnableButton(Button btn, boolean isEnabled) {
		if (isEnabled)
			btn.setTextColor(getResources().getColor(R.color.Black));
		else
			btn.setTextColor(getResources().getColor(R.color.Gray_Footer));

		btn.setEnabled(isEnabled);
	}
}
