package proyecto.integrador.odbyt.Utils;

public class ObjectCustom extends Object {
	private long _id, _idServer;
	private int _color;
	private boolean _checked;

	/**
	 * Function that returns the Id of the object.
	 * 
	 * @return Id (unique number) of the object.
	 */
	public long getId() {
		return _id;
	}

	/**
	 * Method that sets the Id of the object.
	 * 
	 * @param id
	 *            Unique number of the object.
	 */
	public void setId(long _id) {
		this._id = _id;
	}

	/**
	 * Function that returns the Id of the object.
	 * 
	 * @return Id (unique number) of the object.
	 */
	public long getIdServer() {
		return _idServer;
	}

	/**
	 * Method that sets the IdServer of the object.
	 * 
	 * @param idServer
	 *            Unique number (related to the Server) of the object.
	 */
	public void setIdServer(long _idServer) {
		this._idServer = _idServer;
	}

	public int getColor() {
		return _color;
	}

	public void setColor(int _color) {
		this._color = _color;
	}

	public boolean isChecked() {
		return _checked;
	}

	public void setChecked(boolean _checked) {
		this._checked = _checked;
	}

}
