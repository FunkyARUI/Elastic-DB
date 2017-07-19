package com.bittiger.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bittiger.client.ClientEmulator;
import com.bittiger.client.Utilities;
import com.bittiger.querypool.CleanStatsQuery;
import com.bittiger.querypool.StatsQuery;

/**
 * We do not need strong consistency for stats in the monitor
 *
 */
public class Monitor {

	public final Vector<Stats> read;
	public final Vector<Stats> write;
	public final Vector<Stats> notAvailable;
	private ClientEmulator c;
	Connection con;

	private static transient final Logger LOG = LoggerFactory
			.getLogger(Monitor.class);

	public Monitor(ClientEmulator c) {
		read = new Vector<Stats>();
		write = new Vector<Stats>();
		notAvailable = new Vector<Stats>();
		this.c = c;
	}

	public void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(
					Utilities.getStatsUrl(c.getTpcw().writeQueue[0]),
					c.getTpcw().username, c.getTpcw().password);
			con.setAutoCommit(true);
			try {
				Statement stmt = con.createStatement();
				CleanStatsQuery clean = new CleanStatsQuery();
				stmt.executeUpdate(clean.getQueryStr());
				stmt.close();
				LOG.info("Clean stats at server " + c.getTpcw().writeQueue[0]);
			} catch (Exception e) {
				LOG.error(e.toString());
			}
		} catch (Exception e) {
			LOG.error(e.toString());
		}
	}

	private void updateStats(double x, double u, double r, double w, double m, double s) {
		try {
			Statement stmt = con.createStatement();
			StatsQuery stats = new StatsQuery(x, u, r, w, m, s);
			stmt.executeUpdate(stats.getQueryStr());
			stmt.close();
			LOG.info("Stats: Interval:" + x + ", Queries:" + u + ", Read:" + r + ", Write:" + w + ", Nodes:" + m + ", Sessions:" + s);
		} catch (Exception e) {
			LOG.error(e.toString());
		}
	}

	public void close() {
		try {
			con.close();
		} catch (Exception e) {
			LOG.error(e.toString());
		}
	}

	public void addQuery(int sessionId, String type, long start,
			long end) {
		// int id = Integer.parseInt(name.substring(name.indexOf("n") + 1));
		Stats stat = new Stats(sessionId, type, start, end);
		if (end > 0) {
			if (type.contains("b")) {
				read.add(stat);
			} else {
				write.add(stat);
			}
		} else {
			notAvailable.add(stat);
		}
		LOG.debug(stat.toString());
	}

	public String readPerformance() {
		StringBuffer perf = new StringBuffer();
		long currTime = System.currentTimeMillis();
		long validStartTime = Math.max(c.getStartTime() + c.getTpcw().warmup,
				currTime - c.getTpcw().interval);
		long validEndTime = Math.min(
				c.getStartTime() + c.getTpcw().warmup + c.getTpcw().mi,
				currTime);

		long totalTime = 0;
		int count = 0;
		int totCount = 0;
		double avgRead = 0.0;
		double avgWrite = 0.0;
		for (int i = 0; i < read.size(); i++) {
			Stats s = read.get(i);
			if ((validStartTime < s.start) && (s.start < validEndTime)) {
				count += 1;
				totalTime += s.duration;
			}
		}
		perf.append("R:" + count + ":" + totalTime);
		if (count > 0) {
			avgRead = totalTime / count;
			perf.append(":" + avgRead);
		} else {
			perf.append(":NA");
		}
		totCount += count;

		totalTime = 0;
		count = 0;
		for (int i = 0; i < write.size(); i++) {
			Stats s = write.get(i);
			if ((validStartTime < s.start) && (s.start < validEndTime)) {
				count += 1;
				totalTime += s.duration;
			}
		}
		perf.append(",W:" + count + ":" + totalTime);
		if (count > 0) {
			avgWrite = totalTime / count;
			perf.append(":" + avgWrite);
		} else {
			perf.append(":NA");
		}
		totCount += count;

		totalTime = 0;
		count = 0;
		for (int i = 0; i < notAvailable.size(); i++) {
			Stats s = notAvailable.get(i);
			if ((validStartTime < s.start) && (s.start < validEndTime)) {
				count += 1;
				totalTime += s.duration;
			}
		}
		perf.append(",NA:" + count + ":" + totalTime);
		if (count > 0) {
			perf.append(":" + totalTime / count);
		} else {
			perf.append(":NA");
		}
		totCount += count;

		int x = (int) ((currTime - c.getStartTime()) / c.getTpcw().interval);
		updateStats(x, totCount, avgRead, avgWrite,
				c.getLoadBalancer().getReadQueue().size(), c.getcurrNumSessionsStats());
		return perf.toString();
	}

}
