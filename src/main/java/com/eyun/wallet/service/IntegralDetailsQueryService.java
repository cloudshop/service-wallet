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

import com.eyun.wallet.domain.IntegralDetails;
import com.eyun.wallet.domain.*; // for static metamodels
import com.eyun.wallet.repository.IntegralDetailsRepository;
import com.eyun.wallet.service.dto.IntegralDetailsCriteria;

import com.eyun.wallet.service.dto.IntegralDetailsDTO;
import com.eyun.wallet.service.mapper.IntegralDetailsMapper;

/**
 * Service for executing complex queries for IntegralDetails entities in the database.
 * The main input is a {@link IntegralDetailsCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IntegralDetailsDTO} or a {@link Page} of {@link IntegralDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IntegralDetailsQueryService extends QueryService<IntegralDetails> {

    private final Logger log = LoggerFactory.getLogger(IntegralDetailsQueryService.class);


    private final IntegralDetailsRepository integralDetailsRepository;

    private final IntegralDetailsMapper integralDetailsMapper;

    public IntegralDetailsQueryService(IntegralDetailsRepository integralDetailsRepository, IntegralDetailsMapper integralDetailsMapper) {
        this.integralDetailsRepository = integralDetailsRepository;
        this.integralDetailsMapper = integralDetailsMapper;
    }

    /**
     * Return a {@link List} of {@link IntegralDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IntegralDetailsDTO> findByCriteria(IntegralDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<IntegralDetails> specification = createSpecification(criteria);
        return integralDetailsMapper.toDto(integralDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IntegralDetailsDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IntegralDetailsDTO> findByCriteria(IntegralDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<IntegralDetails> specification = createSpecification(criteria);
        final Page<IntegralDetails> result = integralDetailsRepository.findAll(specification, page);
        return result.map(integralDetailsMapper::toDto);
    }

    /**
     * Function to convert IntegralDetailsCriteria to a {@link Specifications}
     */
    private Specifications<IntegralDetails> createSpecification(IntegralDetailsCriteria criteria) {
        Specifications<IntegralDetails> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), IntegralDetails_.id));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), IntegralDetails_.userid));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), IntegralDetails_.amount));
            }
            if (criteria.getAddAmount() != null) {
                specification = specification.and(buildSpecification(criteria.getAddAmount(), IntegralDetails_.addAmount));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), IntegralDetails_.type));
            }
            if (criteria.getTypeString() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTypeString(), IntegralDetails_.typeString));
            }
            if (criteria.getCreatedTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedTime(), IntegralDetails_.createdTime));
            }
            if (criteria.getIntegral() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIntegral(), IntegralDetails_.integral));
            }
            if (criteria.getOrderNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNo(), IntegralDetails_.orderNo));
            }
            if (criteria.getWalletId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWalletId(), IntegralDetails_.wallet, Wallet_.id));
            }
        }
        return specification;
    }

}
