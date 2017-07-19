package com.bittiger.client;

import java.util.concurrent.BlockingQueue;
import java.util.Random;

public class OpenSystemTicketProducer extends Thread {
	private TPCWProperties tpcw = null;
	public long startTime;
	public long warmup;
	public long mi;
	public long warmdown;
	private final BlockingQueue<Integer> queue;
	private Random rand;
	private double[] rate;

	public OpenSystemTicketProducer(ClientEmulator ce, BlockingQueue<Integer> bQueue) {
		this.tpcw = ce.getTpcw();
		this.warmup = tpcw.warmup;
		this.mi = tpcw.mi;
		this.warmdown = tpcw.warmdown;
		this.queue = bQueue;
		this.rand = new Random();
		this.rate = tpcw.rate;
	}

	public void run() {
		int num = 0;
		while (true) {
			long currTime = System.currentTimeMillis();
			if (currTime - startTime < (warmup + mi + warmdown)) {
				double r = rand.nextDouble();
				int rlIndex = (int) (Math.floor((double) (currTime - startTime)
						/ tpcw.interval));
				double sendrate = 0 - rate[rlIndex];
				long wait = ((long) (sendrate * Math.log(r)));
				try {
					Thread.sleep(wait);
					queue.put(num++);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				// put additional tickets to make system end
				for (int j = 0; j < 1000000; j++) {
					try {
						queue.put(num++);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			}
		}
	}

}
