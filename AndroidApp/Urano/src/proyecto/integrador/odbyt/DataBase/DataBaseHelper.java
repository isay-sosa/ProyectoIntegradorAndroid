package proyecto.integrador.odbyt.DataBase;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "odbytDB";
	public static final int DATABASE_VERSION = 1;

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			// Able foreign keys
			db.execSQL("PRAGMA foreign_keys = ON");

			// Creating tables
			db.execSQL(SellerTable.CREATE_TABLE);
			db.execSQL(CustomerTable.CREATE_TABLE);
			db.execSQL(ProductTable.CREATE_TABLE);
			db.execSQL(SellTable.CREATE_TABLE);
			db.execSQL(SellProductTable.CREATE_TABLE);
			db.execSQL(PaymentTable.CREATE_TABLE);
			db.execSQL(SyncTable.CREATE_TABLE);
		} catch (SQLException sqle) {
			Log.e("Error onCreate() - DataBaseHelper", sqle.toString());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
