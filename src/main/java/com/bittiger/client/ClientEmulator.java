package com.bittiger.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.IOException;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bittiger.logic.ActionType;
import com.bittiger.logic.Controller;
import com.bittiger.logic.Destroyer;
import com.bittiger.logic.EventQueue;
import com.bittiger.logic.Executor;
import com.bittiger.logic.LoadBalancer;
import com.bittiger.logic.Monitor;

public class ClientEmulator {

	@Option(name = "-c", usage = "enable controller")
	private boolean enableController;
	@Option(name = "-d", usage = "enable destroyer")
	private boolean enableDestroyer;
	// receives other command line parameters than options
	@Argument
	private List<String> arguments = new ArrayList<String>();

	private TPCWProperties tpcw = null;
	private int numOfRunningThreads = 0;
	private boolean endOfSimulation = false;
	private Monitor monitor;
	private Controller controller;
	private Executor executor;
	private Destroyer destroyer;
	private LoadBalancer loadBalancer;
	OpenSystemTicketProducer producer;
	EventQueue eventQueue = null;
	private long startTime;
	private int currNumSessionsStats = 0;

	private static transient final Logger LOG = LoggerFactory
			.getLogger(ClientEmulator.class);

	public ClientEmulator() throws IOException, InterruptedException {
		super();
		tpcw = new TPCWProperties("tpcw");
		eventQueue = new EventQueue();
	}

	public synchronized void increaseThread() {
		numOfRunningThreads++;
	}

	private synchronized void setEndOfSimulation() {
		endOfSimulation = true;
		LOG.info("Trigger ClientEmulator.isEndOfSimulation()= "
				+ this.isEndOfSimulation());

	}

	public synchronized boolean isEndOfSimulation() {
		return endOfSimulation;
	}

	public void start(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);
		try {
			// parse the arguments.
			parser.parseArgument(args);
			// you can parse additional arguments if you want.
			// parser.parseArgument("more","args");
		} catch (CmdLineException e) {
			// if there's a problem in the command line,
			// you'll get this exception. this will report
			// an error message.
			System.err.println(e.getMessage());
			System.err.println("java ClientEmulator [-c -d]");
			// print the list of available options
			parser.printUsage(System.err);
			System.err.println();
			return;
		}

		if (enableController)
			LOG.info("-c flag is set");

		if (enableDestroyer)
			LOG.info("-d flag is set");

		long warmup = tpcw.warmup;
		long mi = tpcw.mi;
		long warmdown = tpcw.warmdown;
		this.startTime = System.currentTimeMillis();
		int maxNumSessions = 0;
		int workloads[] = tpcw.workloads;
		for (int i = 0; i < workloads.length; i++) {
			if (workloads[i] > maxNumSessions) {
				maxNumSessions = workloads[i];
			}
		}
		LOG.info("The maximum is : " + maxNumSessions);
		BlockingQueue<Integer> bQueue = new LinkedBlockingQueue<Integer>();

		// Each usersession is a user
		//不管怎样，先把所有的session都开了，遇到用不上max的时候就先block掉，关掉。
		UserSession[] sessions = new UserSession[maxNumSessions];
		for (int i = 0; i < maxNumSessions; i++) {
			sessions[i] = new UserSession(i, this, bQueue);
			sessions[i].holdThread();
			sessions[i].start();
		}

		int currNumSessions = 0;
		int currWLInx = 0;
		int diffWL = 0;

		long endTime = startTime + warmup + mi + warmdown;
		long currTime;

		// producer is for semi-open and open models
		// it shares a bQueue with all the usersessions.
		if (tpcw.mixRate > 0) {
			producer = new OpenSystemTicketProducer(this, bQueue);
			producer.start();
		}
		
		this.monitor = new Monitor(this);
		this.monitor.init();
		Timer timer = null;
		if (enableController) {
			this.controller = new Controller(this);
			timer = new Timer();
			timer.schedule(this.controller, warmup, tpcw.interval);
			this.executor = new Executor(this);
			this.executor.start();
			if(enableDestroyer){
				destroyer = new Destroyer(this);
				destroyer.start();
			}
		}
		this.loadBalancer = new LoadBalancer(this);
		LOG.info("Client starts......");
		while (true) {
			currTime = System.currentTimeMillis();
			if (currTime >= endTime) {
				// when it reaches endTime, it ends.
				break;
			}
			diffWL = workloads[currWLInx] - currNumSessions;
			LOG.info("Workload......" + workloads[currWLInx]);
			if (diffWL > 0) {
				for (int i = currNumSessions; i < (currNumSessions + diffWL); i++) {
					sessions[i].releaseThread();
					sessions[i].notifyThread();
				}
			} else if (diffWL < 0) {
				for (int i = (currNumSessions - 1); i > workloads[currWLInx]; i--) {
					sessions[i].holdThread();
				}
			}
			try {
				LOG.info("Client emulator sleep......" + tpcw.interval);
				Thread.sleep(tpcw.interval);
			} catch (InterruptedException ie) {
				LOG.error("ERROR:InterruptedException" + ie.toString());
			}
			currNumSessions = workloads[currWLInx];
			this.currNumSessionsStats = currNumSessions;
			currWLInx = ((currWLInx + 1) % workloads.length);
		}
		setEndOfSimulation();
		for (int i = 0; i < maxNumSessions; i++) {
			sessions[i].releaseThread();
			sessions[i].notifyThread();
		}
		LOG.info("Client: Shutting down threads ...");
		for (int i = 0; i < maxNumSessions; i++) {
			try {
				LOG.info("UserSession " + i + " joins.");
				sessions[i].join();
			} catch (java.lang.InterruptedException ie) {
				LOG.error("ClientEmulator: Thread " + i
						+ " has been interrupted.");
			}
		}
		if (tpcw.mixRate > 0) {
			try {
				producer.join();
				LOG.info("Producer joins");
			} catch (java.lang.InterruptedException ie) {
				LOG.error("Producer has been interrupted.");
			}
		}
		if (enableController) {
			timer.cancel();
			this.eventQueue.put(ActionType.NoOp);
			try {
				executor.join();
				LOG.info("Executor joins");
				if(enableDestroyer){
					destroyer.join();
					LOG.info("Destroyer joins");
				}
			} catch (java.lang.InterruptedException ie) {
				LOG.error("Executor/Destroyer has been interrupted.");
			}
		}
		this.monitor.close();
		LOG.info("Done\n");
		Runtime.getRuntime().exit(0);
	}
   
	public int getcurrNumSessionsStats() {
		return this.currNumSessionsStats;
	}
	
	public Monitor getMonitor() {
		return monitor;
	}

	public void setMonitor(Monitor monitor) {
		this.monitor = monitor;
	}

	public Controller getController() {
		return controller;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public TPCWProperties getTpcw() {
		return tpcw;
	}

	public void setTpcw(TPCWProperties tpcw) {
		this.tpcw = tpcw;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public LoadBalancer getLoadBalancer() {
		return loadBalancer;
	}

	public void setLoadBalancer(LoadBalancer loadBalancer) {
		this.loadBalancer = loadBalancer;
	}

	public EventQueue getEventQueue() {
		return eventQueue;
	}

	public void setEventQueue(EventQueue eventQueue) {
		this.eventQueue = eventQueue;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		ClientEmulator client = new ClientEmulator();
		client.start(args);
	}

}
