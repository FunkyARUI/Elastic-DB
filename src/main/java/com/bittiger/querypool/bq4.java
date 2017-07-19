package com.bittiger.querypool;

import java.util.StringTokenizer;

public class bq4 implements QueryMetaData {
	public String query = "select i_id, i_title, a_lname, a_fname from item, author where "
			+ "i_a_id = a_id and "
			+ "(a_lname like '%?%' or a_fname like '%?%')";
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
				qString += pg.getAuthorLastName();
				break;
			case 2:
				qString += pg.getAuthorFirstName();
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
