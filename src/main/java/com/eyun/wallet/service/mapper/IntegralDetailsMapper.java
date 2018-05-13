package com.eyun.wallet.service.mapper;

import com.eyun.wallet.domain.*;
import com.eyun.wallet.service.dto.IntegralDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity IntegralDetails and its DTO IntegralDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {WalletMapper.class})
public interface IntegralDetailsMapper extends EntityMapper<IntegralDetailsDTO, IntegralDetails> {

    @Mapping(source = "wallet.id", target = "walletId")
    IntegralDetailsDTO toDto(IntegralDetails integralDetails);

    @Mapping(source = "walletId", target = "wallet")
    IntegralDetails toEntity(IntegralDetailsDTO integralDetailsDTO);

    default IntegralDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        IntegralDetails integralDetails = new IntegralDetails();
        integralDetails.setId(id);
        return integralDetails;
    }
}
