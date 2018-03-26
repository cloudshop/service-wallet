package com.eyun.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.wallet.service.WalletDetailsService;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;
import com.eyun.wallet.web.rest.util.HeaderUtil;
import com.eyun.wallet.web.rest.util.PaginationUtil;
import com.eyun.wallet.service.dto.WalletDetailsDTO;
import com.eyun.wallet.service.dto.WalletDetailsCriteria;
import com.eyun.wallet.service.WalletDetailsQueryService;
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
 * REST controller for managing WalletDetails.
 */
@RestController
@RequestMapping("/api")
public class WalletDetailsResource {

    private final Logger log = LoggerFactory.getLogger(WalletDetailsResource.class);

    private static final String ENTITY_NAME = "walletDetails";

    private final WalletDetailsService walletDetailsService;

    private final WalletDetailsQueryService walletDetailsQueryService;

    public WalletDetailsResource(WalletDetailsService walletDetailsService, WalletDetailsQueryService walletDetailsQueryService) {
        this.walletDetailsService = walletDetailsService;
        this.walletDetailsQueryService = walletDetailsQueryService;
    }

    /**
     * POST  /wallet-details : Create a new walletDetails.
     *
     * @param walletDetailsDTO the walletDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new walletDetailsDTO, or with status 400 (Bad Request) if the walletDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wallet-details")
    @Timed
    public ResponseEntity<WalletDetailsDTO> createWalletDetails(@Valid @RequestBody WalletDetailsDTO walletDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save WalletDetails : {}", walletDetailsDTO);
        if (walletDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new walletDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WalletDetailsDTO result = walletDetailsService.save(walletDetailsDTO);
        return ResponseEntity.created(new URI("/api/wallet-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wallet-details : Updates an existing walletDetails.
     *
     * @param walletDetailsDTO the walletDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated walletDetailsDTO,
     * or with status 400 (Bad Request) if the walletDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the walletDetailsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wallet-details")
    @Timed
    public ResponseEntity<WalletDetailsDTO> updateWalletDetails(@Valid @RequestBody WalletDetailsDTO walletDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update WalletDetails : {}", walletDetailsDTO);
        if (walletDetailsDTO.getId() == null) {
            return createWalletDetails(walletDetailsDTO);
        }
        WalletDetailsDTO result = walletDetailsService.save(walletDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, walletDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wallet-details : get all the walletDetails.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of walletDetails in body
     */
    @GetMapping("/wallet-details")
    @Timed
    public ResponseEntity<List<WalletDetailsDTO>> getAllWalletDetails(WalletDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WalletDetails by criteria: {}", criteria);
        Page<WalletDetailsDTO> page = walletDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/wallet-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /wallet-details/:id : get the "id" walletDetails.
     *
     * @param id the id of the walletDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the walletDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/wallet-details/{id}")
    @Timed
    public ResponseEntity<WalletDetailsDTO> getWalletDetails(@PathVariable Long id) {
        log.debug("REST request to get WalletDetails : {}", id);
        WalletDetailsDTO walletDetailsDTO = walletDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(walletDetailsDTO));
    }

    /**
     * DELETE  /wallet-details/:id : delete the "id" walletDetails.
     *
     * @param id the id of the walletDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wallet-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteWalletDetails(@PathVariable Long id) {
        log.debug("REST request to delete WalletDetails : {}", id);
        walletDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
