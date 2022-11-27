package com.tnt.aggregationapi.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tnt.aggregationapi.model.PricingPojo;
import com.tnt.aggregationapi.model.ShipmentPojo;
import com.tnt.aggregationapi.model.TrackPojo;
import com.tnt.aggregationapi.repository.entity.ApiResponse;

@Service
public class ApiResponseRepo {
	private static final Logger log = LoggerFactory.getLogger(ApiResponseRepo.class);
	@Autowired
	SpringDataApiReposeRepository repo;
	

	@Transactional
	public <T> void createRequest(List<T> pList) {//saving response of all req type
		pList.stream().forEach(value->{
			if(value instanceof PricingPojo) {
				PricingPojo val=(PricingPojo)value; 
				repo.createRequest(val.getUuid(),val.getReqType(),val.getPricingKey(),val.getPricingValue().toString(),Timestamp.valueOf(LocalDateTime.now()));
			}else if(value instanceof TrackPojo) {
				TrackPojo val=(TrackPojo)value; 
				repo.createRequest(val.getUuid(),val.getReqType(),String.valueOf(val.getTracKey()),val.getTracValue().toString(),Timestamp.valueOf(LocalDateTime.now()));
			}else if(value instanceof ShipmentPojo) {
				ShipmentPojo val=(ShipmentPojo)value; 
				repo.createRequest(val.getUuid(),val.getReqType(),String.valueOf(val.getShipmentKey()),val.getShipmentValue().toString(),Timestamp.valueOf(LocalDateTime.now()));
			}
		});
	}
 
	
	 
	
	public List<ApiResponse> getResponse(UUID uuid, Set<String> reqParam) {
		List<ApiResponse> res = repo.findRespose(uuid, reqParam);
		if (null != res && res.size() > 0) {
			return res;
		}
		return null;
	}

	@Transactional
	public void deleteApiResponse() {
		repo.deleteApiRespose();
		log.info("Earlier response has been deleted from database table ");

	}

}
