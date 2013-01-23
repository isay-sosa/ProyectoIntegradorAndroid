package proyecto.integrador.odbyt.DataBase;

import java.util.ArrayList;
import java.util.List;

import proyecto.integrador.odbyt.Customer.Customer;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class CustomerTable {
	public static final String KEY_ROW = "IdCustomer";
	public static final String KEY_ID_SERVER = "IdCustomer_Server";
	public static final String KEY_NAME = "Name";
	public static final String KEY_LASTNAME = "LastName";
	public static final String KEY_ADDRESS = "Address";
	public static final String KEY_CITY = "City";
	public static final String KEY_STATE = "State";
	public static final String KEY_PHONE = "Phone";
	public static final String KEY_CELLPHONE = "Cellphone";
	public static final String KEY_EMAIL = "eMail";
	public static final String TABLE_NAME = "customer";
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "(" + KEY_ROW
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ID_SERVER
			+ " INTEGER," + KEY_NAME + " TEXT NOT NULL," + KEY_LASTNAME
			+ " TEXT NOT NULL," + KEY_ADDRESS + " TEXT NOT NULL," + KEY_CITY
			+ " TEXT NOT NULL," + KEY_STATE + " TEXT NOT NULL," + KEY_PHONE
			+ " TEXT," + KEY_CELLPHONE + " TEXT," + KEY_EMAIL + " TEXT);";

	private Context context = null;
	private SQLiteDatabase db = null;
	private DataBaseHelper dbHelper = null;

	public CustomerTable(Context context) {
		dbHelper = new DataBaseHelper(context);
		this.context = context;
	}

	/**
	 * Function that inserts a customer into the Customer Table.
	 * 
	 * @param idServer
	 *            Unique number (Customer Table from the Server) that'll
	 *            correspond to the customer. If the customer has not IdServer
	 *            then pass 0.
	 * @param name
	 *            Name of the customer.
	 * @param lastname
	 *            LastName of the customer.
	 * @param address
	 *            Address of the customer.
	 * @param city
	 *            City where the customer lives.
	 * @param state
	 *            State where the customer lives.
	 * @param phone
	 *            Phone of the customer. It could be an empty String.
	 * @param cellphone
	 *            Cell phone of the customer. It could be an empty String.
	 * @param email
	 *            Email of the customer. It could be an empty String.
	 * @param isSync
	 *            True if the customer will be synchronized later to the server.
	 *            False otherwise.
	 * @return The row ID if the customer was inserted. Otherwise returns -1.
	 */
	public long insertCustomer(long idServer, String name, String lastname,
			String address, String city, String state, String phone,
			String cellphone, String email, boolean isSync) {
		long idInserted = -1;
		String sql = "INSERT INTO " + TABLE_NAME + "(" + KEY_ID_SERVER + ","
				+ KEY_NAME + "," + KEY_LASTNAME + "," + KEY_ADDRESS + ","
				+ KEY_CITY + "," + KEY_STATE + "," + KEY_PHONE + ","
				+ KEY_CELLPHONE + "," + KEY_EMAIL
				+ ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);
			stmnt.bindString(2, name);
			stmnt.bindString(3, lastname);
			stmnt.bindString(4, address);
			stmnt.bindString(5, city);
			stmnt.bindString(6, state);
			stmnt.bindString(7, phone);
			stmnt.bindString(8, cellphone);
			stmnt.bindString(9, email);

			idInserted = stmnt.executeInsert();
			if (isSync && idInserted > 0) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "insert", idInserted,
						idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error insertCustomer - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets all the customers stored on the Customer Table.
	 * 
	 * @return Cursor object with all the customers. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllCustomers() {
		Cursor elements = null;

		try {
			db = dbHelper.getReadableDatabase();

			elements = db.query(TABLE_NAME, null, null, null, null, null, null);
			if (elements != null)
				elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllCustomers - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets all the customers stored on the Customer Table ordered
	 * by their IdServer.
	 * 
	 * @return Cursor object with all the customers. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllCustomersOrderedByIdServer() {
		Cursor elements = null;
		try {
			db = dbHelper.getReadableDatabase();
			elements = db.query(TABLE_NAME, null, null, null, null, null,
					KEY_ID_SERVER);
			elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllCustomersOrderedByIdServer - " + TABLE_NAME,
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
	 * Function that gets all the customers stored on the Customer Table on a
	 * Customer List.
	 * 
	 * @return List object with all the customers. If an error occurs or there
	 *         are no rows on the table, <i>null</i> is returned.
	 */
	public List<Customer> getAllCustomersOnList() {
		List<Customer> list = null;
		Cursor elements = getAllCustomers();

		try {
			if (elements != null) {
				if (elements.moveToFirst()) {
					list = new ArrayList<Customer>();
					Customer c;

					do {
						c = new Customer();
						c.setId(elements.getLong(0));
						c.setIdServer(elements.getLong(1));
						c.setName(elements.getString(2));
						c.setLastName(elements.getString(3));
						c.setAddress(elements.getString(4));
						c.setCity(elements.getString(5));
						c.setState(elements.getString(6));
						c.setPhone(elements.getString(7));
						c.setCellPhone(elements.getString(8));
						c.setEmail(elements.getString(9));

						list.add(c);
						c = null;
					} while (elements.moveToNext());
				}
			}
		} catch (Exception e) {
			list = null;
			Log.e("Error getAllCustomersOnList - " + TABLE_NAME, e.toString());
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
	 * Function that gets a customer from the Customer Table.
	 * 
	 * @param id
	 *            Unique number of the customer.
	 * @return Cursor object with the customer. If an error occurs <i>null</i>
	 *         is returned. The Cursor object must be closed after it uses.
	 */
	public Cursor getCustomerById(long id) {
		Cursor customer = null;
		try {
			db = dbHelper.getReadableDatabase();
			customer = db.query(TABLE_NAME, null, KEY_ROW + " = ?",
					new String[] { String.valueOf(id) }, null, null, null);

			customer.moveToFirst();
		} catch (SQLException sqle) {
			customer = null;
			Log.e("Error getCustomerById - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return customer;
	}

	/**
	 * Function that gets a customer from the Customer Table on a Customer
	 * object.
	 * 
	 * @param id
	 *            Unique number of the seller.
	 * @return Customer object with the customer information. If an error occurs
	 *         <i>null</i> is returned.
	 */
	public Customer getCustomerById_OnCustomerObject(long id) {
		Customer c = null;
		Cursor element = null;

		try {
			db = dbHelper.getReadableDatabase();
			element = getCustomerById(id);
			if (element.moveToFirst()) {
				c = new Customer();
				c.setId(element.getLong(0));
				c.setIdServer(element.getLong(1));
				c.setName(element.getString(2));
				c.setLastName(element.getString(3));
				c.setAddress(element.getString(4));
				c.setCity(element.getString(5));
				c.setState(element.getString(6));
				c.setPhone(element.getString(7));
				c.setCellPhone(element.getString(8));
				c.setEmail(element.getString(9));
			}
		} catch (SQLException sqle) {
			c = null;
			Log.e("Error getCustomerById_OnCustomerObject - " + TABLE_NAME,
					sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return c;
	}

	/**
	 * Function that gets the Customer's ID by the IdServer from the Customer
	 * Table.
	 * 
	 * @param idServer
	 *            Unique number (Customer Table from the Server) that
	 *            corresponds to the customer.
	 * @return The Customer's ID. If an error occurs -1 is returned.
	 */
	public long getCustomerIdByCustomerIdServer(long idServer) {
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
			Log.e("Error getCustomerIdByCustomerIdServer - " + TABLE_NAME,
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
	 * Function that updates a customer from the Customer Table.
	 * 
	 * @param id
	 *            Unique number of the customer, which will be used to sought
	 *            the customer.
	 * @param idServer
	 *            Unique number (Customer Table from the Server) that'll
	 *            correspond to the customer. If the customer has not IdServer
	 *            then pass 0.
	 * @param name
	 *            Name of the customer.
	 * @param lastname
	 *            LastName of the customer.
	 * @param address
	 *            Address of the customer.
	 * @param city
	 *            City where the customer lives.
	 * @param state
	 *            State where the customer lives.
	 * @param phone
	 *            Phone of the customer. It could be an empty String.
	 * @param cellphone
	 *            Cell phone of the customer. It could be an empty String.
	 * @param email
	 *            Email of the customer. It could be an empty String.
	 * @param isSync
	 *            True if the customer will be synchronized later to the server.
	 *            False otherwise.
	 * @return True if the customer was updated. False otherwise.
	 */
	public boolean updateCustomerById(long id, long idServer, String name,
			String lastname, String address, String city, String state,
			String phone, String cellphone, String email, boolean isSync) {
		boolean isUpdated = false;
		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_ID_SERVER
				+ " = ?, " + KEY_NAME + " = ?, " + KEY_LASTNAME + " = ?, "
				+ KEY_ADDRESS + " = ?, " + KEY_CITY + " = ?, " + KEY_STATE
				+ " = ?, " + KEY_PHONE + " = ?, " + KEY_CELLPHONE + " = ?, "
				+ KEY_EMAIL + " = ? WHERE " + KEY_ROW + " = ?";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("PRAGMA foreign_keys = ON");

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);
			stmnt.bindString(2, name);
			stmnt.bindString(3, lastname);
			stmnt.bindString(4, address);
			stmnt.bindString(5, city);
			stmnt.bindString(6, state);
			stmnt.bindString(7, phone);
			stmnt.bindString(8, cellphone);
			stmnt.bindString(9, email);
			stmnt.bindLong(10, id);

			stmnt.execute();
			isUpdated = true;

			if (isSync) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "update", id, idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error updateCustomerById - CustomerTable", sqle.toString());
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
	 * Function that updates the IdServer of a customer from the Customer Table.
	 * 
	 * @param id
	 *            Unique number of the customer, which will be used to sought
	 *            the customer.
	 * @param idServer
	 *            Unique number (Customer Table from the Server) that'll
	 *            correspond to the customer.
	 * @return True if the IdServer was updated. False otherwise.
	 */
	public boolean updateCustomerIdServerById(long id, long idServer) {
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
			Log.e("Error updateCustomerIdServerById - " + TABLE_NAME,
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
	 * Function that deletes all the customers from the Customer Table.
	 * 
	 * @return True if at least 1 customer was deleted. False otherwise.
	 */
	public boolean deleteAllCustomers() {
		boolean areDeleted = false;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("PRAGMA foreign_keys = ON");

			areDeleted = db.delete(TABLE_NAME, null, null) > 0;
		} catch (SQLException sqle) {
			Log.e("Error deleteAllCustomers - " + TABLE_NAME, sqle.toString());
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
	 * Function that deletes a customer from the Customer Table.
	 * 
	 * @param id
	 *            Unique number of the customer, which will be used to sought
	 *            the customer.
	 * @param isSync
	 *            True if the customer will be synchronized later to the server.
	 *            False otherwise.
	 * @return True if the customer was deleted. False otherwise.
	 */
	public boolean deleteCustomerById(long id, boolean isSync) {
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
			Log.e("Error deleteCustomerById - CustomerTable", sqle.toString());
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
