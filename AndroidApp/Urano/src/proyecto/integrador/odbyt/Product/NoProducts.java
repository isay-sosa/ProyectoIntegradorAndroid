package proyecto.integrador.odbyt.Product;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.DataBase.ProductTable;
import proyecto.integrador.odbyt.Utils.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NoProducts extends Activity {
	private Button btnCreateProduct;
	private final int CREATE_PRODUCT_REQUES_CODE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LookForProducts();
		setContentView(R.layout.no_products);
		initComponents();
	}

	/**
	 * Method that looks and gets all the elements on the Product Table and
	 * starts the correct Activity.
	 */
	private void LookForProducts() {
		Utils.setProductsList(new ProductTable(this).getAllProductsOnList());

		if (Utils.getProductsList() != null) {
			if (Utils.getProductsList().size() > 0) {
				startActivity(new Intent(this, ProductsList.class));
				finish();
			}
		}
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);
		btnCreateProduct.setOnClickListener(new View.OnClickListener() {

			public void onClick(View arg0) {
				startActivityForResult(new Intent(NoProducts.this,
						CreateProduct.class), CREATE_PRODUCT_REQUES_CODE);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CREATE_PRODUCT_REQUES_CODE
				&& resultCode == RESULT_OK) {
			startActivity(new Intent(this, ProductsList.class));
			finish();
		}
	}

}
