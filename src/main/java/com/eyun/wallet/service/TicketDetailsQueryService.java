package com.eyun.wallet.service;


import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.eyun.wallet.domain.TicketDetails;
import com.eyun.wallet.domain.*; // for static metamodels
import com.eyun.wallet.repository.TicketDetailsRepository;
import com.eyun.wallet.service.dto.TicketDetailsCriteria;

import com.eyun.wallet.service.dto.TicketDetailsDTO;
import com.eyun.wallet.service.mapper.TicketDetailsMapper;

/**
 * Service for executing complex queries for TicketDetails entities in the database.
 * The main input is a {@link TicketDetailsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TicketDetailsDTO} or a {@link Page} of {@link TicketDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TicketDetailsQueryService extends QueryService<TicketDetails> {

    private final Logger log = LoggerFactory.getLogger(TicketDetailsQueryService.class);


    private final TicketDetailsRepository ticketDetailsRepository;

    private final TicketDetailsMapper ticketDetailsMapper;

    public TicketDetailsQueryService(TicketDetailsRepository ticketDetailsRepository, TicketDetailsMapper ticketDetailsMapper) {
        this.ticketDetailsRepository = ticketDetailsRepository;
        this.ticketDetailsMapper = ticketDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link TicketDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TicketDetailsDTO> findByCriteria(TicketDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<TicketDetails> specification = createSpecification(criteria);
        return ticketDetailsMapper.toDto(ticketDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TicketDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TicketDetailsDTO> findByCriteria(TicketDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<TicketDetails> specification = createSpecification(criteria);
        final Page<TicketDetails> result = ticketDetailsRepository.findAll(specification, page);
        return result.map(ticketDetailsMapper::toDto);
    }

    /**
     * Function to convert TicketDetailsCriteria to a {@link Specifications}
     */
    private Specifications<TicketDetails> createSpecification(TicketDetailsCriteria criteria) {
        Specifications<TicketDetails> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), TicketDetails_.id));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), TicketDetails_.userid));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), TicketDetails_.amount));
            }
            if (criteria.getAddAmount() != null) {
                specification = specification.and(buildSpecification(criteria.getAddAmount(), TicketDetails_.addAmount));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), TicketDetails_.type));
            }
            if (criteria.getTypeString() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypeString(), TicketDetails_.typeString));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), TicketDetails_.createdTime));
            }
            if (criteria.getTicket() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTicket(), TicketDetails_.ticket));
            }
            if (criteria.getOrderNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNo(), TicketDetails_.orderNo));
            }
            if (criteria.getWalletId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWalletId(), TicketDetails_.wallet, Wallet_.id));
            }
        }
        return specification;
    }

}
