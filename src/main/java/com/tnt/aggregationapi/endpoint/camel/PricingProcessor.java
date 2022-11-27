package com.tnt.aggregationapi.endpoint.camel;

import java.math.BigDecimal;
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
import com.tnt.aggregationapi.gateway.PricingGateway;
import com.tnt.aggregationapi.model.PricingPojo;
import com.tnt.aggregationapi.model.PricingRequest;
import com.tnt.aggregationapi.repository.ApiResponseRepo;

@Component
public class PricingProcessor implements Processor {
	private static final Logger log = LoggerFactory.getLogger(PricingProcessor.class);

	@Autowired
	PricingGateway pricingGateway;
	@Autowired
	private ApiResponseRepo apiResponseRepo;

	@Override
	public void process(Exchange exchange) throws Exception {
		// TODO Auto-generated method stub
		List<Exchange> exx = (List<Exchange>) exchange.getIn().getBody();
		String reqStr = null;
		PricingRequest pricingRequest = null;
		for (Exchange ex : exx) {
			pricingRequest = ex.getIn().getBody(PricingRequest.class);
			
		}
		reqStr=String.join(",", pricingRequest.getRequestStr());

		Map<String, BigDecimal> resMap = null;
		try {
			resMap = pricingGateway.getPricing(reqStr);//call to docker service
		} catch (Exception e) {
			resMap = new HashMap<String, BigDecimal>();
			for (String str : pricingRequest.getRequestStr()) {
				{
					resMap.put(str, new BigDecimal(Constant.PRICING_API_ERROR));
				}
			}
		}
		//message received in processor
		List<PricingPojo> pricingPj=new ArrayList<>();
		for (String str : pricingRequest.getRequestStr()) {
			BigDecimal pRespValue= resMap.get(str);
			PricingPojo pricingPp=new PricingPojo();
			pricingPp.setPricingKey(str);
			pricingPp.setPricingValue(pRespValue);
			pricingPp.setUuid(pricingRequest.getUuid());
			pricingPp.setReqType(Constant.PRICING_REQ);
			pricingPj.add(pricingPp);
		} 
		apiResponseRepo.createRequest(pricingPj);
	}

}
