package proyecto.integrador.odbyt.Payment;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.SellTable;
import proyecto.integrador.odbyt.ListAdapters.NextPaymentsAdapter;
import proyecto.integrador.odbyt.Sell.Sell;
import proyecto.integrador.odbyt.Utils.Convert;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NextPaymentsList extends ListActivity {
	private NextPaymentsAdapter mListAdap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		mListAdap = new NextPaymentsAdapter(this) {

			protected View getHeaderView(String caption, int index,
					View convertView, ViewGroup parent) {
				TextView header = (TextView) convertView;

				if (header == null)
					header = (TextView) getLayoutInflater().inflate(
							R.layout.header_section, null);

				header.setText(caption);

				return header;
			}
		};

		SellTable sell = new SellTable(this);
		String[] dates = sell.getNextPaymentsDates();

		if (dates != null) {
			for (int i = 0; i < dates.length; i++) {
				mListAdap.addSection(
						dates[i],
						new ArrayAdapter<Sell>(this,
								R.layout.today_payments_row,
								sell.getSellsByDate(Convert
										.fromDateStringtoLong(dates[i]))));
			}
			
			setListAdapter(mListAdap);
		} else {
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					new String[] { "No hay datos" }));
		}
	}

}
