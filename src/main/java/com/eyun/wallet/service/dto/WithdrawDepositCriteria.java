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
 * Criteria class for the WithdrawDeposit entity. This class is used in WithdrawDepositResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /withdraw-deposits?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WithdrawDepositCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter cardholder;

    private StringFilter openingBank;

    private StringFilter bankcardNumber;

    private BigDecimalFilter money;

    private IntegerFilter status;

    private StringFilter statusString;

    private InstantFilter createdTime;

    private InstantFilter updatedTime;

    private LongFilter userid;

    private LongFilter walletId;

    public WithdrawDepositCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCardholder() {
        return cardholder;
    }

    public void setCardholder(StringFilter cardholder) {
        this.cardholder = cardholder;
    }

    public StringFilter getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(StringFilter openingBank) {
        this.openingBank = openingBank;
    }

    public StringFilter getBankcardNumber() {
        return bankcardNumber;
    }

    public void setBankcardNumber(StringFilter bankcardNumber) {
        this.bankcardNumber = bankcardNumber;
    }

    public BigDecimalFilter getMoney() {
        return money;
    }

    public void setMoney(BigDecimalFilter money) {
        this.money = money;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public StringFilter getStatusString() {
        return statusString;
    }

    public void setStatusString(StringFilter statusString) {
        this.statusString = statusString;
    }

    public InstantFilter getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(InstantFilter createdTime) {
        this.createdTime = createdTime;
    }

    public InstantFilter getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(InstantFilter updatedTime) {
        this.updatedTime = updatedTime;
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

    @Override
    public String toString() {
        return "WithdrawDepositCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (cardholder != null ? "cardholder=" + cardholder + ", " : "") +
                (openingBank != null ? "openingBank=" + openingBank + ", " : "") +
                (bankcardNumber != null ? "bankcardNumber=" + bankcardNumber + ", " : "") +
                (money != null ? "money=" + money + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (statusString != null ? "statusString=" + statusString + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
                (updatedTime != null ? "updatedTime=" + updatedTime + ", " : "") +
                (userid != null ? "userid=" + userid + ", " : "") +
                (walletId != null ? "walletId=" + walletId + ", " : "") +
            "}";
    }

}
