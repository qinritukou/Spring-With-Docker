package com.orangeman.example.zuulsvr.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class TrackingFilter extends ZuulFilter {

	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;
	private static final Logger logger = LoggerFactory.getLogger(TrackingFilter.class);
	
	@Autowired
	FilterUtils filterUtils;
	
	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return SHOULD_FILTER;
	}

	@Override
	public Object run() throws ZuulException {
		// TODO Auto-generated method stub
		if (isCorrelationIdPresent()) {
			logger.info("tmx-correlation-id found in tracking filter: {}.", filterUtils.getCorrelationId());
			filterUtils.getCorrelationId();
		} else {
			filterUtils.setCorrelationId(generateCorrelationId());
			logger.info("tmx-correlation-id generated in tracking filter: {}.", filterUtils.getCorrelationId());
		}
		
		RequestContext ctx = RequestContext.getCurrentContext();
		logger.info("Processing incomding request for {}.", ctx.getRequest().getRequestURI());
		return null;
	}

	private String generateCorrelationId() {
		// TODO Auto-generated method stub
		return java.util.UUID.randomUUID().toString();
	}

	private boolean isCorrelationIdPresent() {
		// TODO Auto-generated method stub
		if (filterUtils.getCorrelationId() != null) {
			return true;
		}
		return false;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return FilterUtils.PRE_FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return FILTER_ORDER;
	}

	
}
