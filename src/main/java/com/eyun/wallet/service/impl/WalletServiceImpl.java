package com.eyun.wallet.service.impl;

import com.eyun.wallet.service.WalletService;
import com.eyun.wallet.domain.BalanceDTO;
import com.eyun.wallet.domain.BalanceDetails;
import com.eyun.wallet.domain.PayOrder;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.repository.BalanceDetailsRepository;
import com.eyun.wallet.repository.IntegralDetailsRepository;
import com.eyun.wallet.repository.PayOrderRepository;
import com.eyun.wallet.repository.TicketDetailsRepository;
import com.eyun.wallet.repository.WalletRepository;
import com.eyun.wallet.service.dto.ServiceProviderRewardDTO;
import com.eyun.wallet.service.dto.WalletDTO;
import com.eyun.wallet.service.mapper.WalletMapper;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Wallet.
 */
@Service
@Transactional
public class WalletServiceImpl implements WalletService {

	private final Logger log = LoggerFactory.getLogger(WalletServiceImpl.class);

	private final WalletRepository walletRepository;

	private final WalletMapper walletMapper;
	
	@Autowired
	private BalanceDetailsRepository balanceDetailsRepository;
	
	@Autowired
	private IntegralDetailsRepository integralDetailsRepository;
	
	@Autowired
	private TicketDetailsRepository ticketDetailsRepository;
	
	@Autowired
	private PayOrderRepository payOrderRepository;

	public WalletServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper) {
		this.walletRepository = walletRepository;
		this.walletMapper = walletMapper;
	}

	/**
	 * Save a wallet.
	 *
	 * @param walletDTO
	 *            the entity to save
	 * @return the persisted entity
	 */
	@Override
	public WalletDTO save(WalletDTO walletDTO) {
		log.debug("Request to save Wallet : {}", walletDTO);
		Wallet wallet = walletMapper.toEntity(walletDTO);
		wallet = walletRepository.save(wallet);
		return walletMapper.toDto(wallet);
	}

	/**
	 * Get all the wallets.
	 *
	 * @param pageable
	 *            the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<WalletDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Wallets");
		return walletRepository.findAll(pageable).map(walletMapper::toDto);
	}

	/**
	 * Get one wallet by id.
	 *
	 * @param id
	 *            the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public WalletDTO findOne(Long id) {
		log.debug("Request to get Wallet : {}", id);
		Wallet wallet = walletRepository.findOne(id);
		return walletMapper.toDto(wallet);
	}

	/**
	 * Delete the wallet by id.
	 *
	 * @param id
	 *            the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Wallet : {}", id);
		walletRepository.delete(id);
	}

	@Override
	public Wallet rechargeBalance(BalanceDTO balanceDTO) {
		Wallet wallet = walletRepository.findByUserid(balanceDTO.getUserid());
		BigDecimal balance = wallet.getBalance();// 获取当前账户余额
		BigDecimal balance1 = balance.add(balanceDTO.getMoney());// 添加金额
		wallet.setBalance(balance1);
		wallet.setUpdatedTime(Instant.now());
		wallet.setVersion(wallet.getVersion() + 1);
		walletRepository.save(wallet);
		// 添加账户明细 TODO
		BalanceDetails balanceDetails = new BalanceDetails();
		balanceDetails.setAddBalance(true);
		balanceDetails.setBalance(balanceDTO.getMoney());
		balanceDetails.setCreatedTime(Instant.now());
		balanceDetails.setOrderNo(balanceDTO.getOrderNo());
		balanceDetails.setType(1);//1 ：充值
		balanceDetails.setTypeString("充值");
		balanceDetails.setWallet(wallet);
		balanceDetails.setUserid(wallet.getUserid());
		balanceDetailsRepository.save(balanceDetails);
		return wallet;
	}

	@Override
	public String giveIntegral(Long fromUserId, Long toUserId, BigDecimal integral) {
		Wallet fromWallet = walletRepository.findByUserid(fromUserId);
		Wallet toWallet = walletRepository.findByUserid(toUserId);
		BigDecimal subtract = fromWallet.getIntegral().subtract(integral);
		if (subtract.doubleValue() < 0.00) {
			return "integral-error";
		}
		fromWallet.setIntegral(subtract);
		toWallet.setIntegral(toWallet.getIntegral().add(integral));
		Instant now = Instant.now();
		fromWallet.setUpdatedTime(now);
		toWallet.setUpdatedTime(now);

		// 添加账户明细-from TODO
//		WalletDetails wdf = new WalletDetails();
//		wdf.setUserid(fromWallet.getUserid());
//		wdf.setType(2);// 2:赠送积分
//		wdf.setCreatedTime(now);
//		wdf.setIntegral(integral);
//		wdf.setWallet(fromWallet);
//		walletDetailsRepository.save(wdf);

		// 添加账户明细-to TODO
//		WalletDetails wdt = new WalletDetails();
//		wdt.setUserid(toWallet.getUserid());
//		wdt.setType(3);// 3:被送积分
//		wdt.setCreatedTime(now);
//		wdt.setIntegral(integral);
//		wdt.setWallet(toWallet);
//		walletDetailsRepository.save(wdt);

		return "success";
	}

	@Override
	public Wallet findByUserid(Long userid) {
		Wallet wallet = walletRepository.findByUserid(userid);
		if (wallet == null) {
			Wallet newWallet = new Wallet();
			newWallet.setUserid(userid);
			newWallet.setCreateTime(Instant.now());
			newWallet.setUpdatedTime(Instant.now());
			newWallet.setBalance(new BigDecimal(0.00));
			newWallet.setIntegral(new BigDecimal(0.00));
			newWallet.setTicket(new BigDecimal(0.00));
			newWallet.setVersion(0);
			Wallet save = walletRepository.save(newWallet);
			return save;
		} else {
			return wallet;
		}
	}

	@Override
	public PayOrder balancePay(Long id, BigDecimal price, String orderNo) {
		Wallet wallet = walletRepository.findOne(id);
		BigDecimal balance = wallet.getBalance();
		BigDecimal subtract = balance.subtract(price);
		if (subtract.doubleValue() < 0.00) {
			throw new BadRequestAlertException("钱包余额不足", "balance", "balanceError");
		}
		wallet.balance(subtract).updatedTime(Instant.now());
		walletRepository.save(wallet);
		//TODO 1、账户记录  
		
		//添加支付账单
		PayOrder payOrder = new PayOrder();
		payOrder
			.userid(wallet.getUserid())
			.balance(balance)
			.wallet(wallet)
			.payTime(Instant.now())
			.orderNo(orderNo);
		return payOrderRepository.save(payOrder);
	}

	@Override
	public void update(Wallet wallet) {
		if (wallet.getId() != null) {
			walletRepository.save(wallet);
		}
	}

	@Override
	public void serviceProviderReward(Long spid,Long incrBID) {
		BalanceDetails bd = balanceDetailsRepository.findByUseridAndTypeAndIncrBID(spid,2,incrBID);
		if (bd != null) {
			throw new BadRequestAlertException("incrYoule", "incr", "incrEr");
		}
		//奖励业务
		final BigDecimal reward = new BigDecimal(400.00);
		Wallet wallet = walletRepository.findByUserid(spid);
		Instant now = Instant.now();
		BigDecimal balance = wallet.getBalance();
		BigDecimal add = balance.add(reward);
		wallet.setBalance(add);
		wallet.setUpdatedTime(now);
		
		//添加明细记录
		BalanceDetails balanceDetails = new BalanceDetails();
		balanceDetails.userid(wallet.getUserid())
			.createdTime(now)
			.balance(reward)
			.addBalance(true)
			.type(2)
			.typeString("增值商家奖励")
			.incrBID(incrBID)
			.wallet(wallet);
		balanceDetailsRepository.save(balanceDetails);
	}
	
	@Override
	public void incrementUserReward(Long incrementUserID, Long incrementBusinessID) {
		BalanceDetails bd = balanceDetailsRepository.findByUseridAndTypeAndIncrBID(incrementUserID,3,incrementBusinessID);
		if (bd != null) {
			throw new BadRequestAlertException("incrYoule", "incr", "incrEr");
		}
		//奖励业务
		final BigDecimal reward = new BigDecimal(100.00);
		Wallet wallet = walletRepository.findByUserid(incrementUserID);
		Instant now = Instant.now();
		BigDecimal balance = wallet.getBalance();
		BigDecimal add = balance.add(reward);
		wallet.setBalance(add);
		wallet.setUpdatedTime(now);
		
		//添加明细记录
		BalanceDetails balanceDetails = new BalanceDetails();
		balanceDetails.userid(wallet.getUserid())
		.createdTime(now)
		.balance(reward)
		.addBalance(true)
		.type(3)
		.typeString("推荐增值商家奖励")
		.incrBID(incrementBusinessID)
		.wallet(wallet);
		balanceDetailsRepository.save(balanceDetails);
	}

}
