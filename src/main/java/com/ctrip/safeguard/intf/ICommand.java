package com.ctrip.safeguard.intf;

public interface ICommand {
	String key();
	String group();
	Status status();
}
