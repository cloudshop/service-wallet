package com.eyun.wallet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A IntegralDetails.
 */
@Entity
@Table(name = "integral_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class IntegralDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userid")
    private Long userid;

    @Column(name = "amount")
    private Long amount;

    @Column(name = "add_amount")
    private Boolean addAmount;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "type_string")
    private String typeString;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "integral", precision=10, scale=2)
    private BigDecimal integral;

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

    public IntegralDetails userid(Long userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getAmount() {
        return amount;
    }

    public IntegralDetails amount(Long amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Boolean isAddAmount() {
        return addAmount;
    }

    public IntegralDetails addAmount(Boolean addAmount) {
        this.addAmount = addAmount;
        return this;
    }

    public void setAddAmount(Boolean addAmount) {
        this.addAmount = addAmount;
    }

    public Integer getType() {
        return type;
    }

    public IntegralDetails type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeString() {
        return typeString;
    }

    public IntegralDetails typeString(String typeString) {
        this.typeString = typeString;
        return this;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public IntegralDetails createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public IntegralDetails integral(BigDecimal integral) {
        this.integral = integral;
        return this;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public IntegralDetails orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public IntegralDetails wallet(Wallet wallet) {
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
        IntegralDetails integralDetails = (IntegralDetails) o;
        if (integralDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), integralDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IntegralDetails{" +
            "id=" + getId() +
            ", userid=" + getUserid() +
            ", amount=" + getAmount() +
            ", addAmount='" + isAddAmount() + "'" +
            ", type=" + getType() +
            ", typeString='" + getTypeString() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", integral=" + getIntegral() +
            ", orderNo='" + getOrderNo() + "'" +
            "}";
    }
}
