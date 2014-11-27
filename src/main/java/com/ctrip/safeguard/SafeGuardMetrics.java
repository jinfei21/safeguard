package com.ctrip.safeguard;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ctrip.safeguard.intf.HealthCounts;
import com.ctrip.safeguard.intf.IMetrics;
import com.ctrip.safeguard.intf.ISafeGuardConfig;
import com.ctrip.safeguard.intf.Status;
import com.ctrip.safeguard.util.CounterBuffer;
import com.ctrip.safeguard.util.LongPercentBuffer;

public class SafeGuardMetrics implements IMetrics {
	
	private static ISafeGuardConfig safeGuardConfig;
	
	
	private String group;
	private String key;
	
	private LongPercentBuffer executeLatencyBuffer;
	private LongPercentBuffer totalExecuteLatencyBuffer;
	private CounterBuffer<Status> executeStatBuffer;
	
    protected long lastGetExecuteDistributeSnapshotTime;
    protected Map<Status, Integer> executeDistributeSnapshot;
    
    protected long lastGetLatencyBufferSnapshotTime;
    protected List<Long> latencyBufferSnapshot;

    protected long lastGetTotalLatencyBufferSnapshotTime;
    protected List<Long> totalLatencyBufferSnapshot;
	

	
	public SafeGuardMetrics(ISafeGuardConfig safeGuardConfig){
		this.safeGuardConfig = safeGuardConfig;
		reset();
	}

	
	public void markExecuteEvent(Status status) {
		executeStatBuffer.increaseCount(status);
	}

	public void markExecuteLatency(long value) {
		executeLatencyBuffer.add(value);
	}

	public void markTotalExecuteLatency(long value) {
		totalExecuteLatencyBuffer.add(value);
	}

	public Map<Status, Integer> getExecuteEventDistribution() {
		UpdateExecuteDistributeSnapshot();
		return executeDistributeSnapshot;
	}

	public long getExecuteLatencyPercent(double percent) {
		UpdateLatencyBufferSnapshot();
		return executeLatencyBuffer.getPercent(latencyBufferSnapshot, percent);
	}

	public long getTotalExecuteLatencyPercent(double percent) {
		UpdateTotalLatencyBufferSnapshot();
		return totalExecuteLatencyBuffer.getPercent(totalLatencyBufferSnapshot, percent);
	}
	
    private void UpdateExecuteDistributeSnapshot(){
    	long currentTime = System.currentTimeMillis();
    	if(lastGetExecuteDistributeSnapshotTime==0||
    	   lastGetExecuteDistributeSnapshotTime+safeGuardConfig.metricsHealthSnapshotInterval()<=currentTime){
    		Map<Status, Integer> _executeDistributeSnapshot = new ConcurrentHashMap<Status, Integer>();
    		for(Status status:Status.values()){
    			executeDistributeSnapshot.put(status, executeStatBuffer.getCount(status));
    		}
    		executeDistributeSnapshot = _executeDistributeSnapshot;
    		lastGetExecuteDistributeSnapshotTime = currentTime;
    	}
    }
    
    private void UpdateLatencyBufferSnapshot(){
    	long currentTime = System.currentTimeMillis();
    	if(lastGetLatencyBufferSnapshotTime==0||
    	  lastGetLatencyBufferSnapshotTime+safeGuardConfig.metricsHealthSnapshotInterval()<=currentTime){
    		List<Long> _latencyBufferSnapshot= executeLatencyBuffer.getSnapShot();
    		Collections.sort(_latencyBufferSnapshot);
    		
    		latencyBufferSnapshot = _latencyBufferSnapshot;
    		lastGetLatencyBufferSnapshotTime = currentTime;
    	}
    }

	private void UpdateTotalLatencyBufferSnapshot() {
		long currentTime = System.currentTimeMillis();
		if (lastGetTotalLatencyBufferSnapshotTime == 0|| 
			lastGetTotalLatencyBufferSnapshotTime + safeGuardConfig.metricsHealthSnapshotInterval() <= currentTime) {
			List<Long> _totalLatencyBufferSnapshot = totalExecuteLatencyBuffer.getSnapShot();
			Collections.sort(_totalLatencyBufferSnapshot);

			totalLatencyBufferSnapshot = _totalLatencyBufferSnapshot;
			lastGetTotalLatencyBufferSnapshotTime = currentTime;
		}
	}

	public void reset() {
		executeStatBuffer = new CounterBuffer<Status>(safeGuardConfig.statWindowTime(), safeGuardConfig.statWindowBuckets());
		executeLatencyBuffer = new LongPercentBuffer(safeGuardConfig.percentWindowTime(), safeGuardConfig.statWindowBuckets(), safeGuardConfig.percentBucketSize());
		totalExecuteLatencyBuffer = new LongPercentBuffer(safeGuardConfig.percentWindowTime(), safeGuardConfig.statWindowBuckets(), safeGuardConfig.percentBucketSize());
	
	    lastGetExecuteDistributeSnapshotTime = 0;
	    executeDistributeSnapshot = null;
	    
	    lastGetLatencyBufferSnapshotTime = 0;
	    latencyBufferSnapshot = null;

	    lastGetTotalLatencyBufferSnapshotTime = 0;
	    totalLatencyBufferSnapshot = null;
	}

	public HealthCounts getHealthSnapshot() {
		
		
		long success = executeDistributeSnapshot.get(Status.Success);
		long fail = executeDistributeSnapshot.get(Status.Fail);
		long timeout = executeDistributeSnapshot.get(Status.Timeout);
		
		long error = fail + timeout ;
		long total = success + error;
		long errorPercentage = 0;
        if (total > 0) {
            errorPercentage = (int) ((double) error / total * 100);
        }
		return new HealthCounts( total,  error,  errorPercentage);
	}
	
	

}
