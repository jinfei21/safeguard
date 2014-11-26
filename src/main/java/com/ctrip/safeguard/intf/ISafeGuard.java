package com.ctrip.safeguard.intf;

public interface ISafeGuard {
	boolean allowRequest();
	boolean isopen();
	void markSuccess();
}
