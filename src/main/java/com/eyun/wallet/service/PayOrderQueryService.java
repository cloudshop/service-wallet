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

import com.eyun.wallet.domain.PayOrder;
import com.eyun.wallet.domain.*; // for static metamodels
import com.eyun.wallet.repository.PayOrderRepository;
import com.eyun.wallet.service.dto.PayOrderCriteria;

import com.eyun.wallet.service.dto.PayOrderDTO;
import com.eyun.wallet.service.mapper.PayOrderMapper;

/**
 * Service for executing complex queries for PayOrder entities in the database.
 * The main input is a {@link PayOrderCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PayOrderDTO} or a {@link Page} of {@link PayOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PayOrderQueryService extends QueryService<PayOrder> {

    private final Logger log = LoggerFactory.getLogger(PayOrderQueryService.class);


    private final PayOrderRepository payOrderRepository;

    private final PayOrderMapper payOrderMapper;

    public PayOrderQueryService(PayOrderRepository payOrderRepository, PayOrderMapper payOrderMapper) {
        this.payOrderRepository = payOrderRepository;
        this.payOrderMapper = payOrderMapper;
    }

    /**
     * Return a {@link List} of {@link PayOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PayOrderDTO> findByCriteria(PayOrderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<PayOrder> specification = createSpecification(criteria);
        return payOrderMapper.toDto(payOrderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PayOrderDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PayOrderDTO> findByCriteria(PayOrderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<PayOrder> specification = createSpecification(criteria);
        final Page<PayOrder> result = payOrderRepository.findAll(specification, page);
        return result.map(payOrderMapper::toDto);
    }

    /**
     * Function to convert PayOrderCriteria to a {@link Specifications}
     */
    private Specifications<PayOrder> createSpecification(PayOrderCriteria criteria) {
        Specifications<PayOrder> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), PayOrder_.id));
            }
            if (criteria.getUserid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUserid(), PayOrder_.userid));
            }
            if (criteria.getPayNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPayNo(), PayOrder_.payNo));
            }
            if (criteria.getOrderNo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrderNo(), PayOrder_.orderNo));
            }
            if (criteria.getBalance() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBalance(), PayOrder_.balance));
            }
            if (criteria.getTicket() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTicket(), PayOrder_.ticket));
            }
            if (criteria.getIntegral() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIntegral(), PayOrder_.integral));
            }
            if (criteria.getPayTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayTime(), PayOrder_.payTime));
            }
            if (criteria.getWalletId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getWalletId(), PayOrder_.wallet, Wallet_.id));
            }
        }
        return specification;
    }

}
