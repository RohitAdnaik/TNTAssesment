package com.tnt.aggregationapi.gateway;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PricingGateway {
	private static final Logger log = LoggerFactory.getLogger(PricingGateway.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${pricing.api.base.url}")
	private String pricing_api_url;
	
	
	
	
	public Map<String, BigDecimal> getPricing(String countriesStr) throws Exception {
		ParameterizedTypeReference<HashMap<String, BigDecimal>> responseType = new ParameterizedTypeReference<HashMap<String, BigDecimal>>() {
		};
		String URL = pricing_api_url + countriesStr;

		RequestEntity<Void> request = RequestEntity.get(URL).accept(MediaType.APPLICATION_JSON).build();
		Map<String, BigDecimal> responseMap = restTemplate.exchange(request, responseType).getBody();
		return responseMap;

	}

}
