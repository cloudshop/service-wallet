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

import com.eyun.wallet.domain.WithdrawDeposit;
import com.eyun.wallet.domain.*; // for static metamodels
import com.eyun.wallet.repository.WithdrawDepositRepository;
import com.eyun.wallet.service.dto.WithdrawDepositCriteria;

import com.eyun.wallet.service.dto.WithdrawDepositDTO;
import com.eyun.wallet.service.mapper.WithdrawDepositMapper;

/**
 * Service for executing complex queries for WithdrawDeposit entities in the database.
 * The main input is a {@link WithdrawDepositCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WithdrawDepositDTO} or a {@link Page} of {@link WithdrawDepositDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WithdrawDepositQueryService extends QueryService<WithdrawDeposit> {

    private final Logger log = LoggerFactory.getLogger(WithdrawDepositQueryService.class);


    private final WithdrawDepositRepository withdrawDepositRepository;

    private final WithdrawDepositMapper withdrawDepositMapper;

    public WithdrawDepositQueryService(WithdrawDepositRepository withdrawDepositRepository, WithdrawDepositMapper withdrawDepositMapper) {
        this.withdrawDepositRepository = withdrawDepositRepository;
        this.withdrawDepositMapper = withdrawDepositMapper;
    }

    /**
     * Return a {@link List} of {@link WithdrawDepositDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WithdrawDepositDTO> findByCriteria(WithdrawDepositCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<WithdrawDeposit> specification = createSpecification(criteria);
        return withdrawDepositMapper.toDto(withdrawDepositRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WithdrawDepositDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WithdrawDepositDTO> findByCriteria(WithdrawDepositCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<WithdrawDeposit> specification = createSpecification(criteria);
        final Page<WithdrawDeposit> result = withdrawDepositRepository.findAll(specification, page);
        return result.map(withdrawDepositMapper::toDto);
    }

    /**
     * Function to convert WithdrawDepositCriteria to a {@link Specifications}
     */
    private Specifications<WithdrawDeposit> createSpecification(WithdrawDepositCriteria criteria) {
        Specifications<WithdrawDeposit> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), WithdrawDeposit_.id));
            }
            if (criteria.getCardholder() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCardholder(), WithdrawDeposit_.cardholder));
            }
            if (criteria.getOpeningBank() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOpeningBank(), WithdrawDeposit_.openingBank));
            }
            if (criteria.getBankcardNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankcardNumber(), WithdrawDeposit_.bankcardNumber));
            }
            if (criteria.getMoney() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMoney(), WithdrawDeposit_.money));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStatus(), WithdrawDeposit_.status));
            }
            if (criteria.getStatusString() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatusString(), WithdrawDeposit_.statusString));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), WithdrawDeposit_.createdTime));
            }
            if (criteria.getUpdatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUpdatedTime(), WithdrawDeposit_.updatedTime));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), WithdrawDeposit_.userid));
            }
            if (criteria.getWalletId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWalletId(), WithdrawDeposit_.wallet, Wallet_.id));
            }
        }
        return specification;
    }

}
