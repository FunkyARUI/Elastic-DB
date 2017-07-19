package com.bittiger.querypool;

import java.util.StringTokenizer;

public class bq7 implements QueryMetaData {
	public String query = "select c_fname, c_lname, c_phone, c_email, "
			+ "o_id, o_date, o_sub_total, o_tax, o_total, o_ship_type, o_ship_date, o_bill_addr_id, o_ship_addr_id, o_c_id, o_status, "
			+ "addr_street1, addr_street2, addr_city, addr_state, addr_zip, co_name from customer, address, country, orders "
			+ "where o_id=? and c_id=? and o_bill_addr_id=addr_id and addr_co_id = co_id";
	ParmGenerator pg = new ParmGenerator();

	public String getQueryStr() {
		String qString = "";
		int count = 0;
		StringTokenizer st = new StringTokenizer(query, "?", false);
		while (st.hasMoreTokens()) {
			qString += st.nextToken();
			count++;
			switch (count) {
			case 1:
				qString += pg.getOrderID();
				break;
			case 2:
				qString += pg.getCustomerID();
				break;
			case 3:
				break;
			default:
				System.out.println("More token than expected");
				System.exit(100);
			}
		}
		return qString;
	}
}
