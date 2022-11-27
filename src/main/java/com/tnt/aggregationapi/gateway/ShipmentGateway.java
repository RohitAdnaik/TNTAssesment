package com.tnt.aggregationapi.gateway;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ShipmentGateway {

	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${shipment.api.base.url}")
	private String shipment_api_url;

	public Map<String, List<String>> getShipmentsInfo(String shipmentsStr) {
		ParameterizedTypeReference<HashMap<String, List<String>>> responseType = new ParameterizedTypeReference<HashMap<String, List<String>>>() {
		};
		String URL = shipment_api_url + shipmentsStr;

		RequestEntity<Void> request = RequestEntity.get(URL).accept(MediaType.APPLICATION_JSON).build();
		Map<String, List<String>> jsonDictionary = restTemplate.exchange(request, responseType).getBody();
		return jsonDictionary;

	}

}
