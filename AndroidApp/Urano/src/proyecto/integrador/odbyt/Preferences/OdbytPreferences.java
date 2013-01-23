package proyecto.integrador.odbyt.Preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class OdbytPreferences {
	private SharedPreferences prefs;
	private final String PREF_NAME = "odbytPrefs";
	private final static String ID_KEY = "id";
	private final static String ID_SERVER_KEY = "idServer";
	private final static String USERNAME_KEY = "username";
	private final static String PASSWORD_KEY = "password";
	private final static String NAME_KEY = "name";
	private final static String LASTNAME_KEY = "lastname";

	@SuppressWarnings("static-access")
	public OdbytPreferences(Context context) {
		prefs = context.getSharedPreferences(PREF_NAME, context.MODE_PRIVATE);
	}

	public void setId(long id) {
		SharedPreferences.Editor editor = prefs.edit();

		editor.putLong(ID_KEY, id);
		editor.commit();
	}
	
	public long getId() {
		return prefs.getLong(ID_KEY, 0);
	}
	
	public void setIdServer(long idServer) {
		SharedPreferences.Editor editor = prefs.edit();

		editor.putLong(ID_SERVER_KEY, idServer);
		editor.commit();
	}
	
	public long getIdServer() {
		return prefs.getLong(ID_SERVER_KEY, 0);
	}
	
	public void setUsername(String username) {
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(USERNAME_KEY, username);
		editor.commit();
	}

	public String getUsername() {
		return prefs.getString(USERNAME_KEY, "");
	}

	public void setPassword(String pwd) {
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(PASSWORD_KEY, pwd);
		editor.commit();
	}

	public String getPassword() {
		return prefs.getString(PASSWORD_KEY, "");
	}
	
	public void setName(String name) {
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(NAME_KEY, name);
		editor.commit();
	}
	
	public String getName() {
		return prefs.getString(NAME_KEY, "");
	}
	
	public void setLastName(String lastname) {
		SharedPreferences.Editor editor = prefs.edit();

		editor.putString(LASTNAME_KEY, lastname);
		editor.commit();
	}
	
	public String getLastName() {
		return prefs.getString(LASTNAME_KEY, "");
	}
}
