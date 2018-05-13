package com.eyun.wallet.service.mapper;

import com.eyun.wallet.domain.*;
import com.eyun.wallet.service.dto.BalanceDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BalanceDetails and its DTO BalanceDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {WalletMapper.class})
public interface BalanceDetailsMapper extends EntityMapper<BalanceDetailsDTO, BalanceDetails> {

    @Mapping(source = "wallet.id", target = "walletId")
    BalanceDetailsDTO toDto(BalanceDetails balanceDetails);

    @Mapping(source = "walletId", target = "wallet")
    BalanceDetails toEntity(BalanceDetailsDTO balanceDetailsDTO);

    default BalanceDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        BalanceDetails balanceDetails = new BalanceDetails();
        balanceDetails.setId(id);
        return balanceDetails;
    }
}
