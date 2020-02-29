package com.orangeman.example.licensingservice.repository;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.orangeman.example.licensingservice.model.Organization;

@Repository
public class OrganizationRedisRepositoryImpl implements OrganizationRedisRepository {

	private static final String HASH_NAME = "organization";
	private RedisTemplate<String, Organization> redisTemplate;
	private HashOperations hashOperations;
	
	public OrganizationRedisRepositoryImpl() {
		super();
	}
	
	@Autowired
	private OrganizationRedisRepositoryImpl(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}
	
	@PostConstruct
	private void init() {
		hashOperations = redisTemplate.opsForHash();
	}
	
	
	@Override
	public void saveOrganization(Organization organization) {
		// TODO Auto-generated method stub
		hashOperations.put(HASH_NAME, organization.getOrganizationId(), organization);
	}

	@Override
	public void updateOrganization(Organization organization) {
		// TODO Auto-generated method stub
		hashOperations.put(HASH_NAME, organization.getOrganizationId(), organization);
	}

	@Override
	public void deleteOrganization(String organizationId) {
		// TODO Auto-generated method stub
		hashOperations.delete(HASH_NAME, organizationId);
	}

	@Override
	public Organization findOrganization(String organizationId) {
		// TODO Auto-generated method stub
		return (Organization) hashOperations.get(HASH_NAME, organizationId);
	}

}
