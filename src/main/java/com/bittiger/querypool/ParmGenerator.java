package com.bittiger.querypool;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Random;

public class ParmGenerator {
	Random rd = new Random();
	StringPool sp = new StringPool();
	PopulateTable pt = new PopulateTable();
	TableMetaData tmd = new TableMetaData();

	// item
	public String getItemSubject() {
		return sp.subjects[rd.nextInt(sp.subjects.length)];
	}

	public String getItemTitle() {
		int count = rd.nextInt(5);
		String title = "";
		for (int i = 0; i <= count; i++) {
			title += "BA"; // this is base on the wgen
		}
		return title;
	}

	public String getItemPublishDate() throws Exception {
		Calendar cal = pt.randomDate(1, 1, 1930);
		return getSimpleDate(cal);
	}

	public String getItemThumbnail() {
		String url = "http://localhost:8080/TPCW/image.jpg";
		int end = rd.nextInt(url.length());
		return url.substring(0, end);
	}

	public String getItemID() {
		return "" + rd.nextInt(tmd.itemCount);
	}

	// author
	public String getAuthorLastName() {
		int numAuthor = tmd.getRowCount("author");
		String name = pt.A_LNAME(rd.nextInt(numAuthor) + 1, numAuthor);
		int end = rd.nextInt(name.length());
		return name.substring(0, end);
	}

	public String getAuthorFirstName() {
		return pt.randomAstring(3, 20);
	}

	public String getAuthorDOB() throws Exception {
		Calendar cal = pt.randomDate(1, 1, 1800, 1, 1, 1990);
		return getSimpleDate(cal);
	}

	// orders
	public String getOrderID() {
		return "" + rd.nextInt(tmd.ordersCount);
	}

	public String getOrderlineDiscount() {
		int discount = rd.nextInt(3) + 1;
		return "0." + discount;
	}

	public int getOrderlineQuanty() {
		return pt.randomBetweenInclusive(1, 300);
	}

	public String getOrderDate() throws Exception {
		Calendar cal = Calendar.getInstance();
		Calendar O_DATE = pt.randomDateAdjust(cal,
				-pt.randomBetweenInclusive(0, 7));
		return getSimpleDate(O_DATE);
	}

	public float getOrderTotal() {
		float O_SUB_TOTAL = pt.randomFloatTwoWithin(10, 0, 9999, 99);
		float O_TAX = pt.O_TAX(O_SUB_TOTAL);
		return pt.O_TOTAL(O_SUB_TOTAL, O_TAX);
	}

	public String getOrderStatus() {
		return sp.status[rd.nextInt(sp.status.length)];
	}

	public float getOrderSubtotal() {
		return pt.randomFloatTwoWithin(10, 0, 9999, 99);
	}

	// cc_xacts
	public String getCXType() {
		return sp.cxtype[rd.nextInt(sp.cxtype.length)];
	}

	public String getCXName() {
		return pt.randomAstring(14, 30);
	}

	// customer

	public String getCustomerID() {
		return "" + rd.nextInt(tmd.customerCount);
	}

	public String getCustomerUserName() {
		int numCustomer = tmd.getRowCount("customer");
		String C_UNAME = pt.DigSyl(rd.nextInt(numCustomer) + 1, 0);
		int end = rd.nextInt(C_UNAME.length());
		return C_UNAME.substring(0, end);
	}

	public String getCustomerSince() throws Exception {
		Calendar cal = Calendar.getInstance();
		Calendar C_SINCE = pt.randomDateAdjust(cal,
				-(pt.randomBetweenInclusive(1, 173)));
		return getSimpleDate(C_SINCE);
	}

	public float getCustomerYTDpmt() {
		return pt.randomFloatTwoWithin(0, 0, 999, 99);
	}

	public int getCustomerBalance() {
		return rd.nextInt(100);
	}

	public String getCountryCurrency() {
		int i = sp.currentExchange.length;
		String[] cx = sp.currentExchange[rd.nextInt(i)];
		return cx[2];
	}

	public String getCountryID() {
		return "" + rd.nextInt(tmd.countryCount);
	}

	public String getCountryName() {
		int i = sp.currentExchange.length;
		String[] cx = sp.currentExchange[rd.nextInt(i)];
		return cx[0];
	}

	// utility
	public String getSimpleDate(Calendar cal) throws ParseException {
		return sp.month[cal.get(Calendar.MONTH)] + "/" + cal.get(Calendar.DATE)
				+ "/" + cal.get(Calendar.YEAR);
	}
}
