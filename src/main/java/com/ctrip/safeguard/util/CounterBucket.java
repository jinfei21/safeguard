package com.ctrip.safeguard.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class CounterBucket<T> extends Bucket {

	protected Map<T,AtomicInteger> countMap;
	protected ReentrantLock lock = new ReentrantLock();
	
	public CounterBucket(long time){
		super(time);
		this.countMap = new ConcurrentHashMap<T, AtomicInteger>();
	}
	
	public int get(T t){
		AtomicInteger count = countMap.get(t);
		if(count != null){
			return count.get();
		}else{
			return 0;
		}
	}
	
	public void increaseCount(T t){
		AtomicInteger count = countMap.get(t);
		if(count == null){
			try{
				lock.lock();
				count = countMap.get(t);
				if(count==null){
					count = new AtomicInteger();
					countMap.put(t, count);
				}
			}finally{
				lock.unlock();
			}
		}
		count.incrementAndGet();
	}
}
