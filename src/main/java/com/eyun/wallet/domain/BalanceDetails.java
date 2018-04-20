package com.eyun.wallet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A BalanceDetails.
 */
@Entity
@Table(name = "balance_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BalanceDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userid")
    private Long userid;

    @Column(name = "balance", precision=10, scale=2)
    private BigDecimal balance;

    @Column(name = "add_balance")
    private Boolean addBalance;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "type_string")
    private String typeString;

    @Column(name = "created_time")
    private Instant createdTime;

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

    public BalanceDetails userid(Long userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BalanceDetails balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean isAddBalance() {
        return addBalance;
    }

    public BalanceDetails addBalance(Boolean addBalance) {
        this.addBalance = addBalance;
        return this;
    }

    public void setAddBalance(Boolean addBalance) {
        this.addBalance = addBalance;
    }

    public Integer getType() {
        return type;
    }

    public BalanceDetails type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeString() {
        return typeString;
    }

    public BalanceDetails typeString(String typeString) {
        this.typeString = typeString;
        return this;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public BalanceDetails createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public BalanceDetails orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public BalanceDetails wallet(Wallet wallet) {
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
        BalanceDetails balanceDetails = (BalanceDetails) o;
        if (balanceDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), balanceDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BalanceDetails{" +
            "id=" + getId() +
            ", userid=" + getUserid() +
            ", balance=" + getBalance() +
            ", addBalance='" + isAddBalance() + "'" +
            ", type=" + getType() +
            ", typeString='" + getTypeString() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", orderNo='" + getOrderNo() + "'" +
            "}";
    }
}
