package proyecto.integrador.odbyt.Sell;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.PaymentTable;
import proyecto.integrador.odbyt.DataBase.SellProductTable;
import proyecto.integrador.odbyt.DataBase.SellTable;
import proyecto.integrador.odbyt.Utils.DateTimeManage;
import proyecto.integrador.odbyt.Utils.SellData;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MakeSell_Payment_Data extends Activity implements OnClickListener {
	private TextView lblHeaderTitle, lblTotal;
	private EditText txtAgreedPayment, txtDownPayment;
	private Button btnPositive, btnNegative;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sell_set_payment_data);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle.setText(R.string.make_sell_Text);

		lblTotal = (TextView) findViewById(R.id.lblTotal);
		lblTotal.setText(lblTotal.getText() + " " + SellData.total);

		txtAgreedPayment = (EditText) findViewById(R.id.txtAgreedPayment);
		txtDownPayment = (EditText) findViewById(R.id.txtDownPayment);

		btnPositive = (Button) findViewById(R.id.btnPositive);
		btnPositive.setText(R.string.btnConfirm_Text);
		btnPositive.setOnClickListener(this);

		btnNegative = (Button) findViewById(R.id.btnNegative);
		btnNegative.setText(R.string.btnGoBack_Text);
		btnNegative.setOnClickListener(this);
	}

	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.btnPositive:
			btnPositive_onClick();
			break;
		case R.id.btnNegative:
			btnNegative_onClick();
			break;
		}
	}

	/**
	 * Called when the Positive button from the footer has been clicked.
	 */
	private void btnPositive_onClick() {
		if (txtAgreedPayment.length() > 0 && txtDownPayment.length() > 0) {
			double downPayment = Double.parseDouble(txtDownPayment.getText()
					.toString());

			if (downPayment <= SellData.total) {
				// Save the sell
				SellTable sell = new SellTable(this);
				PaymentTable payment;
				SellProductTable sp;
				Intent data = new Intent();
				Bundle extras = new Bundle();

				long idSellInserted = sell.insertSell(0,
						SellData.selected_customer.getId(),
						SellData.go_to_charge, SellData.day, SellData.time,
						SellData.total, Double.parseDouble(txtAgreedPayment
								.getText().toString()), SellData.next_payment,
						-1, true, false);

				if (idSellInserted > 0) {
					// Insert payment data
					payment = new PaymentTable(this);
					payment.insertPayment(0, idSellInserted, Double
							.parseDouble(txtDownPayment.getText().toString()),
							DateTimeManage.getTodayDateAsLong(), DateTimeManage
									.getTodayTimeAsLong(), true, false);

					// Insert sell_product data
					sp = new SellProductTable(this);
					for (int i = 0; i < SellData.selected_products.size(); i++)
						sp.insertSellProduct(0, idSellInserted,
								SellData.selected_products.get(i).getId(), 1,
								true, false);

					// Update state from sell
					sell.updateStateSellBySellId(idSellInserted, true);

					// Return to customers/products list
					extras.putBoolean("COMPLETED", true);
				} else {
					extras.putBoolean("COMPLETED", false);
				}

				data.putExtras(extras);
				setResult(RESULT_OK, data);
				finish();
			} else
				Toast.makeText(
						this,
						"El pago debe de ser menor o igual al total de la venta.",
						Toast.LENGTH_LONG).show();
		} else {

			Toast.makeText(this,
					getString(R.string.error_fill_requiered_fields),
					Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * Called when the Negative button from the footer has been clicked.
	 */
	private void btnNegative_onClick() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
