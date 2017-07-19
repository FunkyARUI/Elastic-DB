package com.bittiger.querypool;

import java.util.StringTokenizer;
import java.util.Calendar;
import java.sql.PreparedStatement;
import java.text.ParseException;

public class wq2 implements QueryMetaData {
	public String query = "insert into customer values"
			+ "(?,'?','?','?','?',?,'?','?','?','?','?','?','?','?','?','?','?')";
	ParmGenerator pg = new ParmGenerator();

	public String getQueryStr() throws ParseException {
		PopulateTable pt = new PopulateTable();
		String qString = "";
		int count = 0;
		StringTokenizer st = new StringTokenizer(query, "?", false);
		int id = pt.randomBetweenInclusive(288000, 2147483647);
		String C_UNAME = pt.DigSyl(id + 1, 0);
		Calendar cal = Calendar.getInstance();
		Calendar C_SINCE = pt.randomDateAdjust(cal,
				-(pt.randomBetweenInclusive(1, 173)));
		Calendar C_LAST_VISIT = pt.randomDateAdjust(C_SINCE,
				pt.randomBetweenInclusive(0, 60));
		while (st.hasMoreTokens()) {
			qString += st.nextToken();
			count++;
			switch (count) {
			case 1:
				qString += id;
				break;
			case 2:
				qString += C_UNAME;
				break;
			case 3:
				qString += pt.DigSyl(id + 1, 0).toLowerCase();
				break;
			case 4:
				qString += pt.randomAstring(8, 15);
				break;
			case 5:
				qString += pt.randomAstring(8, 15);
				break;
			case 6:
				qString += pt.randomBetweenInclusive(1, 2 * 140000);
				break;
			case 7:
				qString += pt.randomNstring(9, 16);
				break;
			case 8:
				qString += C_UNAME + "@" + pt.randomAstring(2, 9) + ".com";
				break;
			case 9:
				qString += pt.sqlDate(C_SINCE);
				break;
			case 10:
				qString += pt.sqlDate(pt.currentDateCheck(C_LAST_VISIT));
				break;
			case 11:
				qString += pt.sqlDateTime(cal);
				break;
			case 12:
				qString += pt.sqlDate(pt.randomHourAdjust(cal, 2));
				break;
			case 13:
				qString += pt.randomFloatTwoWithin(0, 0, 0, 50);
				break;
			case 14:
				qString += (float) 0.00;
				break;
			case 15:
				qString += pt.randomFloatTwoWithin(0, 0, 999, 99);
				break;
			case 16:
				qString += pt.sqlDate(pt.randomDate(1, 1, 1880));
				break;
			case 17:
				qString += pt.randomAstring(100, 500);
				break;
			case 18:
				break;
			default:
				System.out.println("More token than expected");
				System.exit(100);
			}
		}
		return qString;
	}

}
