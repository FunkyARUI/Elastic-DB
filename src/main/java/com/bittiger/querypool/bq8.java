package com.bittiger.querypool;

import java.util.StringTokenizer;

//sql.getMostRecentOrder.order
public class bq8 implements QueryMetaData {
	public String query = "SELECT orders.*, customer.*, "
			+ "  cc_xacts.cx_type, "
			+ "  ship.addr_street1 AS ship_addr_street1, "
			+ "  ship.addr_street2 AS ship_addr_street2, "
			+ "  ship.addr_state AS ship_addr_state, "
			+ "  ship.addr_zip AS ship_addr_zip, "
			+ "  ship_co.co_name AS ship_co_name, "
			+ "  bill.addr_street1 AS bill_addr_street1, "
			+ "  bill.addr_street2 AS bill_addr_street2, "
			+ "  bill.addr_state AS bill_addr_state, "
			+ "  bill.addr_zip AS bill_addr_zip, "
			+ "  bill_co.co_name AS bill_co_name "
			+ "FROM customer, orders, cc_xacts," + "  address AS ship, "
			+ "  country AS ship_co, " + "  address AS bill,  "
			+ "  country AS bill_co " + "WHERE orders.o_id = ? "
			+ "  AND cx_o_id = orders.o_id "
			+ "  AND customer.c_id = orders.o_c_id "
			+ "  AND orders.o_bill_addr_id = bill.addr_id "
			+ "  AND bill.addr_co_id = bill_co.co_id "
			+ "  AND orders.o_ship_addr_id = ship.addr_id "
			+ "  AND ship.addr_co_id = ship_co.co_id "
			+ "  AND orders.o_c_id = customer.c_id";
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
				qString += pg.getOrderID();
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
