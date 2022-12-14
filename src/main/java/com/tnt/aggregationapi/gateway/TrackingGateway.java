package com.tnt.aggregationapi.gateway;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TrackingGateway {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${tracking.api.base.url}")
	private String tracking_api_url;

	public Map<String, String> getTrackInfo(String shipmentsStr) throws Exception {
		ParameterizedTypeReference<HashMap<String, String>> responseType = new ParameterizedTypeReference<HashMap<String, String>>() {
		};
		String URL = tracking_api_url + shipmentsStr;

		RequestEntity<Void> request = RequestEntity.get(URL).accept(MediaType.APPLICATION_JSON).build();
		Map<String, String> jsonDictionary = restTemplate.exchange(request, responseType).getBody();
		return jsonDictionary;

	}

}
