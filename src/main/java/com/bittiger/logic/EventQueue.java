package com.bittiger.logic;

import java.util.LinkedList;
import java.util.Queue;

public class EventQueue {
	Queue<ActionType> queue = new LinkedList<ActionType>();

	public synchronized ActionType peek() {
		while (queue.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		return queue.peek();
	}

	public synchronized void get() {
		queue.poll();
	}

	public synchronized void put(ActionType actionType) {
		switch (actionType) {
		case AvailNotEnoughAddServer:
			if (!queue.contains(ActionType.AvailNotEnoughAddServer)
					&& !queue.contains(ActionType.BadPerformanceAddServer)) {
				queue.offer(actionType);
			}
			break;
		case BadPerformanceAddServer:
		case GoodPerformanceRemoveServer:
			// we ignore the performance request if there is anything going on
			// in the queue
			if (queue.isEmpty()) {
				queue.offer(actionType);
			}
			break;
		case NoOp:
			queue.offer(actionType);
			break;
		default:
			break;
		}
		notifyAll();
	}
}
