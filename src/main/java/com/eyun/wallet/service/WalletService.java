package com.eyun.wallet.service;

import com.eyun.wallet.service.dto.WalletDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Wallet.
 */
public interface WalletService {

    /**
     * Save a wallet.
     *
     * @param walletDTO the entity to save
     * @return the persisted entity
     */
    WalletDTO save(WalletDTO walletDTO);

    /**
     * Get all the wallets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WalletDTO> findAll(Pageable pageable);

    /**
     * Get the "id" wallet.
     *
     * @param id the id of the entity
     * @return the entity
     */
    WalletDTO findOne(Long id);

    /**
     * Delete the "id" wallet.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
