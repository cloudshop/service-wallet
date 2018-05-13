package com.eyun.wallet.service.mapper;

import com.eyun.wallet.domain.*;
import com.eyun.wallet.service.dto.PayOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PayOrder and its DTO PayOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {WalletMapper.class})
public interface PayOrderMapper extends EntityMapper<PayOrderDTO, PayOrder> {

    @Mapping(source = "wallet.id", target = "walletId")
    PayOrderDTO toDto(PayOrder payOrder);

    @Mapping(source = "walletId", target = "wallet")
    PayOrder toEntity(PayOrderDTO payOrderDTO);

    default PayOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        PayOrder payOrder = new PayOrder();
        payOrder.setId(id);
        return payOrder;
    }
}
