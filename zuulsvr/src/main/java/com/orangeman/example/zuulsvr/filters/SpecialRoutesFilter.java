package com.orangeman.example.zuulsvr.filters;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.orangeman.example.zuulsvr.model.AbTestingRoute;

/**
 * For A/B Test
 * @author qinritukou
 *
 */
//@Component
public class SpecialRoutesFilter extends ZuulFilter {

	private static final int FILTER_ORDER = 1;
	private static final boolean SHOULD_FILTER = true;
	
	
	@Autowired
	FilterUtils filterUtils;
	
	@Autowired
	RestTemplate restTemplate;
		
	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return SHOULD_FILTER;
	}
	
	@SuppressWarnings("deprecation")
	private ProxyRequestHelper helper = new ProxyRequestHelper();
	
	private AbTestingRoute getAbRoutingInfo(String serviceName) {
		ResponseEntity<AbTestingRoute> restExchange = null;
		try {
			restExchange = restTemplate.exchange(
					"http://specialroutesservice/v1/route/abtesting/{serviceName}", 
					HttpMethod.GET,
					null, AbTestingRoute.class, serviceName);
		} catch (HttpClientErrorException ex) {
			// TODO: handle exception
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND) return null;
			throw ex;
		}
		return restExchange.getBody();
	}
	
	private String buildRouteString(String oldEndpoint, String newEndPoint, String serviceName) {
		int index = oldEndpoint.indexOf(serviceName);
		
		String strippedRoute = oldEndpoint.substring(index + serviceName.length());
		System.out.println("Target route: " + String.format("%s/%s", newEndPoint, strippedRoute));
		return String.format("%s/%s", newEndPoint, strippedRoute);
	}
	
	private String getVerb(HttpServletRequest request) {
		String sMethod = request.getMethod();
		return sMethod.toUpperCase();
	}
	
	private HttpHost getHttpHost(URL host) {
		HttpHost httpHost = new HttpHost(host.getHost(), host.getPort(), host.getProtocol());
		return httpHost;
	}
	
	private Header[] convertHeaders(MultiValueMap<String, String> headers) {
		List<Header> list = new ArrayList<>();
		headers.keySet().stream().forEach(name -> {
			headers.get(name).stream().forEach(value -> {
				list.add(new BasicHeader(name, value));
			});
		});
		return list.toArray(new BasicHeader[0]);
	}
	
	private HttpResponse forwardRequest(HttpClient httpClient,
			HttpHost httpHost,
			HttpRequest httpRequest) throws ClientProtocolException, IOException {
		return httpClient.execute(httpHost, httpRequest);
	}
	
	private MultiValueMap<String, String> revertHeaders(Header[] headers) {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		for (Header header :headers) {
			String name = header.getName();
			if (!map.containsKey(name)) {
				map.put(name, new ArrayList<>());
			}
			map.get(name).add(header.getValue());
		}
		return map;		
	}
	
	private InputStream getRequestBody(HttpServletRequest request) {
		InputStream requestEntity = null;
		try {
			requestEntity = request.getInputStream();
		} catch (IOException ex) {
			// TODO: handle exception
			
		}
		return requestEntity;
	}
	
	private void setResponse(HttpResponse response) throws UnsupportedOperationException, IOException {
		this.helper.setResponse(response.getStatusLine().getStatusCode(), 
				response.getEntity() == null ? null : response.getEntity().getContent(), revertHeaders(response.getAllHeaders()));
	}
	
	private HttpResponse forward(HttpClient httpClient,
			String verb, String uri, 
			HttpServletRequest request,
			MultiValueMap<String, String> headers,
			MultiValueMap<String, String> params,
			InputStream requestEntity) throws IOException {
//		Map<String, Object> info = this.helper.debug(verb, uri, headers, params, requestEntity);
		URL host = new URL(uri);
		HttpHost httpHost = getHttpHost(host);
		
		HttpRequest httpRequest;
		int contentLength = request.getContentLength();
		InputStreamEntity entity = new InputStreamEntity(requestEntity, contentLength, 
				request.getContentType() != null 
				? ContentType.create(request.getContentType()) : null);
		
		switch (verb.toUpperCase()) {
		case "POST":
			HttpPost httpPost = new HttpPost();
			httpRequest = httpPost;
			httpPost.setEntity(entity);
			break;
		case "PUT":
			HttpPut httpPut = new HttpPut();
			httpRequest = httpPut;
			httpPut.setEntity(entity);
			break;
		case "PATCH":
			HttpPatch httpPatch = new HttpPatch();
			httpRequest = httpPatch;
			httpPatch.setEntity(entity);
			break;			
		default:
			httpRequest = new BasicHttpRequest(verb, uri);
		}
		
		try {
			httpRequest.setHeaders(convertHeaders(headers));
			HttpResponse zuulResponse = forwardRequest(httpClient, httpHost, httpRequest);
			
			return zuulResponse;
		} finally {
			
		}
	}
	
	public boolean useSpecialRroute(AbTestingRoute testRoute) {
		Random random = new Random();
		
		if (testRoute.getActive().equals("N")) return false;
		
		int value = random.nextInt((10 - 1) + 1) + 1;
		
		if (testRoute.getWeight() < value) return true;
		
		return false;
	}

	@Override
	public Object run() throws ZuulException {
		// TODO Auto-generated method stub
		RequestContext ctx = RequestContext.getCurrentContext();
		
		AbTestingRoute abTestingRoute = getAbRoutingInfo(filterUtils.getServiceId());
		
		if (abTestingRoute != null && useSpecialRroute(abTestingRoute)) {
			String route = buildRouteString(ctx.getRequest().getRequestURI(), 
					abTestingRoute.getEndpoint(), 
					ctx.get("serviceId").toString());
			forwardToSpecialRoute(route);
		}
		return null;
	}

	private void forwardToSpecialRoute(String route) {
		// TODO Auto-generated method stub
		RequestContext context = RequestContext.getCurrentContext();
		HttpServletRequest request = context.getRequest();
		
		MultiValueMap<String, String> headers = this.helper.buildZuulRequestHeaders(request);
		MultiValueMap<String, String> params = this.helper.buildZuulRequestQueryParams(request);
		String verb = getVerb(request);
		InputStream requestEntity = getRequestBody(request);
		if (request.getContentLength() < 0) {
			context.setChunkedRequestBody();
		}
		
		this.helper.addIgnoredHeaders();
		CloseableHttpClient httpClient = null;
		
		try {
			httpClient = HttpClients.createDefault();
			HttpResponse response = forward(httpClient, verb, route, request, headers, params, requestEntity);
			setResponse(response);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally {
			try {
				httpClient.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return FilterUtils.ROUTE_FILTER_TYPE;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return FILTER_ORDER;
	}

	
}
