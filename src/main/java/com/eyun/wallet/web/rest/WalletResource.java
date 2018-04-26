package com.eyun.wallet.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.wallet.service.WalletService;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;
import com.eyun.wallet.web.rest.util.HeaderUtil;
import com.eyun.wallet.web.rest.util.PaginationUtil;
import com.eyun.wallet.service.dto.WalletDTO;
import com.eyun.wallet.service.dto.BalancePayDTO;
import com.eyun.wallet.service.dto.PasswordDTO;
import com.eyun.wallet.service.dto.PayNotifyDTO;
import com.eyun.wallet.service.dto.ProOrderCriteria;
import com.eyun.wallet.service.dto.ProOrderDTO;
import com.eyun.wallet.service.dto.UserDTO;
import com.eyun.wallet.service.dto.WalletCriteria;
import com.eyun.wallet.domain.BalanceDTO;
import com.eyun.wallet.domain.GiveIntegralDTO;
import com.eyun.wallet.domain.PayOrder;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.service.OrderService;
import com.eyun.wallet.service.PayService;
import com.eyun.wallet.service.UaaService;
import com.eyun.wallet.service.VerifyService;
import com.eyun.wallet.service.WalletQueryService;

import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.math.BigDecimal;
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
    
    @Autowired
    private UaaService uaaService;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private VerifyService VerifyService;
    
    @Autowired
    private PayService payService;

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
     * @return 
     * @return the ResponseEntity with status 200 (OK)
      
    @DeleteMapping("/wallets/{id}")
     * @throws JSONException 
    @Timed
    public ResponseEntity<Void> deleteWallet(@PathVariable Long id) {
        log.debug("REST request to delete Wallet : {}", id);
        walletService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
     */
    
    /**
     * 支付通知回调
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月09日
     * @version 1.0
     * @param balanceDTO
     * @return
     * @throws JSONException
     */
    @PutMapping("/wallets/balance")
    public ResponseEntity<Wallet> updateBalance(@RequestBody BalanceDTO balanceDTO) throws JSONException {
    	switch (balanceDTO.getType()) {
		case 1: //充值
			String order = payService.queryOrder(balanceDTO.getOrderNo());
			JSONObject jsonObject = new JSONObject(order);
			double totalAmount = jsonObject.getJSONObject("alipay_trade_query_response").getDouble("total_amount");
			//if (balanceDTO.getMoney().compareTo(new BigDecimal(totalAmount)) == 0) {
			if (balanceDTO.getMoney().doubleValue() == totalAmount) {//比较充值金额是否和支付宝账单中金额相等
				Wallet wallet = walletService.rechargeBalance(balanceDTO);
				return ResponseEntity.ok()
			            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, wallet.getId().toString()))
			            .body(wallet);
			} else {
				return new ResponseEntity<>(HeaderUtil.createAlert("The amount of recharge is mistaken", "money:"+balanceDTO.getMoney()), HttpStatus.BAD_REQUEST);
			}
		default:
			return new ResponseEntity<>(HeaderUtil.createAlert("Type field is mistaken", "type:"+balanceDTO.getType()), HttpStatus.BAD_REQUEST);
		}
    }
    
    /**
     * 赠送积分
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月11日
     * @version 1.0
     * @param giveIntegralDTO
     * @return
     */
    @SuppressWarnings("all")
    @ApiOperation(value="赠送积分")
    @PostMapping("/wallets/giveIntegral")
    public ResponseEntity giveIntegral (@RequestBody GiveIntegralDTO giveIntegralDTO) {
    	UserDTO account = uaaService.getAccount();
    	Wallet wallet = walletService.findByUserid(account.getId());
    	if (wallet.getPassword() == null) {
    		throw new BadRequestAlertException("请设置钱包密码", "wallet", "walletPsdNull");
    	} else if (!wallet.getPassword().equals(giveIntegralDTO.getPassword())) {
    		throw new BadRequestAlertException("钱包密码输入错误", "wallet", "walletPsdError");
    	}
    	ResponseEntity<UserDTO> resp = uaaService.getUserByLogin(giveIntegralDTO.getTarget());
    	if (!HttpStatus.NOT_FOUND.equals(resp.getStatusCode())) {
    		throw new BadRequestAlertException("赠送目标不存在", "target", "targetIsNull");
    	}
    	if (giveIntegralDTO.getIntegral().doubleValue() < 0.00) {
    		throw new BadRequestAlertException("赠送积分输入错误", "integral", "integralError");
    	}
    	BigDecimal integral = giveIntegralDTO.getIntegral();
    	UserDTO target = resp.getBody();
		Long toUserId = target.getId();
		Long fromUserId = account.getId();
		String result = walletService.giveIntegral(fromUserId , toUserId, integral);
		if ("integral-error".equals(result)) {
			throw new BadRequestAlertException("积分不足", "integral", "integralError");
		}
		return new ResponseEntity<>(HttpStatus.OK);
    }
    
    /**
     * 查看钱包接口
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月11日
     * @version 1.0
     * @param userid
     */
    @ApiOperation(value="查看钱包接口")
    @GetMapping("/wallets/user") 
    public ResponseEntity<Wallet> findWalletsByUserid(){
    	UserDTO user = uaaService.getAccount();
    	Wallet wallet = walletService.findByUserid(user.getId());
    	return new ResponseEntity<Wallet>(wallet, HeaderUtil.createAlert("wallets", "userid："+user.getId()), HttpStatus.OK);
    }
    
    /**
     * 余额支付接口
     * @author 逍遥子
     * @email 756898059@qq.com
     * @date 2018年4月16日
     * @version 1.0
     * @return ResponseEntity
     */
    @SuppressWarnings("all")
    @ApiOperation("余额支付接口")
    @PostMapping("/wallets/balance/pay")
    public ResponseEntity balancePay(@RequestBody BalancePayDTO balancePayDTO) {
    	UserDTO user = uaaService.getAccount();
    	Wallet wallet = walletService.findByUserid(user.getId());
    	ProOrderDTO proOrderDTO = orderService.findOrderByOrderNo(balancePayDTO.getOrderNo());
    	if (user.getId() != proOrderDTO.getcUserid()) {
    		throw new BadRequestAlertException("订单异常,交易关闭", "order", "orderError");
    	}
    	if (wallet.getPassword() != null) {
    		if (!wallet.getPassword().equals(balancePayDTO.getPassword())) {
    			throw new BadRequestAlertException("钱包密码输入错误", "wallet", "walletPsdError");
    		}
    	} else {
    		throw new BadRequestAlertException("请设置钱包密码", "wallet", "walletPsdNull");
    	}
    	PayOrder balancePay = walletService.balancePay(wallet.getId(),proOrderDTO.getPayment(),balancePayDTO.getOrderNo());
		//TODO  调用订单服务 通知支付成功
    	PayNotifyDTO payNotifyDTO = new PayNotifyDTO();
    	payNotifyDTO.setOrderNo(balancePay.getOrderNo());
    	payNotifyDTO.setPayType(1);
    	payNotifyDTO.setPayNo(balancePay.getPayNo());
    	ResponseEntity<ProOrderDTO> resp = orderService.proOrderNotify(payNotifyDTO);
    	return new ResponseEntity(null, HeaderUtil.createAlert("支付成功","orderNo:"+balancePayDTO.getOrderNo()), HttpStatus.OK);
    }
    
    
    @SuppressWarnings("all")
    @ApiOperation("修改钱包密码")
    @PostMapping("/wallets/update-password")
    public ResponseEntity updateWalletPassword(@RequestBody PasswordDTO passwordDTO) {
    	String verifyCode = VerifyService.getVerifyCode();
    	if (StringUtils.isNotBlank(verifyCode) && verifyCode.equals(passwordDTO.getCode())) {
    		UserDTO user = uaaService.getAccount();
    		Wallet wallet = walletService.findByUserid(user.getId());
    		wallet.setPassword(passwordDTO.getPassword());
    		walletService.update(wallet);
    		return new ResponseEntity(null, HeaderUtil.createAlert("success","password:"+passwordDTO.getPassword()), HttpStatus.OK);
    	} else {
    		throw new BadRequestAlertException("验证码错误", "password", "password:"+passwordDTO.getPassword());
    	}
    	
    }
    
}
