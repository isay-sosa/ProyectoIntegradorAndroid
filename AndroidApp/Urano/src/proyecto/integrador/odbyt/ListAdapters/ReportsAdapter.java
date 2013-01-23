package proyecto.integrador.odbyt.ListAdapters;

import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.Report.Report;
import proyecto.integrador.odbyt.Utils.Convert;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReportsAdapter extends ArrayAdapter<Report> {
	private final LayoutInflater inflater;
	private final Context context;
	private boolean colorRow;

	public ReportsAdapter(Context context, List<Report> report) {
		super(context, R.layout.report_row, report);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder viewHolder = null;

		if (row == null) {
			row = inflater.inflate(R.layout.report_row, null);
			viewHolder = new ViewHolder();

			viewHolder.lblCustomer = (TextView) row
					.findViewById(R.id.lblCustomer);
			viewHolder.lblAddress = (TextView) row
					.findViewById(R.id.lblAddress);
			viewHolder.parentProducts = (LinearLayout) row
					.findViewById(R.id.parentProducts);
			viewHolder.lblDate = (TextView) row.findViewById(R.id.lblDate);
			viewHolder.lblPayment = (TextView) row
					.findViewById(R.id.lblPayment);
			viewHolder.lblTotal = (TextView) row.findViewById(R.id.lblTotal);
			viewHolder.lblPayed = (TextView) row.findViewById(R.id.lblPayed);
			viewHolder.lblRemaining = (TextView) row
					.findViewById(R.id.lblRemaining);

			row.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) row.getTag();

		Report rep = (Report) getItem(position);

		viewHolder.lblCustomer.setText(rep.getCustomer().toString());
		viewHolder.lblAddress.setText(rep.getCustomer().getAddress() + "\n"
				+ rep.getCustomer().getCity() + ", "
				+ rep.getCustomer().getState());
		viewHolder.lblDate.setText(Convert.fromLongtoDateString(rep.getDate())
				+ "\n" + Convert.fromLongtoTimeString(rep.getTime()));

		viewHolder.parentProducts.removeAllViews();
		for (int i = 0; i < rep.getProducts().size(); i++)
			addProductToParentProducts(rep.getProducts().get(i),
					viewHolder.parentProducts);

		viewHolder.lblPayment.setText(context.getString(R.string.simbol_$)
				+ rep.getPayment());
		viewHolder.lblTotal.setText(context.getString(R.string.simbol_$)
				+ rep.getTotal());
		viewHolder.lblPayed.setText(context.getString(R.string.simbol_$)
				+ rep.getPayed());
		viewHolder.lblRemaining.setText(context.getString(R.string.simbol_$)
				+ (rep.getTotal() - rep.getPayed()));

		if (rep.getColor() == 0) {
			if (colorRow) {
				row.setBackgroundColor(context.getResources().getColor(
						R.color.LightCyan));
				colorRow = false;
				rep.setColor(2);
			} else {
				row.setBackgroundColor(context.getResources().getColor(
						R.color.White));
				colorRow = true;
				rep.setColor(1);
			}
		} else if (rep.getColor() == 1) {
			row.setBackgroundColor(context.getResources().getColor(
					R.color.White));
			colorRow = true;
		} else if (rep.getColor() == 2) {
			row.setBackgroundColor(context.getResources().getColor(
					R.color.LightCyan));
			colorRow = false;
		}

		return row;
	}

	static class ViewHolder {
		public TextView lblCustomer, lblAddress, lblDate, lblPayment, lblTotal,
				lblPayed, lblRemaining;
		public LinearLayout parentProducts;
	}

	private void addProductToParentProducts(String productName,
			LinearLayout parentProducts) {
		TextView product = new TextView(context);

		product.setText(productName);
		product.setTextColor(context.getResources().getColor(
				R.color.Gray_Footer));

		parentProducts.addView(product);
	}
}
