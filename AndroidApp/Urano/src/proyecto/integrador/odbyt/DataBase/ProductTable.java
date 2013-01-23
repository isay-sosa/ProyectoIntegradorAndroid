package proyecto.integrador.odbyt.DataBase;

import java.util.ArrayList;
import java.util.List;

import proyecto.integrador.odbyt.Product.Product;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class ProductTable {
	public static final String KEY_ROW = "IdProduct";
	public static final String KEY_ID_SERVER = "IdProduct_Server";
	public static final String KEY_NAME = "Name";
	public static final String KEY_BRAND = "Brand";
	public static final String KEY_MODEL = "Model";
	public static final String KEY_PRICE = "Price";
	public static final String KEY_DESC = "Description";
	public static final String TABLE_NAME = "product";
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "(" + KEY_ROW
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ID_SERVER
			+ " INTEGER," + KEY_NAME + " TEXT NOT NULL," + KEY_BRAND + " TEXT,"
			+ KEY_MODEL + " TEXT," + KEY_PRICE + " NUMERIC NOT NULL,"
			+ KEY_DESC + " TEXT);";

	private Context context = null;
	private SQLiteDatabase db = null;
	private DataBaseHelper dbHelper = null;

	public ProductTable(Context context) {
		dbHelper = new DataBaseHelper(context);
		this.context = context;
	}

	/**
	 * Function that inserts a product into the Product Table.
	 * 
	 * @param idServer
	 *            Unique number (Product Table from the Server) that'll
	 *            correspond to the product. If the product has not IdServer
	 *            then pass 0.
	 * @param name
	 *            Name of the product.
	 * @param brand
	 *            Brand of the product. It could be an empty String.
	 * @param model
	 *            Model of the product. It could be an empty String.
	 * @param price
	 *            Price of the product.
	 * @param desc
	 *            Description of the product. It could be an empty String.
	 * @param isSync
	 *            True if the product will be synchronized later to the server.
	 *            False otherwise.
	 * @return The row ID if the product was inserted. Otherwise returns -1.
	 */
	public long insertProduct(long idServer, String name, String brand,
			String model, double price, String desc, boolean isSync) {
		long idInserted = -1;
		String sql = "INSERT INTO " + TABLE_NAME + "(" + KEY_ID_SERVER + ","
				+ KEY_NAME + "," + KEY_BRAND + "," + KEY_MODEL + ","
				+ KEY_PRICE + "," + KEY_DESC + ") VALUES (?, ?, ?, ?, ?, ?);";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);
			stmnt.bindString(2, name);
			stmnt.bindString(3, brand);
			stmnt.bindString(4, model);
			stmnt.bindDouble(5, price);
			stmnt.bindString(6, desc);

			idInserted = stmnt.executeInsert();
			if (isSync && idInserted > 0) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "insert", idInserted,
						idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error insertProduct - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets the Product's ID by the IdServer from the Product
	 * Table.
	 * 
	 * @param idServer
	 *            Unique number (Product Table from the Server) that corresponds
	 *            to the product.
	 * @return The Product's ID. If an error occurs -1 is returned.
	 */
	public long getProductIdByProductIdServer(long idServer) {
		long id = -1;
		Cursor element = null;

		try {
			db = dbHelper.getReadableDatabase();
			element = db
					.query(TABLE_NAME, new String[] { KEY_ROW }, KEY_ID_SERVER
							+ " = ?",
							new String[] { String.valueOf(idServer) }, null,
							null, null);
			if (element.moveToFirst())
				id = element.getLong(0);
		} catch (SQLException sqle) {
			id = -1;
			Log.e("Error getProductIdByProductIdServer - " + TABLE_NAME,
					sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return id;
	}

	/**
	 * Function that gets a product from the Product Table.
	 * 
	 * @param id
	 *            Unique number of the product.
	 * @return Cursor object with the product. If an error occurs <i>null</i> is
	 *         returned. The Cursor object must be closed after it uses.
	 */
	public Cursor getProductById(long id) {
		Cursor product = null;
		try {
			db = dbHelper.getReadableDatabase();
			product = db.query(TABLE_NAME, null, KEY_ROW + " = ?",
					new String[] { String.valueOf(id) }, null, null, null);

			product.moveToFirst();
		} catch (SQLException sqle) {
			product = null;
			Log.e("Error getProductById - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return product;
	}

	/**
	 * Function that gets all the products stored on the Product Table.
	 * 
	 * @return Cursor object with all the products. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllProducts() {
		Cursor elements = null;
		String sql = "SELECT * FROM " + TABLE_NAME;

		try {
			db = dbHelper.getWritableDatabase();

			elements = db.rawQuery(sql, null);
			if (elements != null)
				elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllProducts - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets all the products stored on the Product Table ordered
	 * by their IdServer.
	 * 
	 * @return Cursor object with all the products. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllProductsOrderedByIdServer() {
		Cursor elements = null;
		try {
			db = dbHelper.getReadableDatabase();
			elements = db.query(TABLE_NAME, null, null, null, null, null,
					KEY_ID_SERVER);
			elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllProductsOrderedByIdServer - " + TABLE_NAME,
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
	 * Function that gets all the products stored on the Product Table on a
	 * Product List.
	 * 
	 * @return List object with all the products. If an error occurs or there
	 *         are no rows on the table, <i>null</i> is returned.
	 */
	public List<Product> getAllProductsOnList() {
		List<Product> list = null;
		Cursor elements = getAllProducts();

		try {
			if (elements != null) {
				if (elements.moveToFirst()) {
					list = new ArrayList<Product>();
					Product p;

					do {
						p = new Product();
						p.setId(elements.getLong(0));
						p.setIdServer(elements.getLong(1));
						p.setName(elements.getString(2));
						p.setBrand(elements.getString(3));
						p.setModel(elements.getString(4));
						p.setPrice(elements.getDouble(5));
						p.setDesc(elements.getString(6));

						list.add(p);
						p = null;
					} while (elements.moveToNext());
				}
			}
		} catch (SQLException sqle) {
			list = null;
			Log.e("Error getAllProductsOnList - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				elements.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return list;
	}

	/**
	 * Function that gets the price of a product.
	 * 
	 * @param id
	 *            Unique number of the product.
	 * @return The price of the product.
	 */
	public double getPriceById(long id) {
		double price = 0.0;
		Cursor element = null;

		try {
			db = dbHelper.getReadableDatabase();
			element = db.query(TABLE_NAME, new String[] { KEY_PRICE }, KEY_ROW
					+ " = ?", new String[] { String.valueOf(id) }, null, null,
					null);
			if (element.moveToFirst())
				price = element.getDouble(0);
		} catch (SQLException sqle) {
			Log.e("Error getPriceById - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return price;
	}

	/**
	 * Function that updates a product from the Product Table.
	 * 
	 * @param id
	 *            Unique number of the product, which will be used to sought the
	 *            product.
	 * @param idServer
	 *            Unique number (Product Table from the Server) that'll
	 *            correspond to the product. If the product has not IdServer
	 *            then pass 0.
	 * @param name
	 *            Name of the product.
	 * @param brand
	 *            Brand of the product. It could be an empty String.
	 * @param model
	 *            Model of the product. It could be an empty String.
	 * @param price
	 *            Price of the product.
	 * @param desc
	 *            Description of the product. It could be an empty String.
	 * @param isSync
	 *            True if the product will be synchronized later to the server.
	 *            False otherwise.
	 * @return True if the product was updated. False otherwise.
	 */
	public boolean updateProductById(long id, long idServer, String name,
			String brand, String model, double price, String desc,
			boolean isSync) {
		boolean isUpdated = false;
		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_ID_SERVER
				+ " = ?, " + KEY_NAME + " = ?, " + KEY_BRAND + " = ?, "
				+ KEY_MODEL + " = ?, " + KEY_PRICE + " = ?, " + KEY_DESC
				+ " = ? WHERE " + KEY_ROW + " = ?";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("PRAGMA foreign_keys = ON");

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);
			stmnt.bindString(2, name);
			stmnt.bindString(3, brand);
			stmnt.bindString(4, model);
			stmnt.bindDouble(5, price);
			stmnt.bindString(6, desc);
			stmnt.bindLong(7, id);

			stmnt.execute();
			isUpdated = true;

			if (isSync) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "update", id, idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error updateProductById - " + TABLE_NAME, sqle.toString());
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
	 * Function that updates the IdServer of a product from the Product Table.
	 * 
	 * @param id
	 *            Unique number of the product, which will be used to sought the
	 *            product.
	 * @param idServer
	 *            Unique number (Product Table from the Server) that'll
	 *            correspond to the product.
	 * @return True if the IdServer was updated. False otherwise.
	 */
	public boolean updateProductIdServerById(long id, long idServer) {
		boolean isUpdated = false;
		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_ID_SERVER
				+ " = ? WHERE " + KEY_ROW + " = ?";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("PRAGMA foreign_keys = ON");

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);
			stmnt.bindLong(2, id);

			stmnt.execute();
			isUpdated = true;
		} catch (SQLException sqle) {
			Log.e("Error updateProductIdServerById - " + TABLE_NAME,
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
	 * Function that deletes all the products from the Product Table.
	 * 
	 * @return True if at least 1 product was deleted. False otherwise.
	 */
	public boolean deleteAllProducts() {
		boolean areDeleted = false;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("PRAGMA foreign_keys = ON");

			areDeleted = db.delete(TABLE_NAME, null, null) > 0;
		} catch (SQLException sqle) {
			Log.e("Error deleteAllProducts - " + TABLE_NAME, sqle.toString());
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
	 * Function that deletes a product from the Product Table.
	 * 
	 * @param id
	 *            Unique number of the product, which will be used to sought the
	 *            product.
	 * @param isSync
	 *            True if the product will be synchronized later to the server.
	 *            False otherwise.
	 * @return True if the product was deleted. False otherwise.
	 */
	public boolean deleteProductById(long id, boolean isSync) {
		boolean isDeleted = false;
		Cursor element = null;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("PRAGMA foreign_keys = ON");

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
			Log.e("Error deleteProductById - " + TABLE_NAME, sqle.toString());
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
