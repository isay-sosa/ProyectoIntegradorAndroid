package proyecto.integrador.odbyt.DataBase;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class SellProductTable {
	public static final String KEY_ROW = "IdSellProduct";
	public static final String KEY_ID_SERVER = "IdSellProduct_Server";
	public static final String KEY_SELL = "IdSell";
	public static final String KEY_PRODUCT = "IdProduct";
	public static final String KEY_AMOUNT = "Amount";
	public static final String TABLE_NAME = "sell_product";
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "(" + KEY_ROW
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ID_SERVER
			+ " INTEGER," + KEY_SELL + " INTEGER NOT NULL REFERENCES "
			+ SellTable.TABLE_NAME + "(" + KEY_SELL
			+ ") ON UPDATE CASCADE ON DELETE CASCADE," + KEY_PRODUCT
			+ " INTEGER NOT NULL REFERENCES " + ProductTable.TABLE_NAME + "("
			+ KEY_PRODUCT + ") ON UPDATE CASCADE ON DELETE CASCADE, "
			+ KEY_AMOUNT + " INTEGER NOT NULL);";

	private Context context = null;
	private SQLiteDatabase db = null;
	private DataBaseHelper dbHelper = null;

	public SellProductTable(Context context) {
		dbHelper = new DataBaseHelper(context);
		this.context = context;
	}

	/**
	 * Function that inserts a sell_product into the SellProduct Table.
	 * 
	 * @param idServer
	 *            Unique number (SellProduct Table from the Server) that'll
	 *            correspond to the sell_product. If the sell_product has not
	 *            IdServer then pass 0.
	 * @param idSell
	 *            Sell's unique number that represents a sell element from the
	 *            Sell Table.
	 * @param idProduct
	 *            Product's unique number that represents a product element from
	 *            the Product Table.
	 * @param amount
	 *            Amount of the product that has been sold.
	 * @param isSync
	 *            True if the sell_product will be synchronized later to the server.
	 *            False otherwise.
	 * @param isFromJSON
	 *            True if the sell_product information comes from a JSON file. False
	 *            otherwise.
	 * @return The row ID if the sell_product was inserted. Otherwise returns
	 *         -1.
	 */
	public long insertSellProduct(long idServer, long idSell, long idProduct,
			int amount, boolean isSync, boolean isFromJSON) {
		long idInserted = -1;
		String sql = "INSERT INTO " + TABLE_NAME + "(" + KEY_ID_SERVER + ","
				+ KEY_SELL + "," + KEY_PRODUCT + "," + KEY_AMOUNT
				+ ") VALUES (?, ?, ?, ?);";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();
			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);

			if (isFromJSON) {
				stmnt.bindLong(2,
						new SellTable(context).getSellIdBySellIdServer(idSell));
				stmnt.bindLong(3, new ProductTable(context)
						.getProductIdByProductIdServer(idProduct));
			} else {
				stmnt.bindLong(2, idSell);
				stmnt.bindLong(3, idProduct);
			}

			stmnt.bindLong(4, amount);

			idInserted = stmnt.executeInsert();
			if (isSync && idInserted > 0) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "insert", idInserted,
						idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error insertSellProduct - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return idInserted;
	}

	/**
	 * Function that gets all the sell_products stored on the SellProduct Table.
	 * 
	 * @return Cursor object with all the sell_products. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllSellProducts() {
		Cursor elements = null;
		String sql = "SELECT * FROM " + TABLE_NAME;

		try {
			db = dbHelper.getWritableDatabase();

			elements = db.rawQuery(sql, null);
			if (elements != null)
				elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllSellProducts - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets all the sell_products stored on the SellProduct Table
	 * ordered by their IdServer.
	 * 
	 * @return Cursor object with all the sell_products. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllSellProductsOrderedByIdServer() {
		Cursor elements = null;
		try {
			db = dbHelper.getReadableDatabase();
			elements = db.query(TABLE_NAME, null, null, null, null, null,
					KEY_ID_SERVER);
			elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllSellProductsOrderedByIdServer - " + TABLE_NAME,
					sqle.toString());
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
	 * Function that gets a sell_product from the SellProduct Table.
	 * 
	 * @param id
	 *            Unique number of the sell_product.
	 * @return Cursor object with the sell_product. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getSellProductById(long id) {
		Cursor sell_product = null;
		String sql = "SELECT " + KEY_ROW + "," + KEY_ID_SERVER + ","
				+ SellTable.TABLE_NAME + "." + KEY_SELL + ","
				+ SellTable.TABLE_NAME + "." + SellTable.KEY_ID_SERVER + ","
				+ ProductTable.TABLE_NAME + "." + KEY_PRODUCT + ","
				+ ProductTable.TABLE_NAME + "." + ProductTable.KEY_ID_SERVER
				+ "," + KEY_AMOUNT + " FROM " + TABLE_NAME + " INNER JOIN "
				+ SellTable.TABLE_NAME + " ON " + TABLE_NAME + "." + KEY_SELL
				+ " = " + SellTable.TABLE_NAME + "." + KEY_SELL
				+ " INNER JOIN " + ProductTable.TABLE_NAME + " ON "
				+ TABLE_NAME + "." + KEY_PRODUCT + " = "
				+ ProductTable.TABLE_NAME + "." + KEY_PRODUCT + " WHERE "
				+ KEY_ROW + " = ?";
		try {
			db = dbHelper.getReadableDatabase();
			sell_product = db
					.rawQuery(sql, new String[] { String.valueOf(id) });

			sell_product.moveToFirst();
		} catch (SQLException sqle) {
			sell_product = null;
			Log.e("Error getSellProductById - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return sell_product;
	}

	/**
	 * Function that gets all the products of a sell.
	 * 
	 * @param idSell
	 *            Sell's unique number that represents a sell element from the
	 *            Sell Table.
	 * @return List of strings with the names of the products. If an error
	 *         occurs or there are no rows on the table, <i>null</i> is
	 *         returned.
	 */
	public List<String> getProductsBySellId(long idSell) {
		List<String> products = null;
		Cursor elements = null;
		String sql = "SELECT " + ProductTable.TABLE_NAME + "."
				+ ProductTable.KEY_NAME + " FROM " + TABLE_NAME
				+ " INNER JOIN " + ProductTable.TABLE_NAME + " ON "
				+ TABLE_NAME + "." + KEY_PRODUCT + " = "
				+ ProductTable.TABLE_NAME + "." + KEY_PRODUCT + " WHERE "
				+ TABLE_NAME + "." + KEY_SELL + " = ?";
		try {
			db = dbHelper.getReadableDatabase();
			elements = db
					.rawQuery(sql, new String[] { String.valueOf(idSell) });

			if (elements.moveToFirst()) {
				products = new ArrayList<String>();
				do {
					products.add(elements.getString(0));
				} while (elements.moveToNext());
			}
		} catch (SQLException sqle) {
			products = null;
			Log.e("Error getProductsBySellId - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				elements.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return products;
	}

	/**
	 * Function that updates the IdServer of a sell_product from the SellProduct
	 * Table.
	 * 
	 * @param id
	 *            Unique number of the sell_product, which will be used to
	 *            sought the sell_product.
	 * @param idServer
	 *            Unique number (SellProduct Table from the Server) that'll
	 *            correspond to the sell_product.
	 * @return True if the IdServer was updated. False otherwise.
	 */
	public boolean updateSellProductIdServerById(long id, long idServer) {
		boolean isUpdated = false;
		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_ID_SERVER
				+ " = ? WHERE " + KEY_ROW + " = ?";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);
			stmnt.bindLong(2, id);

			stmnt.execute();
			isUpdated = true;
		} catch (SQLException sqle) {
			Log.e("Error updateSellProductIdServerById - " + TABLE_NAME,
					sqle.toString());
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
	 * @param id
	 *            Unique number of the sell_product, which will be used to
	 *            sought the sell_product.
	 * @param idServer
	 *            Unique number (SellProduct Table from the Server) that'll
	 *            correspond to the sell_product. If the sell_product has not
	 *            IdServer then pass 0.
	 * @param idSell
	 *            Sell's unique number that represents a sell element from the
	 *            Sell Table.
	 * @param idProduct
	 *            Product's unique number that represents a product element from
	 *            the Product Table.
	 * @param amount
	 *            Amount of the product that has been sold.
	 * @param isSync
	 *            True if the product will be synchronized later to the server.
	 *            False otherwise.
	 * @param isFromJSON
	 *            True if the sell information comes from a JSON file. False
	 *            otherwise.
	 * @return True if the sell_product was updated. False otherwise.
	 */
	public boolean updateSellProductById(long id, long idServer, long idSell,
			long idProduct, int amount, boolean isSync, boolean isFromJSON) {
		boolean isUpdated = false;
		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_ID_SERVER
				+ " = ?, " + KEY_SELL + " = ?, " + KEY_PRODUCT + " = ?, "
				+ KEY_AMOUNT + " = ? WHERE " + KEY_ROW + " = ?";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);

			if (isFromJSON) {
				stmnt.bindLong(2,
						new SellTable(context).getSellIdBySellIdServer(idSell));
				stmnt.bindLong(3, new ProductTable(context)
						.getProductIdByProductIdServer(idProduct));
			} else {
				stmnt.bindLong(2, idSell);
				stmnt.bindLong(3, idProduct);
			}

			stmnt.bindLong(4, amount);
			stmnt.bindLong(5, id);

			stmnt.execute();
			isUpdated = true;

			if (isSync) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "update", id, idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error updateSellProductById - " + TABLE_NAME,
					sqle.toString());
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
	 * Function that deletes all the sell_products from the SellProduct Table.
	 * 
	 * @return True if at least 1 sell_product was deleted. False otherwise.
	 */
	public boolean deleteAllSellProducts() {
		boolean areDeleted = false;

		try {
			db = dbHelper.getWritableDatabase();
			areDeleted = db.delete(TABLE_NAME, null, null) > 0;
		} catch (SQLException sqle) {
			Log.e("Error deleteAllSellProducts - " + TABLE_NAME,
					sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return areDeleted;
	}

	/**
	 * Function that deletes a sell_product from the SellProduct Table.
	 * 
	 * @param id
	 *            Unique number of the sell_product, which will be used to
	 *            sought the sell_product.
	 * @param isSync
	 *            True if the sell_product will be synchronized later to the
	 *            server. False otherwise.
	 * @return True if the sell_product was deleted. False otherwise.
	 */
	public boolean deleteSellProductById(long id, boolean isSync) {
		boolean isDeleted = false;
		Cursor element = null;

		try {
			db = dbHelper.getWritableDatabase();

			long idServer = -1;
			element = db.query(TABLE_NAME, new String[] { KEY_ID_SERVER },
					KEY_ROW + " = ?", new String[] { String.valueOf(id) },
					null, null, null);
			element.moveToFirst();
			idServer = element.getLong(0);

			isDeleted = db.delete(TABLE_NAME, KEY_ROW + " = ?",
					new String[] { String.valueOf(id) }) > 0;

			if (isSync && isDeleted) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "delete", id, idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error deleteSellProductById - " + TABLE_NAME,
					sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return isDeleted;
	}
}
