package com.bittiger.logic;

public class Stats {
	public String type;
	public int sessionId;
	public long start;
	public long end;
	public long duration;

	public Stats(int sessionId, String type, long start, long end) {
		super();
		this.type = type;
		this.sessionId = sessionId;
		this.start = start;
		this.end = end;
		this.duration = end - start;
	}
	
	public String toString() {
		return sessionId + "," + type + "," + duration;
	}

}
