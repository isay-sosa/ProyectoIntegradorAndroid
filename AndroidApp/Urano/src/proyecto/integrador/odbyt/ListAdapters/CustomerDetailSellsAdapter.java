package proyecto.integrador.odbyt.ListAdapters;

import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.Sell.Sell;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomerDetailSellsAdapter extends ArrayAdapter<Sell> {
	private final LayoutInflater inflater;
	private final Context context;
	private boolean colorRow;

	static class ViewHolder {
		public TextView lblPurachaseId;
		public TextView lblDate;
		public TextView lblTotal;
		public TextView lblPayed;
		public TextView lblRemaining;
		public LinearLayout parentProducts;
	}

	public CustomerDetailSellsAdapter(Context context, List<Sell> sells) {
		super(context, R.layout.customer_detail_sell_info_row, sells);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder viewHolder;

		if (row == null) {
			row = inflater
					.inflate(R.layout.customer_detail_sell_info_row, null);
			viewHolder = new ViewHolder();

			viewHolder.lblPurachaseId = (TextView) row
					.findViewById(R.id.lblPurachaseId);
			viewHolder.parentProducts = (LinearLayout) row
					.findViewById(R.id.parentProducts);
			viewHolder.lblDate = (TextView) row.findViewById(R.id.lblDate);
			viewHolder.lblTotal = (TextView) row.findViewById(R.id.lblTotal);
			viewHolder.lblPayed = (TextView) row.findViewById(R.id.lblPayed);
			viewHolder.lblRemaining = (TextView) row
					.findViewById(R.id.lblRemaining);

			row.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) row.getTag();

		Sell sell = (Sell) getItem(position);

		viewHolder.lblPurachaseId.setText(sell.getId() + "");
		
		viewHolder.parentProducts.removeAllViews();
		for (int i = 0; i < sell.getProducts().size(); i++)
			addProductToParentProducts(sell.getProducts().get(i),
					viewHolder.parentProducts);

		viewHolder.lblDate.setText(sell.getDate() + "\n" + sell.getTime());
		viewHolder.lblTotal.setText(context.getString(R.string.simbol_$)
				+ sell.getTotalPayment());
		viewHolder.lblPayed.setText(context.getString(R.string.simbol_$)
				+ sell.getPayedPayment());
		viewHolder.lblRemaining.setText(context.getString(R.string.simbol_$)
				+ (sell.getTotalPayment() - sell.getPayedPayment()));

		if (sell.getColor() == 0) {
			if (colorRow) {
				row.setBackgroundDrawable(context.getResources().getDrawable(
						R.drawable.item_row_selector));
				colorRow = false;
				sell.setColor(2);
			} else {
				row.setBackgroundColor(Color.TRANSPARENT);
				colorRow = true;
				sell.setColor(1);
			}
		} else if (sell.getColor() == 1) {
			row.setBackgroundColor(Color.TRANSPARENT);
			colorRow = true;
		} else if (sell.getColor() == 2) {
			row.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.item_row_selector));
			colorRow = false;
		}
		
		return row;
	}
	
	private void addProductToParentProducts(String productName, LinearLayout parentProducts) {
		TextView product = new TextView(context);
		
		product.setText(productName);
		product.setTextColor(context.getResources().getColor(R.color.Gray_Footer));

		parentProducts.addView(product);
	}

}
