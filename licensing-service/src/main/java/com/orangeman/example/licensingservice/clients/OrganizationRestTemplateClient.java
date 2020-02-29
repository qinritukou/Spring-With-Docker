package com.orangeman.example.licensingservice.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.orangeman.example.licensingservice.model.Organization;
import com.orangeman.example.licensingservice.repository.OrganizationRedisRepository;
import com.orangeman.example.licensingservice.utils.UserContextHolder;

@Component
public class OrganizationRestTemplateClient {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	OrganizationRedisRepository organizationRedisRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);
	
	private Organization checkRedisCache(String organizationId) {
		try {
			return organizationRedisRepository.findOrganization(organizationId);			
		} catch (Exception ex) {
			// TODO: handle exception
			logger.info("Error encountered while trying to retrieve organization {} check Redis Cache. Exception {}", organizationId, ex);
			return null;
		}
	}
	
	private void cacheOrganizationObject(Organization organization) {
		try {
			organizationRedisRepository.saveOrganization(organization);			
		} catch (Exception ex) {
			// TODO: handle exception
			logger.info("Unable to cache organization {} in Redis. Exception {}", organization.getOrganizationId(), ex);
		}
	}
	
	
	public Organization getOrganziation(String organizationId) {
		logger.info("In Licensing Service.getOrganization: {}",
				UserContextHolder.getContext().getCorrelationId());
		
		Organization organization = checkRedisCache(organizationId);
		
		if (organization != null) {
			logger.info("I have successfully retrieve an organization {} from the redis cache: {}", organizationId, organization);
			return organization;
		}
		
		logger.info("Unable to locate an organization from the redis cache: {}", organizationId);
		
		
		ResponseEntity<Organization> restExchange = restTemplate.exchange(
				"http://localhost:8081/v1/organizations/{organizationId}", 
				HttpMethod.GET,
				null, Organization.class, organizationId);
		
		organization = restExchange.getBody();
		if (organization != null) {
			cacheOrganizationObject(organization);
		}
		
		return organization;
	}
}
