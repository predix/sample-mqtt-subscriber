package com.github.mqtt.model;

public class AccelerometerMessage {
	
	private String value;
	
	public AccelerometerMessage(String val) {
		this.value = val;
	}

	public String getValue() {
		return value;
	}

}
