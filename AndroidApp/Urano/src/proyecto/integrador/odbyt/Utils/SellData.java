package proyecto.integrador.odbyt.Utils;

import java.util.GregorianCalendar;
import java.util.List;

import proyecto.integrador.odbyt.Customer.Customer;
import proyecto.integrador.odbyt.Product.Product;

public class SellData {
	public static Customer selected_customer = null;
	public static List<Product> selected_products = null;
	public static double total = 0.0;
	public static long time = 0, next_payment = 0;
	public static int go_to_charge = 1, day = 1;
	
	public static void clear_sell_data() {
		selected_customer = null;
		selected_products = null;
		total = 0.0;
		time = 0;
		next_payment = 0;
		go_to_charge = 1;
		day = 1;
	}
	
	public static void setNextPayment(int day_of_week, int go_to_charge) {
		switch (go_to_charge) {
		case 1:
			next_payment = DateTimeManage.every_week(day_of_week, new GregorianCalendar());
			break;
			
		case 2:
			next_payment = DateTimeManage.every_2_weeks(day_of_week, new GregorianCalendar());
			break;
			
		case 3:
			next_payment = DateTimeManage.every_month(day_of_week, new GregorianCalendar());
			break;
		}
	}
}
