package com.orangeman.example.licensingservice.clients.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.orangeman.example.licensingservice.events.CustomerChannels;
import com.orangeman.example.licensingservice.events.models.OrganizationChangeModel;
import com.orangeman.example.licensingservice.repository.OrganizationRedisRepository;

@EnableBinding(CustomerChannels.class)
public class OrganizationChangeHandler {

	@Autowired
	private OrganizationRedisRepository organizationRedisRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(OrganizationChangeHandler.class);
	
	@StreamListener("inboundOrgChanges")
	public void loggerSink(OrganizationChangeModel orgChange) {
		switch (orgChange.getAction()) {
		case "UPDATE":
			logger.debug("Received a UPDATE event from the organization service for ➥ organization id {}", orgChange.getOrganizationId());
		    break;
		case "DELETE":
			logger.debug("Received a DELETE event from the organization service for organization id {}", orgChange.getOrganizationId());
			organizationRedisRepository.deleteOrganization(orgChange.getOrganizationId()); 
			break;
		default:
			logger.error("Received an UNKNOWN event from the organization service of type {}", orgChange.getType());	
			break;
		}
	}
	
}
