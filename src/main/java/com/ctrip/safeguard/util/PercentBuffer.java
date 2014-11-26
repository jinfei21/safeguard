package com.ctrip.safeguard.util;

import java.util.ArrayList;
import java.util.List;

public class PercentBuffer<T> extends CircularBuffer<T> {

	protected int bucketCapacity;
	
	public PercentBuffer(long timeWindow, int bucketCount,int bucketCapacity) {
		super(timeWindow, bucketCount);

		this.bucketCapacity = bucketCapacity;
	}

	@Override
	protected Bucket creatEmptyBucket(long time) {
	
		return new PercentBucket<T>(time, bucketCapacity);
	}
	
	
	public void add(T data){
		Bucket bucket = getCurrentBucket();
		if(bucket != null){
			((PercentBucket<T>) bucket).add(data);
		}
	}
	
	public List<T> getSnapShot(){
		List<T> list = new ArrayList<T>();
		long currrentBucketStartTime = getCurrentBucketStartTime();
		
		for(int i=0;i<buckets.length;i++){
			Bucket bucket = buckets[i];
			if(bucket.getTime() <= currrentBucketStartTime && bucket.getTime()+timeWindow>currrentBucketStartTime){
				PercentBucket<T> pBucket = (PercentBucket<T>)bucket;
				int count = pBucket.count();
				for(int j=0;j<count;j++){
					list.add(pBucket.get(i));
				}
			}
		}
		
		return list;
	}
	
	
}
