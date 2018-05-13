package com.eyun.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.wallet.service.PayOrderService;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;
import com.eyun.wallet.web.rest.util.HeaderUtil;
import com.eyun.wallet.web.rest.util.PaginationUtil;
import com.eyun.wallet.service.dto.PayOrderDTO;
import com.eyun.wallet.service.dto.PayOrderCriteria;
import com.eyun.wallet.service.PayOrderQueryService;
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
 * REST controller for managing PayOrder.
 */
@RestController
@RequestMapping("/api")
public class PayOrderResource {

    private final Logger log = LoggerFactory.getLogger(PayOrderResource.class);

    private static final String ENTITY_NAME = "payOrder";

    private final PayOrderService payOrderService;

    private final PayOrderQueryService payOrderQueryService;

    public PayOrderResource(PayOrderService payOrderService, PayOrderQueryService payOrderQueryService) {
        this.payOrderService = payOrderService;
        this.payOrderQueryService = payOrderQueryService;
    }

    /**
     * POST  /pay-orders : Create a new payOrder.
     *
     * @param payOrderDTO the payOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new payOrderDTO, or with status 400 (Bad Request) if the payOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pay-orders")
    @Timed
    public ResponseEntity<PayOrderDTO> createPayOrder(@RequestBody PayOrderDTO payOrderDTO) throws URISyntaxException {
        log.debug("REST request to save PayOrder : {}", payOrderDTO);
        if (payOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new payOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PayOrderDTO result = payOrderService.save(payOrderDTO);
        return ResponseEntity.created(new URI("/api/pay-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pay-orders : Updates an existing payOrder.
     *
     * @param payOrderDTO the payOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated payOrderDTO,
     * or with status 400 (Bad Request) if the payOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the payOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pay-orders")
    @Timed
    public ResponseEntity<PayOrderDTO> updatePayOrder(@RequestBody PayOrderDTO payOrderDTO) throws URISyntaxException {
        log.debug("REST request to update PayOrder : {}", payOrderDTO);
        if (payOrderDTO.getId() == null) {
            return createPayOrder(payOrderDTO);
        }
        PayOrderDTO result = payOrderService.save(payOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, payOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pay-orders : get all the payOrders.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of payOrders in body
     */
    @GetMapping("/pay-orders")
    @Timed
    public ResponseEntity<List<PayOrderDTO>> getAllPayOrders(PayOrderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PayOrders by criteria: {}", criteria);
        Page<PayOrderDTO> page = payOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pay-orders");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pay-orders/:id : get the "id" payOrder.
     *
     * @param id the id of the payOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the payOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/pay-orders/{id}")
    @Timed
    public ResponseEntity<PayOrderDTO> getPayOrder(@PathVariable Long id) {
        log.debug("REST request to get PayOrder : {}", id);
        PayOrderDTO payOrderDTO = payOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(payOrderDTO));
    }

    /**
     * DELETE  /pay-orders/:id : delete the "id" payOrder.
     *
     * @param id the id of the payOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
    @DeleteMapping("/pay-orders/{id}")
    @Timed
    public ResponseEntity<Void> deletePayOrder(@PathVariable Long id) {
        log.debug("REST request to delete PayOrder : {}", id);
        payOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
     */
    
    @GetMapping("/pay-orders/{payNo}")
    @Timed
    public ResponseEntity<PayOrderDTO> getPayOrderByPayNo(@PathVariable String payNo) {
        log.debug("REST request to get PayOrder : {}", payNo);
        PayOrderDTO payOrderDTO = payOrderService.findPayOrderByPayNo(payNo);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(payOrderDTO));
    }
    
}
