package com.bittiger.querypool;

import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.Random;
import java.sql.PreparedStatement;
import java.text.ParseException;

public class wq4 implements QueryMetaData {
	public String query = "insert into cc_xacts values"
			+ "(?,'?',?,'?','?','?','?','?',?)";
	ParmGenerator pg = new ParmGenerator();

	public String getQueryStr() throws ParseException {
		PopulateTable pt = new PopulateTable();
		String qString = "";
		int count = 0;
		StringTokenizer st = new StringTokenizer(query, "?", false);
		int id = pt.randomBetweenInclusive(259200, 2147483647);
		Calendar cal = Calendar.getInstance();
		Calendar CX_EXPIRY = pt.randomDateAdjust(cal,
				-pt.randomBetweenInclusive(10, 730));
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
				qString += sp.cxtype[rd.nextInt(sp.cxtype.length)];
				break;
			case 3:
				qString += new Long(pt.randomNstring(16, 16)).longValue();
				break;
			case 4:
				qString += pt.randomAstring(14, 30);
				break;
			case 5:
				qString += pt.sqlDate(CX_EXPIRY);
				break;
			case 6:
				qString += pt.randomAstring(15, 15);
				break;
			case 7:
				// qString += pt.randomNstring(9,16);
				qString += rd.nextFloat() * 100000;
				break;
			case 8:
				qString += pt.sqlDate(CX_EXPIRY); // just copy
				break;
			case 9:
				qString += pt.randomBetweenInclusive(1, 92);
				break;
			case 10:
				break;
			default:
				System.out.println("More token than expected");
				System.exit(100);
			}
		}
		return qString;
	}

}
