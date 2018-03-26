package com.eyun.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.wallet.service.WalletService;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;
import com.eyun.wallet.web.rest.util.HeaderUtil;
import com.eyun.wallet.web.rest.util.PaginationUtil;
import com.eyun.wallet.service.dto.WalletDTO;
import com.eyun.wallet.service.dto.WalletCriteria;
import com.eyun.wallet.service.WalletQueryService;

import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Wallet.
 */
@RestController
@RequestMapping("/api")
public class WalletResource {

    private final Logger log = LoggerFactory.getLogger(WalletResource.class);

    private static final String ENTITY_NAME = "wallet";

    private final WalletService walletService;

    private final WalletQueryService walletQueryService;

    public WalletResource(WalletService walletService, WalletQueryService walletQueryService) {
        this.walletService = walletService;
        this.walletQueryService = walletQueryService;
    }

    /**
     * POST  /wallets : Create a new wallet.
     *
     * @param walletDTO the walletDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new walletDTO, or with status 400 (Bad Request) if the wallet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wallets")
    @Timed
    public ResponseEntity<WalletDTO> createWallet(@Valid @RequestBody WalletDTO walletDTO) throws URISyntaxException {
        log.debug("REST request to save Wallet : {}", walletDTO);
        if (walletDTO.getId() != null) {
            throw new BadRequestAlertException("A new wallet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WalletDTO result = walletService.save(walletDTO);
        return ResponseEntity.created(new URI("/api/wallets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wallets : Updates an existing wallet.
     *
     * @param walletDTO the walletDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated walletDTO,
     * or with status 400 (Bad Request) if the walletDTO is not valid,
     * or with status 500 (Internal Server Error) if the walletDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wallets")
    @Timed
    public ResponseEntity<WalletDTO> updateWallet(@Valid @RequestBody WalletDTO walletDTO) throws URISyntaxException {
        log.debug("REST request to update Wallet : {}", walletDTO);
        if (walletDTO.getId() == null) {
            return createWallet(walletDTO);
        }
        WalletDTO result = walletService.save(walletDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, walletDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wallets : get all the wallets.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of wallets in body
     */
    @GetMapping("/wallets")
    @Timed
    public ResponseEntity<List<WalletDTO>> getAllWallets(WalletCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Wallets by criteria: {}", criteria);
        Page<WalletDTO> page = walletQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wallets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
    /**
     * GET  /wallets/:id : get the "id" wallet.
     *
     * @param id the id of the walletDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the walletDTO, or with status 404 (Not Found)
     */
    @GetMapping("/wallets/{id}")
    @Timed
    public ResponseEntity<WalletDTO> getWallet(@PathVariable Long id) {
        log.debug("REST request to get Wallet : {}", id);
        WalletDTO walletDTO = walletService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(walletDTO));
    }

    /**
     * DELETE  /wallets/:id : delete the "id" wallet.
     *
     * @param id the id of the walletDTO to delete
     * @return the ResponseEntity with status 200 (OK)
    @DeleteMapping("/wallets/{id}")
    @Timed
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        log.debug("REST request to delete Wallet : {}", id);
        walletService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
     */
}
