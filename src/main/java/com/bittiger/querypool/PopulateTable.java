package com.bittiger.querypool;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class PopulateTable {

	StringPool sp = new StringPool(); // StringPool
	// row number variables
	int numCountry = 0;
	int numAuthor = 0;
	int numAddress = 0;
	int numItem = 0;
	int numCustomer = 0;
	int numOrders = 0;
	int numCCX = 0;
	int numOL = 0;
	int numEB = 0;
	String url = "http://localhost:8080/TPCW/image.jpg"; // for I_THUMBNAIL and
															// I_IMAGE
	Connection conn;
	String sql = null;
	Random rd = new Random();
	List<Float> O_TOTAL_LIST = new ArrayList<Float>();
	List<java.sql.Date> O_SHIP_DATE_LIST = new ArrayList<java.sql.Date>();
	List<String> I_ITEM_LIST = new ArrayList<String>();

	public PopulateTable() {
	}

	public PopulateTable(int eb, int item, int country, int author,
			int customer, int orders, int ol, int ccx, int address,
			Connection conn) throws SQLException {
		this.numEB = eb;
		this.numCountry = country;
		this.numAuthor = author;
		this.numAddress = address;
		this.numItem = item;
		this.numCustomer = customer;
		this.numOrders = orders;
		this.numCCX = ccx;
		this.numOL = ol;
		this.conn = conn;
	}

	// public void popTables(List<String> I_ITEM_LIST) throws SQLException,
	// ParseException, IOException {
	public void popTables() throws SQLException, ParseException, IOException {
		// ClassLoader cl = this.getClass().getClassLoader();
		// InputStream in =
		// cl.getResourceAsStream("P:\\workspace\\database-temp\\tpc-w\\title10000000");
		BufferedReader br = new BufferedReader(new FileReader(new File(
				"title10000000")));

		// new BufferedReader(new InputStreamReader(in));
		for (int i = 0; i < numItem; i++) {
			I_ITEM_LIST.add(i, br.readLine());
		}
		// this.I_ITEM_LIST = I_ITEM_LIST;
		System.out.println("EB:" + numEB + ",Item:" + numItem + ",Country:"
				+ numCountry + ",Author:" + numAuthor + ",Customer:"
				+ numCustomer + ",Orders:" + numOrders + ",OL:" + numOL
				+ ",CCX:" + numCCX + ",Address:" + numAddress);
		addAuthor(numAuthor);
		addCountry(numCountry);
		addAddress(numAddress);
		addCustomer(numCustomer);
		addOrders(numOrders);
		addCCXacts(numCCX);
		addItem(numItem);
		addOrderLine(numOrders); // number of order line actually determined by
									// number of orders
		System.out.println("Populate tables completed!");
	}

	public void addCountry(int numCountry) throws SQLException {
		sql = "insert into country values(?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (int i = 0; i < numCountry; i++) {
			pstmt.setInt(1, i + 1); // CO_ID number(4)
			pstmt.setString(2, sp.currentExchange[i][0]); // CO_NAME
															// varchar2(50)
			pstmt.setFloat(3,
					(new Float(sp.currentExchange[i][1])).floatValue()); // CO_EXCHANGE
																			// number(6,6)
			pstmt.setString(4, sp.currentExchange[i][2]); // CO_CURRENCY
															// varchar2(18)
			pstmt.addBatch();
		}
		pstmt.executeBatch();
		pstmt.close();
		conn.commit();
		System.out.println("Country table populated");
	}

	public void addAuthor(int numAuthor) throws SQLException, ParseException {
		sql = "insert into author values(?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (int i = 0; i < numAuthor; i++) {
			pstmt.setInt(1, i + 1); // A_ID number(10)
			pstmt.setString(2, randomAstring(3, 20)); // A_FNAME varchar2(20)
			pstmt.setString(3, A_LNAME(i + 1, numAuthor)); // A_LNAME
															// varchar2(20)
			pstmt.setString(4, randomAstring(1, 20)); // A_MNAME varchar2(20)
			pstmt.setDate(5, sqlDate(randomDate(1, 1, 1800, 1, 1, 1990))); // A_DOB
																			// date
			pstmt.setString(6, randomAstring(125, 500)); // A_BIO varchar2(500)
			// pstmt.addBatch();
			pstmt.execute();
		}
		// pstmt.executeBatch();
		pstmt.close();
		conn.commit();
		System.out.println("Author table populated");
	}

	public void addAddress(int numAddress) throws SQLException {
		sql = "insert into address values(?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (int i = 0; i < numAddress; i++) {
			pstmt.setInt(1, i + 1); // ADDR_ID number(10)
			pstmt.setString(2, randomAstring(15, 40)); // ADDR_STREET1
														// varchar2(40)
			pstmt.setString(3, randomAstring(15, 40)); // ADDR_STRRET2
														// varchar2(40)
			pstmt.setString(4, randomAstring(4, 30)); // ADDR_CITY varchar2(30)
			pstmt.setString(5, randomAstring(2, 20)); // ADDR_STATE varchar2(20)
			pstmt.setString(6, randomAstring(5, 10)); // ADDR_ZIP varchar2(5)
			pstmt.setInt(7, randomBetweenInclusive(1, 92)); // ADDR_CO_ID
															// number(4)
			// pstmt.addBatch();
			pstmt.execute();
		}
		// pstmt.executeBatch();
		pstmt.close();
		conn.commit();
		System.out.println("Address table populated");
	}

	public void addItem(int numItem) throws SQLException, ParseException {
		sql = "insert into item values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (int i = 0; i < numItem; i++) {
			pstmt.setInt(1, i + 1); // I_ID number(10)
			pstmt.setString(2, I_ITEM_LIST.get(i)); // I_TITLE varchar2(60)
			pstmt.setInt(3, I_A_ID(i + 1, numItem)); // I_A_ID number(10)
			Calendar cal = randomDate(1, 1, 1930);
			pstmt.setDate(4, sqlDate(cal)); // I_PUB_DATE date
			pstmt.setString(5, randomAstring(14, 60)); // I_PUBLISHER
														// varchar2(60)
			pstmt.setString(6, sp.subjects[rd.nextInt(sp.subjects.length)]); // I_SUBJECT
																				// varchar2(60)
			pstmt.setString(7, randomAstring(100, 500)); // I_DESC varcahr2(500)
			List<Integer> relatedItemList = randomUnique(5, 10);
			pstmt.setInt(8, relatedItemList.get(0).intValue()); // I_RELATED1
																// number(10)
			pstmt.setInt(9, relatedItemList.get(1).intValue()); // I_RELATED2
																// number(10)
			pstmt.setInt(10, relatedItemList.get(2).intValue()); // I_RELATED3
																	// number(10)
			pstmt.setInt(11, relatedItemList.get(3).intValue()); // I_RELATED4
																	// number(10)
			pstmt.setInt(12, relatedItemList.get(4).intValue()); // I_RELATED5
																	// number(10)
			pstmt.setString(13, url); // I_THUMBNAIL varchar2(100)
			pstmt.setString(14, url); // I_IMAGE varchar2(100)
			float I_SRP = randomFloatTwoWithin(1, 0, 9999, 99);
			pstmt.setFloat(15, I_SRP); // I_SRP number(15,2)
			pstmt.setFloat(16, I_COST(I_SRP)); // I_COST number(15,2)
			pstmt.setDate(17, sqlDate(randomDateAdjust(cal, 30))); // I_AVAIL
																	// date
			pstmt.setInt(18, randomBetweenInclusive(10, 30)); // I_STOCK
																// number(4)
			pstmt.setString(19, randomAstring(1, 13)); // I_ISBN varchar2(13)
			pstmt.setInt(20, randomBetweenInclusive(20, 9999)); // I_PAGE
																// number(4)
			pstmt.setString(21, sp.backings[rd.nextInt(sp.backings.length)]); // I_BACKING
																				// varchar2(15)
			pstmt.setString(22, randomFloatTwoWithin(0, 1, 99, 99) + "x"
					+ randomFloatTwoWithin(0, 1, 99, 99) + "x"
					+ randomFloatTwoWithin(0, 1, 99, 99)); // I_DIMENSION
															// varchar2(25)
			// pstmt.addBatch();
			pstmt.execute();
		}
		// pstmt.executeBatch();
		pstmt.close();
		conn.commit();
		System.out.println("Item table populated");
	}

	public void addCustomer(int numCustomer) throws SQLException,
			ParseException {
		sql = "insert into customer values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);

		for (int i = 0; i < numCustomer; i++) {
			pstmt.setInt(1, i + 1); // C_ID number(10)
			String C_UNAME = DigSyl(i + 1, 0);
			pstmt.setString(2, C_UNAME); // C_UNAME varchar2(20)
			pstmt.setString(3, DigSyl(i + 1, 0).toLowerCase()); // C_PASSWD
																// varchar2(20)
			pstmt.setString(4, randomAstring(8, 15)); // C_FNAME varchar2(15)
			pstmt.setString(5, randomAstring(8, 15)); // C_LNAME varchar2(15)
			pstmt.setInt(6, randomBetweenInclusive(1, 2 * numCustomer)); // C_ADDR_ID
																			// number(10)
			pstmt.setString(7, randomNstring(9, 16)); // C_PHONE varchar2(16)
			pstmt.setString(8, C_UNAME + "@" + randomAstring(2, 9) + ".com"); // C_EMAIL
																				// varchar2(50)
			Calendar cal = Calendar.getInstance();
			Calendar C_SINCE = randomDateAdjust(cal,
					-(randomBetweenInclusive(1, 173)));
			pstmt.setDate(9, sqlDate(C_SINCE)); // C_SINCE date
			Calendar C_LAST_VISIT = randomDateAdjust(C_SINCE,
					randomBetweenInclusive(0, 60));
			pstmt.setDate(10, sqlDate(currentDateCheck(C_LAST_VISIT))); // C_LAST_VISIT
																		// date
			pstmt.setDate(11, sqlDateTime(cal)); // C_LOGIN date
			pstmt.setDate(12, sqlDate(randomHourAdjust(cal, 2))); // C_EXPIRATION
																	// date
			pstmt.setFloat(13, randomFloatTwoWithin(0, 0, 0, 50)); // C_DISCOUNT
																	// number(3,2)
			pstmt.setFloat(14, (float) 0.00); // C_BALANCE number(15,2)
			pstmt.setFloat(15, randomFloatTwoWithin(0, 0, 999, 99)); // C_YTD_PMT
																		// number(15,2)
			pstmt.setDate(16, sqlDate(randomDate(1, 1, 1880))); // C_BIRTHDAY
																// date
			pstmt.setString(17, randomAstring(100, 500)); // C_DATA
															// varchar2(500)
			// pstmt.addBatch();
			pstmt.execute();
		}
		// pstmt.executeBatch();
		pstmt.close();
		conn.commit();
		System.out.println("Customer table populated");
	}

	public void addOrders(int numOrders) throws SQLException, ParseException {
		sql = "insert into orders values(?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (int i = 0; i < numOrders; i++) {
			pstmt.setInt(1, i + 1); // O_ID number(10)
			pstmt.setInt(2, randomBetweenInclusive(1, (int) numCustomer)); // O_C_ID
																			// number(10)
			Calendar cal = Calendar.getInstance();
			Calendar O_DATE = randomDateAdjust(cal,
					-randomBetweenInclusive(0, 7));
			pstmt.setDate(3, sqlDate(O_DATE)); // O_DATE date
			float O_SUB_TOTAL = randomFloatTwoWithin(10, 0, 9999, 99);
			pstmt.setFloat(4, O_SUB_TOTAL); // O_SUB_TOTAL number(15,2)
			float O_TAX = O_TAX(O_SUB_TOTAL);
			pstmt.setFloat(5, O_TAX); // O_TAX number(15,2)
			float O_TOTAL = O_TOTAL(O_SUB_TOTAL, O_TAX);
			O_TOTAL_LIST.add(i, (Float) O_TOTAL);
			pstmt.setFloat(6, O_TOTAL); // O_TOTAL number(15,2)
			pstmt.setString(7, sp.shiptype[rd.nextInt(sp.shiptype.length)]); // O_SHIP_TYPE
																				// archar2(10)
			Calendar O_SHIP_DATE = randomDateAdjust(O_DATE,
					randomBetweenInclusive(0, 2));
			O_SHIP_DATE_LIST.add(i, sqlDateTime(O_SHIP_DATE));
			pstmt.setDate(8, sqlDateTime(O_SHIP_DATE)); // O_SHIP_DATE date
			pstmt.setInt(9, randomBetweenInclusive(1, 2 * numCustomer)); // O_BILL_ADDR_ID
																			// number(10)
			pstmt.setInt(10, randomBetweenInclusive(1, 2 * numCustomer)); // O_SHIP_ADDR_ID
																			// number(10)
			pstmt.setString(11, sp.status[rd.nextInt(sp.status.length)]); // O_STATUS
			// pstmt.addBatch();
			pstmt.execute();
		}
		// pstmt.executeBatch();
		pstmt.close();
		conn.commit();
		System.out.println("Orders table populated");
	}

	public void addCCXacts(int numCCX) throws SQLException, ParseException {
		sql = "insert into cc_xacts values(?,?,?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (int i = 0; i < numCCX; i++) {
			pstmt.setInt(1, i + 1); // CX_O_ID number(10)
			pstmt.setString(2, sp.cxtype[rd.nextInt(sp.cxtype.length)]); // CX_TYPE
																			// varchar2(10)
			pstmt.setLong(3, new Long(randomNstring(16, 16)).longValue()); // CX_NUM
																			// number(16)
			pstmt.setString(4, randomAstring(14, 30)); // CX_NAME varchar2(31)
			Calendar cal = Calendar.getInstance();
			Calendar CX_EXPIRY = randomDateAdjust(cal,
					-randomBetweenInclusive(10, 730));
			pstmt.setDate(5, sqlDate(CX_EXPIRY)); // CX_EXPIRY date
			pstmt.setString(6, randomAstring(15, 15)); // CX_AUTH_ID char(15)
			// pstmt.setFloat(7, O_TOTAL_LIST.get(i).floatValue()); //
			// CX_XACT_AMT number(15,2)
			pstmt.setFloat(7, 1); // CX_XACT_AMT number(15,2)
			pstmt.setDate(8, O_SHIP_DATE_LIST.get(i)); // CX_XACT_DATE date
			pstmt.setInt(9, randomBetweenInclusive(1, 92)); // CX_CO_ID
															// number(4)
			// pstmt.addBatch();
			pstmt.execute();
		}
		// pstmt.executeBatch();
		pstmt.close();
		conn.commit();
		System.out.println("CCXacts table populated");
	}

	public void addOrderLine(int numOrders) throws SQLException, ParseException {
		sql = "insert into order_line values(?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		for (int j = 0; j < numOrders; j++) {
			numOL = rd.nextInt(5) + 1;
			List<Integer> ol_id_list = randomUnique(100, 100);
			for (int i = 0; i < numOL + 1; i++) {
				pstmt.setInt(1, ol_id_list.get(i)); // OL_ID number(10)
				pstmt.setInt(2, j + 1); // OL_O_ID number(10)
				pstmt.setInt(3, randomBetweenInclusive(1, numItem)); // OL_I_ID
																		// number(10)
				pstmt.setInt(4, randomBetweenInclusive(1, 300)); // OL_QTY
																	// number(3)
				float ol_discount = randomFloatTwoWithin(0, 0, 0, 3) / 10; // sent
																			// [0.00..0.3]
																			// instead
																			// of
																			// [0.00..0.03],
																			// so
																			// divide
																			// by
																			// 10
				pstmt.setFloat(5, OL_DISCOUNT(ol_discount)); // OL_DISCOUNT
																// number(3,2)
				pstmt.setString(6, randomAstring(20, 100)); // OL_COMMENTS
															// varchar2(100)
				// pstmt.addBatch();
				pstmt.execute();
			}
		}
		// pstmt.executeBatch();
		pstmt.close();
		conn.commit();
		System.out.println("Order_line table populated");
	}

	public String DigSyl(int D, int N) { // TCP-W
		String DString = (new Integer(D)).toString();
		int ddigit = DString.length();
		if (N > ddigit) {
			for (int i = 0; i < N - ddigit; i++) {
				DString = "0" + DString;
			}
		}
		return concateSyl(DString);
	}

	public String concateSyl(String DString) { // used by DigSyl()
		String str = "";
		int ddigit = DString.length();
		for (int i = 0; i < ddigit; i++) {
			int index = Character.getNumericValue(DString.charAt(i));
			str += sp.syllables[index];
		}
		return str;
	}

	public String randomAstring(int x, int y) { // TCP-W
		int strLength = randomBetweenInclusive(x, y);
		String str = "";
		for (int i = 0; i < strLength; i++) {
			str += sp.characters[rd.nextInt(sp.characters.length)];
		}
		return str;
	}

	public String randomNstring(int x, int y) { // TCP-W
		int strLength = randomBetweenInclusive(x, y);
		String str = "";
		for (int i = 0; i < strLength; i++) {
			str += sp.numbers[rd.nextInt(sp.numbers.length)];
		}
		return str;
	}

	public String randomPermutation(int x, int y) { // TPC-W
		String str = "";
		String numStr = "0123456789".substring(x, y + 1);
		while (numStr.length() > 0) {
			int index = rd.nextInt(numStr.length());
			str += numStr.substring(index, index + 1);
			numStr = numStr.substring(0, index) + numStr.substring(index + 1);
		}
		return str;
	}

	public String DBGEN(int I_ID, int numItem) { // TPC-W
		// generate partial I_ITEM as WGEN
		if (I_ID <= numItem / 5)
			return DigSyl(I_ID, 7);
		else
			return DigSyl(randomBetweenInclusive(1, (int) (I_ID / 5)), 7);
	}

	public Calendar randomDate(int smon, int sdate, int syear, int emon,
			int edate, int eyear) throws ParseException {
		// random day between start date and end date
		Calendar cal = Calendar.getInstance();
		int year = randomBetweenInclusive(smon, eyear);
		if (year == syear) {
			cal.set(Calendar.MONTH, randomBetweenInclusive(smon, 12));
			if (emon == 12)
				cal.set(Calendar.DATE, randomBetweenInclusive(sdate, 31));
			else
				cal.set(Calendar.DATE, randomDate(emon, year));
		} else {
			int mon = randomBetweenInclusive(1, 12);
			cal.set(Calendar.MONTH, mon);
			cal.set(Calendar.DATE, randomDate(mon, year));
		}
		return cal;
	}

	public int randomDate(int mon, int year) {
		if (mon == 2) {
			if (year % 4 == 0)
				return randomBetweenInclusive(1, 29);
			else
				return randomBetweenInclusive(1, 28);
		} else {
			if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8
					|| mon == 10 || mon == 12)
				return randomBetweenInclusive(1, 31);
			else
				return randomBetweenInclusive(1, 30);
		}
	}

	public Calendar randomDate(int smon, int sdate, int syear)
			throws ParseException {
		// random day between start date and current date
		Calendar cal = Calendar.getInstance();
		int emon = cal.get(Calendar.MONTH);
		int edate = cal.get(Calendar.DAY_OF_MONTH);
		int eyear = cal.get(Calendar.YEAR);
		return randomDate(smon, sdate, syear, emon, edate, eyear);
	}

	public Calendar randomDateAdjust(Calendar cal, int diff)
			throws ParseException {
		// adjust cal date by diff
		cal.add(Calendar.DATE, diff);
		return cal;
	}

	public Calendar randomHourAdjust(Calendar cal, int diff)
			throws ParseException {
		// adjust cal hour by diff
		cal.add(Calendar.HOUR, diff);
		return cal;
	}

	public java.sql.Date sqlDate(Calendar cal) throws ParseException {
		// convert Calendar to java.sql.Date
		SimpleDateFormat formatter = new SimpleDateFormat("MMM/dd/yyyy");
		String dateStr = sp.month[cal.get(Calendar.MONTH)] + "/"
				+ cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR);
		java.util.Date utilDate = formatter.parse(dateStr);
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		return sqlDate;
	}

	public java.sql.Date sqlDateTime(Calendar cal) throws ParseException {
		// convert Calendar to jave.sql.Date including time
		SimpleDateFormat formatter = new SimpleDateFormat(
				"MMM/dd/yyyy HH:MM:SS aa");
		String AM_PM = "AM";
		if (cal.get(Calendar.AM) == 1)
			AM_PM = "PM";
		String dateStr = sp.month[cal.get(Calendar.MONTH)] + "/"
				+ cal.get(Calendar.DATE) + "/" + cal.get(Calendar.YEAR) + " "
				+ cal.get(Calendar.HOUR) + ":" + cal.get(Calendar.MINUTE) + ":"
				+ cal.get(Calendar.SECOND) + " " + AM_PM;
		java.util.Date utilDate = formatter.parse(dateStr);
		java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		return sqlDate;
	}

	public Calendar currentDateCheck(Calendar cal) {
		// make sure if cal is no later than current date
		Calendar current = Calendar.getInstance();
		if (current.compareTo(cal) < 0)
			return current;
		else
			return cal;
	}

	public int I_A_ID(int iid, int numItem) {
		if (iid <= numItem / 4)
			return iid;
		else
			return rd.nextInt(numItem / 4) + 1;
	}

	public int randomBetweenInclusive(int s, int e) {
		// generate a random integer between s and e both inclusive
		int result = rd.nextInt(e - s + 1) + s;
		// System.out.println("s ="+s+", e ="+e+", result = "+result);
		return result;
	}

	public float randomFloatTwoWithin(int s, int sp, int e, int ep) {
		// generate a random float with 2 digit precision
		String floatStr = ((Integer) (rd.nextInt(e - s + 1) + s)).toString()
				+ "." + ((Integer) (rd.nextInt(ep - sp + 1) + sp)).toString();
		return new Float(floatStr).floatValue();
	}

	public List<Integer> randomUnique(int num, int range) {
		// generate certain num of unique integers fall within the range
		// [1..range]
		List<Integer> strList = new ArrayList<Integer>(num);
		while (num > 0) {
			Integer id = (Integer) (rd.nextInt(range) + 1);
			if (!strList.contains(id)) {
				strList.add(id);
				num--;
			}
		}
		return strList;
	}

	public float I_COST(float I_SRP) {
		// calculated 2 digit precision I_COST
		float costRate = randomFloatTwoWithin(0, 0, 0, 5);
		String I_COST_Str = ((Float) (I_SRP - (I_SRP * costRate))).toString();
		if (I_COST_Str.indexOf(".") + 2 > I_COST_Str.length())
			return new Float(I_COST_Str.substring(0,
					I_COST_Str.indexOf(".") + 3)).floatValue();
		else
			return new Float(I_COST_Str).floatValue();
	}

	public float O_TAX(float O_SUB_TOTAL) {
		// calculate two digit precision O_TAX
		String O_TAX_Str = ((Float) (O_SUB_TOTAL * (float) 0.0825)).toString();
		if (O_TAX_Str.indexOf(".") + 2 > O_TAX_Str.length())
			return new Float(O_TAX_Str.substring(0, O_TAX_Str.indexOf(".") + 3))
					.floatValue();
		else
			return new Float(O_TAX_Str).floatValue();
	}

	public float O_TOTAL(float O_SUB_TOTAL, float O_TAX) {
		// calculate 2 digit precision O_TOTAL
		String O_TOTAL_Str = ((Float) (O_SUB_TOTAL + O_TAX + (float) 3.00 + 1))
				.toString();
		if (O_TOTAL_Str.indexOf(".") + 2 > O_TOTAL_Str.length())
			return new Float(O_TOTAL_Str.substring(0,
					O_TOTAL_Str.indexOf(".") + 3)).floatValue();
		else
			return new Float(O_TOTAL_Str).floatValue();
	}

	public float OL_DISCOUNT(float OL_DISCOUNT) {
		// calculate 2 digit precision OL_DISCOUNT
		String OL_DISC_Str = ((Float) OL_DISCOUNT).toString();
		if (OL_DISC_Str.indexOf(".") + 2 > OL_DISC_Str.length())
			return new Float(OL_DISC_Str.substring(0,
					OL_DISC_Str.indexOf(".") + 3)).floatValue();
		else
			return new Float(OL_DISC_Str).floatValue();
	}

	public String A_LNAME(int A_ID, int numAuthor) {
		// generate A_LNAME as WGEN
		if (A_ID <= numAuthor / 2.5)
			return DigSyl(A_ID, 7);
		else
			return DigSyl(randomBetweenInclusive(1, (int) (A_ID / 2.5)), 7);
	}
}
