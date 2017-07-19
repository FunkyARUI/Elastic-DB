package com.bittiger.querypool;

import java.util.StringTokenizer;

public class wq1 implements QueryMetaData {
	public String query = "insert into address values"
			+ "(?,'?','?','?','?','?',?)";
	ParmGenerator pg = new ParmGenerator();

	public String getQueryStr() {
		PopulateTable pt = new PopulateTable();
		String qString = "";
		int count = 0;
		StringTokenizer st = new StringTokenizer(query, "?", false);
		while (st.hasMoreTokens()) {
			qString += st.nextToken();
			count++;
			switch (count) {
			case 1:
				qString += pt.randomBetweenInclusive(576000, 2147483647);
				break;
			case 2:
				qString += pt.randomAstring(15, 40);
				break;
			case 3:
				qString += pt.randomAstring(15, 40);
				break;
			case 4:
				qString += pt.randomAstring(4, 30);
				break;
			case 5:
				qString += pt.randomAstring(2, 20);
				break;
			case 6:
				qString += pt.randomAstring(5, 10);
				break;
			case 7:
				qString += pt.randomBetweenInclusive(1, 92);
				break;
			case 8:
				break;
			default:
				System.out.println("More token than expected");
				System.exit(100);
			}
		}
		return qString;
	}
}
