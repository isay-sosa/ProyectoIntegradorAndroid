package proyecto.integrador.odbyt.Product;

import proyecto.integrador.odbyt.DataBase.SellTable;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class CustomersPayingList extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		String[] customers = new SellTable(this)
				.getCustomerByProductIdAndState(Utils.getProduct().getId(), 1);

		if (customers == null)
			customers = new String[] { "No hay datos" };

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, customers));
	}
}
