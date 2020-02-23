package com.orangeman.example.organizationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.orangeman.example.organizationservice.model.Organization;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {

	public Organization findByOrganizationId(String organizationId);

	

}
