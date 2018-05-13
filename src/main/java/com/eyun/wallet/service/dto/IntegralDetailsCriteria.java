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
 * Criteria class for the IntegralDetails entity. This class is used in IntegralDetailsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /integral-details?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class IntegralDetailsCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private LongFilter userid;

    private BigDecimalFilter integral;

    private BooleanFilter addIntegral;

    private IntegerFilter type;

    private StringFilter typeString;

    private InstantFilter createdTime;

    private StringFilter orderNo;

    private LongFilter walletId;

    public IntegralDetailsCriteria() {
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

    public BigDecimalFilter getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimalFilter integral) {
        this.integral = integral;
    }

    public BooleanFilter getAddIntegral() {
        return addIntegral;
    }

    public void setAddIntegral(BooleanFilter addIntegral) {
        this.addIntegral = addIntegral;
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
        return "IntegralDetailsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userid != null ? "userid=" + userid + ", " : "") +
                (integral != null ? "integral=" + integral + ", " : "") +
                (addIntegral != null ? "addIntegral=" + addIntegral + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (typeString != null ? "typeString=" + typeString + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
                (orderNo != null ? "orderNo=" + orderNo + ", " : "") +
                (walletId != null ? "walletId=" + walletId + ", " : "") +
            "}";
    }

}
