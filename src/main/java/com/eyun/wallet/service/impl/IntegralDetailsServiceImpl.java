package com.eyun.wallet.service.impl;

import com.eyun.wallet.service.IntegralDetailsService;
import com.eyun.wallet.domain.IntegralDetails;
import com.eyun.wallet.repository.IntegralDetailsRepository;
import com.eyun.wallet.service.dto.IntegralDetailsDTO;
import com.eyun.wallet.service.mapper.IntegralDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing IntegralDetails.
 */
@Service
@Transactional
public class IntegralDetailsServiceImpl implements IntegralDetailsService {

    private final Logger log = LoggerFactory.getLogger(IntegralDetailsServiceImpl.class);

    private final IntegralDetailsRepository integralDetailsRepository;

    private final IntegralDetailsMapper integralDetailsMapper;

    public IntegralDetailsServiceImpl(IntegralDetailsRepository integralDetailsRepository, IntegralDetailsMapper integralDetailsMapper) {
        this.integralDetailsRepository = integralDetailsRepository;
        this.integralDetailsMapper = integralDetailsMapper;
    }

    /**
     * Save a integralDetails.
     *
     * @param integralDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public IntegralDetailsDTO save(IntegralDetailsDTO integralDetailsDTO) {
        log.debug("Request to save IntegralDetails : {}", integralDetailsDTO);
        IntegralDetails integralDetails = integralDetailsMapper.toEntity(integralDetailsDTO);
        integralDetails = integralDetailsRepository.save(integralDetails);
        return integralDetailsMapper.toDto(integralDetails);
    }

    /**
     * Get all the integralDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<IntegralDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IntegralDetails");
        return integralDetailsRepository.findAll(pageable)
            .map(integralDetailsMapper::toDto);
    }

    /**
     * Get one integralDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public IntegralDetailsDTO findOne(Long id) {
        log.debug("Request to get IntegralDetails : {}", id);
        IntegralDetails integralDetails = integralDetailsRepository.findOne(id);
        return integralDetailsMapper.toDto(integralDetails);
    }

    /**
     * Delete the integralDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete IntegralDetails : {}", id);
        integralDetailsRepository.delete(id);
    }
}
