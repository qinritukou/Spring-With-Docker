package com.orangeman.example.licensingservice.utils;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

	public final static String CORRELATION_ID = "tmx-correlation-id";
	public final static String AUTH_TOKEN = "tmx-auth-token";
	public final static String USER_ID = "tmx-user-id";
	public final static String ORG_ID = "tmx-org-id";
	
	private String correlationid = new String();
	private String authToken = new String();
	private String userId = new String();
	private String orgId = new String();
	public String getCorrelationid() {
		return correlationid;
	}
	public void setCorrelationid(String correlationid) {
		this.correlationid = correlationid;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	

	
}
