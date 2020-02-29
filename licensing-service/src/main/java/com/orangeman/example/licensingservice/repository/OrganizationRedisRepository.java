package com.orangeman.example.licensingservice.repository;

import com.orangeman.example.licensingservice.model.Organization;

public interface OrganizationRedisRepository {

	void saveOrganization(Organization organization);
	void updateOrganization(Organization organization);
	void deleteOrganization(String organizationId);
	Organization findOrganization(String organizationId);
	
	
}
