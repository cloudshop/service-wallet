package com.eyun.wallet.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the WithdrawDeposit entity.
 */
public class WithdrawDepositDTO implements Serializable {

    private Long id;

    private String cardholder;

    private String openingBank;

    private String bankcardNumber;

    private BigDecimal money;

    private Integer status;

    private String statusString;

    private Instant createdTime;

    private Instant updatedTime;

    private Long userid;

    private String describe;

    private Long walletId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WithdrawDepositDTO withdrawDepositDTO = (WithdrawDepositDTO) o;
        if(withdrawDepositDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), withdrawDepositDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WithdrawDepositDTO{" +
            "id=" + getId() +
            ", cardholder='" + getCardholder() + "'" +
            ", openingBank='" + getOpeningBank() + "'" +
            ", bankcardNumber='" + getBankcardNumber() + "'" +
            ", money=" + getMoney() +
            ", status=" + getStatus() +
            ", statusString='" + getStatusString() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", userid=" + getUserid() +
            ", describe='" + getDescribe() + "'" +
            "}";
    }
}
