package com.eyun.wallet.service.dto;

import java.io.Serializable;

public class ServiceProviderRewardDTO implements Serializable{

	private Long ServiceProviderID;// 服务商id
	private Long IncrementBusinessID;// 增值商家id

	public Long getServiceProviderID() {
		return ServiceProviderID;
	}

	public void setServiceProviderID(Long serviceProviderID) {
		ServiceProviderID = serviceProviderID;
	}

	public Long getIncrementBusinessID() {
		return IncrementBusinessID;
	}

	public void setIncrementBusinessID(Long incrementBusinessID) {
		IncrementBusinessID = incrementBusinessID;
	}

}
