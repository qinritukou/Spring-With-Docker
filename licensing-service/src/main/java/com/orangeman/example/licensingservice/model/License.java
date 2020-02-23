package com.orangeman.example.licensingservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "licenses")
public class License {

	@Id
	@Column(name = "license_id", nullable = false)
	private String licenseId;
	
	@Column(name = "organization_id", nullable = false)
	private String organizationId;
	
	@Column(name = "product_name", nullable = false)
	private String productName;
	
	@Transient
	private String organizationName = "";
	
	@Transient
	private String contactName = "";
	
	@Transient
	private String contactEmail = "";

	@Transient
	private String contactPhone = "";

	
	@Column(name = "license_type", nullable = false)
	private String licenseType;
	
	@Column(name = "license_max", nullable = false)
	private Integer licenseMax;
	
	@Column(name = "license_allocated", nullable = false)
	private Integer licenseAllocated;
	
	@Column(name = "comment")
	private String comment;
	

	public String getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public Integer getLicenseMax() {
		return licenseMax;
	}

	public void setLicenseMax(Integer licenseMax) {
		this.licenseMax = licenseMax;
	}
	

	public Integer getLicenseAllocated() {
		return licenseAllocated;
	}

	public void setLicenseAllocated(Integer licenseAllocated) {
		this.licenseAllocated = licenseAllocated;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public License withLicenseId(String id){
		this.setLicenseId(id);
		return this;
	}

	public License withOrganizationId(String organizationId){
		this.setOrganizationId(organizationId);
		return this;
	}

	public License withProductName(String productName){
		this.setProductName(productName);
		return this;
	}

	public License withLicenseType(String licenseType){
		this.setLicenseType(licenseType);
		return this;
	}

	public License withComment(String comment) {
		// TODO Auto-generated method stub
		this.setComment(comment);
		return this;
	}

	public License withOrganizationName(String organizationName) {
		// TODO Auto-generated method stub
		this.setOrganizationName(organizationName);
		return this;
	}

	public License withContactName(String contactName) {
		// TODO Auto-generated method stub
		this.setContactName(contactName);
		return this;
	}

	public License withContactEmail(String contactEmail) {
		// TODO Auto-generated method stub
		this.setContactEmail(contactEmail);
		return this;
	}

	public License withContactPhone(String contactPhone) {
		// TODO Auto-generated method stub
		this.setContactPhone(contactPhone);
		return this;
	}
	
}
