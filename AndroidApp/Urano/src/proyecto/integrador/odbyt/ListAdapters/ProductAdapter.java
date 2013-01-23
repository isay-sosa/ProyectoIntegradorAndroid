package proyecto.integrador.odbyt.ListAdapters;

import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.Product.Product;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProductAdapter extends ArrayAdapter<Product> {
	private final LayoutInflater inflater;
	private final Context context;
	private boolean colorRow;

	static class ViewHolder {
		public TextView txtProductName;
		public TextView txtProductBrand;
		public TextView txtProductPrice;
	}

	public ProductAdapter(Context context, List<Product> products) {
		super(context, R.layout.product_row, products);
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder viewHolder;

		if (row == null) {
			row = inflater.inflate(R.layout.product_row, null);
			viewHolder = new ViewHolder();

			viewHolder.txtProductName = (TextView) row
					.findViewById(R.id.lblProductName);
			viewHolder.txtProductBrand = (TextView) row
					.findViewById(R.id.lblProductBrand);
			viewHolder.txtProductPrice = (TextView) row
					.findViewById(R.id.lblProductPrice);

			row.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) row.getTag();

		Product p = getItem(position);

		viewHolder.txtProductName.setText(p.getName());
		viewHolder.txtProductBrand.setText(p.getBrand());
		viewHolder.txtProductPrice.setText(String.valueOf(p.getPrice()));

		if (p.getColor() == 0) {
			if (colorRow) {
				row.setBackgroundDrawable(context.getResources().getDrawable(
						R.drawable.item_row_selector));
				colorRow = false;
				p.setColor(2);
			} else {
				row.setBackgroundColor(Color.TRANSPARENT);
				colorRow = true;
				p.setColor(1);
			}
		} else if (p.getColor() == 1) {
			row.setBackgroundColor(Color.TRANSPARENT);
			colorRow = true;
		} else if (p.getColor() == 2) {
			row.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.item_row_selector));
			colorRow = false;
		}

		return row;
	}
}
