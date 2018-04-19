package com.eyun.wallet.service.mapper;

import com.eyun.wallet.domain.*;
import com.eyun.wallet.service.dto.TicketDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TicketDetails and its DTO TicketDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {WalletMapper.class})
public interface TicketDetailsMapper extends EntityMapper<TicketDetailsDTO, TicketDetails> {

    @Mapping(source = "wallet.id", target = "walletId")
    TicketDetailsDTO toDto(TicketDetails ticketDetails);

    @Mapping(source = "walletId", target = "wallet")
    TicketDetails toEntity(TicketDetailsDTO ticketDetailsDTO);

    default TicketDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        TicketDetails ticketDetails = new TicketDetails();
        ticketDetails.setId(id);
        return ticketDetails;
    }
}
