package com.ctrip.safeguard;

import com.ctrip.safeguard.intf.ISafeGuardConfig;

public class CommandSetting {

	private static ISafeGuardConfig safeGuardConfig=null;
	private String key;
	private String group;
	private int semaphoreCount;
	private boolean hasFallback;
	
	public CommandSetting(){
		
	}

	public String getKey() {
		return key;
	}

	public CommandSetting setKey(String key) {
		this.key = key;
		return this;
	}

	public String getGroup() {
		return group;
	}

	public static ISafeGuardConfig getSafeGuardConfig(){
		if(safeGuardConfig==null){
			return new SafeGuardConfig();
		}
		return safeGuardConfig;
	}
	
	public CommandSetting setGroup(String group) {
		this.group = group;
		return this;
	}

	public int getSemaphoreCount() {
		return semaphoreCount;
	}

	public CommandSetting setSemaphoreCount(int semaphoreCount) {
		this.semaphoreCount = semaphoreCount;
		return this;
	}

	public boolean isHasFallback() {
		return hasFallback;
	}

	public CommandSetting setHasFallback(boolean hasFallback) {
		this.hasFallback = hasFallback;
		return this;
	}

}
