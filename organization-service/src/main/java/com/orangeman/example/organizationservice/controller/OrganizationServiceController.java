package com.orangeman.example.organizationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.orangeman.example.organizationservice.model.Organization;
import com.orangeman.example.organizationservice.services.OrganizationService;

@RestController
@RequestMapping(value = "v1/organizations")
public class OrganizationServiceController {
	
	@Autowired
	private OrganizationService orgService;
	

	@GetMapping("/{organizationId}")
	public Organization getOrganization(
			@PathVariable("organizationId") String organizationId) {
		return orgService.getOrg(organizationId);
	}
	
	@PutMapping("/{organizationId}")
	public Organization updateOgranization(
			@PathVariable("organizationId") String orgId,
			@RequestBody Organization org) {
		org = orgService.updateOrg(org);
		return org;
	}
	
	@PostMapping("/{organizationId}")
	public void saveOrginzation(@RequestBody Organization org) {
		orgService.saveOrg(org);
	}
	
	@RequestMapping(value="/{organizationId}",method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization( @PathVariable("organizationId") String orgId,  @RequestBody Organization org) {
        orgService.deleteOrg( org );
    }	
	
}
