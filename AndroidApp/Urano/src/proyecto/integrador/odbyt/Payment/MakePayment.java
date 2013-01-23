package proyecto.integrador.odbyt.Payment;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.PaymentTable;
import proyecto.integrador.odbyt.DataBase.SellTable;
import proyecto.integrador.odbyt.Sell.Sell;
import proyecto.integrador.odbyt.Utils.Convert;
import proyecto.integrador.odbyt.Utils.DateTimeManage;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MakePayment extends Activity implements OnClickListener {
	private Button btnPositive, btnNegative;
	private TextView lblHeaderTitle, lblTotal, lblPayed, lblRemaining;
	private EditText txtPayment;
	private Sell sell;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_payment);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		sell = Utils.getSell();

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle.setText(getString(R.string.make_payment_Text));

		lblTotal = (TextView) findViewById(R.id.lblTotal);
		lblTotal.setText(getString(R.string.simbol_$) + sell.getTotalPayment());

		lblPayed = (TextView) findViewById(R.id.lblPayed);
		lblPayed.setText(getString(R.string.simbol_$) + sell.getPayedPayment());

		lblRemaining = (TextView) findViewById(R.id.lblRemaining);
		lblRemaining.setText(getString(R.string.simbol_$)
				+ (sell.getTotalPayment() - sell.getPayedPayment()));

		txtPayment = (EditText) findViewById(R.id.txtPayment);

		if ((sell.getTotalPayment() - sell.getPayedPayment()) >= sell
				.getAgreedPayment())
			txtPayment.setText(sell.getAgreedPayment() + "");
		else
			txtPayment
					.setText((sell.getTotalPayment() - sell.getPayedPayment())
							+ "");

		btnPositive = (Button) findViewById(R.id.btnPositive);
		btnPositive.setText(getString(R.string.btnConfirm_Text));
		btnPositive.setOnClickListener(this);

		btnNegative = (Button) findViewById(R.id.btnNegative);
		btnNegative.setText(getString(R.string.btnCancel_Text));
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
		if (txtPayment.length() > 0) {
			PaymentTable p = new PaymentTable(this);

			double payment = Double
					.parseDouble(txtPayment.getText().toString());
			if (payment > (sell.getTotalPayment() - sell.getPayedPayment())) {
				Toast.makeText(
						this,
						"El pago debe de ser menor o igual a lo restante de la venta.",
						Toast.LENGTH_LONG).show();
				
				return;
			}

			long idPaymentInserted = p.insertPayment(0, sell.getId(), payment,
					DateTimeManage.getTodayDateAsLong(),
					DateTimeManage.getTodayTimeAsLong(), true, false);

			if (idPaymentInserted > 0) {
				SellTable s = new SellTable(this);
				long nextPayment = 0;

				s.updateStateSellBySellId(sell.getId(), true);

				switch (Convert.fromStringChargeTypetoIng(this,
						sell.getChargeType())) {
				case 1:
					nextPayment = DateTimeManage.every_week(Convert
							.fromStringDaytoInt(this, sell.getDayToCharge()),
							Convert.fromLongDatetoCalendar(sell
									.getNextPayment()));
					break;

				case 2:
					nextPayment = DateTimeManage.every_2_weeks(Convert
							.fromStringDaytoInt(this, sell.getDayToCharge()),
							Convert.fromLongDatetoCalendar(sell
									.getNextPayment()));
					break;

				case 3:
					nextPayment = DateTimeManage.every_month(Convert
							.fromStringDaytoInt(this, sell.getDayToCharge()),
							Convert.fromLongDatetoCalendar(sell
									.getNextPayment()));
					break;
				}

				s.updateNextPaymentBySellId(sell.getId(), nextPayment, true);

				Toast.makeText(this, getString(R.string.payment_made_message),
						Toast.LENGTH_LONG).show();
				setResult(RESULT_OK);
				finish();
			}
		} else
			Toast.makeText(this,
					getString(R.string.error_fill_requiered_fields),
					Toast.LENGTH_LONG).show();
	}

	/**
	 * Called when the Negative button from the footer has been clicked.
	 */
	private void btnNegative_onClick() {
		setResult(RESULT_CANCELED);
		finish();
	}
}
