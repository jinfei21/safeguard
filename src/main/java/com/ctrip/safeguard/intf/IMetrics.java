package com.ctrip.safeguard.intf;

import java.util.Map;

public interface IMetrics {
	void markExecuteEvent(Status status);
	void markExecuteLatency(long value);
	void markTotalExecuteLatency(long value);
	Map<Status,Integer> getExecuteEventDistribution();
	long getExecuteLatencyPercent(double percent);
	long getTotalExecuteLatencyPercent(double percent);
	void reset();
	HealthCounts getHealthSnapshot();
}
