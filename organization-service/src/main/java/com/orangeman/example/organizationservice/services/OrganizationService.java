package com.orangeman.example.organizationservice.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orangeman.example.organizationservice.events.source.SimpleSourceBean;
import com.orangeman.example.organizationservice.model.Organization;
import com.orangeman.example.organizationservice.repository.OrganizationRepository;

@Service
public class OrganizationService {

	@Autowired
	private OrganizationRepository organizationRepository;
	
	@Autowired
	SimpleSourceBean simpleSourceBean;

	
	public Organization getOrg(String organizationId) {
		Organization organization = organizationRepository.findByOrganizationId(organizationId);
		return organization;
	}
	
	public void saveOrg(Organization org) {
		org.setOrganizationId(UUID.randomUUID().toString());
		
		
		organizationRepository.save(org);
		simpleSourceBean.publishOrgChange("SAVE", org.getOrganizationId());
	}
	
	public void updateOrg(Organization org) {
		organizationRepository.save(org);
	}
	
	public void deleteOrg(Organization org) {
//		organizationRepository.deleteById(org.getId());
	}

	
}
