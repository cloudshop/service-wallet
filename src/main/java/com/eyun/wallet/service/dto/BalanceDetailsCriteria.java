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
 * Criteria class for the BalanceDetails entity. This class is used in BalanceDetailsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /balance-details?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BalanceDetailsCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter userid;

    private LongFilter amount;

    private BooleanFilter addAmount;

    private IntegerFilter type;

    private StringFilter typeString;

    private InstantFilter createdTime;

    private BigDecimalFilter balance;

    private StringFilter orderNo;

    private LongFilter walletId;

    public BalanceDetailsCriteria() {
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

    public LongFilter getAmount() {
        return amount;
    }

    public void setAmount(LongFilter amount) {
        this.amount = amount;
    }

    public BooleanFilter getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(BooleanFilter addAmount) {
        this.addAmount = addAmount;
    }

    public IntegerFilter getType() {
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
    }

    public StringFilter getTypeString() {
        return typeString;
    }

    public void setTypeString(StringFilter typeString) {
        this.typeString = typeString;
    }

    public InstantFilter getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(InstantFilter createdTime) {
        this.createdTime = createdTime;
    }

    public BigDecimalFilter getBalance() {
        return balance;
    }

    public void setBalance(BigDecimalFilter balance) {
        this.balance = balance;
    }

    public StringFilter getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(StringFilter orderNo) {
        this.orderNo = orderNo;
    }

    public LongFilter getWalletId() {
        return walletId;
    }

    public void setWalletId(LongFilter walletId) {
        this.walletId = walletId;
    }

    @Override
    public String toString() {
        return "BalanceDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userid != null ? "userid=" + userid + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (addAmount != null ? "addAmount=" + addAmount + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (typeString != null ? "typeString=" + typeString + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
                (balance != null ? "balance=" + balance + ", " : "") +
                (orderNo != null ? "orderNo=" + orderNo + ", " : "") +
                (walletId != null ? "walletId=" + walletId + ", " : "") +
            "}";
    }

}
