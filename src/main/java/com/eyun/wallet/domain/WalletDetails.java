package com.eyun.wallet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
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

    @Column(name = "amount")
    private Long amount;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "balance", precision=10, scale=2)
    private BigDecimal balance;

    @Column(name = "ticket", precision=10, scale=2)
    private BigDecimal ticket;

    @Column(name = "integral", precision=10, scale=2)
    private BigDecimal integral;

    @Column(name = "pay_price", precision=10, scale=2)
    private BigDecimal pay_price;

    @Column(name = "order_no")
    private String orderNo;

    @ManyToOne
    private Wallet wallet;

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

    public BigDecimal getBalance() {
        return balance;
    }

    public WalletDetails balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTicket() {
        return ticket;
    }

    public WalletDetails ticket(BigDecimal ticket) {
        this.ticket = ticket;
        return this;
    }

    public void setTicket(BigDecimal ticket) {
        this.ticket = ticket;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public WalletDetails integral(BigDecimal integral) {
        this.integral = integral;
        return this;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public BigDecimal getPay_price() {
        return pay_price;
    }

    public WalletDetails pay_price(BigDecimal pay_price) {
        this.pay_price = pay_price;
        return this;
    }

    public void setPay_price(BigDecimal pay_price) {
        this.pay_price = pay_price;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public WalletDetails orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public WalletDetails wallet(Wallet wallet) {
        this.wallet = wallet;
        return this;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
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
            ", amount=" + getAmount() +
            ", type=" + getType() +
            ", createdTime='" + getCreatedTime() + "'" +
            ", balance=" + getBalance() +
            ", ticket=" + getTicket() +
            ", integral=" + getIntegral() +
            ", pay_price=" + getPay_price() +
            ", orderNo='" + getOrderNo() + "'" +
            "}";
    }
}
