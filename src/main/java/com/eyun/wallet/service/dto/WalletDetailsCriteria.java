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
 * Criteria class for the WalletDetails entity. This class is used in WalletDetailsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /wallet-details?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WalletDetailsCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter userid;

    private LongFilter walletId;

    private LongFilter amount;

    private IntegerFilter type;

    private LongFilter balance;

    private LongFilter ticket;

    private LongFilter integral;

    private LongFilter payPrice;

    private LongFilter orderId;

    private InstantFilter createdTime;

    public WalletDetailsCriteria() {
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

    public LongFilter getWalletId() {
        return walletId;
    }

    public void setWalletId(LongFilter walletId) {
        this.walletId = walletId;
    }

    public LongFilter getAmount() {
        return amount;
    }

    public void setAmount(LongFilter amount) {
        this.amount = amount;
    }

    public IntegerFilter getType() {
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
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

    public LongFilter getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(LongFilter payPrice) {
        this.payPrice = payPrice;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public InstantFilter getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(InstantFilter createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "WalletDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userid != null ? "userid=" + userid + ", " : "") +
                (walletId != null ? "walletId=" + walletId + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (balance != null ? "balance=" + balance + ", " : "") +
                (ticket != null ? "ticket=" + ticket + ", " : "") +
                (integral != null ? "integral=" + integral + ", " : "") +
                (payPrice != null ? "payPrice=" + payPrice + ", " : "") +
                (orderId != null ? "orderId=" + orderId + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
            "}";
    }

}
