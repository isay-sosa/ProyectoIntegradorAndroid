package proyecto.integrador.odbyt.ListAdapters;

import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.Payment.Payment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomerPaymentsHistoryAdapter extends ArrayAdapter<Payment> {
	private final LayoutInflater inflater;
	private final Context context;
	private boolean colorRow;

	static class ViewHolder {
		TextView lblDate;
		TextView lblPayment;
	}

	public CustomerPaymentsHistoryAdapter(Context context,
			List<Payment> payments) {
		super(context, R.layout.payments_history_row, payments);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder viewHolder;

		if (row == null) {
			row = inflater.inflate(R.layout.payments_history_row, null);
			viewHolder = new ViewHolder();

			viewHolder.lblDate = (TextView) row.findViewById(R.id.lblDate);
			viewHolder.lblPayment = (TextView) row
					.findViewById(R.id.lblPayment);

			row.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) row.getTag();

		Payment p = (Payment) getItem(position);

		viewHolder.lblDate.setText(p.getDate() + "\n" + p.getTime());
		viewHolder.lblPayment.setText(context.getString(R.string.simbol_$)
				+ p.getPayment());

		if (p.getColor() == 0) {
			if (colorRow) {
				row.setBackgroundColor(context.getResources().getColor(
						R.color.LightCyan));
				colorRow = false;
				p.setColor(2);
			} else {
				row.setBackgroundColor(context.getResources().getColor(
						R.color.White));
				colorRow = true;
				p.setColor(1);
			}
		} else if (p.getColor() == 1) {
			row.setBackgroundColor(context.getResources().getColor(
					R.color.White));
			colorRow = true;
		} else if (p.getColor() == 2) {
			row.setBackgroundColor(context.getResources().getColor(
					R.color.LightCyan));
			colorRow = false;
		}

		return row;
	}
}
