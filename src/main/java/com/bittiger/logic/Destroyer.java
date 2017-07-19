package com.bittiger.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bittiger.client.ClientEmulator;
import com.bittiger.client.Utilities;

public class Destroyer extends Thread {

	private ClientEmulator c;

	public Destroyer(ClientEmulator c) {
		this.c = c;
	}

	private static transient final Logger LOG = LoggerFactory
			.getLogger(Destroyer.class);

	@Override
	public void run() {
		// Executor executors the events in the queue one by one.
		LOG.info("Destroyer starts......");
		try {
			Thread.sleep(c.getTpcw().interval
					* c.getTpcw().destroyerSleepInterval);
			LOG.info("Destroyer destroys " + c.getTpcw().destroyTarget);
			Utilities.scaleIn(c.getTpcw().destroyTarget);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
	}
}
