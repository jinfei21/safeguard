package com.ctrip.safeguard.intf;

public class HealthCounts {
	private final long totalCount;
	private final long errorCount;
	private final long errorPercentage;

	public HealthCounts(long total, long error, long errorPercentage) {
		this.totalCount = total;
		this.errorCount = error;
		this.errorPercentage = errorPercentage;
	}

	public long getTotalRequests() {
		return totalCount;
	}

	public long getErrorCount() {
		return errorCount;
	}

	public long getErrorPercentage() {
		return errorPercentage;
	}
}
