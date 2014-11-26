package com.ctrip.safeguard.util;

import java.util.List;

public class LongPercentBuffer extends PercentBuffer<Long>{

	public LongPercentBuffer(long timeWindow, int bucketCount,int bucketCapacity) {
		super(timeWindow, bucketCount, bucketCapacity);
		
	}
	
	public long getPercent(double percent){
		List<Long> snapshot = getSnapShot();
		if(snapshot.size() == 0){
			return 0;
		}
		
		if(percent <= 0){
			return snapshot.get(0);
		}
		
		if(percent >= 100){
			return snapshot.get(snapshot.size() - 1);
		}
		
		int rank = (int)(percent*(snapshot.size()-1)/100);
		return snapshot.get(rank);
	}

}
