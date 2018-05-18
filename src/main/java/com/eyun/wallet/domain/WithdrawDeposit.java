package com.eyun.wallet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A WithdrawDeposit.
 */
@Entity
@Table(name = "withdraw_deposit")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WithdrawDeposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cardholder")
    private String cardholder;

    @Column(name = "opening_bank")
    private String openingBank;

    @Column(name = "bankcard_number")
    private String bankcardNumber;

    @Column(name = "money", precision=10, scale=2)
    private BigDecimal money;

    @Column(name = "status")
    private Integer status;

    @Column(name = "status_string")
    private String statusString;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "updated_time")
    private Instant updatedTime;

    @Column(name = "userid")
    private Long userid;

    @Column(name = "jhi_describe")
    private String describe;

    @ManyToOne
    private Wallet wallet;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardholder() {
        return cardholder;
    }

    public WithdrawDeposit cardholder(String cardholder) {
        this.cardholder = cardholder;
        return this;
    }

    public void setCardholder(String cardholder) {
        this.cardholder = cardholder;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public WithdrawDeposit openingBank(String openingBank) {
        this.openingBank = openingBank;
        return this;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getBankcardNumber() {
        return bankcardNumber;
    }

    public WithdrawDeposit bankcardNumber(String bankcardNumber) {
        this.bankcardNumber = bankcardNumber;
        return this;
    }

    public void setBankcardNumber(String bankcardNumber) {
        this.bankcardNumber = bankcardNumber;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public WithdrawDeposit money(BigDecimal money) {
        this.money = money;
        return this;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getStatus() {
        return status;
    }

    public WithdrawDeposit status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusString() {
        return statusString;
    }

    public WithdrawDeposit statusString(String statusString) {
        this.statusString = statusString;
        return this;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public WithdrawDeposit createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public WithdrawDeposit updatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getUserid() {
        return userid;
    }

    public WithdrawDeposit userid(Long userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getDescribe() {
        return describe;
    }

    public WithdrawDeposit describe(String describe) {
        this.describe = describe;
        return this;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public WithdrawDeposit wallet(Wallet wallet) {
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
        WithdrawDeposit withdrawDeposit = (WithdrawDeposit) o;
        if (withdrawDeposit.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), withdrawDeposit.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WithdrawDeposit{" +
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
