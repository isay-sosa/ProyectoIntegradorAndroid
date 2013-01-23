package proyecto.integrador.odbyt.Payment;

import java.util.List;

import proyecto.integrador.odbyt.DataBase.SellTable;
import proyecto.integrador.odbyt.ListAdapters.TodayPaymentsAdapter;
import proyecto.integrador.odbyt.Sell.Sell;
import proyecto.integrador.odbyt.Utils.DateTimeManage;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TodayPaymentsList extends ListActivity {
	private ListView mList;
	private List<Sell> sells;
	private TodayPaymentsAdapter mListAdap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		mList = getListView();
		sells = new SellTable(this).getSellsByDate(DateTimeManage
				.getTodayDateAsLong());

		if (sells != null) {
			mListAdap = new TodayPaymentsAdapter(this, sells);
			mList.setAdapter(mListAdap);
		} else {
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					new String[] { "No hay datos" }));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			initComponents();
		}
	}
}
