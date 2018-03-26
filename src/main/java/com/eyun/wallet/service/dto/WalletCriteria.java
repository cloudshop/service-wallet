package com.eyun.wallet.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

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

    private LongFilter balance;

    private LongFilter ticket;

    private LongFilter integral;

    private LongFilter userid;

    private InstantFilter createTime;

    private InstantFilter updatedTime;

    public WalletCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getBalance() {
        return balance;
    }

    public void setBalance(LongFilter balance) {
        this.balance = balance;
    }

    public LongFilter getTicket() {
        return ticket;
    }

    public void setTicket(LongFilter ticket) {
        this.ticket = ticket;
    }

    public LongFilter getIntegral() {
        return integral;
    }

    public void setIntegral(LongFilter integral) {
        this.integral = integral;
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

    @Override
    public String toString() {
        return "WalletCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (balance != null ? "balance=" + balance + ", " : "") +
                (ticket != null ? "ticket=" + ticket + ", " : "") +
                (integral != null ? "integral=" + integral + ", " : "") +
                (userid != null ? "userid=" + userid + ", " : "") +
                (createTime != null ? "createTime=" + createTime + ", " : "") +
                (updatedTime != null ? "updatedTime=" + updatedTime + ", " : "") +
            "}";
    }

}
