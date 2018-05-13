package com.eyun.wallet.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PasswordDTO {
	
	@ApiModelProperty(name="支付密码")
	private String password;
	@ApiModelProperty(name="短信验证码")
	private String code;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	

}
