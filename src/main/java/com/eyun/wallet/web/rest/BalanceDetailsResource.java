package com.eyun.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.wallet.service.BalanceDetailsService;
import com.eyun.wallet.service.UaaService;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;
import com.eyun.wallet.web.rest.util.HeaderUtil;
import com.eyun.wallet.web.rest.util.PaginationUtil;
import com.eyun.wallet.service.dto.BalanceDetailsDTO;
import com.eyun.wallet.service.dto.UserDTO;
import com.eyun.wallet.service.dto.BalanceDetailsCriteria;
import com.eyun.wallet.service.BalanceDetailsQueryService;

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

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BalanceDetails.
 */
@RestController
@RequestMapping("/api")
public class BalanceDetailsResource {

    private final Logger log = LoggerFactory.getLogger(BalanceDetailsResource.class);

    private static final String ENTITY_NAME = "balanceDetails";

    private final BalanceDetailsService balanceDetailsService;

    private final BalanceDetailsQueryService balanceDetailsQueryService;

    @Autowired
    private UaaService uaaService;

    public BalanceDetailsResource(BalanceDetailsService balanceDetailsService, BalanceDetailsQueryService balanceDetailsQueryService) {
        this.balanceDetailsService = balanceDetailsService;
        this.balanceDetailsQueryService = balanceDetailsQueryService;
    }

    /**
     * POST  /balance-details : Create a new balanceDetails.
     *
     * @param balanceDetailsDTO the balanceDetailsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new balanceDetailsDTO, or with status 400 (Bad Request) if the balanceDetails has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/balance-details")
    @Timed
    public ResponseEntity<BalanceDetailsDTO> createBalanceDetails(@RequestBody BalanceDetailsDTO balanceDetailsDTO) throws URISyntaxException {
        log.debug("REST request to save BalanceDetails : {}", balanceDetailsDTO);
        if (balanceDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new balanceDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BalanceDetailsDTO result = balanceDetailsService.save(balanceDetailsDTO);
        return ResponseEntity.created(new URI("/api/balance-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /balance-details : Updates an existing balanceDetails.
     *
     * @param balanceDetailsDTO the balanceDetailsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated balanceDetailsDTO,
     * or with status 400 (Bad Request) if the balanceDetailsDTO is not valid,
     * or with status 500 (Internal Server Error) if the balanceDetailsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/balance-details")
    @Timed
    public ResponseEntity<BalanceDetailsDTO> updateBalanceDetails(@RequestBody BalanceDetailsDTO balanceDetailsDTO) throws URISyntaxException {
        log.debug("REST request to update BalanceDetails : {}", balanceDetailsDTO);
        if (balanceDetailsDTO.getId() == null) {
            return createBalanceDetails(balanceDetailsDTO);
        }
        BalanceDetailsDTO result = balanceDetailsService.save(balanceDetailsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, balanceDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /balance-details : get all the balanceDetails.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of balanceDetails in body
     */
    @GetMapping("/balance-details")
    @Timed
    public ResponseEntity<List<BalanceDetailsDTO>> getAllBalanceDetails(BalanceDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BalanceDetails by criteria: {}", criteria);
        Page<BalanceDetailsDTO> page = balanceDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/balance-details");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /balance-details/:id : get the "id" balanceDetails.
     *
     * @param id the id of the balanceDetailsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the balanceDetailsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/balance-details/{id}")
    @Timed
    public ResponseEntity<BalanceDetailsDTO> getBalanceDetails(@PathVariable Long id) {
        log.debug("REST request to get BalanceDetails : {}", id);
        BalanceDetailsDTO balanceDetailsDTO = balanceDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(balanceDetailsDTO));
    }

    /**
     * DELETE  /balance-details/:id : delete the "id" balanceDetails.
     *
     * @param id the id of the balanceDetailsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
    @DeleteMapping("/balance-details/{id}")
    @Timed
    public ResponseEntity<Void> deleteBalanceDetails(@PathVariable Long id) {
        log.debug("REST request to delete BalanceDetails : {}", id);
        balanceDetailsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
     */

    /**
     * 获取钱包余额明细
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月20日
     * @version 1.0
     * @param pageable
     * @return
     */
    @ApiOperation("获取钱包余额明细")
    @GetMapping("/wallet/details/balance")
    public ResponseEntity<List<BalanceDetailsDTO>> getWalletDetailsWithBalance(Pageable pageable) {
    	UserDTO user = uaaService.getAccount();
        BalanceDetailsCriteria criteria = new BalanceDetailsCriteria();
        LongFilter longFilter = new LongFilter();
        longFilter.setEquals(user.getId());
        criteria.setUserid(longFilter);
		Page<BalanceDetailsDTO> page = balanceDetailsQueryService.findByCriteria(criteria , pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/wallet/details/balance");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * 文亮
     * @param money
     * @return
     */
    @ApiOperation("扣除商户的资金")
    @GetMapping("/wallet/Deductmoney/{money}")
    public ResponseEntity<String> Deductmoney(@PathVariable BigDecimal money){
        UserDTO user = uaaService.getAccount();
        String deductmoney = balanceDetailsService.Deductmoney(user.getId(), money);
        return ResponseEntity.ok().body(deductmoney);
    }

}
