package proyecto.integrador.odbyt.ListAdapters;

import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.Payment.MakePayment;
import proyecto.integrador.odbyt.Sell.Sell;
import proyecto.integrador.odbyt.Utils.Convert;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class TodayPaymentsAdapter extends ArrayAdapter<Sell> {
	private final LayoutInflater inflater;
	private final Context context;
	private boolean colorRow;
	private static final int MAKE_PAYMENT_CODE_REQUEST = 100;

	static class ViewHolder {
		public TextView txtCustomer, txtDate, txtPayment;
		public Button btnMakePayment;
	}

	public TodayPaymentsAdapter(Context context, List<Sell> sells) {
		super(context, R.layout.today_payments_row, sells);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder viewHolder;

		if (row == null) {
			row = inflater.inflate(R.layout.today_payments_row, null);
			viewHolder = new ViewHolder();

			viewHolder.txtCustomer = (TextView) row
					.findViewById(R.id.txtCustomer);
			viewHolder.txtDate = (TextView) row.findViewById(R.id.txtDate);
			viewHolder.txtPayment = (TextView) row
					.findViewById(R.id.txtPayment);
			viewHolder.btnMakePayment = (Button) row
					.findViewById(R.id.btnMakePayment);

			viewHolder.btnMakePayment
					.setOnClickListener(new View.OnClickListener() {

						public void onClick(View v) {
							Utils.setSell(null);
							Utils.setSell((Sell) getItem(position));

							Activity ac = (Activity) context;
							ac.startActivityForResult(
									new Intent(ac.getBaseContext(),
											MakePayment.class),
									MAKE_PAYMENT_CODE_REQUEST);
						}
					});

			row.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) row.getTag();

		Sell s = (Sell) getItem(position);
		
		viewHolder.txtCustomer.setText(s.getCustomerName());
		viewHolder.txtDate.setText(Convert.fromLongtoDateString(s
				.getNextPayment()) + "\n" + s.getHourToCharge());
		viewHolder.txtPayment.setText(context.getString(R.string.simbol_$)
				+ s.getAgreedPayment());

		if (s.getColor() == 0) {
			if (colorRow) {
				row.setBackgroundColor(context.getResources().getColor(
						R.color.LightCyan));
				colorRow = false;
				s.setColor(2);
			} else {
				row.setBackgroundColor(context.getResources().getColor(
						R.color.White));
				colorRow = true;
				s.setColor(1);
			}
		} else if (s.getColor() == 1) {
			row.setBackgroundColor(context.getResources().getColor(
					R.color.White));
			colorRow = true;
		} else if (s.getColor() == 2) {
			row.setBackgroundColor(context.getResources().getColor(
					R.color.LightCyan));
			colorRow = false;
		}

		return row;
	}
}
