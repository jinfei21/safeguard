package com.ctrip.safeguard;

import java.util.concurrent.atomic.AtomicInteger;

import com.ctrip.safeguard.intf.ISemaphore;

public class IsolationSemaphore implements ISemaphore{

	private int count;
	private AtomicInteger usedCount;
	
	public IsolationSemaphore(int count){
		this.count = count;
		this.usedCount = new AtomicInteger();
	}
	
	public int currentCount() {
		return count - usedCount.get();
	}
	
	public boolean tryAcquire() {
		if(usedCount.incrementAndGet()>count){
			usedCount.decrementAndGet();
			return false;
		}
		return true;
	}
	
	public void release() {
		usedCount.decrementAndGet();
	}
}
