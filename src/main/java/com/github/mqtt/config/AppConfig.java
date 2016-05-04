package com.github.mqtt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.mqtt.subscriber.AccelerometerSubscriber;

@Configuration
public class AppConfig {
	
	@Bean
	public AccelerometerSubscriber subscriber() {
		return new AccelerometerSubscriber();
	}

}
