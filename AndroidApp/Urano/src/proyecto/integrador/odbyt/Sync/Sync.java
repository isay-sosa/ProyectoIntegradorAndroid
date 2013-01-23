package proyecto.integrador.odbyt.Sync;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import proyecto.integrador.odbyt.DataBase.CustomerTable;
import proyecto.integrador.odbyt.DataBase.PaymentTable;
import proyecto.integrador.odbyt.DataBase.ProductTable;
import proyecto.integrador.odbyt.DataBase.SellProductTable;
import proyecto.integrador.odbyt.DataBase.SellTable;
import proyecto.integrador.odbyt.DataBase.SellerTable;
import proyecto.integrador.odbyt.DataBase.SyncTable;
import proyecto.integrador.odbyt.Utils.Convert;
import android.content.Context;
import android.database.Cursor;

public class Sync {
	private HttpHelper httpHelper;
	private final String IP_Server = "148.213.225.87";
	private String URL_export_db_from_local = "http://" + IP_Server
			+ "/odbyt/importDB.php";
	private String URL_import_db_from_server = "http://" + IP_Server
			+ "/odbyt/exportDB.php";

	private SellerTable seller;
	private CustomerTable customer;
	private ProductTable product;
	private SellTable sell;
	private SellProductTable sell_product;
	private PaymentTable payment;
	private SyncTable sync;

	public void export_db_from_local(Context context) {
		httpHelper = new HttpHelper("POST", URL_export_db_from_local);
		sync = new SyncTable(context);
		JSONArray array = sync.getJSONArrayFromAllSyncElements();

		if (array != null) {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("importDB_JSON", array.toString()));

			JSONObject responseJSON = null;
			try {
				String json_response = httpHelper.getJSONData(params);
				responseJSON = new JSONObject(json_response);
				JSONObject status = responseJSON.getJSONObject("status");

				if (status.getInt("status") != -1) {
					if (status.getInt("status") == 1) {
						array = responseJSON.getJSONArray("sync");
						for (int i = 0; i < array.length(); i++) {
							updateElementIdServerFromJSONObject(context,
									array.getJSONObject(i));
						}
					}

					sync.deleteAllSyncElements();
				}

			} catch (JSONException jsone) {
				jsone.printStackTrace();
			}
		}
	}

	public void import_db_from_server(Context context) {
		httpHelper = new HttpHelper("GET", URL_import_db_from_server);
		String JSONData = httpHelper.getJSONData(null);

		try {
			JSONObject root = new JSONObject(JSONData);
			JSONObject status = root.getJSONObject("status");

			if (status.getInt("status") == 1) {
				// Server database has elements, so evaluates
				// the information.
				evaluateImportedJSON(root);
			} else {
				// Server database has not elements, so needs to
				// delete the elements of the local tables too.
				sync.deleteAllSyncElements();
				seller.deleteAllSellers();
				customer.deleteAllCustomers();
				product.deleteAllProducts();
				sell.deleteAllSells();
				sell_product.deleteAllSellProducts();
				payment.deleteAllPayments();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		httpHelper = null;
	}

	public void initialTableObjects(Context context) {
		seller = new SellerTable(context);
		customer = new CustomerTable(context);
		product = new ProductTable(context);
		sell = new SellTable(context);
		sell_product = new SellProductTable(context);
		payment = new PaymentTable(context);
	}

	private void updateElementIdServerFromJSONObject(Context context,
			JSONObject object) {
		try {
			String tablename = object.getString("TableName");

			if (tablename.equals(SellerTable.TABLE_NAME)) {
				seller.updateSellerIdServerById(
						object.getLong(SellerTable.KEY_ROW),
						object.getLong(SellerTable.KEY_ID_SERVER));

			} else if (tablename.equals(CustomerTable.TABLE_NAME)) {
				customer.updateCustomerIdServerById(
						object.getLong(CustomerTable.KEY_ROW),
						object.getLong(CustomerTable.KEY_ID_SERVER));

			} else if (tablename.equals(ProductTable.TABLE_NAME)) {
				product.updateProductIdServerById(
						object.getLong(ProductTable.KEY_ROW),
						object.getLong(ProductTable.KEY_ID_SERVER));

			} else if (tablename.equals(SellTable.TABLE_NAME)) {
				sell.updateSellIdServerById(object.getLong(SellTable.KEY_ROW),
						object.getLong(SellTable.KEY_ID_SERVER));

			} else if (tablename.equals(SellProductTable.TABLE_NAME)) {
				sell_product.updateSellProductIdServerById(
						object.getLong(SellProductTable.KEY_ROW),
						object.getLong(SellProductTable.KEY_ID_SERVER));

			} else if (tablename.equals(PaymentTable.TABLE_NAME)) {
				payment.updatePaymentIdServerById(
						object.getLong(PaymentTable.KEY_ROW),
						object.getLong(PaymentTable.KEY_ID_SERVER));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void insertElementToLocal(JSONObject object, String tablename)
			throws JSONException {
		if (tablename.equals(SellerTable.TABLE_NAME)) {
			seller.insertSeller(object.getLong(SellerTable.KEY_ROW),
					Convert.__(object.getString(SellerTable.KEY_USER)),
					Convert.__(object.getString(SellerTable.KEY_PWD)),
					Convert.__(object.getString(SellerTable.KEY_NAME)),
					Convert.__(object.getString(SellerTable.KEY_LASTNAME)),
					false);

		} else if (tablename.equals(CustomerTable.TABLE_NAME)) {
			customer.insertCustomer(object.getLong(CustomerTable.KEY_ROW),
					Convert.__(object.getString(CustomerTable.KEY_NAME)),
					Convert.__(object.getString(CustomerTable.KEY_LASTNAME)),
					Convert.__(object.getString(CustomerTable.KEY_ADDRESS)),
					Convert.__(object.getString(CustomerTable.KEY_CITY)),
					Convert.__(object.getString(CustomerTable.KEY_STATE)),
					object.getString(CustomerTable.KEY_PHONE),
					object.getString(CustomerTable.KEY_CELLPHONE),
					Convert.__(object.getString(CustomerTable.KEY_EMAIL)),
					false);

		} else if (tablename.equals(ProductTable.TABLE_NAME)) {
			product.insertProduct(object.getLong(ProductTable.KEY_ROW),
					Convert.__(object.getString(ProductTable.KEY_NAME)),
					Convert.__(object.getString(ProductTable.KEY_BRAND)),
					Convert.__(object.getString(ProductTable.KEY_MODEL)),
					object.getDouble(ProductTable.KEY_PRICE),
					Convert.__(object.getString(ProductTable.KEY_DESC)), false);

		} else if (tablename.equals(SellTable.TABLE_NAME)) {
			sell.insertSell(object.getLong(SellTable.KEY_ROW),
					object.getLong(SellTable.KEY_CUSTOMER),
					object.getInt(SellTable.KEY_CHARGE_TYPE),
					object.getInt(SellTable.KEY_DAY_TO_CHARGE),
					object.getLong(SellTable.KEY_HOUR_TO_CHARGE),
					object.getDouble(SellTable.KEY_TOTAL_PAYMENT),
					object.getDouble(SellTable.KEY_AGREED_PAYMENT),
					object.getLong(SellTable.KEY_NEXT_PAYMENT),
					object.getInt(SellTable.KEY_STATE), false, true);

		} else if (tablename.equals(SellProductTable.TABLE_NAME)) {
			sell_product.insertSellProduct(
					object.getLong(SellProductTable.KEY_ROW),
					object.getLong(SellProductTable.KEY_SELL),
					object.getLong(SellProductTable.KEY_PRODUCT),
					object.getInt(SellProductTable.KEY_AMOUNT), false, true);

		} else if (tablename.equals(PaymentTable.TABLE_NAME)) {
			payment.insertPayment(object.getLong(PaymentTable.KEY_ROW),
					object.getLong(PaymentTable.KEY_SELL),
					object.getDouble(PaymentTable.KEY_PAYMENT),
					object.getLong(PaymentTable.KEY_PAYMENT_DATE),
					object.getLong(PaymentTable.KEY_PAYMENT_HOUR), false, true);

		}
	}

	private void updateElementFromLocal(JSONObject object, long idLocal,
			String tablename) throws JSONException {

		if (tablename.equals(SellerTable.TABLE_NAME)) {
			seller.updateSellerById(idLocal,
					object.getLong(SellerTable.KEY_ROW),
					Convert.__(object.getString(SellerTable.KEY_USER)),
					Convert.__(object.getString(SellerTable.KEY_PWD)),
					Convert.__(object.getString(SellerTable.KEY_NAME)),
					Convert.__(object.getString(SellerTable.KEY_LASTNAME)),
					false);

		} else if (tablename.equals(CustomerTable.TABLE_NAME)) {
			customer.updateCustomerById(idLocal,
					object.getLong(CustomerTable.KEY_ROW),
					Convert.__(object.getString(CustomerTable.KEY_NAME)),
					Convert.__(object.getString(CustomerTable.KEY_LASTNAME)),
					Convert.__(object.getString(CustomerTable.KEY_ADDRESS)),
					Convert.__(object.getString(CustomerTable.KEY_CITY)),
					Convert.__(object.getString(CustomerTable.KEY_STATE)),
					object.getString(CustomerTable.KEY_PHONE),
					object.getString(CustomerTable.KEY_CELLPHONE),
					Convert.__(object.getString(CustomerTable.KEY_EMAIL)),
					false);

		} else if (tablename.equals(ProductTable.TABLE_NAME)) {
			product.updateProductById(idLocal,
					object.getLong(ProductTable.KEY_ROW),
					Convert.__(object.getString(ProductTable.KEY_NAME)),
					Convert.__(object.getString(ProductTable.KEY_BRAND)),
					Convert.__(object.getString(ProductTable.KEY_MODEL)),
					object.getDouble(ProductTable.KEY_PRICE),
					Convert.__(object.getString(ProductTable.KEY_DESC)), false);

		} else if (tablename.equals(SellTable.TABLE_NAME)) {
			sell.updateSellById(idLocal, object.getLong(SellTable.KEY_ROW),
					object.getLong(SellTable.KEY_CUSTOMER),
					object.getInt(SellTable.KEY_CHARGE_TYPE),
					object.getInt(SellTable.KEY_DAY_TO_CHARGE),
					object.getLong(SellTable.KEY_HOUR_TO_CHARGE),
					object.getDouble(SellTable.KEY_TOTAL_PAYMENT),
					object.getDouble(SellTable.KEY_AGREED_PAYMENT),
					object.getLong(SellTable.KEY_NEXT_PAYMENT),
					object.getInt(SellTable.KEY_STATE), false, true);

		} else if (tablename.equals(SellProductTable.TABLE_NAME)) {
			sell_product.updateSellProductById(idLocal,
					object.getLong(SellProductTable.KEY_ROW),
					object.getLong(SellProductTable.KEY_SELL),
					object.getLong(SellProductTable.KEY_PRODUCT),
					object.getInt(SellProductTable.KEY_AMOUNT), false, true);

		} else if (tablename.equals(PaymentTable.TABLE_NAME)) {
			payment.updatePaymentById(idLocal,
					object.getLong(PaymentTable.KEY_ROW),
					object.getLong(PaymentTable.KEY_SELL),
					object.getDouble(PaymentTable.KEY_PAYMENT),
					object.getLong(PaymentTable.KEY_PAYMENT_DATE),
					object.getLong(PaymentTable.KEY_PAYMENT_HOUR), false, true);
		}
	}

	private void deleteElementFromLocalTable(String tablename, long idLocal) {
		if (tablename.equals(SellerTable.TABLE_NAME)) {
			seller.deleteSellerById(idLocal, false);
		} else if (tablename.equals(CustomerTable.TABLE_NAME)) {
			customer.deleteCustomerById(idLocal, false);
		} else if (tablename.equals(ProductTable.TABLE_NAME)) {
			product.deleteProductById(idLocal, false);
		} else if (tablename.equals(SellTable.TABLE_NAME)) {
			sell.deleteSellById(idLocal, false);
		} else if (tablename.equals(SellProductTable.TABLE_NAME)) {
			sell_product.deleteSellProductById(idLocal, false);
		} else if (tablename.equals(PaymentTable.TABLE_NAME)) {
			payment.deletePaymentById(idLocal, false);
		}
	}

	private void compareData(JSONArray array, Cursor elements, String tablename)
			throws JSONException {
		JSONObject object;
		boolean indexLocal;

		if ((indexLocal = elements.moveToFirst())) {
			int indexJSONArray = 0;
			long idServer;

			do {
				object = array.getJSONObject(indexJSONArray);
				idServer = getIdServerFromJSONObject(object, tablename);

				if (idServer == elements.getLong(1)) {
					// Update the element from the local table
					updateElementFromLocal(object, elements.getLong(0),
							tablename);
					indexJSONArray++;
					indexLocal = elements.moveToNext();

				} else if (idServer < elements.getLong(1)) {
					// Insert the element to the local table
					insertElementToLocal(object, tablename);
					indexJSONArray++;

				} else if (idServer > elements.getLong(1)) {
					// Delete the element from the local table
					deleteElementFromLocalTable(tablename, elements.getLong(0));
					indexLocal = elements.moveToNext();
				}

				if (indexJSONArray == array.length()) {
					// Delete the remaining elements from the local table
					while (elements.moveToNext()) {
						deleteElementFromLocalTable(tablename,
								elements.getLong(0));
					}

					object = null;
					break;
				}

				object = null;
			} while (indexLocal);

			if (indexJSONArray < array.length()) {
				// Insert the remaining elements to the local table
				for (int i = indexJSONArray; i < array.length(); i++) {
					insertElementToLocal(array.getJSONObject(i), tablename);
				}
			}
		} else {
			// Insert all the elements to the local table
			for (int i = 0; i < array.length(); i++) {
				insertElementToLocal(array.getJSONObject(i), tablename);
			}
		}
	}

	private long getIdServerFromJSONObject(JSONObject object, String tablename)
			throws JSONException {
		long idServer = -1;

		if (tablename.equals(SellerTable.TABLE_NAME)) {
			idServer = object.getLong(SellerTable.KEY_ROW);
		} else if (tablename.equals(CustomerTable.TABLE_NAME)) {
			idServer = object.getLong(CustomerTable.KEY_ROW);
		} else if (tablename.equals(ProductTable.TABLE_NAME)) {
			idServer = object.getLong(ProductTable.KEY_ROW);
		} else if (tablename.equals(SellTable.TABLE_NAME)) {
			idServer = object.getLong(SellTable.KEY_ROW);
		} else if (tablename.equals(SellProductTable.TABLE_NAME)) {
			idServer = object.getLong(SellProductTable.KEY_ROW);
		} else if (tablename.equals(PaymentTable.TABLE_NAME)) {
			idServer = object.getLong(PaymentTable.KEY_ROW);
		}

		return idServer;
	}

	private void evaluateImportedJSON(JSONObject root) {
		String tables[] = { SellerTable.TABLE_NAME, CustomerTable.TABLE_NAME,
				ProductTable.TABLE_NAME, SellTable.TABLE_NAME,
				SellProductTable.TABLE_NAME, PaymentTable.TABLE_NAME };
		JSONArray array;
		Cursor elements = null;

		for (int i = 0; i < tables.length; i++) {
			try {
				array = root.getJSONArray(tables[i]);

				if (tables[i].equals(SellerTable.TABLE_NAME)) {
					elements = seller.getAllSellersOrderedByIdServer();
				} else if (tables[i].equals(CustomerTable.TABLE_NAME)) {
					elements = customer.getAllCustomersOrderedByIdServer();
				} else if (tables[i].equals(ProductTable.TABLE_NAME)) {
					elements = product.getAllProductsOrderedByIdServer();
				} else if (tables[i].equals(SellTable.TABLE_NAME)) {
					elements = sell.getAllSellsOrderedByIdServer();
				} else if (tables[i].equals(SellProductTable.TABLE_NAME)) {
					elements = sell_product
							.getAllSellProductsOrderedByIdServer();
				} else if (tables[i].equals(PaymentTable.TABLE_NAME)) {
					elements = payment.getAllPaymentsOrderedByIdServer();
				}

				compareData(array, elements, tables[i]);
			} catch (JSONException e) {
				if (tables[i].equals(SellerTable.TABLE_NAME)) {
					seller.deleteAllSellers();
				} else if (tables[i].equals(CustomerTable.TABLE_NAME)) {
					customer.deleteAllCustomers();
				} else if (tables[i].equals(ProductTable.TABLE_NAME)) {
					product.deleteAllProducts();
				} else if (tables[i].equals(SellTable.TABLE_NAME)) {
					sell.deleteAllSells();
				} else if (tables[i].equals(SellProductTable.TABLE_NAME)) {
					sell_product.deleteAllSellProducts();
				} else if (tables[i].equals(PaymentTable.TABLE_NAME)) {
					payment.deleteAllPayments();
				}
			} finally {
				try {
					elements.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
