package proyecto.integrador.odbyt.ListAdapters;

import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.Customer.Customer;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomerAdapter extends ArrayAdapter<Customer> {
	private final LayoutInflater inflater;
	private final Context context;
	private boolean colorRow;

	static class ViewHolder {
		public TextView txtCustomerName;
		public TextView txtCustomerAddress;
	}

	public CustomerAdapter(Context context, List<Customer> customers) {
		super(context, R.layout.customer_row, customers);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder viewHolder;

		if (row == null) {
			row = inflater.inflate(R.layout.customer_row, null);
			viewHolder = new ViewHolder();

			viewHolder.txtCustomerName = (TextView) row
					.findViewById(R.id.lblCustomerName);
			viewHolder.txtCustomerAddress = (TextView) row
					.findViewById(R.id.lblCustomerAddress);

			row.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) row.getTag();

		Customer c = (Customer) getItem(position);
		viewHolder.txtCustomerName.setText(c.toString());
		viewHolder.txtCustomerAddress.setText(c.getAddress() + "\n"
				+ c.getCity() + ", " + c.getState());
		
		if (c.getColor() == 0) {
			if (colorRow) {
				row.setBackgroundDrawable(context.getResources().getDrawable(
						R.drawable.item_row_selector));
				colorRow = false;
				c.setColor(2);
			} else {
				row.setBackgroundColor(Color.TRANSPARENT);
				colorRow = true;
				c.setColor(1);
			}
		} else if (c.getColor() == 1) {
			row.setBackgroundColor(Color.TRANSPARENT);
			colorRow = true;
		} else if (c.getColor() == 2) {
			row.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.item_row_selector));
			colorRow = false;
		}

		return row;
	}
}
