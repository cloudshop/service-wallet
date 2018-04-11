package com.eyun.wallet.domain;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class GiveIntegralDTO {

	@ApiModelProperty("钱包所有用户id")
	private Long userid;
	@ApiModelProperty("赠送积分额")
	private BigDecimal integral;
	@ApiModelProperty("赠送目标id")
	private Long target;
	public Long getUserid() {
		return userid;
	}
	public void setUserid(Long userid) {
		this.userid = userid;
	}
	public BigDecimal getIntegral() {
		return integral;
	}
	public void setIntegral(BigDecimal integral) {
		this.integral = integral;
	}
	public Long getTarget() {
		return target;
	}
	public void setTarget(Long target) {
		this.target = target;
	}
	
}
