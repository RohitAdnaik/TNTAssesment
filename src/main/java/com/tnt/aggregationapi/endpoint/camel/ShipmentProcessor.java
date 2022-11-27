package com.tnt.aggregationapi.endpoint.camel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tnt.aggregationapi.constants.Constant;
import com.tnt.aggregationapi.gateway.ShipmentGateway;
import com.tnt.aggregationapi.model.ShipmentPojo;
import com.tnt.aggregationapi.model.ShipmentRequest;
import com.tnt.aggregationapi.repository.ApiResponseRepo;

@Component
public class ShipmentProcessor implements Processor {
	private static final Logger log = LoggerFactory.getLogger(ShipmentProcessor.class);

	@Autowired
	ShipmentGateway shipmentGateway;
	@Autowired
	private ApiResponseRepo apiResponseRepo;

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List<Exchange> exx = (List<Exchange>) exchange.getIn().getBody();
		ShipmentRequest shipmentRequest = null;

		for (Exchange ex : exx) {
			shipmentRequest = ex.getIn().getBody(ShipmentRequest.class);
		}
		String reqStr=String.join(",", shipmentRequest.getRequestStr());
		Map<String, List<String>> resMap = null;
		try {
			resMap = shipmentGateway.getShipmentsInfo(reqStr);
		} catch (Exception e) {
			log.error("Error occured while calling shipment api");
			resMap = new HashMap<String, List<String>>();
			for (String str : shipmentRequest.getRequestStr()) {
					resMap.put(str, Arrays.asList(Constant.APR_ERROR));
			}
		}
		List<ShipmentPojo> shipPjLst=new ArrayList<>();
		for (String key : shipmentRequest.getRequestStr()) {
			List<String> pRespValue= resMap.get(key);
			ShipmentPojo shipPj=new ShipmentPojo();
			shipPj.setShipmentKey(Long.parseLong(key));
			shipPj.setShipmentValue(pRespValue.stream().map(Object::toString).collect(Collectors.joining(",")));
			shipPj.setUuid(shipmentRequest.getUuid());
			shipPj.setReqType(Constant.SHIPMENT_REQ);
			shipPjLst.add(shipPj);
		} 
		apiResponseRepo.createRequest(shipPjLst);
	}

}
