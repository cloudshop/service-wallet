package com.eyun.wallet.service.dto;


import java.time.Instant;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the IntegralDetails entity.
 */
public class IntegralDetailsDTO implements Serializable {

    private Long id;

    private Long userid;

    private BigDecimal integral;

    private Boolean addIntegral;

    private Integer type;

    private String typeString;

    private Instant createdTime;

    private String orderNo;

    private Long walletId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public Boolean isAddIntegral() {
        return addIntegral;
    }

    public void setAddIntegral(Boolean addIntegral) {
        this.addIntegral = addIntegral;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        IntegralDetailsDTO integralDetailsDTO = (IntegralDetailsDTO) o;
        if(integralDetailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), integralDetailsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "IntegralDetailsDTO{" +
            "id=" + getId() +
            ", userid=" + getUserid() +
            ", integral=" + getIntegral() +
            ", addIntegral='" + isAddIntegral() + "'" +
            ", type=" + getType() +
            ", typeString='" + getTypeString() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", orderNo='" + getOrderNo() + "'" +
            "}";
    }
}
