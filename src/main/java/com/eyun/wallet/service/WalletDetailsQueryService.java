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

import com.eyun.wallet.domain.WalletDetails;
import com.eyun.wallet.domain.*; // for static metamodels
import com.eyun.wallet.repository.WalletDetailsRepository;
import com.eyun.wallet.service.dto.WalletDetailsCriteria;

import com.eyun.wallet.service.dto.WalletDetailsDTO;
import com.eyun.wallet.service.mapper.WalletDetailsMapper;

/**
 * Service for executing complex queries for WalletDetails entities in the database.
 * The main input is a {@link WalletDetailsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WalletDetailsDTO} or a {@link Page} of {@link WalletDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WalletDetailsQueryService extends QueryService<WalletDetails> {

    private final Logger log = LoggerFactory.getLogger(WalletDetailsQueryService.class);


    private final WalletDetailsRepository walletDetailsRepository;

    private final WalletDetailsMapper walletDetailsMapper;

    public WalletDetailsQueryService(WalletDetailsRepository walletDetailsRepository, WalletDetailsMapper walletDetailsMapper) {
        this.walletDetailsRepository = walletDetailsRepository;
        this.walletDetailsMapper = walletDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link WalletDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WalletDetailsDTO> findByCriteria(WalletDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<WalletDetails> specification = createSpecification(criteria);
        return walletDetailsMapper.toDto(walletDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WalletDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WalletDetailsDTO> findByCriteria(WalletDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<WalletDetails> specification = createSpecification(criteria);
        final Page<WalletDetails> result = walletDetailsRepository.findAll(specification, page);
        return result.map(walletDetailsMapper::toDto);
    }

    /**
     * Function to convert WalletDetailsCriteria to a {@link Specifications}
     */
    private Specifications<WalletDetails> createSpecification(WalletDetailsCriteria criteria) {
        Specifications<WalletDetails> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WalletDetails_.id));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), WalletDetails_.userid));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), WalletDetails_.amount));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), WalletDetails_.type));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), WalletDetails_.createdTime));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), WalletDetails_.balance));
            }
            if (criteria.getTicket() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTicket(), WalletDetails_.ticket));
            }
            if (criteria.getIntegral() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIntegral(), WalletDetails_.integral));
            }
            if (criteria.getPay_price() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPay_price(), WalletDetails_.pay_price));
            }
            if (criteria.getOrderNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNo(), WalletDetails_.orderNo));
            }
            if (criteria.getWalletId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWalletId(), WalletDetails_.wallet, Wallet_.id));
            }
        }
        return specification;
    }

}
