package com.bittiger.querypool;

import java.util.StringTokenizer;

public class bq5 implements QueryMetaData {
	public String query = "select i_id, i_title,a_lname, a_fname from item, author where "
			+ "(i_subject like '%?%' or i_title like '%?%') and i_a_id = a_id";
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
				qString += pg.getItemTitle();
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
