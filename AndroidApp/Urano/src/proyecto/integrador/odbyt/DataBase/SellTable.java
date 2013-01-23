package proyecto.integrador.odbyt.DataBase;

import java.util.ArrayList;
import java.util.List;

import proyecto.integrador.odbyt.Customer.Customer;
import proyecto.integrador.odbyt.Payment.Payment;
import proyecto.integrador.odbyt.Sell.Sell;
import proyecto.integrador.odbyt.Utils.Convert;
import proyecto.integrador.odbyt.Utils.DateTimeManage;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class SellTable {
	public static final String KEY_ROW = "IdSell";
	public static final String KEY_ID_SERVER = "IdSell_Server";
	public static final String KEY_CUSTOMER = "IdCustomer";
	public static final String KEY_CHARGE_TYPE = "ChargeType";
	public static final String KEY_DAY_TO_CHARGE = "DayToCharge";
	public static final String KEY_HOUR_TO_CHARGE = "HourToCharge";
	public static final String KEY_TOTAL_PAYMENT = "TotalPayment";
	public static final String KEY_AGREED_PAYMENT = "AgreedPayment";
	public static final String KEY_NEXT_PAYMENT = "NextPayment";
	public static final String KEY_STATE = "State";
	public static final String TABLE_NAME = "sell";
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "(" + KEY_ROW
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ID_SERVER
			+ " INTEGER," + KEY_CUSTOMER + " INTEGER NOT NULL REFERENCES "
			+ CustomerTable.TABLE_NAME + "(" + KEY_CUSTOMER
			+ ") ON UPDATE CASCADE ON DELETE CASCADE," + KEY_CHARGE_TYPE
			+ " INTEGER NOT NULL," + KEY_DAY_TO_CHARGE + " INTEGER NOT NULL,"
			+ KEY_HOUR_TO_CHARGE + " INTEGER NOT NULL," + KEY_TOTAL_PAYMENT
			+ " NUMERIC," + KEY_AGREED_PAYMENT + " NUMERIC," + KEY_NEXT_PAYMENT
			+ " NUMERIC," + KEY_STATE + " INTEGER);";

	private Context context = null;
	private SQLiteDatabase db = null;
	private DataBaseHelper dbHelper = null;

	public SellTable(Context context) {
		dbHelper = new DataBaseHelper(context);
		this.context = context;
	}

	/**
	 * Function that inserts a sell into the Sell Table.
	 * 
	 * @param idServer
	 *            Unique number (Sell Table from the Server) that'll correspond
	 *            to the sell. If the sell has not IdServer then pass 0.
	 * @param idCustomer
	 *            Unique number of the customer, which is related to this sell.
	 * @param chargetype
	 *            1 if is every week, 2 if is every two weeks, 3 if is every
	 *            month.
	 * @param daytocharge
	 *            1 if is Monday, 2 if is Tuesday, 3 if is Wednesday, 4 if is
	 *            Thursday, 5 if is Friday, 6 if is Saturday.
	 * @param hourtocharge
	 *            Time in which the seller is going to pass to charge. The time
	 *            must be in long format (hhmm).
	 * @param totalpayment
	 *            Total of the sell.
	 * @param agreedpayment
	 *            Payment that the customer will pay every time the seller pass
	 *            to charge.
	 * @param nextpayment
	 *            Date of the next payment. It's when the seller is going to
	 *            pass again to charge. The date must be in long format
	 *            (yyyymmdd).
	 * @param state
	 *            1 if is still paying, 0 if is finished.
	 * @param isSync
	 *            True if the sell will be synchronized later to the server.
	 *            False otherwise.
	 * @param isFromJSON
	 *            True if the sell information comes from a JSON file. False
	 *            otherwise.
	 * @return The row ID if the sell was inserted. Otherwise returns -1.
	 */
	public long insertSell(long idServer, long idCustomer, int chargetype,
			int daytocharge, long hourtocharge, double totalpayment,
			double agreedpayment, long nextpayment, int state, boolean isSync,
			boolean isFromJSON) {
		long idInserted = -1;
		String sql = "INSERT INTO " + TABLE_NAME + "(" + KEY_ID_SERVER + ","
				+ KEY_CUSTOMER + "," + KEY_CHARGE_TYPE + ","
				+ KEY_DAY_TO_CHARGE + "," + KEY_HOUR_TO_CHARGE + ","
				+ KEY_TOTAL_PAYMENT + "," + KEY_AGREED_PAYMENT + ","
				+ KEY_NEXT_PAYMENT + "," + KEY_STATE
				+ ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);

			if (isFromJSON)
				stmnt.bindLong(2, new CustomerTable(context)
						.getCustomerIdByCustomerIdServer(idCustomer));
			else
				stmnt.bindLong(2, idCustomer);

			stmnt.bindLong(3, chargetype);
			stmnt.bindLong(4, daytocharge);
			stmnt.bindLong(5, hourtocharge);
			stmnt.bindDouble(6, totalpayment);
			stmnt.bindDouble(7, agreedpayment);
			stmnt.bindLong(8, nextpayment);
			stmnt.bindLong(9, state);

			idInserted = stmnt.executeInsert();
			if (isSync && idInserted > 0) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "insert", idInserted,
						idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			idInserted = -1;
			Log.e("Error insertSell - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets all the sells stored on the Sell Table.
	 * 
	 * @return Cursor object with all the sells. If an error occurs <i>null</i>
	 *         is returned. The Cursor object must be closed after it uses.
	 */
	public Cursor getAllSells() {
		Cursor elements = null;
		String sql = "SELECT * FROM " + TABLE_NAME;

		try {
			db = dbHelper.getWritableDatabase();

			elements = db.rawQuery(sql, null);
			if (elements != null)
				elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllSells - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets all the sells stored on the Sell Table ordered by
	 * their IdServer.
	 * 
	 * @return Cursor object with all the sells. If an error occurs <i>null</i>
	 *         is returned. The Cursor object must be closed after it uses.
	 */
	public Cursor getAllSellsOrderedByIdServer() {
		Cursor elements = null;
		try {
			db = dbHelper.getReadableDatabase();
			elements = db.query(TABLE_NAME, null, null, null, null, null,
					KEY_ID_SERVER);
			elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllSellsOrderedByIdServer - " + TABLE_NAME,
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
	 * Function that gets a sell from the Sell Table.
	 * 
	 * @param id
	 *            Unique number of the sell.
	 * @return Cursor object with the sell. If an error occurs <i>null</i> is
	 *         returned. The Cursor object must be closed after it uses.
	 */
	public Cursor getSellById(long id) {
		Cursor sell = null;
		String sql = "SELECT " + KEY_ROW + "," + KEY_ID_SERVER + ","
				+ CustomerTable.TABLE_NAME + "." + KEY_CUSTOMER + ","
				+ CustomerTable.TABLE_NAME + "." + CustomerTable.KEY_ID_SERVER
				+ "," + KEY_CHARGE_TYPE + "," + KEY_DAY_TO_CHARGE + ","
				+ KEY_HOUR_TO_CHARGE + "," + KEY_TOTAL_PAYMENT + ","
				+ KEY_AGREED_PAYMENT + "," + KEY_NEXT_PAYMENT + ","
				+ TABLE_NAME + "." + KEY_STATE + " FROM " + TABLE_NAME
				+ " INNER JOIN " + CustomerTable.TABLE_NAME + " ON "
				+ TABLE_NAME + "." + KEY_CUSTOMER + " = "
				+ CustomerTable.TABLE_NAME + "." + KEY_CUSTOMER + " WHERE "
				+ KEY_ROW + " = ?";
		try {
			db = dbHelper.getReadableDatabase();
			sell = db.rawQuery(sql, new String[] { String.valueOf(id) });

			sell.moveToFirst();
		} catch (SQLException sqle) {
			sell = null;
			Log.e("Error getSellById - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return sell;
	}

	/**
	 * Function that gets the total of a sell from the Sell Table.
	 * 
	 * @param id
	 *            Unique number of the sell.
	 * @return The total of the sell.
	 */
	public double getTotalBySellId(long id) {
		double total = 0.0;
		Cursor element = null;

		try {
			db = dbHelper.getReadableDatabase();
			element = db.query(TABLE_NAME, new String[] { KEY_TOTAL_PAYMENT },
					KEY_ROW + " = ?", new String[] { String.valueOf(id) },
					null, null, null);

			if (element.moveToFirst())
				total = element.getDouble(0);
		} catch (SQLException sqle) {
			total = 0.0;
			Log.e("Error getTotalBySellId - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return total;
	}

	/**
	 * Function that gets the customer of the sell.
	 * 
	 * @param id
	 *            Unique number of the sell.
	 * @return Customer object with the customer information. If an error occurs
	 *         <i>null</i> is returned.
	 */
	public Customer getCustomerBySellId(long id) {
		Customer c = null;
		Cursor element = null;

		try {
			db = dbHelper.getReadableDatabase();
			element = db.query(TABLE_NAME, new String[] { KEY_CUSTOMER },
					KEY_ROW + " = ?", new String[] { String.valueOf(id) },
					null, null, null);

			if (element.moveToFirst())
				c = new CustomerTable(context)
						.getCustomerById_OnCustomerObject(element.getLong(0));

		} catch (SQLException sqle) {
			c = null;
			Log.e("Error getCustomerBySellId - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets all the sells sought by the CustomerId and the State,
	 * on a Sell List.
	 * 
	 * @param idCustomer
	 *            Unique number of the customer, which is related to this sell.
	 * @param State
	 *            1 if is still paying, 0 if is finished.
	 * @return List object with all the sells. If an error occurs or there are
	 *         no rows on the table, <i>null</i> is returned.
	 */
	public List<Sell> getSellsByCustomerIdAndState(long idCustomer, int State) {
		List<Sell> sells = null;
		Cursor elements = null;
		String sql = "SELECT " + TABLE_NAME + ".* FROM " + TABLE_NAME
				+ " WHERE " + KEY_CUSTOMER + " = ? AND " + TABLE_NAME + "."
				+ KEY_STATE + " = ? ";
		try {
			db = dbHelper.getReadableDatabase();
			elements = db.rawQuery(
					sql,
					new String[] { String.valueOf(idCustomer),
							String.valueOf(State) });

			if (elements.moveToFirst()) {
				sells = new ArrayList<Sell>();
				Sell sell;
				PaymentTable payment = new PaymentTable(context);
				SellProductTable sp = new SellProductTable(context);

				do {
					sell = new Sell();

					sell.setId(elements.getLong(0));
					sell.setIdServer(elements.getLong(1));
					sell.setIdCustomer(idCustomer);
					sell.setChargeType(Convert.fromIntChargeTypetoString(
							context, elements.getInt(3)));
					sell.setDayToCharge(Convert.fromIntDaytoString(context,
							elements.getInt(4)));
					sell.setHourToCharge(Convert.fromLongtoTimeString(elements
							.getLong(5)));
					sell.setProducts(sp.getProductsBySellId(sell.getId()));
					sell.setPayments(payment.getPaymentsBySellId(sell.getId()));
					sell.setDate(sell.getPayments().get(0).getDate());
					sell.setTime(sell.getPayments().get(0).getTime());
					sell.setTotalPayment(elements.getDouble(6));
					sell.setAgreedPayment(elements.getDouble(7));
					sell.setPayedPayment(payment.getPaymentsSumBySellId(sell
							.getId()));
					sell.setNextPayment(elements.getLong(8));
					sell.setState(Convert.fromIntStatetoString(context,
							elements.getInt(9)));

					sells.add(sell);
					sell = null;
				} while (elements.moveToNext());
			}
		} catch (SQLException sqle) {
			sells = null;
			Log.e("Error getSellsByCustomerIdAndState - " + TABLE_NAME,
					sqle.toString());
		} finally {
			try {
				elements.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return sells;
	}

	/**
	 * Function that gets the last sell made by a customer from the Sell Table.
	 * 
	 * @param idCustomer
	 *            Unique number of the customer, which is related to this sell.
	 * @return Sell object with the sell information. If an error occurs
	 *         <i>null</i> is returned.
	 */
	public Sell getLastSellByCustomerId(long idCustomer) {
		Cursor element = null;
		Sell sell = null;
		String sql = "SELECT " + TABLE_NAME + ".* FROM " + TABLE_NAME
				+ " WHERE " + KEY_CUSTOMER + " = ? ORDER BY " + TABLE_NAME
				+ "." + KEY_ROW + " DESC";
		try {
			db = dbHelper.getReadableDatabase();
			element = db.rawQuery(sql,
					new String[] { String.valueOf(idCustomer) });

			if (element.moveToFirst()) {
				sell = new Sell();
				Payment p = new PaymentTable(context).getPaymentsBySellId(
						element.getLong(0)).get(0);

				sell.setId(element.getLong(0));
				sell.setIdServer(element.getLong(1));
				sell.setIdCustomer(idCustomer);
				sell.setChargeType(Convert.fromIntChargeTypetoString(context,
						element.getInt(3)));
				sell.setDayToCharge(Convert.fromIntDaytoString(context,
						element.getInt(4)));
				sell.setHourToCharge(Convert.fromLongtoTimeString(element
						.getLong(5)));
				sell.setDate(p.getDate());
				sell.setTime(p.getTime());
				sell.setTotalPayment(element.getDouble(6));
				sell.setAgreedPayment(element.getDouble(7));
				sell.setPayedPayment(new PaymentTable(context)
						.getPaymentsSumBySellId(sell.getId()));
				sell.setNextPayment(element.getLong(8));
				sell.setState(Convert.fromIntStatetoString(context,
						element.getInt(9)));
			}
		} catch (SQLException sqle) {
			sell = null;
			Log.e("Error getSellByCustomerId - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return sell;
	}

	/**
	 * Function that gets all the next payment dates (dd/mm/yyyy) from the Sell
	 * Table.
	 * 
	 * @return Array of strings with all the dates. If an error occurs or there
	 *         are no rows on the table, <i>null</i> is returned.
	 */
	public String[] getNextPaymentsDates() {
		String[] dates = null;
		Cursor elements = null;
		String sql = "SELECT DISTINCT " + KEY_NEXT_PAYMENT + " FROM "
				+ TABLE_NAME + " WHERE " + KEY_NEXT_PAYMENT + " > ? AND "
				+ KEY_STATE + " = 1 ORDER BY " + KEY_NEXT_PAYMENT;

		try {
			db = dbHelper.getReadableDatabase();
			elements = db.rawQuery(sql, new String[] { String
					.valueOf(DateTimeManage.getTodayDateAsLong()) });

			if (elements.moveToFirst()) {
				dates = new String[elements.getCount()];

				for (int i = 0; i < elements.getCount(); i++) {
					dates[i] = Convert
							.fromLongtoDateString(elements.getLong(0));

					elements.moveToNext();
				}
			}
		} catch (SQLException sqle) {
			dates = null;
			Log.e("Error getNextPaymentsDates - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				elements.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return dates;
	}

	/**
	 * Function that gets all the sells that match with the date.
	 * 
	 * @param date
	 *            In long format (yyyymmdd).
	 * @return List object with all the sells. If an error occurs or there are
	 *         no rows on the table, <i>null</i> is returned.
	 */
	public List<Sell> getSellsByDate(long date) {
		List<Sell> sells = null;
		Cursor elements = null;
		String sql = "SELECT " + CustomerTable.TABLE_NAME + "."
				+ CustomerTable.KEY_LASTNAME + " || ' ' || "
				+ CustomerTable.TABLE_NAME + "." + CustomerTable.KEY_NAME + ","
				+ TABLE_NAME + ".* FROM " + TABLE_NAME + " INNER JOIN "
				+ CustomerTable.TABLE_NAME + " ON " + TABLE_NAME + "."
				+ KEY_CUSTOMER + " = " + CustomerTable.TABLE_NAME + "."
				+ KEY_CUSTOMER + " WHERE " + KEY_NEXT_PAYMENT + " = ? AND "
				+ TABLE_NAME + "." + KEY_STATE + " = 1 ORDER BY "
				+ KEY_HOUR_TO_CHARGE;
		try {
			db = dbHelper.getReadableDatabase();
			elements = db.rawQuery(sql, new String[] { String.valueOf(date) });

			if (elements.moveToFirst()) {
				sells = new ArrayList<Sell>();
				Sell sell;
				PaymentTable p = new PaymentTable(context);

				do {
					sell = new Sell();

					sell.setCustomerName(elements.getString(0));
					sell.setId(elements.getLong(1));
					sell.setIdServer(elements.getLong(2));
					sell.setChargeType(Convert.fromIntChargeTypetoString(
							context, elements.getInt(4)));
					sell.setDayToCharge(Convert.fromIntDaytoString(context,
							elements.getInt(5)));
					sell.setHourToCharge(Convert.fromLongtoTimeString(elements
							.getLong(6)));
					sell.setPayments(p.getPaymentsBySellId(sell.getId()));
					sell.setTotalPayment(elements.getDouble(7));
					sell.setAgreedPayment(elements.getDouble(8));
					sell.setPayedPayment(p.getPaymentsSumBySellId(sell.getId()));
					sell.setNextPayment(elements.getLong(9));
					sell.setState(Convert.fromIntStatetoString(context,
							elements.getInt(10)));

					sells.add(sell);
					sell = null;
				} while (elements.moveToNext());
			}
		} catch (SQLException sqle) {
			sells = null;
			Log.e("Error getSellsByDate - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				elements.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return sells;
	}

	/**
	 * Function that gets all the customers that match with the IdProduct and
	 * State. This function is used on the ProductDetails.java.
	 * 
	 * @param idProduct
	 *            Product's unique number.
	 * @param state
	 *            1 if is still paying, 0 if is finished.
	 * @return Array of strings with the customers names. If an error occurs or
	 *         there are no rows on the table, <i>null</i> is returned.
	 */
	public String[] getCustomerByProductIdAndState(long idProduct, int state) {
		String[] customers = null;
		Cursor elements = null;
		String sql = "SELECT DISTINCT " + CustomerTable.TABLE_NAME + "."
				+ CustomerTable.KEY_LASTNAME + " || ' ' || "
				+ CustomerTable.TABLE_NAME + "." + CustomerTable.KEY_NAME
				+ " FROM " + TABLE_NAME + " INNER JOIN "
				+ CustomerTable.TABLE_NAME + " ON " + TABLE_NAME + "."
				+ KEY_CUSTOMER + " = " + CustomerTable.TABLE_NAME + "."
				+ KEY_CUSTOMER + " INNER JOIN " + SellProductTable.TABLE_NAME
				+ " ON " + TABLE_NAME + "." + KEY_ROW + " = "
				+ SellProductTable.TABLE_NAME + "." + KEY_ROW + " WHERE "
				+ SellProductTable.TABLE_NAME + "."
				+ SellProductTable.KEY_PRODUCT + " = ? AND " + TABLE_NAME + "."
				+ KEY_STATE + " = ?";
		try {
			db = dbHelper.getReadableDatabase();
			elements = db.rawQuery(
					sql,
					new String[] { String.valueOf(idProduct),
							String.valueOf(state) });

			if (elements.moveToFirst()) {
				customers = new String[elements.getCount()];

				for (int i = 0; i < customers.length; i++) {
					customers[i] = elements.getString(0);
					elements.moveToNext();
				}
			}
		} catch (SQLException sqle) {
			customers = null;
			Log.e("Error getCustomerByProductIdAndState - " + TABLE_NAME,
					sqle.toString());
		} finally {
			try {
				elements.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return customers;
	}

	/**
	 * Function that gets the Sell's ID by the IdServer from the Sell Table.
	 * 
	 * @param idServer
	 *            Unique number (Sell Table from the Server) that corresponds to
	 *            the sell.
	 * @return The Sell's ID. If an error occurs -1 is returned.
	 */
	public long getSellIdBySellIdServer(long idServer) {
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
			Log.e("Error getSellIdBySellIdServer - " + TABLE_NAME,
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
	 * Function that updates a sell from the Sell Table.
	 * 
	 * @param id
	 *            Unique number of the sell, which will be used to sought the
	 *            sell.
	 * @param idServer
	 *            Unique number (Sell Table from the Server) that'll correspond
	 *            to the sell. If the sell has not IdServer then pass 0.
	 * @param idCustomer
	 *            Unique number of the customer, which is related to this sell.
	 * @param chargetype
	 *            1 if is every week, 2 if is every two weeks, 3 if is every
	 *            month.
	 * @param daytocharge
	 *            1 if is Monday, 2 if is Tuesday, 3 if is Wednesday, 4 if is
	 *            Thursday, 5 if is Friday, 6 if is Saturday.
	 * @param hourtocharge
	 *            Time in which the seller is going to pass to charge. The time
	 *            must be in long format (hhmm).
	 * @param totalpayment
	 *            Total of the sell.
	 * @param agreedpayment
	 *            Payment that the customer will pay every time the seller pass
	 *            to charge.
	 * @param nextpayment
	 *            Date of the next payment. It's when the seller is going to
	 *            pass again to charge. The date must be in long format
	 *            (yyyymmdd).
	 * @param state
	 *            1 if is still paying, 0 if is finished.
	 * @param isSync
	 *            True if the sell will be synchronized later to the server.
	 *            False otherwise.
	 * @param isFromJSON
	 *            True if the sell information comes from a JSON file. False
	 *            otherwise.
	 * @return True if the sell was updated. False otherwise.
	 */
	public boolean updateSellById(long id, long idServer, long idCustomer,
			int chargetype, int daytocharge, long hourtocharge,
			double totalpayment, double agreedpayment, long nextpayment,
			int state, boolean isSync, boolean isFromJSON) {
		boolean isUpdated = false;
		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_ID_SERVER
				+ " = ?, " + KEY_CUSTOMER + " = ?, " + KEY_CHARGE_TYPE
				+ " = ?, " + KEY_DAY_TO_CHARGE + " = ?, " + KEY_HOUR_TO_CHARGE
				+ " = ?, " + KEY_TOTAL_PAYMENT + " = ?, " + KEY_AGREED_PAYMENT
				+ " = ?, " + KEY_NEXT_PAYMENT + " = ?, " + KEY_STATE
				+ " = ? WHERE " + KEY_ROW + " = ?";

		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("PRAGMA foreign_keys = ON");

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);

			if (isFromJSON)
				stmnt.bindLong(2, new CustomerTable(context)
						.getCustomerIdByCustomerIdServer(idCustomer));
			else
				stmnt.bindLong(2, idCustomer);

			stmnt.bindLong(3, chargetype);
			stmnt.bindLong(4, daytocharge);
			stmnt.bindLong(5, hourtocharge);
			stmnt.bindDouble(6, totalpayment);
			stmnt.bindDouble(7, agreedpayment);
			stmnt.bindLong(8, nextpayment);
			stmnt.bindLong(9, state);
			stmnt.bindLong(10, id);

			stmnt.execute();
			isUpdated = true;

			if (isSync) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "update", id, idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error updateSellById - " + TABLE_NAME, sqle.toString());
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
	 * Function that updates the IdServer of a sell from the Sell Table.
	 * 
	 * @param id
	 *            Unique number of the sell, which will be used to sought the
	 *            sell.
	 * @param idServer
	 *            Unique number (Sell Table from the Server) that'll correspond
	 *            to the sell.
	 * @return True if the IdServer was updated. False otherwise.
	 */
	public boolean updateSellIdServerById(long id, long idServer) {
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
			Log.e("Error updateSellIdServerById - " + TABLE_NAME,
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
	 *            Unique number of the sell, which will be used to sought the
	 *            sell.
	 * @param isSync
	 *            True if the sell will be synchronized later to the server.
	 *            False otherwise.
	 * @return True if the State was updated. False otherwise.
	 */
	public boolean updateStateSellBySellId(long id, boolean isSync) {
		boolean isUpdated = false;
		Cursor element = null;
		double total = getTotalBySellId(id);
		double payments = new PaymentTable(context).getPaymentsSumBySellId(id);
		int state = (total > payments ? 1 : 0); // 1 -> Still paying, 0 -> Is
												// finished

		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_STATE
				+ " = ? WHERE " + KEY_ROW + " = ?";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("PRAGMA foreign_keys = ON");

			long idServer = -1;
			element = db.query(TABLE_NAME, new String[] { KEY_ID_SERVER },
					KEY_ROW + " = ?", new String[] { String.valueOf(id) },
					null, null, null);
			element.moveToFirst();
			idServer = element.getLong(0);

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, state);
			stmnt.bindLong(2, id);
			stmnt.execute();

			isUpdated = true;

			if (isSync) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "update", id, idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			isUpdated = false;
			Log.e("Error updateStateSellBySellId - " + TABLE_NAME,
					sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return isUpdated;
	}

	/**
	 * @param id
	 *            Unique number of the sell, which will be used to sought the
	 *            sell.
	 * @param nextPayment
	 *            Date of the next payment. It's when the seller is going to
	 *            pass again to charge. The date must be in long format
	 *            (yyyymmdd).
	 * @param isSync
	 *            True if the sell will be synchronized later to the server.
	 *            False otherwise.
	 * @return True if the NextPayment was updated. False otherwise.
	 */
	public boolean updateNextPaymentBySellId(long id, long nextPayment,
			boolean isSync) {
		boolean isUpdated = false;
		Cursor element = null;
		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_NEXT_PAYMENT
				+ " = ? WHERE " + KEY_ROW + " = ?";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("PRAGMA foreign_keys = ON");

			long idServer = -1;
			element = db.query(TABLE_NAME, new String[] { KEY_ID_SERVER },
					KEY_ROW + " = ?", new String[] { String.valueOf(id) },
					null, null, null);
			element.moveToFirst();
			idServer = element.getLong(0);

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, nextPayment);
			stmnt.bindLong(2, id);

			stmnt.execute();
			isUpdated = true;

			if (isSync) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "update", id, idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			isUpdated = false;
			Log.e("Error updateNextPaymentBySellId - " + TABLE_NAME,
					sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return isUpdated;
	}

	/**
	 * Function that deletes all the sells from the Sell Table.
	 * 
	 * @return True if at least 1 sell was deleted. False otherwise.
	 */
	public boolean deleteAllSells() {
		boolean areDeleted = false;

		try {
			db = dbHelper.getWritableDatabase();
			db.execSQL("PRAGMA foreign_keys = ON");

			areDeleted = db.delete(TABLE_NAME, null, null) > 0;
		} catch (SQLException sqle) {
			Log.e("Error deleteAllSells - " + TABLE_NAME, sqle.toString());
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
	 * Function that deletes a sell from the Sell Table.
	 * 
	 * @param id
	 *            Unique number of the sell, which will be used to sought the
	 *            sell.
	 * @param isSync
	 *            True if the sell will be synchronized later to the server.
	 *            False otherwise.
	 * @return True if the sell was deleted. False otherwise.
	 */
	public boolean deleteSellById(long id, boolean isSync) {
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
			Log.e("Error deleteSellById - " + TABLE_NAME, sqle.toString());
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
