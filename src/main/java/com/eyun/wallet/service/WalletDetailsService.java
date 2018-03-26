package com.eyun.wallet.service;

import com.eyun.wallet.service.dto.WalletDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing WalletDetails.
 */
public interface WalletDetailsService {

    /**
     * Save a walletDetails.
     *
     * @param walletDetailsDTO the entity to save
     * @return the persisted entity
     */
    WalletDetailsDTO save(WalletDetailsDTO walletDetailsDTO);

    /**
     * Get all the walletDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WalletDetailsDTO> findAll(Pageable pageable);

    /**
     * Get the "id" walletDetails.
     *
     * @param id the id of the entity
     * @return the entity
     */
    WalletDetailsDTO findOne(Long id);

    /**
     * Delete the "id" walletDetails.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
