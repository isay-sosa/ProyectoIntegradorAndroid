package proyecto.integrador.odbyt.DataBase;

import proyecto.integrador.odbyt.Preferences.OdbytPreferences;
import proyecto.integrador.odbyt.Security.Encrypt;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class SellerTable {
	public static final String KEY_ROW = "IdSeller";
	public static final String KEY_ID_SERVER = "IdSeller_Server";
	public static final String KEY_USER = "Username";
	public static final String KEY_PWD = "Password";
	public static final String KEY_NAME = "Name";
	public static final String KEY_LASTNAME = "LastName";
	public static final String TABLE_NAME = "seller";
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "(" + KEY_ROW
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ID_SERVER
			+ " INTEGER," + KEY_USER + " TEXT NOT NULL," + KEY_PWD
			+ " TEXT NOT NULL," + KEY_NAME + " TEXT NOT NULL," + KEY_LASTNAME
			+ " TEXT NOT NULL);";

	private Context context;
	private SQLiteDatabase db = null;
	private DataBaseHelper dbHelper = null;

	public SellerTable(Context context) {
		dbHelper = new DataBaseHelper(context);
		this.context = context;
	}

	/**
	 * Function that allows to get access to the system.
	 * 
	 * @param user
	 *            User name of the seller.
	 * @param pwd
	 *            Password of the seller.
	 * @return True if access is allowed. False otherwise.
	 */
	public boolean LogIn(String user, String pwd) {
		Cursor element = null;
		boolean access = false;

		try {
			db = dbHelper.getReadableDatabase();
			element = db.query(TABLE_NAME, null, KEY_USER + " = ? AND "
					+ KEY_PWD + " = ?",
					new String[] { user, Encrypt.MD5(pwd) }, null, null, null);

			if (element != null && element.moveToFirst()) {
				access = true;

				OdbytPreferences prefs = new OdbytPreferences(context);

				if (prefs.getId() == 0)
					prefs.setId(element.getLong(0));

				if (prefs.getIdServer() == 0)
					prefs.setIdServer(element.getLong(1));

				prefs.setName(element.getString(4));
				prefs.setLastName(element.getString(5));

				prefs = null;
			}
		} catch (SQLException sqle) {
			access = false;
			Log.e("Error LogIn - SellerTable", sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return access;
	}

	/**
	 * Function that inserts a seller into the Seller Table.
	 * 
	 * @param idServer
	 *            Unique number (Seller Table from the Server) that'll
	 *            correspond to the seller. If the seller has not IdServer then
	 *            pass 0.
	 * @param username
	 *            User name of the seller.
	 * @param pwd
	 *            Password of the seller.
	 * @param name
	 *            Name of the seller.
	 * @param lastname
	 *            LastName of the seller.
	 * @param isSync
	 *            True if the seller will be synchronized later to the server.
	 *            False otherwise.
	 * @return The row ID if the seller was inserted. Otherwise returns -1.
	 */
	public long insertSeller(long idServer, String username, String pwd,
			String name, String lastname, boolean isSync) {
		long idInserted = -1;
		String sql = "INSERT INTO " + TABLE_NAME + "(" + KEY_ID_SERVER + ","
				+ KEY_USER + "," + KEY_PWD + "," + KEY_NAME + ","
				+ KEY_LASTNAME + ") VALUES(?, ?, ?, ?, ?);";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);
			stmnt.bindString(2, username);
			stmnt.bindString(3, Encrypt.MD5(pwd));
			stmnt.bindString(4, name);
			stmnt.bindString(5, lastname);

			idInserted = stmnt.executeInsert();
			if (isSync && idInserted > 0) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "insert", idInserted,
						idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error insertSeller - SellerTable", sqle.toString());
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
	 * Function that gets all the sellers stored on the Seller Table.
	 * 
	 * @return Cursor object with all the sellers. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllSellers() {
		Cursor elements = null;

		try {
			db = dbHelper.getReadableDatabase();
			elements = db.query(TABLE_NAME, null, null, null, null, null, null);
			elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllSellers - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets all the sellers stored on the Seller Table ordered by
	 * their IdServer.
	 * 
	 * @return Cursor object with all the sellers. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllSellersOrderedByIdServer() {
		Cursor elements = null;
		try {
			db = dbHelper.getReadableDatabase();
			elements = db.query(TABLE_NAME, null, null, null, null, null,
					KEY_ID_SERVER);
			elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllSellersOrderedByIdServer - " + TABLE_NAME,
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
	 * Function that gets a seller from the Seller Table.
	 * 
	 * @param id
	 *            Unique number of the seller.
	 * @return Cursor object with the seller. If an error occurs <i>null</i> is
	 *         returned. The Cursor object must be closed after it uses.
	 */
	public Cursor getSellerById(long id) {
		Cursor element = null;
		try {
			db = dbHelper.getReadableDatabase();
			element = db.query(TABLE_NAME, null, KEY_ROW + " = ?",
					new String[] { String.valueOf(id) }, null, null, null);

			element.moveToFirst();
		} catch (SQLException sqle) {
			element = null;
			Log.e("Error getSellerById - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return element;
	}

	/**
	 * Function that updates the IdServer of a seller from the Seller Table.
	 * 
	 * @param id
	 *            Unique number of the seller, which will be used to sought the
	 *            seller.
	 * @param idServer
	 *            Unique number (Seller Table from the Server) that'll
	 *            correspond to the seller.
	 * @return True if the IdServer was updated. False otherwise.
	 */
	public boolean updateSellerIdServerById(long id, long idServer) {
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
			Log.e("Error updateSellerIdServerById - " + TABLE_NAME,
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
	 * Function that updates a seller from the Seller Table.
	 * 
	 * @param id
	 *            Unique number of the seller, which will be used to sought the
	 *            seller.
	 * @param idServer
	 *            Unique number (Seller Table from the Server) that'll
	 *            correspond to the seller. If the seller has not IdServer then
	 *            pass 0.
	 * @param username
	 *            User name of the seller.
	 * @param pwd
	 *            Password of the seller.
	 * @param name
	 *            Name of the seller.
	 * @param lastname
	 *            LastName of the seller.
	 * @param isSync
	 *            True if the seller will be synchronized later to the server.
	 *            False otherwise.
	 * @return True if the seller was updated. False otherwise.
	 */
	public boolean updateSellerById(long id, long idServer, String username,
			String pwd, String name, String lastname, boolean isSync) {
		boolean isUpdated = false;
		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_ID_SERVER
				+ " = ?, " + KEY_USER + " = ?, " + KEY_PWD + " = ?, "
				+ KEY_NAME + " = ?, " + KEY_LASTNAME + " = ? WHERE " + KEY_ROW
				+ " = ?";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);
			stmnt.bindString(2, username);
			stmnt.bindString(3, pwd);
			stmnt.bindString(4, name);
			stmnt.bindString(5, lastname);
			stmnt.bindLong(6, id);

			stmnt.execute();
			isUpdated = true;

			if (isSync) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "update", id, idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error updateSellerById - " + TABLE_NAME, sqle.toString());
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
	 * Function that deletes all the sellers from the Seller Table.
	 * 
	 * @return True if at least 1 seller was deleted. False otherwise.
	 */
	public boolean deleteAllSellers() {
		boolean areDeleted = false;

		try {
			db = dbHelper.getWritableDatabase();
			areDeleted = db.delete(TABLE_NAME, null, null) > 0;
		} catch (SQLException sqle) {
			Log.e("Error deleteAllSellers - " + TABLE_NAME, sqle.toString());
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
	 * Function that deletes a seller from the Seller Table.
	 * 
	 * @param id
	 *            Unique number of the seller, which will be used to sought the
	 *            seller.
	 * @param isSync
	 *            True if the seller will be synchronized later to the server.
	 *            False otherwise.
	 * @return True if the seller was deleted. False otherwise.
	 */
	public boolean deleteSellerById(long id, boolean isSync) {
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
			Log.e("Error deleteSellerById - " + TABLE_NAME, sqle.toString());
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
