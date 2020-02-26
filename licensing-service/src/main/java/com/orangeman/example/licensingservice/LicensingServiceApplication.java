package com.orangeman.example.licensingservice;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.orangeman.example.licensingservice.events.models.OrganizationChangeModel;
import com.orangeman.example.licensingservice.utils.UserContextInterceptor;

@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@EnableBinding(Sink.class)
public class LicensingServiceApplication {

	private Logger logger = LoggerFactory.getLogger(LicensingServiceApplication.class);
	
	@Bean
	public RestTemplate getRestTemplate() {
		RestTemplate template = new RestTemplate();
		List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
		
		if (interceptors == null) {
			template.setInterceptors(Collections.singletonList(new UserContextInterceptor()));
		} else {
			interceptors.add(new UserContextInterceptor());
			template.setInterceptors(interceptors);
		}
		
		return template;
	}
	
	@StreamListener(Sink.INPUT)
	public void loggerSink(OrganizationChangeModel orgChange) {
		logger.info("Received an event for organization id {}", orgChange.getOrganizationId());
	}

	public static void main(String[] args) {
		SpringApplication.run(LicensingServiceApplication.class, args);
	}

}
