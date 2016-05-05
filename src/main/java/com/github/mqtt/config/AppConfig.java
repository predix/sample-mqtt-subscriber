package com.github.mqtt.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.github.mqtt.controller.AccelerometerHandler;
import com.github.mqtt.subscriber.AccelerometerSubscriber;

@Configuration
@EnableWebSocket
@EnableAutoConfiguration
public class AppConfig  implements WebSocketConfigurer {
	
	@Bean
	public AccelerometerSubscriber subscriber() {
		return new AccelerometerSubscriber();
	}
	

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(handler(), "/accelerometer").withSockJS();
	}

	@Bean
	public WebSocketHandler handler() {
		return new AccelerometerHandler();
	}

}
