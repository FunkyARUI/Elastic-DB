package com.bittiger.querypool;

import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.Random;
import java.sql.PreparedStatement;
import java.text.ParseException;

public class wq3 implements QueryMetaData {
	public String query = "insert into orders values"
			+ "(?,?,'?','?','?','?','?','?',?,?,'?')";
	ParmGenerator pg = new ParmGenerator();

	public String getQueryStr() throws ParseException {
		PopulateTable pt = new PopulateTable();
		String qString = "";
		int count = 0;
		StringTokenizer st = new StringTokenizer(query, "?", false);
		int id = pt.randomBetweenInclusive(126000, 2147483647);

		Calendar cal = Calendar.getInstance();
		Calendar O_DATE = pt.randomDateAdjust(cal,
				-pt.randomBetweenInclusive(0, 7));
		float O_SUB_TOTAL = pt.randomFloatTwoWithin(10, 0, 9999, 99);
		float O_TAX = pt.O_TAX(O_SUB_TOTAL);
		float O_TOTAL = pt.O_TOTAL(O_SUB_TOTAL, O_TAX);
		Calendar O_SHIP_DATE = pt.randomDateAdjust(O_DATE,
				pt.randomBetweenInclusive(0, 2));
		StringPool sp = new StringPool(); // StringPool
		Random rd = new Random();

		while (st.hasMoreTokens()) {
			qString += st.nextToken();
			count++;
			switch (count) {
			case 1:
				qString += id;
				break;
			case 2:
				qString += pt.randomBetweenInclusive(1, (int) 288000);
				break;
			case 3:
				qString += pt.sqlDate(O_DATE);
				break;
			case 4:
				qString += O_SUB_TOTAL;
				break;
			case 5:
				qString += O_TAX;
				break;
			case 6:
				qString += O_TOTAL;
				break;
			case 7:
				qString += sp.shiptype[rd.nextInt(sp.shiptype.length)];
				break;
			case 8:
				qString += pt.sqlDateTime(O_SHIP_DATE);
				break;
			case 9:
				qString += pt.randomBetweenInclusive(1, 576000);
				break;
			case 10:
				qString += pt.randomBetweenInclusive(1, 576000);
				break;
			case 11:
				qString += sp.status[rd.nextInt(sp.status.length)];
				break;
			case 12:
				break;
			default:
				System.out.println("More token than expected");
				System.exit(100);
			}
		}
		return qString;
	}

}
