package proyecto.integrador.odbyt.Report;

import java.util.List;

import proyecto.integrador.odbyt.Customer.Customer;

public class Report {
	private long _idSell, _idPayment, _date, _time;
	private Customer _customer;
	private List<String> _products;
	private double _payment, _total, _payed;
	private int _color;
	
	public Report() {
		
	}

	public long getIdSell() {
		return _idSell;
	}

	public void setIdSell(long _idSell) {
		this._idSell = _idSell;
	}

	public long getIdPayment() {
		return _idPayment;
	}

	public void setIdPayment(long _idPayment) {
		this._idPayment = _idPayment;
	}

	public long getDate() {
		return _date;
	}

	public void setDate(long _date) {
		this._date = _date;
	}

	public long getTime() {
		return _time;
	}

	public void setTime(long _time) {
		this._time = _time;
	}

	public Customer getCustomer() {
		return _customer;
	}

	public void setCustomer(Customer _customer) {
		this._customer = _customer;
	}

	public List<String> getProducts() {
		return _products;
	}

	public void setProducts(List<String> _products) {
		this._products = _products;
	}

	public double getPayment() {
		return _payment;
	}

	public void setPayment(double _payment) {
		this._payment = _payment;
	}

	public double getTotal() {
		return _total;
	}

	public void setTotal(double _total) {
		this._total = _total;
	}

	public double getPayed() {
		return _payed;
	}

	public void setPayed(double _payed) {
		this._payed = _payed;
	}

	public int getColor() {
		return _color;
	}

	public void setColor(int _color) {
		this._color = _color;
	}
}
