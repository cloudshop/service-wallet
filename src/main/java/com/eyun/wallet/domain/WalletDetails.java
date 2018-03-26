package com.eyun.wallet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A WalletDetails.
 */
@Entity
@Table(name = "wallet_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WalletDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "userid", nullable = false)
    private Long userid;

    @NotNull
    @Column(name = "wallet_id", nullable = false)
    private Long walletId;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "ticket")
    private Long ticket;

    @Column(name = "integral")
    private Long integral;

    @Column(name = "pay_price")
    private Long payPrice;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "created_time")
    private Instant createdTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public WalletDetails userid(Long userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getWalletId() {
        return walletId;
    }

    public WalletDetails walletId(Long walletId) {
        this.walletId = walletId;
        return this;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getAmount() {
        return amount;
    }

    public WalletDetails amount(Long amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Integer getType() {
        return type;
    }

    public WalletDetails type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getBalance() {
        return balance;
    }

    public WalletDetails balance(Long balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getTicket() {
        return ticket;
    }

    public WalletDetails ticket(Long ticket) {
        this.ticket = ticket;
        return this;
    }

    public void setTicket(Long ticket) {
        this.ticket = ticket;
    }

    public Long getIntegral() {
        return integral;
    }

    public WalletDetails integral(Long integral) {
        this.integral = integral;
        return this;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public Long getPayPrice() {
        return payPrice;
    }

    public WalletDetails payPrice(Long payPrice) {
        this.payPrice = payPrice;
        return this;
    }

    public void setPayPrice(Long payPrice) {
        this.payPrice = payPrice;
    }

    public Long getOrderId() {
        return orderId;
    }

    public WalletDetails orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public WalletDetails createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WalletDetails walletDetails = (WalletDetails) o;
        if (walletDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), walletDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WalletDetails{" +
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
