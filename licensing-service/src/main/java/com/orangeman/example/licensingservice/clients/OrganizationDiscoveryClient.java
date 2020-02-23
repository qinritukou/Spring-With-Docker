package com.orangeman.example.licensingservice.clients;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.orangeman.example.licensingservice.model.Organization;

@Component
public class OrganizationDiscoveryClient {

//	@Autowired
//	private DiscoveryClient discoveryClient;
//	
	public Organization getOrganization(String organizationId) {
		RestTemplate restTemplate = new RestTemplate();
		List<InstanceInfo> instances = new ArrayList<InstanceInfo>(); // discoveryClient.getInstancesById("organizationservice");
		
		if (instances.size() == 0) return null;
		String serviceUri = String.format("%s/v1/organizations/%s", 
				URI.create(instances.get(0).getHomePageUrl()),
				organizationId);
		
		ResponseEntity<Organization> restExchange = restTemplate.exchange(serviceUri, 
				HttpMethod.GET, null,
				Organization.class, organizationId);

		return restExchange.getBody();
	}
	
	
}
