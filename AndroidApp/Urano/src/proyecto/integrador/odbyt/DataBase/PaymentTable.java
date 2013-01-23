package proyecto.integrador.odbyt.DataBase;

import java.util.ArrayList;
import java.util.List;

import proyecto.integrador.odbyt.Payment.Payment;
import proyecto.integrador.odbyt.Report.Report;
import proyecto.integrador.odbyt.Utils.Convert;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class PaymentTable {
	public static final String KEY_ROW = "IdPayment";
	public static final String KEY_ID_SERVER = "IdPayment_Server";
	public static final String KEY_SELL = "IdSell";
	public static final String KEY_PAYMENT = "Payment";
	public static final String KEY_PAYMENT_DATE = "Payment_Date";
	public static final String KEY_PAYMENT_HOUR = "Payment_Hour";
	public static final String TABLE_NAME = "payment";
	public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ TABLE_NAME + "(" + KEY_ROW
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ID_SERVER
			+ " INTEGER," + KEY_SELL + " INTEGER NOT NULL REFERENCES "
			+ SellTable.TABLE_NAME + "(" + KEY_SELL
			+ ") ON UPDATE CASCADE ON DELETE CASCADE," + KEY_PAYMENT
			+ " NUMERIC NOT NULL," + KEY_PAYMENT_DATE + " INTEGER NOT NULL,"
			+ KEY_PAYMENT_HOUR + " INTEGER NOT NULL);";

	private Context context = null;
	private SQLiteDatabase db = null;
	private DataBaseHelper dbHelper = null;

	public PaymentTable(Context context) {
		dbHelper = new DataBaseHelper(context);
		this.context = context;
	}

	/**
	 * Function that inserts a payment into the Payment Table.
	 * 
	 * @param idServer
	 *            Unique number (Payment Table from the Server) that'll
	 *            correspond to the payment. If the payment has not IdServer
	 *            then pass 0.
	 * @param idSell
	 *            Sell's unique number that represents a sell element from the
	 *            Sell Table.
	 * @param payment
	 *            The payment made.
	 * @param payment_date
	 *            Date of the payment. The date must be in long format
	 *            (yyyymmdd).
	 * @param payment_hour
	 *            Time of the payment. The time must be in long format (hhmm).
	 * @param isSync
	 *            True if the payment will be synchronized later to the server.
	 *            False otherwise.
	 * @param isFromJSON
	 *            True if the payment information comes from a JSON file. False
	 *            otherwise.
	 * @return The row ID if the payment was inserted. Otherwise returns -1.
	 */
	public long insertPayment(long idServer, long idSell, double payment,
			long payment_date, long payment_hour, boolean isSync,
			boolean isFromJSON) {
		long idInserted = -1;
		String sql = "INSERT INTO " + TABLE_NAME + "(" + KEY_ID_SERVER + ","
				+ KEY_SELL + "," + KEY_PAYMENT + "," + KEY_PAYMENT_DATE + ","
				+ KEY_PAYMENT_HOUR + ") VALUES(?, ?, ?, ?, ?);";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);

			if (isFromJSON)
				stmnt.bindLong(2,
						new SellTable(context).getSellIdBySellIdServer(idSell));
			else
				stmnt.bindLong(2, idSell);

			stmnt.bindDouble(3, payment);
			stmnt.bindLong(4, payment_date);
			stmnt.bindLong(5, payment_hour);

			idInserted = stmnt.executeInsert();
			if (isSync && idInserted > 0) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "insert", idInserted,
						idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error insertPayment - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets all the payments stored on the Payments Table.
	 * 
	 * @return Cursor object with all the payments. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllPayments() {
		Cursor elements = null;
		String sql = "SELECT * FROM " + TABLE_NAME;

		try {
			db = dbHelper.getWritableDatabase();

			elements = db.rawQuery(sql, null);
			if (elements != null)
				elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllPayments - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets all the payments stored on the Payment Table ordered
	 * by their IdServer.
	 * 
	 * @return Cursor object with all the payments. If an error occurs
	 *         <i>null</i> is returned. The Cursor object must be closed after
	 *         it uses.
	 */
	public Cursor getAllPaymentsOrderedByIdServer() {
		Cursor elements = null;
		try {
			db = dbHelper.getReadableDatabase();
			elements = db.query(TABLE_NAME, null, null, null, null, null,
					KEY_ID_SERVER);
			elements.moveToFirst();
		} catch (SQLException sqle) {
			elements = null;
			Log.e("Error getAllPaymentsOrderedByIdServer - " + TABLE_NAME,
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
	 * Function that gets a payment from the Payment Table.
	 * 
	 * @param id
	 *            Unique number of the payment.
	 * @return Cursor object with the payment. If an error occurs <i>null</i> is
	 *         returned. The Cursor object must be closed after it uses.
	 */
	public Cursor getPaymentById(long id) {
		Cursor payment = null;
		String sql = "SELECT " + KEY_ROW + "," + KEY_ID_SERVER + ","
				+ SellTable.TABLE_NAME + "." + KEY_SELL + ","
				+ SellTable.TABLE_NAME + "." + SellTable.KEY_ID_SERVER + ","
				+ KEY_PAYMENT + "," + KEY_PAYMENT_DATE + "," + KEY_PAYMENT_HOUR
				+ " FROM " + TABLE_NAME + " INNER JOIN " + SellTable.TABLE_NAME
				+ " ON " + TABLE_NAME + "." + KEY_SELL + " = "
				+ SellTable.TABLE_NAME + "." + KEY_SELL + " WHERE " + KEY_ROW
				+ " = ?";
		try {
			db = dbHelper.getReadableDatabase();
			payment = db.rawQuery(sql, new String[] { String.valueOf(id) });

			payment.moveToFirst();
		} catch (SQLException sqle) {
			payment = null;
			Log.e("Error getPaymentById - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return payment;
	}

	/**
	 * Function that gets the sum of all the payments of a sell.
	 * 
	 * @param idSell
	 *            Sell's unique number that represents a sell element from the
	 *            Sell Table.
	 * @return Sum of the payments.
	 */
	public double getPaymentsSumBySellId(long idSell) {
		double payments = 0.0;
		String sql = "SELECT SUM(" + KEY_PAYMENT + ") FROM " + TABLE_NAME
				+ " WHERE " + KEY_SELL + " = ?";
		Cursor element = null;

		try {
			db = dbHelper.getReadableDatabase();

			element = db.rawQuery(sql, new String[] { String.valueOf(idSell) });

			if (element.moveToFirst())
				payments = element.getDouble(0);
		} catch (SQLException sqle) {
			payments = 0.0;
			Log.e("Error getPaymentsBySellId - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return payments;
	}

	/**
	 * Function that gets the sum of the payments of a sell that are inside the
	 * date limit.
	 * 
	 * @param idSell
	 *            Sell's unique number that represents a sell element from the
	 *            Sell Table.
	 * @param limit
	 *            Date limit in long format (yyyymmdd).
	 * @return Sum of the payments.
	 */
	public double getPaymentsSumBySellIdByDateLimit(long idSell, long limit) {
		double payments = 0.0;
		String sql = "SELECT SUM(" + KEY_PAYMENT + ") FROM " + TABLE_NAME
				+ " WHERE " + KEY_SELL + " = ? AND " + KEY_PAYMENT_DATE
				+ " <= ?";
		Cursor element = null;

		try {
			db = dbHelper.getReadableDatabase();

			element = db.rawQuery(sql, new String[] { String.valueOf(idSell),
					String.valueOf(limit) });

			if (element.moveToFirst())
				payments = element.getDouble(0);
		} catch (SQLException sqle) {
			payments = 0.0;
			Log.e("Error getPaymentsSumBySellIdByDateLimit - " + TABLE_NAME,
					sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return payments;
	}

	/**
	 * Function that gets all the payments of a sell on a List Payment.
	 * 
	 * @param idSell
	 *            Sell's unique number that represents a sell element from the
	 *            Sell Table.
	 * @return List object with all the payments. If an error occurs or there
	 *         are no rows on the table, <i>null</i> is returned.
	 */
	public List<Payment> getPaymentsBySellId(long idSell) {
		Cursor element = null;
		List<Payment> payments = null;

		try {
			db = dbHelper.getReadableDatabase();
			element = db.query(TABLE_NAME, null, KEY_SELL + " = ?",
					new String[] { String.valueOf(idSell) }, null, null, null);

			if (element.moveToFirst()) {
				payments = new ArrayList<Payment>();
				Payment payment;

				do {
					payment = new Payment();

					payment.setId(element.getLong(0));
					payment.setIdServer(element.getLong(1));
					payment.setIdSell(idSell);
					payment.setPayment(element.getDouble(3));
					payment.setDate(Convert.fromLongtoDateString(element
							.getLong(4)));
					payment.setTime(Convert.fromLongtoTimeString(element
							.getLong(5)));

					payments.add(payment);
					payment = null;
				} while (element.moveToNext());
			}
		} catch (SQLException sqle) {
			payments = null;
			Log.e("Error getPaymentsBySellId - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				element.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return payments;
	}

	/**
	 * Function that gets all the dates stored on the Payment Table. These dates
	 * are no repeated and are in long format (yyyymmdd).
	 * 
	 * @return Array of long with all the distinct dates stored on the Payment
	 *         Table. If an error occurs or are no rows on the table,
	 *         <i>null</i> is returned.
	 */
	public long[] getAllDistinctDates() {
		long[] dates = null;
		String sql = "SELECT DISTINCT " + KEY_PAYMENT_DATE + " FROM "
				+ TABLE_NAME + " ORDER BY " + KEY_PAYMENT_DATE;
		Cursor elements = null;

		try {
			db = dbHelper.getReadableDatabase();
			elements = db.rawQuery(sql, null);

			if (elements.moveToFirst()) {
				dates = new long[elements.getCount()];

				for (int i = 0; i < dates.length; i++) {
					dates[i] = elements.getLong(0);
					elements.moveToNext();
				}
			}
		} catch (SQLException sqle) {
			dates = null;
			Log.e("Error getAllDistinctDates - " + TABLE_NAME, sqle.toString());
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
	 * Function that gets a List Report from the data stored on the Payment
	 * Table. The system gets the data on a specific day.
	 * 
	 * @param date
	 *            Day in which the system is going to get the data. The dates
	 *            must be in long format (yyyymmdd).
	 * @return List object with all the payments. If an error occurs or there
	 *         are no rows on the table, <i>null</i> is returned.
	 */
	public List<Report> getReportsByDay(long date) {
		List<Report> reports = null;
		Cursor elements = null;

		try {
			db = dbHelper.getReadableDatabase();
			elements = db.query(TABLE_NAME, null, KEY_PAYMENT_DATE + " = ?",
					new String[] { String.valueOf(date) }, null, null,
					KEY_PAYMENT_DATE);

			if (elements.moveToFirst()) {
				reports = new ArrayList<Report>();
				Report r;
				SellTable st = new SellTable(context);
				SellProductTable spt = new SellProductTable(context);

				do {
					r = new Report();

					r.setIdPayment(elements.getLong(0));
					r.setIdSell(elements.getLong(2));
					r.setPayment(elements.getDouble(3));
					r.setDate(elements.getLong(4));
					r.setTime(elements.getLong(5));

					r.setCustomer(st.getCustomerBySellId(r.getIdSell()));
					r.setProducts(spt.getProductsBySellId(r.getIdSell()));
					r.setTotal(st.getTotalBySellId(r.getIdSell()));
					r.setPayed(getPaymentsSumBySellIdByDateLimit(r.getIdSell(),
							date));

					reports.add(r);
					r = null;
				} while (elements.moveToNext());
			}
		} catch (SQLException sqle) {
			reports = null;
			Log.e("Error getReportsByDay - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				elements.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return reports;
	}

	/**
	 * Function that gets a List Report from the data stored on the Payment
	 * Table. The system gets the data on a determined range.
	 * 
	 * @param start
	 *            Start's range date. It must be in long format (yyyymmdd).
	 * @param end
	 *            End's range date. It must be in long format (yyyymmdd).
	 * @return List object with all the payments. If an error occurs or there
	 *         are no rows on the table, <i>null</i> is returned.
	 */
	public List<Report> getReportsByRange(long start, long end) {
		List<Report> reports = null;
		Cursor elements = null;

		try {
			db = dbHelper.getReadableDatabase();
			elements = db.query(TABLE_NAME, null, KEY_PAYMENT_DATE
					+ " >= ? AND " + KEY_PAYMENT_DATE + " <= ?", new String[] {
					String.valueOf(start), String.valueOf(end) }, null, null,
					KEY_PAYMENT_DATE + "," + KEY_PAYMENT_HOUR);

			if (elements.moveToFirst()) {
				reports = new ArrayList<Report>();
				Report r;
				SellTable st = new SellTable(context);
				SellProductTable spt = new SellProductTable(context);

				do {
					r = new Report();

					r.setIdPayment(elements.getLong(0));
					r.setIdSell(elements.getLong(2));
					r.setPayment(elements.getDouble(3));
					r.setDate(elements.getLong(4));
					r.setTime(elements.getLong(5));

					r.setCustomer(st.getCustomerBySellId(r.getIdSell()));
					r.setProducts(spt.getProductsBySellId(r.getIdSell()));
					r.setTotal(st.getTotalBySellId(r.getIdSell()));
					r.setPayed(getPaymentsSumBySellIdByDateLimit(r.getIdSell(),
							end));

					reports.add(r);
					r = null;
				} while (elements.moveToNext());
			}
		} catch (SQLException sqle) {
			reports = null;
			Log.e("Error getReportsByRange - " + TABLE_NAME, sqle.toString());
		} finally {
			try {
				elements.close();
				db.close();
			} catch (Exception e) {
				Log.e("Error closing BDconnection", e.toString());
			}
		}

		return reports;
	}

	/**
	 * @param id
	 *            Unique number of the payment, which will be used to sought the
	 *            payment.
	 * @param idServer
	 *            Unique number (Payment Table from the Server) that'll
	 *            correspond to the payment. If the payment has not IdServer
	 *            then pass 0.
	 * @param idSell
	 *            Sell's unique number that represents a sell element from the
	 *            Sell Table.
	 * @param payment
	 *            The payment made.
	 * @param payment_date
	 *            Date of the payment. The date must be in long format
	 *            (yyyymmdd).
	 * @param payment_hour
	 *            Time of the payment. The time must be in long format (hhmm).
	 * @param isSync
	 *            True if the payment will be synchronized later to the server.
	 *            False otherwise.
	 * @param isFromJSON
	 *            True if the payment information comes from a JSON file. False
	 *            otherwise.
	 * @return True if the payment was updated. False otherwise.
	 */
	public boolean updatePaymentById(long id, long idServer, long idSell,
			double payment, long payment_date, long payment_hour,
			boolean isSync, boolean isFromJSON) {
		boolean isUpdated = false;
		String sql = "UPDATE " + TABLE_NAME + " SET " + KEY_ID_SERVER
				+ " = ?, " + KEY_SELL + " = ?, " + KEY_PAYMENT + " = ?, "
				+ KEY_PAYMENT_DATE + " = ?, " + KEY_PAYMENT_HOUR
				+ " = ? WHERE " + KEY_ROW + " = ?";
		SQLiteStatement stmnt = null;

		try {
			db = dbHelper.getWritableDatabase();

			stmnt = db.compileStatement(sql);
			stmnt.bindLong(1, idServer);

			if (isFromJSON)
				stmnt.bindLong(2,
						new SellTable(context).getSellIdBySellIdServer(idSell));
			else
				stmnt.bindLong(2, idSell);

			stmnt.bindDouble(3, payment);
			stmnt.bindLong(4, payment_date);
			stmnt.bindLong(5, payment_hour);
			stmnt.bindLong(6, id);

			stmnt.execute();
			isUpdated = true;

			if (isSync) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "update", id, idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error updatePaymentById - " + TABLE_NAME, sqle.toString());
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
	 * Function that updates the IdServer of a payment from the Payment Table.
	 * 
	 * @param id
	 *            Unique number of the payment, which will be used to sought the
	 *            payment.
	 * @param idServer
	 *            Unique number (Payment Table from the Server) that'll
	 *            correspond to the payment.
	 * @return True if the IdServer was payment. False otherwise.
	 */
	public boolean updatePaymentIdServerById(long id, long idServer) {
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
			Log.e("Error updatePaymentIdServerById - " + TABLE_NAME,
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
	 * Function that deletes all the payments from the Payments Table.
	 * 
	 * @return True if at least 1 payment was deleted. False otherwise.
	 */
	public boolean deleteAllPayments() {
		boolean areDeleted = false;

		try {
			db = dbHelper.getWritableDatabase();
			areDeleted = db.delete(TABLE_NAME, null, null) > 0;
		} catch (SQLException sqle) {
			Log.e("Error deleteAllPayments - " + TABLE_NAME, sqle.toString());
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
	 * Function that deletes a payment from the Payment Table.
	 * 
	 * @param id
	 *            Unique number of the payment, which will be used to sought the
	 *            payment.
	 * @param isSync
	 *            True if the payment will be synchronized later to the server.
	 *            False otherwise.
	 * @return True if the payment was deleted. False otherwise.
	 */
	public boolean deletePaymentById(long id, boolean isSync) {
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

			if (isSync) {
				SyncTable sync = new SyncTable(context);
				sync.insertSyncElement(TABLE_NAME, "delete", id, idServer);
				sync = null;
			}
		} catch (SQLException sqle) {
			Log.e("Error deletePaymentById - " + TABLE_NAME, sqle.toString());
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
