package proyecto.integrador.odbyt.Customer;

import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.ListAdapters.CustomerDetailSellsAdapter;
import proyecto.integrador.odbyt.Sell.Sell;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class CustomerDetailSellsList extends ListActivity implements
		OnItemClickListener {
	private ListView mList;
	private int state;
	private CustomerDetailSellsAdapter mListAdapter;
	private TextView lblHeaderTitle;
	private List<Sell> sells;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_detail_sells_list);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		sells = Utils.getSellsList();

		Bundle extras = getIntent().getExtras();
		state = extras.getInt("STATE");

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle
				.setText((state > 0 ? getString(R.string.title_activity_paying_purachase)
						: getString(R.string.title_activity_finished_purachase)));

		mList = getListView();
		mListAdapter = new CustomerDetailSellsAdapter(this, sells);
		mList.setAdapter(mListAdapter);
		mList.setOnItemClickListener(this);
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
		// Start payments history Activity
		Utils.setSell(null);
		Utils.setSell(sells.get(position));

		Bundle extras = new Bundle();
		extras.putInt("STATE", state);

		Intent i = new Intent(CustomerDetailSellsList.this,
				CustomerPaymentsHistoryList.class);
		i.putExtras(extras);

		startActivity(i);
	}

}
