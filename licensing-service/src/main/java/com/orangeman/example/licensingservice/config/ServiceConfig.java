package com.orangeman.example.licensingservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceConfig {

	@Value("${example.property}")
	private String exampleProperty;
	
	@Value("${redis.server}")
	private String redisHost;

	@Value("${redis.port}")
	private int redisPort;

	public String getExampleProperty() {
		return exampleProperty;
	}

	public void setExampleProperty(String exampleProperty) {
		this.exampleProperty = exampleProperty;
	}

	public String getRedisHost() {
		// TODO Auto-generated method stub
		return this.redisHost;
	}

	public int getRedisPort() {
		// TODO Auto-generated method stub
		return this.redisPort;
	}

	public void setRedisHost(String redisHost) {
		this.redisHost = redisHost;
	}

	public void setRedisPort(int redisPort) {
		this.redisPort = redisPort;
	}

	
}
