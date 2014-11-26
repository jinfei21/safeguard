package com.ctrip.safeguard.util;

public abstract class Bucket<T> {
	private long time;
	
	public Bucket(long time){
		this.time = time;
	}
	
	public Bucket(){
		this(0);
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
}
