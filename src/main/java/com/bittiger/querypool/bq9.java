package com.bittiger.querypool;

import java.util.StringTokenizer;

public class bq9 implements QueryMetaData {
	public String query = "select ol_i_id, i_title, i_publisher, i_cost, ol_qty, ol_discount, ol_comments "
			+ "from order_line, item orders "
			+ "where ol_i_id=i_id and ol_o_id=?";
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
				break;
			default:
				System.out.println("More token than expected");
				System.exit(100);
			}
		}
		return qString;
	}

}
