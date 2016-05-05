package com.github.mqtt.controller;

import javax.validation.Valid;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.mqtt.model.MQTTBrokerConfig;
import com.github.mqtt.subscriber.AccelerometerSubscriber;

@Controller
public class MainController {

	@Autowired
	private AccelerometerSubscriber sub;

	private boolean connected;

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MainController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(MQTTBrokerConfig brokerConfig, Model model) {
		LOGGER.info("Serving home page");
		if (this.connected) {
			LOGGER.info("Already connected");
			model.addAttribute("connectionStatus", getConnectionStatus());
			return "redirect:/showmessages";
		} else {
			model.addAttribute("connectionStatus", getConnectionStatus());
			model.addAttribute("brokerConfig", new MQTTBrokerConfig());
			return "home";
		}
	}

	private String getConnectionStatus() {
		String connectionString = "Not connected";
		if (this.connected) {
			connectionString = "Connected to " + this.sub.getConnectionString() + "/" + this.sub.getTopic();
		}
		LOGGER.debug("Connection status {}", connectionString);
		return connectionString;
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String connectToBroker(@Valid MQTTBrokerConfig brokerConfig, BindingResult bindingResult, Model model) {
		LOGGER.info("Connecting to broker");
		if (bindingResult.hasErrors()) {
			return "home";
		}
		try {
			this.sub.connect(brokerConfig);
			this.connected = true;
		} catch (MqttException e) {
			LOGGER.error("error when connecting to broker", e);
			this.connected = false;
		}
		model.addAttribute("connectionStatus", getConnectionStatus());
		return "redirect:/showmessages";
	}

	@RequestMapping(value = "/showmessages", method = RequestMethod.GET)
	public String showMessages(Model model) {
		LOGGER.info("Serving show messages page");
		model.addAttribute("connectionStatus", getConnectionStatus());
		model.addAttribute("messages", this.sub.messages());
		return "showmessages";
	}

	@RequestMapping(value = "/showmessages", method = RequestMethod.POST)
	public String disconnect(Model model) {
		LOGGER.info("Disconnecting from broker");
		this.sub.disconnect();
		this.connected = false;
		model.addAttribute("connectionStatus", getConnectionStatus());
		model.addAttribute("brokerConfig", new MQTTBrokerConfig());
		return "redirect:/";
	}

}
