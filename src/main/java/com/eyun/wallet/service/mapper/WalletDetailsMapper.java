package com.eyun.wallet.service.mapper;

import com.eyun.wallet.domain.*;
import com.eyun.wallet.service.dto.WalletDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WalletDetails and its DTO WalletDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WalletDetailsMapper extends EntityMapper<WalletDetailsDTO, WalletDetails> {



    default WalletDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        WalletDetails walletDetails = new WalletDetails();
        walletDetails.setId(id);
        return walletDetails;
    }
}
