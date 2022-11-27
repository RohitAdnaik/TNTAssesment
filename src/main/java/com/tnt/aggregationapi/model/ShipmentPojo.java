package com.tnt.aggregationapi.model;

import java.util.UUID;

public class ShipmentPojo {
	private UUID uuid;
	private String reqType;
	private long shipmentKey;
	private String shipmentValue;
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
	public long getShipmentKey() {
		return shipmentKey;
	}
	public void setShipmentKey(long shipmentKey) {
		this.shipmentKey = shipmentKey;
	}
	public String getShipmentValue() {
		return shipmentValue;
	}
	public void setShipmentValue(String shipmentValue) {
		this.shipmentValue = shipmentValue;
	}
	@Override
	public String toString() {
		return "ShipmentPojo [uuid=" + uuid + ", reqType=" + reqType + ", shipmentKey=" + shipmentKey
				+ ", shipmentValue=" + shipmentValue + "]";
	}
	
}