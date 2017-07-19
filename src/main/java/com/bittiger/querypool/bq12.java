package com.bittiger.querypool;

import java.util.StringTokenizer;

public class bq12 implements QueryMetaData {
	public String query = "select c_uname,c_passwd, c_fname, c_lname, c_phone, c_email, c_birthdate, c_data, "
			+ "addr_street1, addr_street2, addr_city, addr_state, addr_zip, co_name from customer, address, country "
			+ "where c_addr_id=addr_id and addr_co_id = co_id and c_id = ?";
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
				qString += pg.getCustomerID();
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
