package com.ctrip.safeguard;

import com.ctrip.safeguard.intf.ISafeGuardConfig;

public class SafeGuardConfig implements ISafeGuardConfig{
	
	private boolean safeGuardEnabled;
	private int safeGuardRequestCountThreshold;
	private int safeGuardSleepWindow;
	private int safeGuardErrorThresholdPercent;
	private boolean safeGuardForceOpen;
	private boolean safeGuardForceClose;
	private int commandTimeout;
	private int commandMaxConcurrentCount;
	private int metricsHealthSnapshotInterval;
	
	public SafeGuardConfig(){
		
	}
	
	public boolean safeGuardEnabled() {

		return safeGuardEnabled;
	}

	public int safeGuardRequestCountThreshold() {
		
		return safeGuardRequestCountThreshold;
	}

	public int safeGuardSleepWindow() {
		
		return safeGuardSleepWindow;
	}

	public int safeGuardErrorThresholdPercent() {

		return safeGuardErrorThresholdPercent;
	}

	public boolean safeGuardForceOpen() {
		
		return safeGuardForceOpen;
	}

	public boolean safeGuardForceClose() {
		
		return safeGuardForceClose;
	}

	public int commandTimeout() {
		
		return commandTimeout;
	}

	public int commandMaxConcurrentCount() {
		
		return commandMaxConcurrentCount;
	}

	public int metricsHealthSnapshotInterval() {
		
		return metricsHealthSnapshotInterval;
	}

}
