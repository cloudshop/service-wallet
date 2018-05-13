package com.eyun.wallet.service;

import com.eyun.wallet.service.dto.BalanceDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing BalanceDetails.
 */
public interface BalanceDetailsService {

    /**
     * Save a balanceDetails.
     *
     * @param balanceDetailsDTO the entity to save
     * @return the persisted entity
     */
    BalanceDetailsDTO save(BalanceDetailsDTO balanceDetailsDTO);

    /**
     * Get all the balanceDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<BalanceDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" balanceDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    BalanceDetailsDTO findOne(Long id);

    /**
     * Delete the "id" balanceDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
