package com.ctrip.safeguard;

import com.ctrip.safeguard.intf.ISafeGuardConfig;

public class SafeGuardConfig implements ISafeGuardConfig{
	
	private boolean safeGuardEnabled = false;
	private int safeGuardRequestCountThreshold=99;
	private int safeGuardSleepWindow=4999;
	private int safeGuardErrorThresholdPercent = 99;
	private boolean safeGuardForceOpen=true;
	private boolean safeGuardForceClose=false;
	private int commandTimeout=4999;
	private int commandMaxConcurrentCount=99;
	private int metricsHealthSnapshotInterval=10;
	private int statWindowBuckets = 20;
	private int statWindowTime = 200*1000;
	private int percentWindowBuckets = 20;
	private int percentWindowTime = 200*1000;
	private int percentBucketSize = 50;
	
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


	public int statWindowBuckets() {
		return statWindowBuckets;
	}

	public int statWindowTime() {
		return statWindowTime;
	}

	public int percentWindowBuckets() {
		return percentWindowBuckets;
	}

	public int percentWindowTime() {
		return percentWindowTime;
	}

	public int percentBucketSize() {
		return percentBucketSize;
	}
}
