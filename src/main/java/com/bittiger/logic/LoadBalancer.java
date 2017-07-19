package com.bittiger.logic;

import java.util.ArrayList;
import java.util.List;

import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bittiger.client.ClientEmulator;
import com.bittiger.logic.rules.AvailabilityNotEnoughRule;

public class LoadBalancer {
	private List<Server> readQueue = new ArrayList<Server>();
	private Server writeQueue = null;
	private List<Server> candidateQueue = new ArrayList<Server>();
	private int nextReadServer = 0;
	private ClientEmulator c;
	private static transient final Logger LOG = LoggerFactory
			.getLogger(LoadBalancer.class);

	public LoadBalancer(ClientEmulator ce) {
		this.c = ce;
		writeQueue = new Server(ce.getTpcw().writeQueue[0]);
		for (int i = 0; i < ce.getTpcw().readQueue.length; i++) {
			readQueue.add(new Server(ce.getTpcw().readQueue[i]));
		}
		for (int i = 0; i < ce.getTpcw().candidateQueue.length; i++) {
			candidateQueue.add(new Server(ce.getTpcw().candidateQueue[i]));
		}
	}

	// there is only one server in the writequeue.
	public Server getWriteQueue() {
		return writeQueue;
	}

	public synchronized Server getNextReadServer() {
		nextReadServer = (nextReadServer + 1) % readQueue.size();
		Server server = readQueue.get(nextReadServer);
		LOG.debug("choose read server as " + server.getIp());
		return server;
	}

	public synchronized void addServer(Server server) {
		readQueue.add(server);
	}

	public synchronized Server removeServer() {
		Server server = readQueue.remove(readQueue.size() - 1);
		candidateQueue.add(server);
		return server;
	}

	public synchronized List<Server> getReadQueue() {
		return readQueue;
	}

	// readQueue is shared by the UserSessions and Executor.
	// However, candidateQueue is only called by Executor.
	public List<Server> getCandidateQueue() {
		return candidateQueue;
	}

	public synchronized void detectFailure() {
		/**
		 * Create a rules engine and register the business rule
		 */
		RulesEngine rulesEngine = RulesEngineBuilder.aNewRulesEngine().build();
		AvailabilityNotEnoughRule availabilityRule = new AvailabilityNotEnoughRule();
		availabilityRule.setInput(c, readQueue.size());
		rulesEngine.registerRule(availabilityRule);
		rulesEngine.fireRules();
	}

}
