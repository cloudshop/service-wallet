package com.eyun.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.wallet.service.TicketDetailsService;
import com.eyun.wallet.service.UaaService;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;
import com.eyun.wallet.web.rest.util.HeaderUtil;
import com.eyun.wallet.web.rest.util.PaginationUtil;
import com.eyun.wallet.service.dto.TicketDetailsDTO;
import com.eyun.wallet.service.dto.UserDTO;
import com.eyun.wallet.service.dto.TicketDetailsCriteria;
import com.eyun.wallet.service.TicketDetailsQueryService;

import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * REST controller for managing TicketDetails.
 */
@RestController
@RequestMapping("/api")
public class TicketDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TicketDetailsResource.class);

    private static final String ENTITY_NAME = "ticketDetails";

    private final TicketDetailsService ticketDetailsService;

    private final TicketDetailsQueryService ticketDetailsQueryService;
    
    @Autowired
    private UaaService uaaService;

    public TicketDetailsResource(TicketDetailsService ticketDetailsService, TicketDetailsQueryService ticketDetailsQueryService) {
        this.ticketDetailsService = ticketDetailsService;
        this.ticketDetailsQueryService = ticketDetailsQueryService;
    }

    /**
     * POST  /ticket-details : Create a new ticketDetails.
     *
     * @param ticketDetailsDTO the ticketDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ticketDetailsDTO, or with status 400 (Bad Request) if the ticketDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/ticket-details")
    @Timed
    public ResponseEntity<TicketDetailsDTO> createTicketDetails(@RequestBody TicketDetailsDTO ticketDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save TicketDetails : {}", ticketDetailsDTO);
        if (ticketDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TicketDetailsDTO result = ticketDetailsService.save(ticketDetailsDTO);
        return ResponseEntity.created(new URI("/api/ticket-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /ticket-details : Updates an existing ticketDetails.
     *
     * @param ticketDetailsDTO the ticketDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ticketDetailsDTO,
     * or with status 400 (Bad Request) if the ticketDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the ticketDetailsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/ticket-details")
    @Timed
    public ResponseEntity<TicketDetailsDTO> updateTicketDetails(@RequestBody TicketDetailsDTO ticketDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update TicketDetails : {}", ticketDetailsDTO);
        if (ticketDetailsDTO.getId() == null) {
            return createTicketDetails(ticketDetailsDTO);
        }
        TicketDetailsDTO result = ticketDetailsService.save(ticketDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ticketDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /ticket-details : get all the ticketDetails.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of ticketDetails in body
     */
    @GetMapping("/ticket-details")
    @Timed
    public ResponseEntity<List<TicketDetailsDTO>> getAllTicketDetails(TicketDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TicketDetails by criteria: {}", criteria);
        Page<TicketDetailsDTO> page = ticketDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/ticket-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /ticket-details/:id : get the "id" ticketDetails.
     *
     * @param id the id of the ticketDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ticketDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/ticket-details/{id}")
    @Timed
    public ResponseEntity<TicketDetailsDTO> getTicketDetails(@PathVariable Long id) {
        log.debug("REST request to get TicketDetails : {}", id);
        TicketDetailsDTO ticketDetailsDTO = ticketDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(ticketDetailsDTO));
    }

    /**
     * DELETE  /ticket-details/:id : delete the "id" ticketDetails.
     *
     * @param id the id of the ticketDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
    @DeleteMapping("/ticket-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteTicketDetails(@PathVariable Long id) {
        log.debug("REST request to delete TicketDetails : {}", id);
        ticketDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
     */
    
    /**
     * 获取钱包贡融卷明细
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月20日
     * @version 1.0
     * @param pageable
     * @return
     */
    @ApiOperation("获取钱包贡融卷明细")
    @GetMapping("/wallet/details/ticket")
    public ResponseEntity<List<TicketDetailsDTO>> getWalletDetailsWithTicket(Pageable pageable) {
    	UserDTO user = uaaService.getAccount();
    	TicketDetailsCriteria criteria = new TicketDetailsCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(user.getId());
		criteria.setUserid(longFilter);
		Page<TicketDetailsDTO> page = ticketDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/wallet/details/ticket");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    
}
