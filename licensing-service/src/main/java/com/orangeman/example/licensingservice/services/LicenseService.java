package com.orangeman.example.licensingservice.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.orangeman.example.licensingservice.clients.OrganizationDiscoveryClient;
import com.orangeman.example.licensingservice.clients.OrganizationFeignClient;
import com.orangeman.example.licensingservice.clients.OrganizationRestTemplateClient;
import com.orangeman.example.licensingservice.config.ServiceConfig;
import com.orangeman.example.licensingservice.model.License;
import com.orangeman.example.licensingservice.model.Organization;
import com.orangeman.example.licensingservice.repository.LicenseRepository;

@Service
public class LicenseService {
	
	@Autowired
	private LicenseRepository licenseRepository;
	
	@Autowired
	ServiceConfig config;
	
	@Autowired
	OrganizationFeignClient organizationFeignClient;
	
	@Autowired
	OrganizationRestTemplateClient organizationRestTemplateClient;
	
	@Autowired
	OrganizationDiscoveryClient organizationDiscoveryClient;
	
	private Organization retrieveOrgInfo(String organizationId, String clientType) {
		Organization organization = null;
		
		switch (clientType) {
		case "feign":
			System.out.println("I am using the feign client");
			organization = organizationFeignClient.getOrganization(organizationId);
			break;
		case "rest":
			System.out.println("I am using the discovery client");
			organization = organizationRestTemplateClient.getOrganziation(organizationId);
			break;
		case "discovery":			
			System.out.println("I am using the discovery client");
			organization = organizationDiscoveryClient.getOrganization(organizationId);
			break;
		default:
			organization = organizationRestTemplateClient.getOrganziation(organizationId);
			break;
		}
		
		return organization;
	}
	
	
	public License getLicense(String organizationId, String licenseId, String clientType) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		
		Organization org = retrieveOrgInfo(organizationId, clientType);
		license.withComment(config.getExampleProperty());
		if (org != null) {
			license.withOrganizationName(org.getOrganizationName())
					.withContactName(org.getContactName())
					.withContactEmail(org.getContactEmail())
					.withContactPhone(org.getContactPhone());
		}
		return license;
	}
	
	private void randomlyRunLong() {
		Random rand = new Random();
		int randomNum = rand.nextInt((3 - 1) + 1) + 1;
		
		if (randomNum == 3) sleep();
	}
	

	private void sleep() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1100);
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private List<License> buildFallbackLicenseList(String organizationId) {
		List<License> fallbackList = new ArrayList<>();
		License license = new License()
				.withLicenseId("0000000000-0000000")
				.withOrganizationId(organizationId)
				.withProductName("Sorry no licesing information currently available");
		
		fallbackList.add(license);
		return fallbackList;
	}


	@HystrixCommand(
			commandProperties = {
					@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value="1200"),
					@HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
			},
			fallbackMethod = "buildFallbackLicenseList",
			// some thread pool configuration
			threadPoolKey = "licenseByOrgThreadPool",
			threadPoolProperties = {
					@HystrixProperty(name = "coreSize", value = "30"),
					@HystrixProperty(name = "maxQueueSize", value = "10")
			}
			)
	public List<License> getLicensesByOrg(String organizationId) {
		// TODO Auto-generated method stub
		randomlyRunLong();
		List<License> list = licenseRepository.findByOrganizationId(organizationId);
		Organization org = retrieveOrgInfo(organizationId, "feign");
		
		if (list != null) {
			list.stream().forEach(license -> {
				license.withOrganizationName(org.getOrganizationName())
					.withContactName(org.getContactName())
					.withContactEmail(org.getContactEmail())
					.withContactPhone(org.getContactPhone());
			});			
		}
		
		return list;
	}

	public void saveLicense(License license) {
		license.withLicenseId(UUID.randomUUID().toString());
		
		licenseRepository.save(license);
	}
	
	public void updateLicense(License license) {
		licenseRepository.save(license);
	}
	
	public void deleteLicense(License license) {
//		licenseRepository.delete(license.getLicenseId());
	}
	

}
