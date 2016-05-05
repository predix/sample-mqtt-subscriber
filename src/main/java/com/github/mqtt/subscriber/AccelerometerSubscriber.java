package com.github.mqtt.subscriber;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.mqtt.model.MQTTBrokerConfig;

public class AccelerometerSubscriber implements MqttCallback {
	
	private String connectionString;

	private String topic;

	private Queue<String> queue = new LinkedList<>();

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(AccelerometerSubscriber.class);

	private MqttClient mqttClient;
	
	private MessageListener listener;

	public AccelerometerSubscriber() {
		this.setTopic("accelerometer");
	}

	public void connect(MQTTBrokerConfig config) throws MqttException {
		this.connectionString = config.getConnectionString();
		mqttClient = new MqttClient(this.connectionString, "client1");
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		LOGGER.info("Connecting to broker: {}", this.connectionString);
		mqttClient.connect(connOpts);
		LOGGER.info("Connected to {}", this.connectionString);
		mqttClient.setCallback(this);
		mqttClient.subscribe(getTopic(), 0);
		LOGGER.info("Subscribed to {}", getTopic());
	}
	
	public void setMessageListener(MessageListener listener) {
		this.listener = listener;
	}

	@Override
	public void connectionLost(Throwable cause) {
		LOGGER.error("Lost connection", cause);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		synchronized (this.queue) {
			LOGGER.info("Received message: {}", message.toString());
			if (this.queue.size() >= 100) {
				this.queue.remove();
			}
			this.queue.add(message.toString());
			if (this.listener != null) {
				this.listener.messageReceived(message.toString());
			}
		}
	}

	public List<String> messages() {
		return new ArrayList<String>(this.queue);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		LOGGER.debug("delivery completed");
	}

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public void disconnect() {
		if (this.mqttClient != null) {
			try {
				this.mqttClient.disconnect();
				this.mqttClient.close();
			} catch (MqttException e) {
				LOGGER.error("Error when closing connection", e);
			}
		}
	}

}
