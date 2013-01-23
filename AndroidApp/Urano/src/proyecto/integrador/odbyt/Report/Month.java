package proyecto.integrador.odbyt.Report;

import java.util.Calendar;

import proyecto.integrador.odbyt.R;
import proyecto.integrador.odbyt.Utils.Convert;
import android.content.Context;

public class Month {
	private long _start, _end;
	private Context _context;

	public Month(Context context) {
		_context = context;
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
		String month = "";
		Calendar c = Convert.fromLongDatetoCalendar(_end);

		switch (c.get(Calendar.MONTH)) {
		case 0:
			month = _context.getString(R.string.january) + " " + c.get(Calendar.YEAR);
			break;
		case 1:
			month = _context.getString(R.string.february) + " " + c.get(Calendar.YEAR);
			break;
		case 2:
			month = _context.getString(R.string.march) + " " + c.get(Calendar.YEAR);
			break;
		case 3:
			month = _context.getString(R.string.april) + " " + c.get(Calendar.YEAR);
			break;
		case 4:
			month = _context.getString(R.string.may) + " " + c.get(Calendar.YEAR);
			break;
		case 5:
			month = _context.getString(R.string.june) + " " + c.get(Calendar.YEAR);
			break;
		case 6:
			month = _context.getString(R.string.july) + " " + c.get(Calendar.YEAR);
			break;
		case 7:
			month = _context.getString(R.string.august) + " " + c.get(Calendar.YEAR);
			break;
		case 8:
			month = _context.getString(R.string.september) + " " + c.get(Calendar.YEAR);
			break;
		case 9:
			month = _context.getString(R.string.october) + " " + c.get(Calendar.YEAR);
			break;
		case 10:
			month = _context.getString(R.string.november) + " " + c.get(Calendar.YEAR);
			break;
		case 11:
			month = _context.getString(R.string.december) + " " + c.get(Calendar.YEAR);
			break;
		}
		
		return month;
	}
}
