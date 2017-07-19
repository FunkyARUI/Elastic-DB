package com.bittiger.querypool;

import java.util.StringTokenizer;

//sql.getBook
public class bq3 implements QueryMetaData {
	public String query = "select * from item, author where "
			+ "i_a_id = a_id and " + "i_id = ?";
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
				qString += pg.getItemID();
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
