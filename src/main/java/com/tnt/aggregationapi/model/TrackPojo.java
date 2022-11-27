package com.tnt.aggregationapi.model;

import java.util.UUID;

public class TrackPojo {
	private UUID uuid;
	private String reqType;
	private long tracKey;
	private String tracValue;
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
	public long getTracKey() {
		return tracKey;
	}
	public void setTracKey(long tracKey) {
		this.tracKey = tracKey;
	}
	public String getTracValue() {
		return tracValue;
	}
	public void setTracValue(String tracValue) {
		this.tracValue = tracValue;
	}
	@Override
	public String toString() {
		return "TrackPojo [uuid=" + uuid + ", reqType=" + reqType + ", tracKey=" + tracKey + ", tracValue=" + tracValue
				+ "]";
	}
	
}
