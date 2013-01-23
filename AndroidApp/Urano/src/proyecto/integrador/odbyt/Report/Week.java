package proyecto.integrador.odbyt.Report;

import proyecto.integrador.odbyt.Utils.Convert;

public class Week {
	private long _start, _end;

	public Week() {

	}

	public long getStart() {
		return _start;
	}

	public void setStart(long _start) {
		this._start = _start;
	}

	public long getEnd() {
		return _end;
	}

	public void setEnd(long _end) {
		this._end = _end;
	}

	public String toString() {
		return Convert.fromLongtoDateString(_start) + " - "
				+ Convert.fromLongtoDateString(_end);
	}
}
