package com.bittiger.querypool;

import java.util.StringTokenizer;

// sql.getCDiscount
public class bq13 implements QueryMetaData {
	public String query = "select max(c_discount) from customer where "
			+ "(c_uname not like '?')";
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
