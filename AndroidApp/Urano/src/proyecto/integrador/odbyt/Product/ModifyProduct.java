package proyecto.integrador.odbyt.Product;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.ProductTable;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyProduct extends Activity implements OnClickListener {
	private TextView lblHeaderTitle;
	private EditText txtName, txtPrice, txtBrand, txtModel, txtDesc;
	private Button btnPositive, btnNegative;
	private Product p;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_product);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		p = Utils.getProduct();

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle
				.setText(getString(R.string.title_activity_modify_product));

		txtName = (EditText) findViewById(R.id.txtName);
		txtPrice = (EditText) findViewById(R.id.txtPrice);
		txtBrand = (EditText) findViewById(R.id.txtBrand);
		txtModel = (EditText) findViewById(R.id.txtModel);
		txtDesc = (EditText) findViewById(R.id.txtDesc);

		btnPositive = (Button) findViewById(R.id.btnPositive);
		btnPositive.setText(getString(R.string.btnSave_Text));
		btnPositive.setOnClickListener(this);

		btnNegative = (Button) findViewById(R.id.btnNegative);
		btnNegative.setText(getString(R.string.btnCancel_Text));
		btnNegative.setOnClickListener(this);

		if (p != null) {
			txtName.setText(p.getName());
			txtPrice.setText(String.valueOf(p.getPrice()));
			txtBrand.setText(p.getBrand());
			txtModel.setText(p.getModel());
			txtDesc.setText(p.getDesc());
		}
	}

	public void onClick(View v) {
		int id = v.getId();

		switch (id) {
		case R.id.btnNegative:
			btnNegative_onClick();
			break;
		case R.id.btnPositive:
			btnPositive_onClick();
			break;
		}
	}

	/**
	 * Called when the Negative button from the footer has been clicked.
	 */
	private void btnNegative_onClick() {
		setResult(RESULT_CANCELED);
		finish();
	}

	/**
	 * Called when the Positive button from the footer has been clicked.
	 */
	private void btnPositive_onClick() {
		if (txtName.getText().toString().trim().length() > 0
				&& txtPrice.getText().toString().trim().length() > 0) {
			ProductTable product = new ProductTable(this);
			if (product.updateProductById(p.getId(), p.getIdServer(), txtName
					.getText().toString().trim(), txtBrand.getText().toString()
					.trim(), txtModel.getText().toString().trim(),
					Double.valueOf(txtPrice.getText().toString().trim()),
					txtDesc.getText().toString().trim(), true)) {

				p.setName(txtName.getText().toString().trim());
				p.setBrand(txtBrand.getText().toString().trim());
				p.setModel(txtModel.getText().toString().trim());
				p.setPrice(Double.valueOf(txtPrice.getText().toString().trim()));
				p.setDesc(txtDesc.getText().toString().trim());

				setResult(RESULT_OK);
				finish();
			} else
				// Error occurred.
				Toast.makeText(this, getString(R.string.error_modify_product),
						Toast.LENGTH_LONG).show();
		} else
			// Fill the * fields.
			Toast.makeText(this,
					getString(R.string.error_fill_requiered_fields),
					Toast.LENGTH_LONG).show();
	}

}
