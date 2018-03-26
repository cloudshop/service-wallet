package com.eyun.wallet.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the WalletDetails entity.
 */
public class WalletDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    private Long userid;

    @NotNull
    private Long walletId;

    private Long amount;

    private Integer type;

    private Long balance;

    private Long ticket;

    private Long integral;

    private Long payPrice;

    private Long orderId;

    private Instant createdTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getTicket() {
        return ticket;
    }

    public void setTicket(Long ticket) {
        this.ticket = ticket;
    }

    public Long getIntegral() {
        return integral;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public Long getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Long payPrice) {
        this.payPrice = payPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WalletDetailsDTO walletDetailsDTO = (WalletDetailsDTO) o;
        if(walletDetailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), walletDetailsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WalletDetailsDTO{" +
            "id=" + getId() +
            ", userid=" + getUserid() +
            ", walletId=" + getWalletId() +
            ", amount=" + getAmount() +
            ", type=" + getType() +
            ", balance=" + getBalance() +
            ", ticket=" + getTicket() +
            ", integral=" + getIntegral() +
            ", payPrice=" + getPayPrice() +
            ", orderId=" + getOrderId() +
            ", createdTime='" + getCreatedTime() + "'" +
            "}";
    }
}
