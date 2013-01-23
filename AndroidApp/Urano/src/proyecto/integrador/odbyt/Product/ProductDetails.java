package proyecto.integrador.odbyt.Product;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.ProductTable;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetails extends TabActivity implements OnClickListener,
		android.content.DialogInterface.OnClickListener {
	private TextView lblHeaderTitle, lblProductName, lblProductBrand,
			lblProductModel, lblProductPrice, lblProductDesc;
	private Button btnModify, btnDelete;
	private Product product;
	private final int UPDATE_PRODUCT_REQUEST_CODE = 30;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail);
		initComponents();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			setProductInfo();
			setResult(RESULT_OK);
		}
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		product = Utils.getProduct();

		lblHeaderTitle = (TextView) findViewById(R.id.header_title);
		lblHeaderTitle
				.setText(getString(R.string.title_activity_product_details));

		lblProductName = (TextView) findViewById(R.id.lblProductName);
		lblProductBrand = (TextView) findViewById(R.id.lblProductBrand);
		lblProductModel = (TextView) findViewById(R.id.lblProductModel);
		lblProductPrice = (TextView) findViewById(R.id.lblProductPrice);
		lblProductDesc = (TextView) findViewById(R.id.lblProductDesc);

		setProductInfo();

		btnModify = (Button) findViewById(R.id.btnModify);
		btnModify.setOnClickListener(this);

		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);

		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent(this, CustomersPayingList.class);
		spec = tabHost.newTabSpec("paying");
		spec.setIndicator(getString(R.string.paying_Text));
		spec.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent(this, CustomersFinishedList.class);
		spec = tabHost.newTabSpec("finished");
		spec.setIndicator(getString(R.string.finished_Text));
		spec.setContent(intent);
		tabHost.addTab(spec);
	}

	private void setProductInfo() {
		lblProductName.setText(product.getName());
		lblProductBrand.setText(product.getBrand());
		lblProductModel.setText(product.getModel());
		lblProductPrice.setText(getString(R.string.simbol_$)
				+ product.getPrice());
		lblProductDesc.setText(product.getDesc());
	}

	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnModify:
			btnModify_onClick();
			break;

		case R.id.btnDelete:
			btnDelete_onClick();
			break;
		}
	}

	/**
	 * Called when the Modify button from the Product Info has been clicked.
	 */
	private void btnModify_onClick() {
		startActivityForResult(new Intent(this, ModifyProduct.class),
				UPDATE_PRODUCT_REQUEST_CODE);
	}

	/**
	 * Called when the Delete button from the Product Info has been clicked.
	 */
	private void btnDelete_onClick() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle(R.string.delete_sigle_product_title);
		builder.setMessage(R.string.delete_single_product_message);
		builder.setIcon(android.R.drawable.ic_dialog_alert);

		builder.setPositiveButton(android.R.string.yes, this);
		builder.setNegativeButton(android.R.string.no, this);

		builder.create().show();
	}

	public void onClick(DialogInterface dialog, int which) {
		// -1 = positive
		// -2 = negative
		if (which == -1) {
			ProductTable p = new ProductTable(this);
			
			if (p.deleteProductById(product.getId(), true)) {
				Utils.getProductsList().remove(product);
				setResult(RESULT_OK);
				finish();
			} else
				Toast.makeText(this,
						getString(R.string.error_deleting_single_product),
						Toast.LENGTH_LONG).show();
		}
	}
}
