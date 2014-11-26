package com.ctrip.safeguard.util;

import java.util.concurrent.atomic.AtomicInteger;

public class PercentBucket<T> extends Bucket<T> {

	private Object[] data;
	private AtomicInteger count;
	
	public PercentBucket(long time,int count){
		super(time);
		data = new Object[count];
		this.count = new AtomicInteger();
	}
	
	public void add(T t){
		int index = (count.incrementAndGet() - 1)%data.length;
		data[index]=t;
	}
	
	public int count(){
		return Math.min(count.get(), data.length);
	}
	
	public T get(int index){
		return (T) data[index];
	}
}
