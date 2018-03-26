package com.eyun.wallet.service.impl;

import com.eyun.wallet.service.WalletDetailsService;
import com.eyun.wallet.domain.WalletDetails;
import com.eyun.wallet.repository.WalletDetailsRepository;
import com.eyun.wallet.service.dto.WalletDetailsDTO;
import com.eyun.wallet.service.mapper.WalletDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing WalletDetails.
 */
@Service
@Transactional
public class WalletDetailsServiceImpl implements WalletDetailsService {

    private final Logger log = LoggerFactory.getLogger(WalletDetailsServiceImpl.class);

    private final WalletDetailsRepository walletDetailsRepository;

    private final WalletDetailsMapper walletDetailsMapper;

    public WalletDetailsServiceImpl(WalletDetailsRepository walletDetailsRepository, WalletDetailsMapper walletDetailsMapper) {
        this.walletDetailsRepository = walletDetailsRepository;
        this.walletDetailsMapper = walletDetailsMapper;
    }

    /**
     * Save a walletDetails.
     *
     * @param walletDetailsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WalletDetailsDTO save(WalletDetailsDTO walletDetailsDTO) {
        log.debug("Request to save WalletDetails : {}", walletDetailsDTO);
        WalletDetails walletDetails = walletDetailsMapper.toEntity(walletDetailsDTO);
        walletDetails = walletDetailsRepository.save(walletDetails);
        return walletDetailsMapper.toDto(walletDetails);
    }

    /**
     * Get all the walletDetails.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WalletDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WalletDetails");
        return walletDetailsRepository.findAll(pageable)
            .map(walletDetailsMapper::toDto);
    }

    /**
     * Get one walletDetails by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WalletDetailsDTO findOne(Long id) {
        log.debug("Request to get WalletDetails : {}", id);
        WalletDetails walletDetails = walletDetailsRepository.findOne(id);
        return walletDetailsMapper.toDto(walletDetails);
    }

    /**
     * Delete the walletDetails by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WalletDetails : {}", id);
        walletDetailsRepository.delete(id);
    }
}
