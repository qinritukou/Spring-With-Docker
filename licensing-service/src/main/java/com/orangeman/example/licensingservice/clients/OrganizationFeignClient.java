package com.orangeman.example.licensingservice.clients;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.orangeman.example.licensingservice.model.Organization;

@FeignClient(name = "organizationservice")
@RibbonClient(name = "organizationservice")
public interface OrganizationFeignClient {

	@GetMapping(
			value="/v1/organizations/{organizationId}", 
			consumes = "application/json")
	Organization getOrganization(
			@PathVariable("organizationId") String organizationId);
	
}