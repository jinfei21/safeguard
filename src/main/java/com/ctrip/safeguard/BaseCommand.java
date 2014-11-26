package com.ctrip.safeguard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import com.ctrip.safeguard.intf.ICommand;
import com.ctrip.safeguard.intf.IMetrics;
import com.ctrip.safeguard.intf.ISafeGuard;
import com.ctrip.safeguard.intf.ISafeGuardConfig;
import com.ctrip.safeguard.intf.ISemaphore;
import com.ctrip.safeguard.intf.Status;

public abstract class BaseCommand<T> implements ICommand{
	

	private final static Map<String,ISemaphore> executeSemaphores = new ConcurrentHashMap<String, ISemaphore>();
	private final static Map<String,IMetrics> metricsMap = new ConcurrentHashMap<String, IMetrics>();
	private final static Map<String,ISafeGuard> safeguardsMap = new ConcurrentHashMap<String, ISafeGuard>();
	
	private final AtomicInteger commandCount = new AtomicInteger();
	protected final static ReentrantLock lock = new ReentrantLock();
	protected ISafeGuard safeGuard = null;
	protected IMetrics metrics = null;
	protected ISafeGuardConfig config;
	private String key;
	private String group;
	private Status status;	
	
	public BaseCommand(String key,String group,Status status,ISafeGuardConfig config){
		this.key = key;
		this.group = group;
		this.status = status;
		initSafeGuard(config); 
		this.commandCount.incrementAndGet();
		this.config = config;
	}
	
	private void initSafeGuard(ISafeGuardConfig config){
		if(safeGuard==null){
			try{
				lock.lock();
				if(safeGuard==null){
					metrics = new SafeGuardMetrics(config);
					safeGuard = new SafeGuardControl(config,metrics); 
					this.metricsMap.put(key, metrics);
					this.safeguardsMap.put(key, safeGuard);
				}
			}finally{
				lock.unlock();
			}
		}
	}
	
	public ISemaphore getOrAddExecuteSemaphores(String key,ISemaphore isolate){
		return getSemaphores(executeSemaphores, key, isolate);
	}
	
	private ISemaphore getSemaphores(Map<String,ISemaphore> semaphores,String key,ISemaphore isolate){
		ISemaphore instance = semaphores.get(key);
		try{
			lock.lock();
			instance = semaphores.get(key);
			if(instance==null){
				instance = isolate;
				semaphores.put(key, instance);
			}
		}finally{
			lock.unlock();
		}
		return instance;
	}
	
	public String key() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String group() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public Status status() {
		return status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
}
