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

import com.eyun.wallet.domain.BalanceDetails;
import com.eyun.wallet.domain.*; // for static metamodels
import com.eyun.wallet.repository.BalanceDetailsRepository;
import com.eyun.wallet.service.dto.BalanceDetailsCriteria;

import com.eyun.wallet.service.dto.BalanceDetailsDTO;
import com.eyun.wallet.service.mapper.BalanceDetailsMapper;

/**
 * Service for executing complex queries for BalanceDetails entities in the database.
 * The main input is a {@link BalanceDetailsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BalanceDetailsDTO} or a {@link Page} of {@link BalanceDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BalanceDetailsQueryService extends QueryService<BalanceDetails> {

    private final Logger log = LoggerFactory.getLogger(BalanceDetailsQueryService.class);


    private final BalanceDetailsRepository balanceDetailsRepository;

    private final BalanceDetailsMapper balanceDetailsMapper;

    public BalanceDetailsQueryService(BalanceDetailsRepository balanceDetailsRepository, BalanceDetailsMapper balanceDetailsMapper) {
        this.balanceDetailsRepository = balanceDetailsRepository;
        this.balanceDetailsMapper = balanceDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link BalanceDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BalanceDetailsDTO> findByCriteria(BalanceDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<BalanceDetails> specification = createSpecification(criteria);
        return balanceDetailsMapper.toDto(balanceDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BalanceDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BalanceDetailsDTO> findByCriteria(BalanceDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<BalanceDetails> specification = createSpecification(criteria);
        final Page<BalanceDetails> result = balanceDetailsRepository.findAll(specification, page);
        return result.map(balanceDetailsMapper::toDto);
    }

    /**
     * Function to convert BalanceDetailsCriteria to a {@link Specifications}
     */
    private Specifications<BalanceDetails> createSpecification(BalanceDetailsCriteria criteria) {
        Specifications<BalanceDetails> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BalanceDetails_.id));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), BalanceDetails_.userid));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), BalanceDetails_.amount));
            }
            if (criteria.getAddAmount() != null) {
                specification = specification.and(buildSpecification(criteria.getAddAmount(), BalanceDetails_.addAmount));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), BalanceDetails_.type));
            }
            if (criteria.getTypeString() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypeString(), BalanceDetails_.typeString));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), BalanceDetails_.createdTime));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), BalanceDetails_.balance));
            }
            if (criteria.getOrderNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNo(), BalanceDetails_.orderNo));
            }
            if (criteria.getWalletId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWalletId(), BalanceDetails_.wallet, Wallet_.id));
            }
        }
        return specification;
    }

}
