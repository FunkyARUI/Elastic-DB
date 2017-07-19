package com.bittiger.querypool;

import java.util.StringTokenizer;

public class StatsQuery {
	public StatsQuery(double x, double u, double r, double w, double m, double s) {
		super();
		this.x = x;
		this.u = u;
		this.r = r;
		this.w = w;
		this.m = m;
		this.s = s;
	}

	double x;
	double u;
	double r;
	double w;
	double m;
	double s;

	public String query = "insert into datapoints values" + "(?,?,?,?,?,?)";

	public String getQueryStr() {
		String qString = "";
		int count = 0;
		StringTokenizer st = new StringTokenizer(query, "?", false);
		while (st.hasMoreTokens()) {
			qString += st.nextToken();
			count++;
			switch (count) {
			case 1:
				qString += x;
				break;
			case 2:
				qString += u;
				break;
			case 3:
				qString += r;
				break;
			case 4:
				qString += w;
				break;
			case 5:
				qString += m;
				break;
			case 6:
				qString += s;
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
