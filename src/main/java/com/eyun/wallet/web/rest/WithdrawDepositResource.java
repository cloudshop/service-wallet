package com.eyun.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.wallet.service.WithdrawDepositService;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;
import com.eyun.wallet.web.rest.util.HeaderUtil;
import com.eyun.wallet.web.rest.util.PaginationUtil;
import com.eyun.wallet.service.dto.WithdrawDepositDTO;
import com.eyun.wallet.service.dto.PutForwardDTO;
import com.eyun.wallet.service.dto.UserDTO;
import com.eyun.wallet.service.dto.WithdrawDepositCriteria;
import com.eyun.wallet.security.SecurityUtils;
import com.eyun.wallet.service.PushService;
import com.eyun.wallet.service.UaaService;
import com.eyun.wallet.service.WithdrawDepositQueryService;
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
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.annotation.security.RolesAllowed;

/**
 * REST controller for managing WithdrawDeposit.
 */
@RestController
@RequestMapping("/api")
public class WithdrawDepositResource {

    private final Logger log = LoggerFactory.getLogger(WithdrawDepositResource.class);

    private static final String ENTITY_NAME = "withdrawDeposit";

    private final WithdrawDepositService withdrawDepositService;

    private final WithdrawDepositQueryService withdrawDepositQueryService;
    
    @Autowired
    private UaaService uaaService;
    
    @Autowired
    private PushService pushService;

    public WithdrawDepositResource(WithdrawDepositService withdrawDepositService, WithdrawDepositQueryService withdrawDepositQueryService) {
        this.withdrawDepositService = withdrawDepositService;
        this.withdrawDepositQueryService = withdrawDepositQueryService;
    }

    /**
     * POST  /withdraw-deposits : Create a new withdrawDeposit.
     *
     * @param withdrawDepositDTO the withdrawDepositDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new withdrawDepositDTO, or with status 400 (Bad Request) if the withdrawDeposit has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/withdraw-deposits")
    @Timed
    public ResponseEntity<WithdrawDepositDTO> createWithdrawDeposit(@RequestBody WithdrawDepositDTO withdrawDepositDTO) throws URISyntaxException {
        log.debug("REST request to save WithdrawDeposit : {}", withdrawDepositDTO);
        if (withdrawDepositDTO.getId() != null) {
            throw new BadRequestAlertException("A new withdrawDeposit cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WithdrawDepositDTO result = withdrawDepositService.save(withdrawDepositDTO);
        return ResponseEntity.created(new URI("/api/withdraw-deposits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /withdraw-deposits : Updates an existing withdrawDeposit.
     *
     * @param withdrawDepositDTO the withdrawDepositDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated withdrawDepositDTO,
     * or with status 400 (Bad Request) if the withdrawDepositDTO is not valid,
     * or with status 500 (Internal Server Error) if the withdrawDepositDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/withdraw-deposits")
    @Timed
    public ResponseEntity<WithdrawDepositDTO> updateWithdrawDeposit(@RequestBody WithdrawDepositDTO withdrawDepositDTO) throws URISyntaxException {
        log.debug("REST request to update WithdrawDeposit : {}", withdrawDepositDTO);
        if (withdrawDepositDTO.getId() == null) {
            return createWithdrawDeposit(withdrawDepositDTO);
        }
        WithdrawDepositDTO result = withdrawDepositService.save(withdrawDepositDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, withdrawDepositDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /withdraw-deposits : get all the withdrawDeposits.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of withdrawDeposits in body
     */
    @GetMapping("/withdraw-deposits")
    @Timed
    public ResponseEntity<List<WithdrawDepositDTO>> getAllWithdrawDeposits(WithdrawDepositCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WithdrawDeposits by criteria: {}", criteria);
        Page<WithdrawDepositDTO> page = withdrawDepositQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/withdraw-deposits");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /withdraw-deposits/:id : get the "id" withdrawDeposit.
     *
     * @param id the id of the withdrawDepositDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the withdrawDepositDTO, or with status 404 (Not Found)
     */
    @GetMapping("/withdraw-deposits/{id}")
    @Timed
    public ResponseEntity<WithdrawDepositDTO> getWithdrawDeposit(@PathVariable Long id) {
        log.debug("REST request to get WithdrawDeposit : {}", id);
        WithdrawDepositDTO withdrawDepositDTO = withdrawDepositService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(withdrawDepositDTO));
    }

    /**
     * DELETE  /withdraw-deposits/:id : delete the "id" withdrawDeposit.
     *
     * @param id the id of the withdrawDepositDTO to delete
     * @return the ResponseEntity with status 200 (OK)
    @DeleteMapping("/withdraw-deposits/{id}")
    @Timed
    public ResponseEntity<Void> deleteWithdrawDeposit(@PathVariable Long id) {
        log.debug("REST request to delete WithdrawDeposit : {}", id);
        withdrawDepositService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
     */
    
    /**
     * 申请提现
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年5月17日
     * @version 1.0
     * @param putForwardDTO
     */
    @ApiOperation("申请提现")
    @PostMapping("/put-forward")
    public void putForward(@RequestBody PutForwardDTO putForwardDTO) {
    	UserDTO user = uaaService.getAccount();
    	withdrawDepositService.putForward(putForwardDTO,user);
    }
    
    /**
     * 提现通过
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年5月17日
     * @version 1.0
     * @param id
     */
    @ApiOperation("提现通过")
    @RolesAllowed("ROLE_ADMIN")
    @PutMapping("/put-forward/adopt/{withdrawDepositID}")
    public void putForwardAdopt(@PathVariable("withdrawDeposit") Long withdrawDepositID) {
    	WithdrawDepositDTO withdrawDepositDTO = withdrawDepositService.findOne(withdrawDepositID);
    	Instant now = Instant.now();
    	withdrawDepositDTO.setUpdatedTime(now);
    	withdrawDepositDTO.setStatus(3);
    	withdrawDepositDTO.setStatusString("提现成功");
    	withdrawDepositService.save(withdrawDepositDTO);
    	pushService.sendPushByUserid(withdrawDepositDTO.getUserid().toString(), "提现成功");
    	Optional<String> optional = SecurityUtils.getCurrentUserLogin();
    	log.info("提现操作财务员账号："+optional.get()+",提现记录id:"+withdrawDepositDTO.getId());
    }
    
}
