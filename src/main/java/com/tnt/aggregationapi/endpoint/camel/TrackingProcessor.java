package com.tnt.aggregationapi.endpoint.camel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tnt.aggregationapi.constants.Constant;
import com.tnt.aggregationapi.gateway.TrackingGateway;
import com.tnt.aggregationapi.model.TrackPojo;
import com.tnt.aggregationapi.model.TrackingRequest;
import com.tnt.aggregationapi.repository.ApiResponseRepo;

@Component
public class TrackingProcessor implements Processor {
	private static final Logger log = LoggerFactory.getLogger(TrackingProcessor.class);

	@Autowired
	TrackingGateway trackingGateway;
	@Autowired
	private ApiResponseRepo apiResponseRepo;

	@Override
	public void process(Exchange exchange) throws Exception {
		List<Exchange> exx = (List<Exchange>) exchange.getIn().getBody();
		String reqStr = null;
		TrackingRequest trackingRequest = null;
		for (Exchange ex : exx) {
			trackingRequest = ex.getIn().getBody(TrackingRequest.class);
		}
		reqStr=String.join(",", trackingRequest.getRequestStr());
		Map<String, String> resMap = null;
		try {
			resMap = trackingGateway.getTrackInfo(reqStr);
		} catch (Exception e) {
			resMap = new HashMap<String, String>();
			for (String str : trackingRequest.getRequestStr()) {
				{
					log.info("puuting error :" + str);
					resMap.put(str, Constant.APR_ERROR);
				}
			}
		}
		List<TrackPojo> trackPj=new ArrayList<>();
		for (String key : trackingRequest.getRequestStr()) {
			String pRespValue= resMap.get(key);
			TrackPojo trackPp=new TrackPojo();
			trackPp.setTracKey(Long.parseLong(key));
			trackPp.setTracValue(pRespValue);
			trackPp.setUuid(trackingRequest.getUuid());
			trackPp.setReqType(Constant.TRACKING_REQ);
			trackPj.add(trackPp);
		} 
		apiResponseRepo.createRequest(trackPj);
	}

}
