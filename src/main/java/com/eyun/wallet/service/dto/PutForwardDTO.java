package com.eyun.wallet.service.dto;

import java.math.BigDecimal;

public class PutForwardDTO {

	private String cardholder;

    private String openingBank;

    private String bankcardNumber;

    private BigDecimal money;
    
    private String password;

	public String getCardholder() {
		return cardholder;
	}

	public void setCardholder(String cardholder) {
		this.cardholder = cardholder;
	}

	public String getOpeningBank() {
		return openingBank;
	}

	public void setOpeningBank(String openingBank) {
		this.openingBank = openingBank;
	}

	public String getBankcardNumber() {
		return bankcardNumber;
	}

	public void setBankcardNumber(String bankcardNumber) {
		this.bankcardNumber = bankcardNumber;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
}
