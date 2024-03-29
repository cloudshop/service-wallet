package com.eyun.wallet.domain;

import java.math.BigDecimal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class GiveIntegralDTO {

	@ApiModelProperty("赠送积分额")
	private BigDecimal integral;
	@ApiModelProperty("赠送目标手机号")
	private String target;
	@ApiModelProperty("支付密码")
	private String password;

	public BigDecimal getIntegral() {
		return integral;
	}

	public void setIntegral(BigDecimal integral) {
		this.integral = integral;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
