package proyecto.integrador.odbyt.Seller;

import proyecto.integrador.odbyt.Utils.ObjectCustom;

public class Seller extends ObjectCustom {
	private String _username, _pwd, _name, _lastname;

	/**
	 * Method that sets the User name of the Seller object.
	 * 
	 * @param username
	 *            User name of the Seller.
	 */
	public void setUsername(String username) {
		_username = username;
	}

	/**
	 * Function that returns the User name of the Seller object.
	 * 
	 * @return User name of the Seller object.
	 */
	public String getUsername() {
		return _username;
	}

	/**
	 * Method that sets the Password of the Seller object.
	 * 
	 * @param pwd
	 *            Password of the Seller.
	 */
	public void setPassword(String pwd) {
		_pwd = pwd;
	}

	/**
	 * Function that returns the Password of the Seller object.
	 * 
	 * @return Password of the Seller object.
	 */
	public String getPassword() {
		return _pwd;
	}

	/**
	 * Method that sets the Name of the Seller object.
	 * 
	 * @param name
	 *            Name of the Seller.
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * Function that returns the Name of the Seller object.
	 * 
	 * @return Name of the Seller object.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Method that sets the LastName of the Seller object.
	 * 
	 * @param lastname
	 *            LastName of the Seller.
	 */
	public void setLastName(String lastname) {
		_lastname = lastname;
	}

	/**
	 * Function that returns the LastName of the Seller object.
	 * 
	 * @return LastName of the Seller object.
	 */
	public String getLastName() {
		return _lastname;
	}
}
