package com.orangeman.example.organizationservice.events.source;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.orangeman.example.organizationservice.events.models.OrganizationChangeModel;
import com.orangeman.example.organizationservice.utils.UserContextHolder;

@Component
public class SimpleSourceBean {
	private Source source;
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleSourceBean.class);
	
	@Autowired
	public SimpleSourceBean(Source source) {
		this.source = source;
	}
	
	public void publishOrgChange(String action, String orgId) {
		logger.info("Sending Kafka message {} for Organization Id: {}", action, orgId);
		OrganizationChangeModel change = new OrganizationChangeModel(
				OrganizationChangeModel.class.getTypeName(),
				action,
				orgId,
				UserContextHolder.getContext().getCorrelationId());
		
		source
			.output()
			.send(MessageBuilder.withPayload(change).build());
	}
	
}
