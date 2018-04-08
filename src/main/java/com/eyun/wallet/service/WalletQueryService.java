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

import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.domain.*; // for static metamodels
import com.eyun.wallet.repository.WalletRepository;
import com.eyun.wallet.service.dto.WalletCriteria;

import com.eyun.wallet.service.dto.WalletDTO;
import com.eyun.wallet.service.mapper.WalletMapper;

/**
 * Service for executing complex queries for Wallet entities in the database.
 * The main input is a {@link WalletCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WalletDTO} or a {@link Page} of {@link WalletDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WalletQueryService extends QueryService<Wallet> {

    private final Logger log = LoggerFactory.getLogger(WalletQueryService.class);


    private final WalletRepository walletRepository;

    private final WalletMapper walletMapper;

    public WalletQueryService(WalletRepository walletRepository, WalletMapper walletMapper) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    /**
     * Return a {@link List} of {@link WalletDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WalletDTO> findByCriteria(WalletCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Wallet> specification = createSpecification(criteria);
        return walletMapper.toDto(walletRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WalletDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WalletDTO> findByCriteria(WalletCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Wallet> specification = createSpecification(criteria);
        final Page<Wallet> result = walletRepository.findAll(specification, page);
        return result.map(walletMapper::toDto);
    }

    /**
     * Function to convert WalletCriteria to a {@link Specifications}
     */
    private Specifications<Wallet> createSpecification(WalletCriteria criteria) {
        Specifications<Wallet> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Wallet_.id));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), Wallet_.userid));
            }
            if (criteria.getCreateTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreateTime(), Wallet_.createTime));
            }
            if (criteria.getUpdatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedTime(), Wallet_.updatedTime));
            }
            if (criteria.getVersion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getVersion(), Wallet_.version));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), Wallet_.balance));
            }
            if (criteria.getTicket() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTicket(), Wallet_.ticket));
            }
            if (criteria.getIntegral() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIntegral(), Wallet_.integral));
            }
            if (criteria.getWalletDetailsId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWalletDetailsId(), Wallet_.walletDetails, WalletDetails_.id));
            }
        }
        return specification;
    }

}
