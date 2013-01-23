package proyecto.integrador.odbyt.Sell;

import java.util.List;

import proyecto.integrador.odbyt.Payment.Payment;
import proyecto.integrador.odbyt.Utils.ObjectCustom;

public class Sell extends ObjectCustom {
	private long _idCustomer;
	private String _customerName, _chargeType, _dayToCharge, _hourToCharge,
			_state, _date, _time;
	private List<String> _products;
	private double _totalPayment, _agreedPayment, _payedPayment;
	private long _nextPayment;
	private List<Payment> _payments;

	/**
	 * Function that returns the IdCustomer of the Sell object.
	 * 
	 * @return IdCustomer of the Sell object.
	 */
	public long getIdCustomer() {
		return _idCustomer;
	}

	/**
	 * Method that sets the IdCustomer of the Sell object.
	 * 
	 * @param _idCustomer
	 *            Unique number (related to the Customer) of the Sell object.
	 */
	public void setIdCustomer(long _idCustomer) {
		this._idCustomer = _idCustomer;
	}

	/**
	 * Function that returns the Name of the Customer of the Sell object.
	 * 
	 * @return Name of the Customer.
	 */
	public String getCustomerName() {
		return _customerName;
	}

	/**
	 * Method that sets the Name of the Customer of the Sell object.
	 * 
	 * @param _customerName
	 *            Name of the Customer.
	 */
	public void setCustomerName(String _customerName) {
		this._customerName = _customerName;
	}

	public String getChargeType() {
		return _chargeType;
	}

	public void setChargeType(String _chargeType) {
		this._chargeType = _chargeType;
	}

	public String getDayToCharge() {
		return _dayToCharge;
	}

	public void setDayToCharge(String _dayToCharge) {
		this._dayToCharge = _dayToCharge;
	}

	public String getHourToCharge() {
		return _hourToCharge;
	}

	public void setHourToCharge(String _hourToCharge) {
		this._hourToCharge = _hourToCharge;
	}

	public String getState() {
		return _state;
	}

	public void setState(String _state) {
		this._state = _state;
	}

	public String getDate() {
		return _date;
	}

	public void setDate(String _date) {
		this._date = _date;
	}

	public String getTime() {
		return _time;
	}

	public void setTime(String _time) {
		this._time = _time;
	}

	public List<String> getProducts() {
		return _products;
	}

	public void setProducts(List<String> _products) {
		this._products = _products;
	}

	public double getTotalPayment() {
		return _totalPayment;
	}

	public void setTotalPayment(double _totalPayment) {
		this._totalPayment = _totalPayment;
	}

	public double getAgreedPayment() {
		return _agreedPayment;
	}

	public void setAgreedPayment(double _agreedPayment) {
		this._agreedPayment = _agreedPayment;
	}

	public double getPayedPayment() {
		return _payedPayment;
	}

	public void setPayedPayment(double _payedPayment) {
		this._payedPayment = _payedPayment;
	}

	public long getNextPayment() {
		return _nextPayment;
	}

	public void setNextPayment(long _nextPayment) {
		this._nextPayment = _nextPayment;
	}

	public List<Payment> getPayments() {
		return _payments;
	}

	public void setPayments(List<Payment> _payments) {
		this._payments = _payments;
	}
}
