package com.github.mqtt.model;

import javax.validation.constraints.NotNull;

public class MQTTBrokerConfig {

	@NotNull
	private String hostName;

	@NotNull
	private Integer port;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getConnectionString() {
		return "tcp://" + this.hostName + ":" + this.port;
	}

	public String toString() {
		return this.hostName + ":" + this.port;
	}

}
