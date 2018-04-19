package com.eyun.wallet.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Wallet entity. This class is used in WalletResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /wallets?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WalletCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter userid;

    private InstantFilter createTime;

    private InstantFilter updatedTime;

    private IntegerFilter version;

    private BigDecimalFilter balance;

    private BigDecimalFilter ticket;

    private BigDecimalFilter integral;

    private StringFilter password;

    private LongFilter balanceDetailsId;

    private LongFilter integralDetailsId;

    private LongFilter ticketDetailsId;

    public WalletCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getUserid() {
        return userid;
    }

    public void setUserid(LongFilter userid) {
        this.userid = userid;
    }

    public InstantFilter getCreateTime() {
        return createTime;
    }

    public void setCreateTime(InstantFilter createTime) {
        this.createTime = createTime;
    }

    public InstantFilter getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(InstantFilter updatedTime) {
        this.updatedTime = updatedTime;
    }

    public IntegerFilter getVersion() {
        return version;
    }

    public void setVersion(IntegerFilter version) {
        this.version = version;
    }

    public BigDecimalFilter getBalance() {
        return balance;
    }

    public void setBalance(BigDecimalFilter balance) {
        this.balance = balance;
    }

    public BigDecimalFilter getTicket() {
        return ticket;
    }

    public void setTicket(BigDecimalFilter ticket) {
        this.ticket = ticket;
    }

    public BigDecimalFilter getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimalFilter integral) {
        this.integral = integral;
    }

    public StringFilter getPassword() {
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public LongFilter getBalanceDetailsId() {
        return balanceDetailsId;
    }

    public void setBalanceDetailsId(LongFilter balanceDetailsId) {
        this.balanceDetailsId = balanceDetailsId;
    }

    public LongFilter getIntegralDetailsId() {
        return integralDetailsId;
    }

    public void setIntegralDetailsId(LongFilter integralDetailsId) {
        this.integralDetailsId = integralDetailsId;
    }

    public LongFilter getTicketDetailsId() {
        return ticketDetailsId;
    }

    public void setTicketDetailsId(LongFilter ticketDetailsId) {
        this.ticketDetailsId = ticketDetailsId;
    }

    @Override
    public String toString() {
        return "WalletCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userid != null ? "userid=" + userid + ", " : "") +
                (createTime != null ? "createTime=" + createTime + ", " : "") +
                (updatedTime != null ? "updatedTime=" + updatedTime + ", " : "") +
                (version != null ? "version=" + version + ", " : "") +
                (balance != null ? "balance=" + balance + ", " : "") +
                (ticket != null ? "ticket=" + ticket + ", " : "") +
                (integral != null ? "integral=" + integral + ", " : "") +
                (password != null ? "password=" + password + ", " : "") +
                (balanceDetailsId != null ? "balanceDetailsId=" + balanceDetailsId + ", " : "") +
                (integralDetailsId != null ? "integralDetailsId=" + integralDetailsId + ", " : "") +
                (ticketDetailsId != null ? "ticketDetailsId=" + ticketDetailsId + ", " : "") +
            "}";
    }

}
