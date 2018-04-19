package com.eyun.wallet.service.impl;

import com.eyun.wallet.service.BalanceDetailsService;
import com.eyun.wallet.domain.BalanceDetails;
import com.eyun.wallet.repository.BalanceDetailsRepository;
import com.eyun.wallet.service.dto.BalanceDetailsDTO;
import com.eyun.wallet.service.mapper.BalanceDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing BalanceDetails.
 */
@Service
@Transactional
public class BalanceDetailsServiceImpl implements BalanceDetailsService {

    private final Logger log = LoggerFactory.getLogger(BalanceDetailsServiceImpl.class);

    private final BalanceDetailsRepository balanceDetailsRepository;

    private final BalanceDetailsMapper balanceDetailsMapper;

    public BalanceDetailsServiceImpl(BalanceDetailsRepository balanceDetailsRepository, BalanceDetailsMapper balanceDetailsMapper) {
        this.balanceDetailsRepository = balanceDetailsRepository;
        this.balanceDetailsMapper = balanceDetailsMapper;
    }

    /**
     * Save a balanceDetails.
     *
     * @param balanceDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public BalanceDetailsDTO save(BalanceDetailsDTO balanceDetailsDTO) {
        log.debug("Request to save BalanceDetails : {}", balanceDetailsDTO);
        BalanceDetails balanceDetails = balanceDetailsMapper.toEntity(balanceDetailsDTO);
        balanceDetails = balanceDetailsRepository.save(balanceDetails);
        return balanceDetailsMapper.toDto(balanceDetails);
    }

    /**
     * Get all the balanceDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BalanceDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BalanceDetails");
        return balanceDetailsRepository.findAll(pageable)
            .map(balanceDetailsMapper::toDto);
    }

    /**
     * Get one balanceDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public BalanceDetailsDTO findOne(Long id) {
        log.debug("Request to get BalanceDetails : {}", id);
        BalanceDetails balanceDetails = balanceDetailsRepository.findOne(id);
        return balanceDetailsMapper.toDto(balanceDetails);
    }

    /**
     * Delete the balanceDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BalanceDetails : {}", id);
        balanceDetailsRepository.delete(id);
    }
}
