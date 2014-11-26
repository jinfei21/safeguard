package com.ctrip.safeguard;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.ctrip.safeguard.intf.HealthCounts;
import com.ctrip.safeguard.intf.IMetrics;
import com.ctrip.safeguard.intf.ISafeGuard;
import com.ctrip.safeguard.intf.ISafeGuardConfig;

public class SafeGuardControl implements ISafeGuard{
	
	private ISafeGuardConfig safeGuardConfig;
	private IMetrics metrics;
	private AtomicBoolean openFlag;
	
	private AtomicLong controlOpenedOrLastTestedTime;
	
	public SafeGuardControl(ISafeGuardConfig safeGuardConfig,IMetrics metrics){
		this.safeGuardConfig = safeGuardConfig;
		this.metrics = metrics;
		this.openFlag = new AtomicBoolean();
		this.controlOpenedOrLastTestedTime = new AtomicLong();
	}

	public boolean allowRequest() {

		if(!safeGuardConfig.safeGuardEnabled()){
			return true;
		}
		
		if(safeGuardConfig.safeGuardForceOpen()){
			return false;
		}
		
		if(safeGuardConfig.safeGuardForceClose()){
			isopen();
			return true;
		}
		return isopen()||allowSingleTest();
	}

	public boolean isopen() {

		if(!safeGuardConfig.safeGuardEnabled()){
			return false;
		}
		
		if(safeGuardConfig.safeGuardForceOpen()){
			return true;
		}
		
		if(openFlag.get()){
			return true;
		}
		
		HealthCounts health = metrics.getHealthSnapshot();
		
		if(health.getTotalRequests() < safeGuardConfig.safeGuardRequestCountThreshold()){
			return false;
		}
		
		if(health.getErrorPercentage() < safeGuardConfig.safeGuardErrorThresholdPercent()){
			return false;
		}
		
		if(openFlag.compareAndSet(false, true)){
			//TODO log
		}
		
		return true;
	}

	public void markSuccess() {
		if(!safeGuardConfig.safeGuardEnabled()){
			return;
		}
		

		if(openFlag.get()){
			openFlag.set(false);
			metrics.reset();
		}
	}
	
	//Gets whether the circuit breaker should permit a single test request.
	private boolean allowSingleTest(){
		long timeControlOpenedOrLastTested = controlOpenedOrLastTestedTime.get();
        // 1) if the circuit is open
        // 2) and it's been longer than 'sleepWindow' since we opened the circuit
		if(openFlag.get()&&System.currentTimeMillis()>timeControlOpenedOrLastTested + safeGuardConfig.safeGuardSleepWindow()){
            // We push the 'circuitOpenedTime' ahead by 'sleepWindow' since we have allowed one request to try.
            // If it succeeds the circuit will be closed, otherwise another singleTest will be allowed at the end of the 'sleepWindow'.
			if(controlOpenedOrLastTestedTime.compareAndSet(timeControlOpenedOrLastTested, System.currentTimeMillis())){
				
                // if this returns true that means we set the time so we'll return true to allow the singleTest
                // if it returned false it means another thread raced us and allowed the singleTest before we did
				return true;
			}
		}
		return false;
	}

}
