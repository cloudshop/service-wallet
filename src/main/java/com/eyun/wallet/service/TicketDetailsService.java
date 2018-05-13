package com.eyun.wallet.service;

import com.eyun.wallet.service.dto.TicketDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing TicketDetails.
 */
public interface TicketDetailsService {

    /**
     * Save a ticketDetails.
     *
     * @param ticketDetailsDTO the entity to save
     * @return the persisted entity
     */
    TicketDetailsDTO save(TicketDetailsDTO ticketDetailsDTO);

    /**
     * Get all the ticketDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TicketDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" ticketDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    TicketDetailsDTO findOne(Long id);

    /**
     * Delete the "id" ticketDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
