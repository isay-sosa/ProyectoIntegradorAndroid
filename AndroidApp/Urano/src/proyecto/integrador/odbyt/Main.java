package proyecto.integrador.odbyt;

import proyecto.integrador.odbyt.DataBase.SellerTable;
import proyecto.integrador.odbyt.Preferences.OdbytPreferences;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initComponents();
	}

	/**
	 * Method that initializes all the components.
	 */
	private void initComponents() {
		OdbytPreferences prefs = new OdbytPreferences(this);
		String user = prefs.getUsername(), pwd = prefs.getPassword();

		Login(user, pwd);
	}

	/**
	 * Method that allows log in to the system.
	 * 
	 * @param user
	 *            Of the seller.
	 * @param pwd
	 *            Of the seller
	 */
	private void Login(String user, String pwd) {
		if (user.length() > 0 && pwd.length() > 0) {
			SellerTable seller = new SellerTable(this);
			if (seller.LogIn(user, pwd)) {
				startActivity(new Intent(this, Home.class));
				finish();
			} else {
				startActivity(new Intent(this, Login.class));
				finish();
			}
		} else {
			startActivity(new Intent(this, Login.class));
			finish();
		}
	}
}
