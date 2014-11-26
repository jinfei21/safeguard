package com.ctrip.safeguard.util;

import java.util.concurrent.locks.ReentrantLock;

public abstract class CircularBuffer<T> extends Bucket<T> {

	protected final long timeWindow;
	protected final long oneBucketTime;
	protected final int bucketCount;
	protected final int legacyBucketCount = 1;
	protected final Bucket<T>[] buckets;
	protected final ReentrantLock lock = new ReentrantLock();
	protected int endIndex = 0;
	public CircularBuffer(long timeWindow,int bucketCount) {
		super(timeWindow/bucketCount);
		this.bucketCount = bucketCount;
		this.timeWindow = timeWindow;
		this.oneBucketTime = timeWindow/bucketCount;
		
		this.buckets = new Bucket[bucketCount + legacyBucketCount];
		
		for(int i=0;i<buckets.length;i++){
			buckets[i] = creatEmptyBucket(0);
		}
	}
	
	protected long getCurrentBucketStartTime(){
		long currentTime = System.currentTimeMillis();
		return currentTime - currentTime%oneBucketTime;
	}
	
	
	protected Bucket getCurrentBucket(){
		long currentBucketStartTime = getCurrentBucketStartTime();
		if(buckets[endIndex].getTime() + oneBucketTime <= currentBucketStartTime){
			try{
				lock.lock();
				
				if(buckets[endIndex].getTime() + oneBucketTime <= currentBucketStartTime){
					int newEndIndex = (endIndex + 1)%buckets.length;
					buckets[newEndIndex] = creatEmptyBucket(currentBucketStartTime);
					endIndex = newEndIndex;
				}
			}finally{
				lock.unlock();
			}
		}
		return buckets[endIndex];
	}
	protected abstract Bucket creatEmptyBucket(long time);
}
