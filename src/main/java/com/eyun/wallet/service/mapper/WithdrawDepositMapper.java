package com.eyun.wallet.service.mapper;

import com.eyun.wallet.domain.*;
import com.eyun.wallet.service.dto.WithdrawDepositDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity WithdrawDeposit and its DTO WithdrawDepositDTO.
 */
@Mapper(componentModel = "spring", uses = {WalletMapper.class})
public interface WithdrawDepositMapper extends EntityMapper<WithdrawDepositDTO, WithdrawDeposit> {

    @Mapping(source = "wallet.id", target = "walletId")
    WithdrawDepositDTO toDto(WithdrawDeposit withdrawDeposit);

    @Mapping(source = "walletId", target = "wallet")
    WithdrawDeposit toEntity(WithdrawDepositDTO withdrawDepositDTO);

    default WithdrawDeposit fromId(Long id) {
        if (id == null) {
            return null;
        }
        WithdrawDeposit withdrawDeposit = new WithdrawDeposit();
        withdrawDeposit.setId(id);
        return withdrawDeposit;
    }
}
