package com.eyun.wallet.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.annotation.Version;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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

    @NotNull
    @Column(name = "userid", nullable = false)
    private Long userid;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "updated_time")
    private Instant updatedTime;

    @Version
    @Column(name = "version")
    private Integer version;

    @Column(name = "balance", precision=10, scale=2)
    private BigDecimal balance;

    @Column(name = "ticket", precision=10, scale=2)
    private BigDecimal ticket;

    @Column(name = "integral", precision=10, scale=2)
    private BigDecimal integral;

    @Column(name = "jhi_password")
    private String password;

    @OneToMany(mappedBy = "wallet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BalanceDetails> balanceDetails = new HashSet<>();

    @OneToMany(mappedBy = "wallet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<IntegralDetails> integralDetails = new HashSet<>();

    @OneToMany(mappedBy = "wallet")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<TicketDetails> ticketDetails = new HashSet<>();

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

    public Integer getVersion() {
        return version;
    }

    public Wallet version(Integer version) {
        this.version = version;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Wallet balance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getTicket() {
        return ticket;
    }

    public Wallet ticket(BigDecimal ticket) {
        this.ticket = ticket;
        return this;
    }

    public void setTicket(BigDecimal ticket) {
        this.ticket = ticket;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public Wallet integral(BigDecimal integral) {
        this.integral = integral;
        return this;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public String getPassword() {
        return password;
    }

    public Wallet password(String password) {
        this.password = password;
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<BalanceDetails> getBalanceDetails() {
        return balanceDetails;
    }

    public Wallet balanceDetails(Set<BalanceDetails> balanceDetails) {
        this.balanceDetails = balanceDetails;
        return this;
    }

    public Wallet addBalanceDetails(BalanceDetails balanceDetails) {
        this.balanceDetails.add(balanceDetails);
        balanceDetails.setWallet(this);
        return this;
    }

    public Wallet removeBalanceDetails(BalanceDetails balanceDetails) {
        this.balanceDetails.remove(balanceDetails);
        balanceDetails.setWallet(null);
        return this;
    }

    public void setBalanceDetails(Set<BalanceDetails> balanceDetails) {
        this.balanceDetails = balanceDetails;
    }

    public Set<IntegralDetails> getIntegralDetails() {
        return integralDetails;
    }

    public Wallet integralDetails(Set<IntegralDetails> integralDetails) {
        this.integralDetails = integralDetails;
        return this;
    }

    public Wallet addIntegralDetails(IntegralDetails integralDetails) {
        this.integralDetails.add(integralDetails);
        integralDetails.setWallet(this);
        return this;
    }

    public Wallet removeIntegralDetails(IntegralDetails integralDetails) {
        this.integralDetails.remove(integralDetails);
        integralDetails.setWallet(null);
        return this;
    }

    public void setIntegralDetails(Set<IntegralDetails> integralDetails) {
        this.integralDetails = integralDetails;
    }

    public Set<TicketDetails> getTicketDetails() {
        return ticketDetails;
    }

    public Wallet ticketDetails(Set<TicketDetails> ticketDetails) {
        this.ticketDetails = ticketDetails;
        return this;
    }

    public Wallet addTicketDetails(TicketDetails ticketDetails) {
        this.ticketDetails.add(ticketDetails);
        ticketDetails.setWallet(this);
        return this;
    }

    public Wallet removeTicketDetails(TicketDetails ticketDetails) {
        this.ticketDetails.remove(ticketDetails);
        ticketDetails.setWallet(null);
        return this;
    }

    public void setTicketDetails(Set<TicketDetails> ticketDetails) {
        this.ticketDetails = ticketDetails;
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
            ", userid=" + getUserid() +
            ", createTime='" + getCreateTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", version=" + getVersion() +
            ", balance=" + getBalance() +
            ", ticket=" + getTicket() +
            ", integral=" + getIntegral() +
            ", password='" + getPassword() + "'" +
            "}";
    }
    
}
