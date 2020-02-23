package com.orangeman.example.licensingservice.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	

	public List<License> getLicensesByOrg(String organizationId) {
		// TODO Auto-generated method stub
		return licenseRepository.findByOrganizationId(organizationId);
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
