package com.orangeman.example.licensingservice.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.orangeman.example.licensingservice.model.Organization;

@Component
public class OrganizationRestTemplateClient {

	@Autowired
	RestTemplate restTemplate;
	
	public Organization getOrganziation(String organizationId) {
		ResponseEntity<Organization> restExchange = restTemplate.exchange(
				"http://localhost:8081/v1/organizations/{organizationId}", 
				HttpMethod.GET,
				null, Organization.class, organizationId);
		
		return restExchange.getBody();
	}
}
