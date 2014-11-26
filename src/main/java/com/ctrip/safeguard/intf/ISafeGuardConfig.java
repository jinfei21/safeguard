package com.ctrip.safeguard.intf;

public interface ISafeGuardConfig {

	boolean safeGuardEnabled();
	
	int safeGuardRequestCountThreshold();
	
	int safeGuardSleepWindow();
	
	int safeGuardErrorThresholdPercent();
	
	boolean safeGuardForceOpen();
	
	boolean safeGuardForceClose();
	
	int commandTimeout();
	
	int commandMaxConcurrentCount();
	
	int metricsHealthSnapshotInterval();
}
