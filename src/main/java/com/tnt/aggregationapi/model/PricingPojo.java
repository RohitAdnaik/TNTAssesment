package com.tnt.aggregationapi.model;

import java.math.BigDecimal;
import java.util.UUID;

public class PricingPojo {

	//uuid: cc2c501f-ee6d-4487-81ad-47f2e96b76d6 reqType: TRACKING requestParam: 109347263
	private UUID uuid;
	private String reqType;
	private String pricingKey;
	private BigDecimal pricingValue;
	public UUID getUuid() {
		return uuid;
	}
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	public String getReqType() {
		return reqType;
	}
	public void setReqType(String reqType) {
		this.reqType = reqType;
	}
	public String getPricingKey() {
		return pricingKey;
	}
	public void setPricingKey(String pricingKey) {
		this.pricingKey = pricingKey;
	}
	public BigDecimal getPricingValue() {
		return pricingValue;
	}
	public void setPricingValue(BigDecimal pricingValue) {
		this.pricingValue = pricingValue;
	}
	@Override
	public String toString() {
		return "PricingPojo [uuid=" + uuid + ", reqType=" + reqType + ", pricingKey=" + pricingKey + ", pricingValue="
				+ pricingValue + "]";
	}
	
}
