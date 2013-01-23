package proyecto.integrador.odbyt.Utils;

import java.util.List;

import proyecto.integrador.odbyt.Customer.Customer;
import proyecto.integrador.odbyt.Product.Product;
import proyecto.integrador.odbyt.Report.Month;
import proyecto.integrador.odbyt.Report.Report;
import proyecto.integrador.odbyt.Report.Week;
import proyecto.integrador.odbyt.Sell.Sell;

public class Utils {
	private static List<Customer> CUSTOMERS_LIST = null;
	private static Customer CUSTOMER = null;
	private static List<Product> PRODUCTS_LIST = null;
	private static Product PRODUCT = null;
	private static List<Sell> SELLS_LIST = null;
	private static Sell SELL = null;
	private static List<String> DAYS_LIST = null;
	private static String DAY = null;
	private static List<Month> MONTHS_LIST = null;
	private static Month MONTH = null;
	private static List<Week> WEEKS_LIST = null;
	private static Week WEEK = null;
	private static List<Report> REPORTS_LIST = null;
	private static Report REPORT = null;

	public static void setCustomersList(final List<Customer> customers) {
		CUSTOMERS_LIST = customers;
	}

	public final static List<Customer> getCustomersList() {
		return CUSTOMERS_LIST;
	}

	public static void setCustomer(final Customer customer) {
		CUSTOMER = customer;
	}

	public final static Customer getCustomer() {
		return CUSTOMER;
	}

	public static void setProductsList(final List<Product> products) {
		PRODUCTS_LIST = products;
	}

	public final static List<Product> getProductsList() {
		return PRODUCTS_LIST;
	}

	public static void setProduct(final Product product) {
		PRODUCT = product;
	}

	public final static Product getProduct() {
		return PRODUCT;
	}
	
	public static void setSellsList(final List<Sell> sells) {
		SELLS_LIST = sells;
	}
	
	public final static List<Sell> getSellsList() {
		return SELLS_LIST;
	}
	
	public static void setSell(final Sell sell) {
		SELL = sell;
	}
	
	public final static Sell getSell() {
		return SELL;
	}
	
	public static void setDaysList(final List<String> days) {
		DAYS_LIST = days;
	}
	
	public final static List<String> getDaysList() {
		return DAYS_LIST;
	}
	
	public static void setDay(final String day) {
		DAY = day;
	}
	
	public final static String getDay() {
		return DAY;
	}
	
	public static void setMonthsList(final List<Month> months) {
		MONTHS_LIST = months;
	}
	
	public final static List<Month> getMonthsList() {
		return MONTHS_LIST;
	}
	
	public static void setMonth(final Month month) {
		MONTH = month;
	}
	
	public final static Month getMonth() {
		return MONTH;
	}
	
	public static void setWeeksList(final List<Week> weeks) {
		WEEKS_LIST = weeks;
	}
	
	public final static List<Week> getWeeksList() {
		return WEEKS_LIST;
	}
	
	public static void setWeek(final Week week) {
		WEEK = week;
	}
	
	public final static Week getWeek() {
		return WEEK;
	}

	public static void setReportssList(final List<Report> reports) {
		REPORTS_LIST = reports;
	}
	
	public final static List<Report> getReportsList() {
		return REPORTS_LIST;
	}
	
	public static void setReport(final Report report) {
		REPORT = report;
	}
	
	public final static Report getReport() {
		return REPORT;
	}
}
