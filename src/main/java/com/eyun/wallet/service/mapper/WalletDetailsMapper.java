package com.eyun.wallet.service.mapper;

import com.eyun.wallet.domain.*;
import com.eyun.wallet.service.dto.WalletDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WalletDetails and its DTO WalletDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {WalletMapper.class})
public interface WalletDetailsMapper extends EntityMapper<WalletDetailsDTO, WalletDetails> {

    @Mapping(source = "wallet.id", target = "walletId")
    WalletDetailsDTO toDto(WalletDetails walletDetails);

    @Mapping(source = "walletId", target = "wallet")
    WalletDetails toEntity(WalletDetailsDTO walletDetailsDTO);

    default WalletDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        WalletDetails walletDetails = new WalletDetails();
        walletDetails.setId(id);
        return walletDetails;
    }
}
