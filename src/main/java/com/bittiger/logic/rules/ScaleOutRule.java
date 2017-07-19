package com.bittiger.logic.rules;

import org.easyrules.annotation.Action;
import org.easyrules.annotation.Condition;
import org.easyrules.annotation.Rule;

import com.bittiger.client.ClientEmulator;
import com.bittiger.client.Utilities;
import com.bittiger.logic.ActionType;
import com.bittiger.logic.Controller;

@Rule(name = "ScaleOutRule", description = "Check if we need to add server for better performance")
public class ScaleOutRule {

	private ClientEmulator c;
	private String perf;

	@Condition
	public boolean checkPerformance() {
		String[] tokens = perf.split(",");
		String[] details = tokens[0].split(":");
		return !details[3].equals("NA")
				&& (Double.parseDouble(details[3]) > 400);
	}

	@Action
	public void addServer() throws Exception {
		c.getEventQueue().put(ActionType.BadPerformanceAddServer);
	}

	public void setInput(ClientEmulator c, String perf) {
		this.c = c;
		this.perf = perf;
	}

}
