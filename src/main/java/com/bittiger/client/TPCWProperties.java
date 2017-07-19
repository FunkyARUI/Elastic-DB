package com.bittiger.client;

import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TPCWProperties {

	private static ResourceBundle configuration = null;
	// Information about Workload
	public int workloads[];
	public double rate[];
	public double mixRate;
	public double TPCmean;
	public long warmup;
	public long mi;
	public long warmdown;
	public long interval;

	public double rwratio;
	public double read[];
	public double write[];

	// Information about server
	public String writeQueue[];
	public String readQueue[];
	public String candidateQueue[];

	public String username;
	public String password;
	
	public int destroyerSleepInterval;
	public String destroyTarget;

	private static transient final Logger LOG = LoggerFactory
			.getLogger(TPCWProperties.class);

	public TPCWProperties(String fileName) {
		try {
			configuration = ResourceBundle.getBundle(fileName);
			checkPropertiesFileAndGetURLGenerator();
		} catch (java.util.MissingResourceException e) {
			System.err
					.println("No properties file has been found in your classpath.<p>");
			Runtime.getRuntime().exit(1);
		}
	}

	protected String getProperty(String property) {
		return configuration.getString(property);
	}

	public void checkPropertiesFileAndGetURLGenerator() {
		try {
			username = getProperty("username");
			password = getProperty("password");
			destroyerSleepInterval = Integer.parseInt(getProperty("destroyerSleepInterval"));
			destroyTarget = getProperty("destroyTarget");
			mixRate = Double.parseDouble(getProperty("mixRate"));
			TPCmean = Double.parseDouble(getProperty("TPCmean"));
			warmup = Long.parseLong(getProperty("warmup"));
			warmdown = Long.parseLong(getProperty("warmdown"));
			mi = Long.parseLong(getProperty("mi"));
			interval = Long.parseLong(getProperty("interval"));

			StringTokenizer rl = null;
			int rlCnt = 0;
			rl = new StringTokenizer(getProperty("rate_vector"), ",");
			rate = new double[rl.countTokens()];
			while (rl.hasMoreTokens()) {
				rate[rlCnt] = Double.parseDouble(rl.nextToken().trim());
				rlCnt++;
			}
//			LOG.info("rate is " + Arrays.toString(rate));

			StringTokenizer wl = new StringTokenizer(
					getProperty("workload_vector"), ",");
			workloads = new int[wl.countTokens()];
			int wlCnt = 0;
			while (wl.hasMoreTokens()) {
				workloads[wlCnt] = Integer.parseInt(wl.nextToken().trim());
				wlCnt++;
			}
			LOG.info("workloads is " + Arrays.toString(workloads));
			
			if (workloads.length * interval != warmup + warmdown + mi) {
				LOG.error("workload length can not match warm up/down + mi");
				Runtime.getRuntime().exit(0);
			}

			rwratio = Double.parseDouble(getProperty("rwratio"));

			rl = new StringTokenizer(getProperty("read"), ",");
			read = new double[rl.countTokens()];
			rlCnt = 0;
			while (rl.hasMoreTokens()) {
				read[rlCnt] = Double.parseDouble(rl.nextToken().trim());
				rlCnt++;
			}
			LOG.info("read is " + Arrays.toString(read));

			rl = new StringTokenizer(getProperty("write"), ",");
			write = new double[rl.countTokens()];
			rlCnt = 0;
			while (rl.hasMoreTokens()) {
				write[rlCnt] = Double.parseDouble(rl.nextToken().trim());
				rlCnt++;
			}
			LOG.info("write is " + Arrays.toString(write));

			rl = new StringTokenizer(getProperty("readQueue"), ",");
			readQueue = new String[rl.countTokens()];
			rlCnt = 0;
			while (rl.hasMoreTokens()) {
				readQueue[rlCnt] = rl.nextToken().trim();
				rlCnt++;
			}
			LOG.info("readQueue is " + Arrays.toString(readQueue));

			
			rl = new StringTokenizer(getProperty("writeQueue"), ",");
			writeQueue = new String[rl.countTokens()];
			rlCnt = 0;
			while (rl.hasMoreTokens()) {
				writeQueue[rlCnt] = rl.nextToken().trim();
				rlCnt++;
			}
			LOG.info("writeQueue is " + Arrays.toString(writeQueue));

			rl = new StringTokenizer(getProperty("candidateQueue"), ",");
			candidateQueue = new String[rl.countTokens()];
			rlCnt = 0;
			while (rl.hasMoreTokens()) {
				candidateQueue[rlCnt] = rl.nextToken().trim();
				rlCnt++;
			}
			LOG.info("candidateQueue is " + Arrays.toString(candidateQueue));


		} catch (Exception e) {
			System.err.println("Error while checking properties: "
					+ e.getMessage());
			Runtime.getRuntime().exit(0);
		}
	}

	public static ResourceBundle getConfiguration() {
		return configuration;
	}

	public static void setConfiguration(ResourceBundle configuration) {
		TPCWProperties.configuration = configuration;
	}

}
