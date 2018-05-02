package com.eyun.wallet.service.dto;

import java.io.Serializable;

public class IncrementUserRewardDTO implements Serializable{

	private Long IncrementUserID;// 增值用户id
	private Long IncrementBusinessID;// 增值商家id
	public Long getIncrementUserID() {
		return IncrementUserID;
	}
	public void setIncrementUserID(Long incrementUserID) {
		IncrementUserID = incrementUserID;
	}
	public Long getIncrementBusinessID() {
		return IncrementBusinessID;
	}
	public void setIncrementBusinessID(Long incrementBusinessID) {
		IncrementBusinessID = incrementBusinessID;
	}

}
