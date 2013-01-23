package proyecto.integrador.odbyt.Customer;

import proyecto.integrador.odbyt.Utils.ObjectCustom;

public class Customer extends ObjectCustom {
	private String _name, _lastname, _address, _city, _state, _phone,
			_cellphone, _email;

	/**
	 * Method that sets the Name of the Customer object.
	 * 
	 * @param name
	 *            Name of the Customer.
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * Function that returns the Name of the Customer object.
	 * 
	 * @return Name of the Customer.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Method that sets the LastName of the Customer object.
	 * 
	 * @param lastname
	 *            LastName of the Customer.
	 */
	public void setLastName(String lastname) {
		_lastname = lastname;
	}

	/**
	 * Function that returns the LastName of the Customer object.
	 * 
	 * @return LastName of the Customer.
	 */
	public String getLastName() {
		return _lastname;
	}

	/**
	 * Method that sets the Address of the Customer object.
	 * 
	 * @param address
	 *            Address of the Customer.
	 */
	public void setAddress(String address) {
		_address = address;
	}

	/**
	 * Function that returns the Address of the Customer object.
	 * 
	 * @return Address of the Customer.
	 */
	public String getAddress() {
		return _address;
	}

	/**
	 * Method that sets the City of the Customer object.
	 * 
	 * @param city
	 *            City where the Customer lives.
	 */
	public void setCity(String city) {
		_city = city;
	}

	/**
	 * Function that returns the City of the Customer object.
	 * 
	 * @return City where the Customer lives.
	 */
	public String getCity() {
		return _city;
	}

	/**
	 * Method that sets the State of the Customer object.
	 * 
	 * @param state
	 *            State where the Customer lives.
	 */
	public void setState(String state) {
		_state = state;
	}

	/**
	 * Function that returns the State of the Customer object.
	 * 
	 * @return State where the Customer lives.
	 */
	public String getState() {
		return _state;
	}

	/**
	 * Method that sets the Phone of the Customer object.
	 * 
	 * @param phone
	 *            Phone of the Customer.
	 */
	public void setPhone(String phone) {
		_phone = phone;
	}

	/**
	 * Function that returns the Phone of the Customer object.
	 * 
	 * @return Phone of the Customer.
	 */
	public String getPhone() {
		return _phone;
	}

	/**
	 * Method that sets the Cell phone of the Customer object.
	 * 
	 * @param cellphone
	 *            Cell phone of the Customer.
	 */
	public void setCellPhone(String cellphone) {
		_cellphone = cellphone;
	}

	/**
	 * Function that returns the Cell phone of the Customer object.
	 * 
	 * @return Cell phone of the Customer.
	 */
	public String getCellPhone() {
		return _cellphone;
	}

	/**
	 * Method that sets the Email of the Customer object.
	 * 
	 * @param email
	 *            Email of the Customer.
	 */
	public void setEmail(String email) {
		_email = email;
	}

	/**
	 * Function that returns the Email of the Customer object.
	 * 
	 * @return Email of the Customer.
	 */
	public String getEmail() {
		return _email;
	}

	public String toString() {
		return _lastname + " " + _name;
	}
}
