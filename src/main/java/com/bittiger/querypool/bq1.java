package com.bittiger.querypool;

import java.util.StringTokenizer;

//sql.getCustomer
public class bq1 implements QueryMetaData {
	public String query = "SELECT * FROM customer, address, country WHERE customer.c_addr_id = address.addr_id AND address.addr_co_id = country.co_id AND "
			+ "(customer.c_uname = '?')";
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
				qString += pg.getCustomerUserName();
				break;
			case 2:
				break;
			default:
				System.out.println("More token than expected");
				System.exit(100);
			}
		}
		return qString;
	}
}
