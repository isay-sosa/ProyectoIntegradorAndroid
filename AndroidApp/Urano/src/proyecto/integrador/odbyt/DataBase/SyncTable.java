package proyecto.integrador.odbyt.DataBase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class SyncTable {
	public static final String KEY_ROW = "IdSync";
	public static final String KEY_TABLE = "TableName";
	public static final String KEY_ACTION = "Action";
	public static final String KEY_ID_ELEMENT = "IdElement";
	public static final String KEY_ID_ELEMENT_SERVER = "IdElement_Server";
	public static final String TABLE_NAME = "sync";
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "(" + KEY_ROW
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_TABLE
			+ " TEXT NOT NULL," + KEY_ACTION + " TEXT NOT NULL,"
			+ KEY_ID_ELEMENT + " INTEGER NOT NULL," + KEY_ID_ELEMENT_SERVER
			+ " INTEGER NOT NULL);";

	private SQLiteDatabase db = null;
	private DataBaseHelper dbHelper = null;
	private Context context;

	public SyncTable(Context context) {
		dbHelper = new DataBaseHelper(context);
		this.context = context;
	}

	/**
	 * Function that inserts an element to the SyncTable.
	 * 
	 * @param tablename
	 *            Name of the table that corresponds to the inserted element.
	 * @param action
	 *            Action that'll be executed when the synchronization occurs
	 *            (<b>insert</b>, <b>update</b>, <b>delete</b>).
	 * @param idElement
	 *            Unique number that corresponds with the inserted element.
	 * @param idElement_Server
	 *            Unique number from the server that corresponds with the
	 *            inserted element.
	 * @return True if the element was inserted. False otherwise.
	 */
	public boolean insertSyncElement(String tablename, String action,
			long idElement, long idElement_Server) {
		long idInserted = -1;
		String sql = "INSERT INTO " + TABLE_NAME + "(" + KEY_TABLE + ","
				+ KEY_ACTION + "," + KEY_ID_ELEMENT + ","
				+ KEY_ID_ELEMENT_SERVER + ") VALUES (?, ?, ?, ?);";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindString(1, tablename);
			stmnt.bindString(2, action);
			stmnt.bindLong(3, idElement);
			stmnt.bindLong(4, idElement_Server);

			if (action.equals("insert")) {
				idInserted = stmnt.executeInsert();
			} else {
				Cursor elements = getSyncElementsByIdElement(idElement);

				if (elements.moveToFirst()) {
					if (action.equals("update")) {
						if (elements.getString(2).equals("insert")) {
							idInserted = idElement;
						} else {
							deleteSyncElementByIdElement(elements.getLong(3));
							idInserted = stmnt.executeInsert();
						}
					} else if (action.equals("delete")) {
						if (elements.getString(2).equals("insert")) {
							deleteSyncElementByIdElement(elements.getLong(3));
							idInserted = idElement;
						} else {
							deleteSyncElementByIdElement(elements.getLong(3));
							idInserted = stmnt.executeInsert();
						}
					}
				} else
					idInserted = stmnt.executeInsert();

				elements.close();
			}
		} catch (SQLException sqle) {
			Log.e("Error insertSyncElement - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return (idInserted > 0);
	}

	/**
	 * Function that returns all the elements stored on the Sync Table.
	 * 
	 * @return Cursor object with all the elements. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllSyncElements() {
		Cursor elements = null;
		String sql = "SELECT * FROM " + TABLE_NAME;

		try {
			db = dbHelper.getWritableDatabase();

			elements = db.rawQuery(sql, null);
			if (elements != null)
				elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllSyncElements - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return elements;
	}

	/**
	 * Function that returns all the elements stored on the Sync Table that
	 * match with the id of the element.
	 * 
	 * @return Cursor object with all the elements. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getSyncElementsByIdElement(long idElement) {
		Cursor elements = null;
		String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + KEY_ID_ELEMENT
				+ " = ?";

		try {
			elements = db.rawQuery(sql,
					new String[] { String.valueOf(idElement) });
			if (elements != null)
				elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getSyncElementsByIdElement - " + TABLE_NAME,
					sqle.toString());
		}

		return elements;
	}

	/**
	 * Function that returns a JSON Array with all the elements (and their own
	 * information) from the Sync Table.
	 * 
	 * @return JSON Array with all the information. If an errors occurs
	 *         <i>null</i> is returned.
	 */
	public JSONArray getJSONArrayFromAllSyncElements() {
		JSONArray array = null;
		Cursor elements = getAllSyncElements();

		if (elements.moveToFirst()) {
			array = new JSONArray();
			JSONObject object;

			do {
				object = getJSONObjectFromSyncElement(elements.getString(1),
						elements.getString(2), elements.getLong(3),
						elements.getLong(4));

				if (object != null) {
					array.put(object);
					object = null;
				} else {
					array = null;
					break;
				}

			} while (elements.moveToNext());
		}

		try {
			elements.close();
		} catch (Exception e) {
			Log.e("Error closing Cursor", e.toString());
		}

		return array;
	}

	/**
	 * Function that returns a JSON Object from a sync element.
	 * 
	 * @param tablename
	 *            Name of the table that corresponds to the element.
	 * @param action
	 *            Action that'll be executed when the synchronization occurs
	 *            (<b>insert</b>, <b>update</b>, <b>delete</b>).
	 * @param idElement
	 *            Unique number that corresponds to the element.
	 * @param IdElement_Server
	 *            Unique number from the server that corresponds with the
	 *            inserted element.
	 * @return JSON Object with the element data. If an errors occurs
	 *         <i>null</i> is returned.
	 */
	private JSONObject getJSONObjectFromSyncElement(String tablename,
			String action, long idElement, long idElement_Server) {
		JSONObject object = null;
		Cursor element = null;

		try {
			object = new JSONObject();

			if (tablename.equals(SellerTable.TABLE_NAME)) {
				element = new SellerTable(context).getSellerById(idElement);

				if (element.moveToFirst()) {
					object.put(SellerTable.KEY_ROW, idElement);
					object.put(SellerTable.KEY_ID_SERVER, element.getLong(1));
					object.put(SellerTable.KEY_USER, element.getString(2));
					object.put(SellerTable.KEY_PWD, element.getString(3));
					object.put(SellerTable.KEY_NAME, element.getString(4));
					object.put(SellerTable.KEY_LASTNAME, element.getString(5));
				} else
					object.put(SellerTable.KEY_ID_SERVER, idElement_Server);

			} else if (tablename.equals(CustomerTable.TABLE_NAME)) {
				element = new CustomerTable(context).getCustomerById(idElement);

				if (element.moveToFirst()) {
					object.put(CustomerTable.KEY_ROW, idElement);
					object.put(CustomerTable.KEY_ID_SERVER, element.getLong(1));
					object.put(CustomerTable.KEY_NAME, element.getString(2));
					object.put(CustomerTable.KEY_LASTNAME, element.getString(3));
					object.put(CustomerTable.KEY_ADDRESS, element.getString(4));
					object.put(CustomerTable.KEY_CITY, element.getString(5));
					object.put(CustomerTable.KEY_STATE, element.getString(6));
					object.put(
							CustomerTable.KEY_PHONE,
							(element.getString(7) != null ? element
									.getString(7) : ""));
					object.put(CustomerTable.KEY_CELLPHONE, (element
							.getString(8) != null ? element.getString(8) : ""));
					object.put(
							CustomerTable.KEY_EMAIL,
							(element.getString(9) != null ? element
									.getString(9) : ""));
				} else
					object.put(CustomerTable.KEY_ID_SERVER, idElement_Server);

			} else if (tablename.equals(ProductTable.TABLE_NAME)) {
				element = new ProductTable(context).getProductById(idElement);

				if (element.moveToFirst()) {
					object.put(ProductTable.KEY_ROW, idElement);
					object.put(ProductTable.KEY_ID_SERVER, element.getLong(1));
					object.put(ProductTable.KEY_NAME, element.getString(2));
					object.put(
							ProductTable.KEY_BRAND,
							(element.getString(3) != null ? element
									.getString(3) : ""));
					object.put(
							ProductTable.KEY_MODEL,
							(element.getString(4) != null ? element
									.getString(4) : ""));
					object.put(ProductTable.KEY_PRICE, element.getDouble(5));
					object.put(ProductTable.KEY_DESC, element.getString(6));
				} else
					object.put(ProductTable.KEY_ID_SERVER, idElement_Server);
				
			} else if (tablename.equals(SellTable.TABLE_NAME)) {
				element = new SellTable(context).getSellById(idElement);

				if (element.moveToFirst()) {
					object.put(SellTable.KEY_ROW, idElement);
					object.put(SellTable.KEY_ID_SERVER, element.getLong(1));
					object.put(SellTable.KEY_CUSTOMER, element.getInt(2));
					object.put(CustomerTable.KEY_ID_SERVER, element.getInt(3));
					object.put(SellTable.KEY_CHARGE_TYPE, element.getInt(4));
					object.put(SellTable.KEY_DAY_TO_CHARGE, element.getInt(5));
					object.put(SellTable.KEY_HOUR_TO_CHARGE, element.getInt(6));
					object.put(SellTable.KEY_TOTAL_PAYMENT,
							element.getDouble(7));
					object.put(SellTable.KEY_AGREED_PAYMENT,
							element.getDouble(8));
					object.put(SellTable.KEY_NEXT_PAYMENT, element.getDouble(9));
					object.put(SellTable.KEY_STATE, element.getInt(10));
				} else
					object.put(SellTable.KEY_ID_SERVER, idElement_Server);
				
			} else if (tablename.equals(SellProductTable.TABLE_NAME)) {
				element = new SellProductTable(context)
						.getSellProductById(idElement);

				if (element.moveToFirst()) {
					object.put(SellProductTable.KEY_ROW, idElement);
					object.put(SellProductTable.KEY_ID_SERVER,
							element.getLong(1));
					object.put(SellProductTable.KEY_SELL, element.getLong(2));
					object.put(SellTable.KEY_ID_SERVER, element.getLong(3));
					object.put(SellProductTable.KEY_PRODUCT, element.getLong(4));
					object.put(ProductTable.KEY_ID_SERVER, element.getLong(5));
					object.put(SellProductTable.KEY_AMOUNT, element.getInt(6));
				} else
					object.put(SellProductTable.KEY_ID_SERVER, idElement_Server);
				
			} else if (tablename.equals(PaymentTable.TABLE_NAME)) {
				element = new PaymentTable(context).getPaymentById(idElement);

				if (element.moveToFirst()) {
					object.put(PaymentTable.KEY_ROW, idElement);
					object.put(PaymentTable.KEY_ID_SERVER, element.getLong(1));
					object.put(PaymentTable.KEY_SELL, element.getLong(2));
					object.put(SellTable.KEY_ID_SERVER, element.getLong(3));
					object.put(PaymentTable.KEY_PAYMENT, element.getDouble(4));
					object.put(PaymentTable.KEY_PAYMENT_DATE,
							element.getLong(5));
					object.put(PaymentTable.KEY_PAYMENT_HOUR,
							element.getLong(6));
				} else
					object.put(PaymentTable.KEY_ID_SERVER, idElement_Server);
			}

			object.put(KEY_ACTION, action);
			object.put(KEY_TABLE, tablename);
		} catch (JSONException jsone) {
			object = null;
		} finally {
			try {
				element.close();
			} catch (Exception e) {
				Log.e("Error closing Cursor", e.toString());
			}
		}

		return object;
	}

	/**
	 * Function that updates an element from the Sync Table.
	 * 
	 * @param id
	 *            Unique number of the Sync row, which will be searched for the
	 *            update.
	 * @param tablename
	 *            New name of the table.
	 * @param action
	 *            New action ("insert", "update", "delete").
	 * @param idElement
	 *            Unique number of the element (not of the Sync row).
	 * @return True if the element was updated. False otherwise.
	 */
	public boolean updateSyncElementById(long id, String tablename,
			String action, long idElement) {
		boolean isUpdated = false;
		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_TABLE + " = ?, "
				+ KEY_ACTION + " = ?, " + KEY_ID_ELEMENT + " = ? WHERE "
				+ KEY_ROW + " = ?";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindString(1, tablename);
			stmnt.bindString(2, action);
			stmnt.bindLong(3, idElement);
			stmnt.bindLong(4, id);
			stmnt.execute();
			isUpdated = true;
		} catch (SQLException sqle) {
			isUpdated = false;
			Log.e("Error updateSyncElementById - SyncTable", sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return isUpdated;
	}

	/**
	 * Function that deletes an element from the Sync Table by the Id of the
	 * row.
	 * 
	 * @param id
	 *            Unique number of the row from the Sync Table that'll be
	 *            deleted.
	 * @return True if the element was deleted. False otherwise.
	 */
	public boolean deleteSyncElementById(long id) {
		boolean isDeleted = false;

		try {
			db = dbHelper.getWritableDatabase();
			isDeleted = db.delete(TABLE_NAME, KEY_ROW + " = ?",
					new String[] { String.valueOf(id) }) > 0;
		} catch (SQLException sqle) {
			Log.e("Error deleteSyncElementById - SyncTable", sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return isDeleted;
	}

	/**
	 * Function that deletes an element from the Sync Table by the id of the
	 * element.
	 * 
	 * @param idElement
	 *            Unique number of the element from the Sync Table that'll be
	 *            deleted.
	 * @return True if the element was deleted. False otherwise.
	 */
	public boolean deleteSyncElementByIdElement(long idElement) {
		boolean isDeleted = false;

		try {
			isDeleted = db.delete(TABLE_NAME, KEY_ID_ELEMENT + " = ?",
					new String[] { String.valueOf(idElement) }) > 0;
		} catch (SQLException sqle) {
			Log.e("Error deleteSyncElementByIdElement - SyncTable",
					sqle.toString());
		}

		return isDeleted;
	}

	/**
	 * Function that deletes all the elements from the Sync Table.
	 * 
	 * @return True if the elements were deleted. False otherwise.
	 */
	public boolean deleteAllSyncElements() {
		boolean areDeleted = false;

		try {
			db = dbHelper.getReadableDatabase();
			areDeleted = db.delete(TABLE_NAME, null, null) > 0;
		} catch (SQLException sqle) {
			Log.e("Error deleteAllSyncElements - " + TABLE_NAME,
					sqle.toString());
		}

		return areDeleted;
	}
}
