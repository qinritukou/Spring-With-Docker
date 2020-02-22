package com.orangeman.example.licensingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.orangeman.example.licensingservice.model.License;
import com.orangeman.example.licensingservice.service.LicenseService;

@RestController
@RequestMapping(value = "/v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {

	@Autowired
	private LicenseService licenseService;
	
	@GetMapping("/{licenseId}")
	public License getLicense(
			@PathVariable("organizationId") String organizationId, 
			@PathVariable("licenseId") String licenseId) {
		return new License()
				.withId(licenseId)
				.withProductName("Teleco")
				.withLicenseType("Seat")
				.withOrganizationId("TestOrg");		
	}
	
	@PutMapping("{licenseId}")
	public String updateLicenses(
			@PathVariable("licenseId") String licensed) {
		return String.format("This is the put");
	}
	
	@PostMapping("{licenseId}")
	public String saveLicenses(
			@PathVariable("licenseId") String licenseId) {
		return String.format("This is the post");
	}
	
	@DeleteMapping("{licenseId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String deleteLicenses(
			@PathVariable("licenseId") String licensed) {
		return String.format("This is the delete");
	}
}
