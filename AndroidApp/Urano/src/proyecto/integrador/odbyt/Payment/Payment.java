package proyecto.integrador.odbyt.Payment;

import proyecto.integrador.odbyt.Utils.ObjectCustom;

public class Payment extends ObjectCustom {
	private long _idSell;
	private String _date, _time;
	private double _payment;

	public long getIdSell() {
		return _idSell;
	}

	public void setIdSell(long _idSell) {
		this._idSell = _idSell;
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

	public double getPayment() {
		return _payment;
	}

	public void setPayment(double _payment) {
		this._payment = _payment;
	}
}
