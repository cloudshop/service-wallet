package com.eyun.wallet.service.impl;

import com.eyun.wallet.service.TicketDetailsService;
import com.eyun.wallet.domain.TicketDetails;
import com.eyun.wallet.repository.TicketDetailsRepository;
import com.eyun.wallet.service.dto.TicketDetailsDTO;
import com.eyun.wallet.service.mapper.TicketDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing TicketDetails.
 */
@Service
@Transactional
public class TicketDetailsServiceImpl implements TicketDetailsService {

    private final Logger log = LoggerFactory.getLogger(TicketDetailsServiceImpl.class);

    private final TicketDetailsRepository ticketDetailsRepository;

    private final TicketDetailsMapper ticketDetailsMapper;

    public TicketDetailsServiceImpl(TicketDetailsRepository ticketDetailsRepository, TicketDetailsMapper ticketDetailsMapper) {
        this.ticketDetailsRepository = ticketDetailsRepository;
        this.ticketDetailsMapper = ticketDetailsMapper;
    }

    /**
     * Save a ticketDetails.
     *
     * @param ticketDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TicketDetailsDTO save(TicketDetailsDTO ticketDetailsDTO) {
        log.debug("Request to save TicketDetails : {}", ticketDetailsDTO);
        TicketDetails ticketDetails = ticketDetailsMapper.toEntity(ticketDetailsDTO);
        ticketDetails = ticketDetailsRepository.save(ticketDetails);
        return ticketDetailsMapper.toDto(ticketDetails);
    }

    /**
     * Get all the ticketDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TicketDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all TicketDetails");
        return ticketDetailsRepository.findAll(pageable)
            .map(ticketDetailsMapper::toDto);
    }

    /**
     * Get one ticketDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public TicketDetailsDTO findOne(Long id) {
        log.debug("Request to get TicketDetails : {}", id);
        TicketDetails ticketDetails = ticketDetailsRepository.findOne(id);
        return ticketDetailsMapper.toDto(ticketDetails);
    }

    /**
     * Delete the ticketDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete TicketDetails : {}", id);
        ticketDetailsRepository.delete(id);
    }
}
