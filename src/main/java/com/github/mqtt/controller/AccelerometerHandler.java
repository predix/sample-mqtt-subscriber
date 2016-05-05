package com.github.mqtt.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.github.mqtt.subscriber.AccelerometerSubscriber;
import com.github.mqtt.subscriber.MessageListener;

public class AccelerometerHandler extends TextWebSocketHandler implements MessageListener {
	
	@Autowired
	private AccelerometerSubscriber sub;

	private WebSocketSession session;

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(AccelerometerHandler.class);

	public AccelerometerHandler() {
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		this.session = session;
		this.sub.setMessageListener(this);
	}

	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		LOGGER.debug("Recevied message {}", message.getPayload());
	}

	@Override
	public void messageReceived(String message) {
		try {
			LOGGER.info("Send new message to client {}", message);
			this.session.sendMessage(new TextMessage(message));
		} catch (IOException e) {
			LOGGER.error("Error when sending message", e);
		}
	}

}
