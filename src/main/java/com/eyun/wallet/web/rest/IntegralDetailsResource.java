package com.eyun.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.wallet.service.IntegralDetailsService;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;
import com.eyun.wallet.web.rest.util.HeaderUtil;
import com.eyun.wallet.web.rest.util.PaginationUtil;
import com.eyun.wallet.service.dto.IntegralDetailsDTO;
import com.eyun.wallet.service.dto.IntegralDetailsCriteria;
import com.eyun.wallet.service.IntegralDetailsQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing IntegralDetails.
 */
@RestController
@RequestMapping("/api")
public class IntegralDetailsResource {

    private final Logger log = LoggerFactory.getLogger(IntegralDetailsResource.class);

    private static final String ENTITY_NAME = "integralDetails";

    private final IntegralDetailsService integralDetailsService;

    private final IntegralDetailsQueryService integralDetailsQueryService;

    public IntegralDetailsResource(IntegralDetailsService integralDetailsService, IntegralDetailsQueryService integralDetailsQueryService) {
        this.integralDetailsService = integralDetailsService;
        this.integralDetailsQueryService = integralDetailsQueryService;
    }

    /**
     * POST  /integral-details : Create a new integralDetails.
     *
     * @param integralDetailsDTO the integralDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new integralDetailsDTO, or with status 400 (Bad Request) if the integralDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/integral-details")
    @Timed
    public ResponseEntity<IntegralDetailsDTO> createIntegralDetails(@RequestBody IntegralDetailsDTO integralDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save IntegralDetails : {}", integralDetailsDTO);
        if (integralDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new integralDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IntegralDetailsDTO result = integralDetailsService.save(integralDetailsDTO);
        return ResponseEntity.created(new URI("/api/integral-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /integral-details : Updates an existing integralDetails.
     *
     * @param integralDetailsDTO the integralDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated integralDetailsDTO,
     * or with status 400 (Bad Request) if the integralDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the integralDetailsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/integral-details")
    @Timed
    public ResponseEntity<IntegralDetailsDTO> updateIntegralDetails(@RequestBody IntegralDetailsDTO integralDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update IntegralDetails : {}", integralDetailsDTO);
        if (integralDetailsDTO.getId() == null) {
            return createIntegralDetails(integralDetailsDTO);
        }
        IntegralDetailsDTO result = integralDetailsService.save(integralDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, integralDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /integral-details : get all the integralDetails.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of integralDetails in body
     */
    @GetMapping("/integral-details")
    @Timed
    public ResponseEntity<List<IntegralDetailsDTO>> getAllIntegralDetails(IntegralDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get IntegralDetails by criteria: {}", criteria);
        Page<IntegralDetailsDTO> page = integralDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/integral-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /integral-details/:id : get the "id" integralDetails.
     *
     * @param id the id of the integralDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the integralDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/integral-details/{id}")
    @Timed
    public ResponseEntity<IntegralDetailsDTO> getIntegralDetails(@PathVariable Long id) {
        log.debug("REST request to get IntegralDetails : {}", id);
        IntegralDetailsDTO integralDetailsDTO = integralDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(integralDetailsDTO));
    }

    /**
     * DELETE  /integral-details/:id : delete the "id" integralDetails.
     *
     * @param id the id of the integralDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/integral-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteIntegralDetails(@PathVariable Long id) {
        log.debug("REST request to delete IntegralDetails : {}", id);
        integralDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
