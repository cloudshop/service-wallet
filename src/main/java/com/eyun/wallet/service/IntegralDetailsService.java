package com.eyun.wallet.service;

import com.eyun.wallet.service.dto.IntegralDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * Service Interface for managing IntegralDetails.
 */
public interface IntegralDetailsService {

    /**
     * Save a integralDetails.
     *
     * @param integralDetailsDTO the entity to save
     * @return the persisted entity
     */
    IntegralDetailsDTO save(IntegralDetailsDTO integralDetailsDTO);

    /**
     * Get all the integralDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<IntegralDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" integralDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    IntegralDetailsDTO findOne(Long id);

    /**
     * Delete the "id" integralDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    BigDecimal integralStatistical(Long userId);



}
