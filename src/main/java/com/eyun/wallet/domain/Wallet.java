package com.eyun.wallet.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Wallet.
 */
@Entity
@Table(name = "wallet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Wallet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "ticket")
    private Long ticket;

    @Column(name = "integral")
    private Long integral;

    @NotNull
    @Column(name = "userid", nullable = false)
    private Long userid;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "updated_time")
    private Instant updatedTime;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBalance() {
        return balance;
    }

    public Wallet balance(Long balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getTicket() {
        return ticket;
    }

    public Wallet ticket(Long ticket) {
        this.ticket = ticket;
        return this;
    }

    public void setTicket(Long ticket) {
        this.ticket = ticket;
    }

    public Long getIntegral() {
        return integral;
    }

    public Wallet integral(Long integral) {
        this.integral = integral;
        return this;
    }

    public void setIntegral(Long integral) {
        this.integral = integral;
    }

    public Long getUserid() {
        return userid;
    }

    public Wallet userid(Long userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public Wallet createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public Wallet updatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
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
        Wallet wallet = (Wallet) o;
        if (wallet.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), wallet.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Wallet{" +
            "id=" + getId() +
            ", balance=" + getBalance() +
            ", ticket=" + getTicket() +
            ", integral=" + getIntegral() +
            ", userid=" + getUserid() +
            ", createTime='" + getCreateTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            "}";
    }
}
