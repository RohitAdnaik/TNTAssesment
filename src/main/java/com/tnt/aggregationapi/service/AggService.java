package com.tnt.aggregationapi.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tnt.aggregationapi.constants.Constant;
import com.tnt.aggregationapi.model.PricingRequest;
import com.tnt.aggregationapi.model.Response;
import com.tnt.aggregationapi.model.ShipmentRequest;
import com.tnt.aggregationapi.model.TrackingRequest;
import com.tnt.aggregationapi.repository.ApiResponseRepo;
import com.tnt.aggregationapi.repository.entity.ApiResponse;

@Service
@Async
public class AggService {
	private static final Logger log = LoggerFactory.getLogger(AggService.class);
	@Autowired
	private ApiResponseRepo apiResponseRepo;

	public Response buidAggRes(PricingRequest pricing, TrackingRequest track, ShipmentRequest shipments)
			throws InterruptedException, ExecutionException {
		Response res = new Response();
		CompletableFuture<Map<String, BigDecimal>> pricingResult = buildPricingRes(pricing);
		CompletableFuture<Map<String, String>> trackingResult = buildTrackingRes(track);
		CompletableFuture<Map<String, List<String>>> shipmentResult = buildShipmentRes(shipments);
		CompletableFuture.allOf(pricingResult,trackingResult).join();
		res.setPricing(pricingResult.get());
		res.setTrack(trackingResult.get());
		res.setShipments(shipmentResult.get());

		return res;
	}

	@Async("taskExecutor")
	private CompletableFuture<Map<String, BigDecimal>> buildPricingRes(PricingRequest pricingRequest)
			throws InterruptedException {
		//log.info("start buldnig pircing response for :" + pricingRequest);
		Map<String, BigDecimal> resMap = new HashMap<String, BigDecimal>();
		Set<String> counrtySet = pricingRequest.getRequestStr();

		List<ApiResponse> responseData = null;
		while (resMap.isEmpty()) {
				responseData = apiResponseRepo.getResponse(pricingRequest.getUuid(), counrtySet);
				if (null != responseData) {
					responseData.stream().forEach(v->{
						resMap.put(v.getReqParam(),
								new BigDecimal(v.getResParam()));
					});
					break;
				}
		}
		return CompletableFuture.completedFuture(resMap);
	}

	@Async("taskExecutor")
	private CompletableFuture<Map<String, String>> buildTrackingRes(TrackingRequest trackingRequest)
			throws InterruptedException {
		//log.info("start buldnig tracking response for :" + trackingRequest);
		Map<String, String> resMap = new HashMap<String, String>();
		Set<String> shipmentSet = trackingRequest.getRequestStr();
		List<ApiResponse> responseData = null;
		while (resMap.isEmpty()) {
				responseData = apiResponseRepo.getResponse(trackingRequest.getUuid(), shipmentSet);
				if (null != responseData) {
					responseData.stream().forEach(v->{
						if (v.getResParam().equals(Constant.APR_ERROR))
							resMap.put(v.getReqParam(), null);
						else
							resMap.put(v.getReqParam(), v.getResParam());
					});
					break;
				}
		}
		
		//log.info("tracking response build complete");
		return CompletableFuture.completedFuture(resMap);
	}

	@Async("taskExecutor")
	private CompletableFuture<Map<String, List<String>>> buildShipmentRes(ShipmentRequest shipmentRequest)
			throws InterruptedException {
		//log.info("start buldnig shipment response for :" + shipmentRequest);
		Map<String, List<String>> resMap = new HashMap<String, List<String>>();
		Set<String> shipmentSet = shipmentRequest.getRequestStr();

		/*ApiResponse responseData = null;
		while (shipmentSet.size() != resMap.size()) {
			for (String shipment : shipmentSet) {
				if (!resMap.containsKey(shipment)) {
					responseData = apiResponseRepo.getResponse(shipmentRequest.getUuid(), shipment);
				}
				if (null != responseData) {
					if (responseData.getResParam().equals(Constant.APR_ERROR))
						resMap.put(responseData.getReqParam(), null);
					else
						resMap.put(responseData.getReqParam(), Arrays.asList(responseData.getResParam().split(",")));
				}
			}
		}*/
		
		List<ApiResponse> responseData = null;
		while (resMap.isEmpty()) {
				responseData = apiResponseRepo.getResponse(shipmentRequest.getUuid(), shipmentSet);
				if (null != responseData) {
					responseData.stream().forEach(v->{
						if (v.getResParam().equals(Constant.APR_ERROR))
							resMap.put(v.getReqParam(), null);
						else
							resMap.put(v.getReqParam(), Arrays.asList(v.getResParam().split(",")));
					});
					break;
				}
		}
		return CompletableFuture.completedFuture(resMap);
	}

}
