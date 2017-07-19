package com.bittiger.logic.rules;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;

import com.bittiger.client.ClientEmulator;
import com.bittiger.client.Utilities;
import com.bittiger.logic.ActionType;
import com.bittiger.logic.Controller;

@Rule(name = "AvailabilityRule", description = "Guarrantee the minimum number of slaves")
public class AvailabilityNotEnoughRule {

	private ClientEmulator c;
	private int readQueueSize;

	@Condition
	public boolean checkNumSlave() {
		// The rule should be applied only if
		// the number of slaves is less than minimum.
		return readQueueSize < Utilities.minimumSlave;
	}

	@Action
	public void addServer() throws Exception {
		c.getEventQueue().put(ActionType.AvailNotEnoughAddServer);
	}

	public void setInput(ClientEmulator c, int readQueueSize) {
		this.c = c;
		this.readQueueSize = readQueueSize;
	}

}
