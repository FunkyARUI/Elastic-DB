package com.bittiger.querypool;

public class TableMetaData {
	// table attributes
	public String[] itemAtts = { "i_id number", "i_title", "i_a_id",
			"i_pub_date", "i_publisher", "i_subject", "i_desc", "i_related1",
			"i_related2", "i_related3", "i_related4", "i_related5",
			"i_thumbnail", "i_image", "i_srp", "i_cost", "i_avail", "i_stock",
			"i_isbn", "i_page", "i_backing", "i_dimentions" };
	public String[] authorAtts = { "a_id", "a_fname", "a_lname", "a_mname",
			"a_dob", "a_bio" };
	public String[] orderLineAtts = { "ol_id", "ol_o_id", "ol_i_id", "ol_qty",
			"ol_discount", "ol_comment" };
	public String[] ccXactsAtts = { "cx_o_id", "cx_type", "cx_num", "cx_name",
			"cx_expiry", "cx_auth_id", "cx_xact_amt", "cx_xact_date",
			"cx_co_id" };
	public String[] ordersAtts = { "o_id", "o_c_id", "o_date", "o_sub_total",
			"o_tax", "o_total", "o_ship_type", "o_ship_date", "o_bill_addr_id",
			"o_ship_addr_id", "o_status" };
	public String[] countryAtts = { "co_id", "co_name", "co_exchange",
			"co_currency" };
	public String[] customerAtts = { "c_id", "c_uname", "c_passwd", "c_fname",
			"c_lname", "c_addr_id", "c_phone", "c_email", "c_since",
			"c_last_visit", "c_login", "c_expiration", "c_discount",
			"c_balance", "c_ytd_pmt", "c_birthdate", "c_data" };
	public String[] addressAtts = { "addr_id", "addr_street1", "addr_street2",
			"addr_city", "addr_state", "addr_zip", "addr_co_id" };

	// table tow count
	// [java] Populating ADDRESS Table with 576000 addresses
	// [java] Complete (in 10,000's): 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
	// 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37 38 39 40 41
	// 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57
	// [java] Populating AUTHOR Table with 2500 authors
	// [java] Populating COUNTRY with 92 countries
	// [java] Populating CUSTOMER Table with 288000 customers
	// [java] Complete (in 10,000's): 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
	// 18 19 20 21 22 23 24 25 26 27 28
	// [java] Populating ITEM table with 10000 items
	// [java] Populating ORDERS, ORDER_LINES, CC_XACTS with 259200 orders
	// [java] Complete (in 10,000's): 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
	// 18 19 20 21 22 23 24 25
	// [java] Adding Indexes
	// [java] Done
	public int itemCount = 10000;
	public int authorCount = 2500;
	public int orderLineCount = 777342; //503344;
	public int ccXactsCount = 259200; //126000;
	public int countryCount = 92;
	public int customerCount = 288000; // 140000;
	public int addressCount = 57600;
	public int ordersCount = 126000;

	public int getRowCount(String tname) {
		if (tname.equalsIgnoreCase("item"))
			return itemCount;
		else if (tname.equalsIgnoreCase("author"))
			return authorCount;
		else if (tname.equalsIgnoreCase("order_line"))
			return orderLineCount;
		else if (tname.equalsIgnoreCase("cc_xacts"))
			return ccXactsCount;
		else if (tname.equalsIgnoreCase("country"))
			return countryCount;
		else if (tname.equalsIgnoreCase("customer"))
			return customerCount;
		else if (tname.equalsIgnoreCase("address"))
			return addressCount;
		else if (tname.equalsIgnoreCase("orders"))
			return ordersCount;
		else
			return 0;
	}

	public String[] getTableAtts(String tname) {
		if (tname.equalsIgnoreCase("item"))
			return itemAtts;
		else if (tname.equalsIgnoreCase("author"))
			return authorAtts;
		else if (tname.equalsIgnoreCase("order_line"))
			return orderLineAtts;
		else if (tname.equalsIgnoreCase("cc_xacts"))
			return ccXactsAtts;
		else if (tname.equalsIgnoreCase("country"))
			return countryAtts;
		else if (tname.equalsIgnoreCase("customer"))
			return customerAtts;
		else if (tname.equalsIgnoreCase("address"))
			return addressAtts;
		else if (tname.equalsIgnoreCase("orders"))
			return ordersAtts;
		else
			return null;
	}

	public String getTableByAtts(String atts) {
		if (atts.startsWith("i_"))
			return "item";
		else if (atts.startsWith("a_"))
			return "author";
		else if (atts.startsWith("ol_"))
			return "order_line";
		else if (atts.startsWith("cx_"))
			return "cc_xacts";
		else if (atts.startsWith("co_"))
			return "country";
		else if (atts.startsWith("c_"))
			return "customer";
		else if (atts.startsWith("addr_"))
			return "address";
		else if (atts.startsWith("o_"))
			return "orders";
		else
			return null;
	}

}
