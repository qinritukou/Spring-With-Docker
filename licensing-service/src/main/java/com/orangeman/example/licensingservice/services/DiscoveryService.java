package com.orangeman.example.licensingservice.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DiscoveryService {

	
	@Autowired
	RestTemplate restTemplate;
	
//	@Autowired
//	private DiscoveryClient discoveryClient;
	
	public List<String> getEurekaServices() {
		List<String> services = new ArrayList<>();
		
//		discoveryClient.getInstancesById("organizationservice").forEach(instanceInfo -> {
//			services.add(String.format("%s:%s", "organizationservice", instanceInfo.getHomePageUrl()));
//		});
		
		return services;
	}
}
