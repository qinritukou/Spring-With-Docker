package com.orangeman.example.licensingservice.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orangeman.example.licensingservice.config.ServiceConfig;
import com.orangeman.example.licensingservice.model.License;
import com.orangeman.example.licensingservice.repository.LicenseRepository;

@Service
public class LicenseService {
	
	@Autowired
	private LicenseRepository licenseRepository;
	
	@Autowired
	ServiceConfig config;
	
	public License getLicense(String organizationId, String licenseId) {
		License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		return license.withComment(config.getExampleProperty());
	}
	

	public List<License> getLicensesByOrg(String organizationId) {
		// TODO Auto-generated method stub
		return licenseRepository.findByOrganizationId(organizationId);
	}

	public void saveLicense(License license) {
		license.withLicenseId(UUID.randomUUID().toString());
		
		licenseRepository.save(license);
	}
	
	public void deleteLicense(License license) {
//		licenseRepository.delete(license.getLicenseId());
	}
	

}
