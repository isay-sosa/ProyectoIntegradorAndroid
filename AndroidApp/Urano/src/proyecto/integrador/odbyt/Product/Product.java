package proyecto.integrador.odbyt.Product;

import proyecto.integrador.odbyt.Utils.ObjectCustom;

public class Product extends ObjectCustom {
	private String _name, _brand, _model, _desc;
	private double _price;

	/**
	 * Method that sets the Name of the Product object.
	 * 
	 * @param name
	 *            Name of the Product.
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * Function that returns the Name of the Product object.
	 * 
	 * @return Name of the Product.
	 */
	public String getName() {
		return _name;
	}

	/**
	 * Method that sets the Brand of the Product object.
	 * 
	 * @param brand
	 *            Brand of the Product.
	 */
	public void setBrand(String brand) {
		_brand = brand;
	}

	/**
	 * Function that returns the Brand of the Product object.
	 * 
	 * @return Brand of the Product.
	 */
	public String getBrand() {
		return _brand;
	}

	/**
	 * Method that sets the Model of the Product object.
	 * 
	 * @param model
	 *            Model of the Product.
	 */
	public void setModel(String model) {
		_model = model;
	}

	/**
	 * Function that returns the Model of the Product object.
	 * 
	 * @return Model of the Product.
	 */
	public String getModel() {
		return _model;
	}

	/**
	 * Method that sets the Price of the Product object.
	 * 
	 * @param price
	 *            Price of the Product.
	 */
	public void setPrice(double price) {
		_price = price;
	}

	/**
	 * Function that returns the Price of the Product object.
	 * 
	 * @return Price of the Product.
	 */
	public double getPrice() {
		return _price;
	}

	/**
	 * Method that sets the Description of the Product object.
	 * 
	 * @param desc
	 *            Description of the Product.
	 */
	public void setDesc(String desc) {
		_desc = desc;
	}

	/**
	 * Function that returns the Description of the Product object.
	 * 
	 * @return Price of the Description.
	 */
	public String getDesc() {
		return _desc;
	}

	public String toString() {
		return _name;
	}
}
