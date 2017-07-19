package com.bittiger.querypool;

import java.util.StringTokenizer;

public class bq6 implements QueryMetaData {
	public String query = "SELECT i_id, i_title, a_fname, a_lname "
			+ "FROM item, author, order_line "
			+ "WHERE item.i_id = order_line.ol_i_id "
			+ "AND item.i_a_id = author.a_id "
			+ "AND (item.i_subject like '%?%')"
			+ "GROUP BY i_id, i_title, a_fname, a_lname "
			+ "ORDER BY SUM(ol_qty) DESC " + "limit 50";
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
				qString += pg.getItemSubject();
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
