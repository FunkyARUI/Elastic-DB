package com.bittiger.querypool;

import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.Random;
import java.sql.PreparedStatement;
import java.text.ParseException;

public class wq5 implements QueryMetaData {
	public String query = "insert into order_line values" + "(?,?,?,?,?,'?')";
	ParmGenerator pg = new ParmGenerator();

	public String getQueryStr() throws ParseException {
		PopulateTable pt = new PopulateTable();
		String qString = "";
		int count = 0;
		StringTokenizer st = new StringTokenizer(query, "?", false);
		int id = pt.randomBetweenInclusive(6, 1000000);

		while (st.hasMoreTokens()) {
			qString += st.nextToken();
			count++;
			switch (count) {
			case 1:
				qString += id;
				break;
			case 2:
				qString += pt.randomBetweenInclusive(1, 259200);
				break;
			case 3:
				qString += pt.randomBetweenInclusive(1, 10000);
				break;
			case 4:
				qString += pt.randomBetweenInclusive(1, 300);
				break;
			case 5:
				float ol_discount = pt.randomFloatTwoWithin(0, 0, 0, 3) / 10; // sent
																				// [0.00..0.3]
																				// instead
																				// of
																				// [0.00..0.03],
																				// so
																				// divide
																				// by
																				// 10
				qString += pt.OL_DISCOUNT(ol_discount);
				break;
			case 6:
				qString += pt.randomAstring(20, 100);
				break;
			case 7:
				break;
			default:
				System.out.println("More token than expected");
				System.exit(100);
			}
		}
		return qString;
	}

}
