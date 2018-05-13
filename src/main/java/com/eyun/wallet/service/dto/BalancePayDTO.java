package com.eyun.wallet.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BalancePayDTO {

	@ApiModelProperty(name="支付密码")
	private String password;
	
	@ApiModelProperty(name="订单号")
	private String orderNo;
	
	public String getPassword() {
		return password;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	
	
}
