package com.ctrip.safeguard.util;

public class CounterBuffer<T> extends CircularBuffer{
	
	

	public CounterBuffer(long timeWindow, int bucketCount) {
		super(timeWindow, bucketCount);
		
	}

	@Override
	protected Bucket creatEmptyBucket(long time) {
	
		return new CounterBucket<T>(time);
	}

	public int getCount(T t){
		long currentBufferStartTime = getCurrentBucketStartTime();
		int count = 0;
		
		for(int i=0;i<buckets.length;i++){
			Bucket bucket = buckets[i];
			if(currentBufferStartTime - bucket.getTime() < timeWindow){
				count += ((CounterBucket<T>) bucket).get(t);
			}
		}
		
		return count;
	}
	
	public void increaseCount(T t){
		Bucket bucket = getCurrentBucket();
		if(bucket != null){
			((CounterBucket<T>) bucket).increaseCount(t);
		}
	}
}
