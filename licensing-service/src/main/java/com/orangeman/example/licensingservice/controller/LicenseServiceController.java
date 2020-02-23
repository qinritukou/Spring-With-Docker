package com.orangeman.example.licensingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.orangeman.example.licensingservice.model.License;
import com.orangeman.example.licensingservice.services.LicenseService;

@RestController
@RequestMapping(value = "/v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

	@Autowired
	private LicenseService licenseService;
	
	@GetMapping("/")
	public List<License> getLicenses(
			@PathVariable("organizationId") String organizationId) {
		return licenseService.getLicensesByOrg(organizationId);
	}

	@GetMapping("/{licenseId}")
	public License getLicense(
			@PathVariable("organizationId") String organizationId, 
			@PathVariable("licenseId") String licenseId) {
		return licenseService.getLicense(organizationId, licenseId);
	}
	
	@PutMapping("{licenseId}")
	public String updateLicenses(
			@PathVariable("licenseId") String licensed) {
		return String.format("This is the put");
	}
	
	@PostMapping("{licenseId}")
	public void saveLicense(
			@RequestBody License license) {
		licenseService.saveLicense(license);
	}
	
	@DeleteMapping("{licenseId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String deleteLicenses(
			@PathVariable("licenseId") String licensed) {
		return String.format("This is the delete");
	}
}
