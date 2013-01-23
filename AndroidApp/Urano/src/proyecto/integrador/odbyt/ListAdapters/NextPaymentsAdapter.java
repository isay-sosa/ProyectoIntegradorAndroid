package proyecto.integrador.odbyt.ListAdapters;

import java.util.ArrayList;
import java.util.List;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.Sell.Sell;
import proyecto.integrador.odbyt.Utils.Convert;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

abstract public class NextPaymentsAdapter extends BaseAdapter {
	abstract protected View getHeaderView(String caption, int index,
			View convertView, ViewGroup parent);

	private List<Section> sections = new ArrayList<Section>();
	private static int TYPE_SECTION_HEADER = 0;
	private final LayoutInflater inflater;
	private final Context context;
	private boolean colorRow;

	class Section {
		String _caption;
		Adapter _sells;

		Section(String _caption, Adapter _persons) {
			this._caption = _caption;
			this._sells = _persons;
		}
	}

	static class ViewHolder {
		public TextView txtCustomer, txtDate, txtPayment;
		public Button btnMakePayment;
	}

	public NextPaymentsAdapter(Context context) {
		super();
		inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public void addSection(String _caption, Adapter _sells) {
		sections.add(new Section(_caption, _sells));
	}

	public void clearSections() {
		sections.clear();
	}

	public int getCount() {
		int total = 0;

		for (Section section : sections) {
			total += section._sells.getCount() + 1; // Se agrega 1 por la
													// cabecera
		}

		return total;
	}

	public Object getItem(int position) {
		for (Section section : sections) {
			if (position == 0)
				return section;

			int size = section._sells.getCount() + 1;

			if (position < size)
				return section._sells.getItem(position - 1);

			position -= size;
		}

		return null;
	}
	
	public int getViewTypeCount() {
		int total = 1; // one for the header, plus those from sections

		for (Section section : this.sections) {
			total += section._sells.getViewTypeCount();
		}

		return total;
	}
	
	public int getItemViewType(int position) {
		int typeOffset = TYPE_SECTION_HEADER + 1; // start counting from here

		for (Section section : this.sections) {
			if (position == 0) {
				return (TYPE_SECTION_HEADER);
			}

			int size = section._sells.getCount() + 1;

			if (position < size) {
				return (typeOffset + section._sells
						.getItemViewType(position - 1));
			}

			position -= size;
			typeOffset += section._sells.getViewTypeCount();
		}

		return -1;
	}
	
	public boolean areAllItemsSelectable() {
		return false;
	}

	public boolean isEnabled(int position) {
		return (getItemViewType(position) != TYPE_SECTION_HEADER);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		int sectionIndex = 0;
		View row = convertView;
		ViewHolder viewHolder = null;

		for (Section section : sections) {
			if (position == 0) {
				colorRow = false;
				return getHeaderView(section._caption, sectionIndex,
						convertView, parent);
			}

			int size = section._sells.getCount() + 1;

			if (position < size) {
				if (row == null) {
					row = inflater.inflate(R.layout.today_payments_row, null);
					viewHolder = new ViewHolder();

					viewHolder.txtCustomer = (TextView) row
							.findViewById(R.id.txtCustomer);
					viewHolder.txtDate = (TextView) row
							.findViewById(R.id.txtDate);
					viewHolder.txtPayment = (TextView) row
							.findViewById(R.id.txtPayment);
					viewHolder.btnMakePayment = (Button) row
							.findViewById(R.id.btnMakePayment);

					row.setTag(viewHolder);
				} else
					viewHolder = (ViewHolder) row.getTag();

				Sell s = (Sell) section._sells.getItem(position - 1);

				viewHolder.txtCustomer.setText(s.getCustomerName());
				viewHolder.txtDate.setText(Convert.fromLongtoDateString(s
						.getNextPayment()) + "\n" + s.getHourToCharge());
				viewHolder.txtPayment.setText(context
						.getString(R.string.simbol_$) + s.getAgreedPayment());
				viewHolder.btnMakePayment.setVisibility(8);

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
				
				s = null;
				return row;
			}

			position -= size;
			sectionIndex++;
		}

		return null;
	}
}
