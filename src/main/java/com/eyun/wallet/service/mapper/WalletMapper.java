package com.eyun.wallet.service.mapper;

import com.eyun.wallet.domain.*;
import com.eyun.wallet.service.dto.WalletDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Wallet and its DTO WalletDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WalletMapper extends EntityMapper<WalletDTO, Wallet> {


    @Mapping(target = "balanceDetails", ignore = true)
    @Mapping(target = "integralDetails", ignore = true)
    @Mapping(target = "ticketDetails", ignore = true)
    Wallet toEntity(WalletDTO walletDTO);

    default Wallet fromId(Long id) {
        if (id == null) {
            return null;
        }
        Wallet wallet = new Wallet();
        wallet.setId(id);
        return wallet;
    }
}
