package com.ctrip.safeguard.intf;

public interface ISemaphore {
	int currentCount();
	boolean tryAcquire();
	void release();
}
