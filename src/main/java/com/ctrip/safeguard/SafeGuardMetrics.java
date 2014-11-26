package com.ctrip.safeguard;

import java.util.Map;

import com.ctrip.safeguard.intf.HealthCounts;
import com.ctrip.safeguard.intf.IMetrics;
import com.ctrip.safeguard.intf.ISafeGuardConfig;
import com.ctrip.safeguard.intf.Status;

public class SafeGuardMetrics implements IMetrics {
	
	private static ISafeGuardConfig safeGuardConfig;
	
	
	private String group;
	private String key;
	
	public SafeGuardMetrics(ISafeGuardConfig safeGuardConfig){
		this.safeGuardConfig = safeGuardConfig;
	}

	
	public void markExecuteEvent(Status status) {


		
	}

	public void markExecuteLatency(long value) {
		// TODO Auto-generated method stub
		
	}

	public void markTotalExecuteLatency(long value) {
		// TODO Auto-generated method stub
		
	}

	public Map<Status, Integer> getExecuteEventDistribution() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getExecuteLatencyPercent(double percent) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getTotalExecuteLatencyPercent(double percent) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public HealthCounts getHealthSnapshot() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
