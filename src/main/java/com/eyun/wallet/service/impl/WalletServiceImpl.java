package com.eyun.wallet.service.impl;

import com.eyun.wallet.service.WalletService;
import com.eyun.wallet.domain.BalanceDTO;
import com.eyun.wallet.domain.BalanceDetails;
import com.eyun.wallet.domain.IntegralDetails;
import com.eyun.wallet.domain.PayOrder;
import com.eyun.wallet.domain.TicketDetails;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.repository.BalanceDetailsRepository;
import com.eyun.wallet.repository.IntegralDetailsRepository;
import com.eyun.wallet.repository.PayOrderRepository;
import com.eyun.wallet.repository.TicketDetailsRepository;
import com.eyun.wallet.repository.WalletRepository;
import com.eyun.wallet.service.dto.*;
import com.eyun.wallet.service.mapper.WalletMapper;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
	public PayOrder balancePay(Long walletId, BigDecimal balance, BigDecimal ticket, String orderNo) {
		System.out.println(walletId + " * " + balance + " * " + ticket + " * " + orderNo + "&&&&&&&&&&");
		Wallet wallet = walletRepository.findOne(walletId);
		Instant now = Instant.now();
		if (balance.doubleValue() != 0.00) {
			BigDecimal wbalance = wallet.getBalance();
			BigDecimal subtract = wbalance.subtract(balance);
			if (subtract.doubleValue() < 0.00) {
				throw new BadRequestAlertException("钱包余额不足", "balance", "balanceError");
			}
			wallet.balance(subtract).updatedTime(now);
			walletRepository.save(wallet);
			//添加明细记录
			BalanceDetails balanceDetails = new BalanceDetails();
			balanceDetails.userid(wallet.getUserid())
				.createdTime(now)
				.balance(balance)
				.addBalance(false)
				.type(4)
				.typeString("消费")
				.orderNo(orderNo)
				.wallet(wallet);

			balanceDetailsRepository.save(balanceDetails);

		}
		if (ticket.doubleValue() != 0.00) {
			BigDecimal wticket = wallet.getTicket();
			BigDecimal subtract = wticket.subtract(ticket);
			if (subtract.doubleValue() < 0.00) {
				throw new BadRequestAlertException("钱包贡融卷不足", "ticket", "ticketError");
			}
			wallet.ticket(subtract).updatedTime(now);
			walletRepository.save(wallet);
			//添加明细记录
			TicketDetails ticketDetails = new TicketDetails();
			ticketDetails.userid(wallet.getUserid())
				.createdTime(now)
				.ticket(ticket)
				.addTicket(false)
				.type(4)
				.typeString("消费")
				.orderNo(orderNo)
				.wallet(wallet);
			ticketDetailsRepository.save(ticketDetails);
		}

		//添加支付账单
		PayOrder payOrder = new PayOrder();
		payOrder
			.userid(wallet.getUserid())
			.balance(balance)
			.ticket(ticket)
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


    /**
     * 线下付
     * @param settlementWalletDTO
     */
    @Override
    public void batchintegrals(SettlementWalletDTO settlementWalletDTO) {
        Instant now = Instant.now();
        Wallet wallet = walletRepository.findByUserid(settlementWalletDTO.getUserid());
        switch (settlementWalletDTO.getType()) {
          /*  case 1://1、b端收入余额
                BigDecimal balance = wallet.getBalance();
                BigDecimal addBalance = balance.add(settlementWalletDTO.getAmount());
                wallet.setBalance(addBalance);
                //添加明细记录
                BalanceDetails balanceDetails = new BalanceDetails();
                balanceDetails.userid(wallet.getUserid())
                    .createdTime(now)
                    .balance(addBalance)
                    .addBalance(true)
                    .type(4)
                    .typeString("线下卖出商品收入")
                    .wallet(wallet)
                    .orderNo(settlementWalletDTO.getOrderNo());
                balanceDetailsRepository.save(balanceDetails);
                break;*/
            case 1://2、c端获得积分
                wallet.setIntegral(wallet.getIntegral().add(settlementWalletDTO.getAmount()));
                //添加明细记录
                IntegralDetails integralDetails = new IntegralDetails();
                integralDetails.userid(wallet.getUserid())
                    .createdTime(now)
                    .integral(settlementWalletDTO.getAmount())
                    .addIntegral(true)
                    .type(1)
                    .typeString("用户消费获得积分")
                    .wallet(wallet)
                    .orderNo(settlementWalletDTO.getOrderNo());
                integralDetailsRepository.save(integralDetails);
                break;
            case 2://3、b端获得积分
                wallet.setIntegral(wallet.getIntegral().add(settlementWalletDTO.getAmount()));
                //添加明细记录
                IntegralDetails integralDetails3 = new IntegralDetails();
                integralDetails3.userid(wallet.getUserid())
                    .createdTime(now)
                    .integral(settlementWalletDTO.getAmount())
                    .addIntegral(true)
                    .type(1)
                    .typeString("直接邀请人获得积分")
                    .wallet(wallet)
                    .orderNo(settlementWalletDTO.getOrderNo());
                integralDetailsRepository.save(integralDetails3);
                break;
            case 3://4、邀请人获得积分
                wallet.setIntegral(wallet.getIntegral().add(settlementWalletDTO.getAmount()));
                //添加明细记录
                IntegralDetails integralDetails4 = new IntegralDetails();
                integralDetails4.userid(wallet.getUserid())
                    .createdTime(now)
                    .integral(settlementWalletDTO.getAmount())
                    .addIntegral(true)
                    .type(1)
                    .typeString("间接邀请人获得积分")
                    .wallet(wallet)
                    .orderNo(settlementWalletDTO.getOrderNo());
                integralDetailsRepository.save(integralDetails4);
                break;

            case 4:
                wallet.setIntegral(wallet.getIntegral().add(settlementWalletDTO.getAmount()));
                //添加明细记录
                IntegralDetails integralDetails1 = new IntegralDetails();
                integralDetails1.userid(wallet.getUserid())
                    .createdTime(now)
                    .integral(settlementWalletDTO.getAmount())
                    .addIntegral(true)
                    .type(1)
                    .typeString("增值商户获得积分")
                    .wallet(wallet)
                    .orderNo(settlementWalletDTO.getOrderNo());
                integralDetailsRepository.save(integralDetails1);
                break;

            case 5:
                wallet.setIntegral(wallet.getIntegral().add(settlementWalletDTO.getAmount()));
                //添加明细记录
                IntegralDetails integralDetails2 = new IntegralDetails();
                integralDetails2.userid(wallet.getUserid())
                    .createdTime(now)
                    .integral(settlementWalletDTO.getAmount())
                    .addIntegral(true)
                    .type(1)
                    .typeString("直接邀请商户获得积分")
                    .wallet(wallet)
                    .orderNo(settlementWalletDTO.getOrderNo());
                integralDetailsRepository.save(integralDetails2);
                break;
            case 6:
                wallet.setIntegral(wallet.getIntegral().add(settlementWalletDTO.getAmount()));
                //添加明细记录
                IntegralDetails integralDetails9 = new IntegralDetails();
                integralDetails9.userid(wallet.getUserid())
                    .createdTime(now)
                    .integral(settlementWalletDTO.getAmount())
                    .addIntegral(true)
                    .type(1)
                    .typeString("间接邀请商户获得积分")
                    .wallet(wallet)
                    .orderNo(settlementWalletDTO.getOrderNo());
                integralDetailsRepository.save(integralDetails9);
                break;

            case 7://1、b端收入余额
                BigDecimal balance = wallet.getBalance();
                BigDecimal addBalance = balance.add(settlementWalletDTO.getAmount());
                wallet.setBalance(addBalance);
                //添加明细记录
                BalanceDetails balanceDetails = new BalanceDetails();
                balanceDetails.userid(wallet.getUserid())
                    .createdTime(now)
                    .balance(settlementWalletDTO.getAmount())
                    .addBalance(true)
                    .type(4)
                    .typeString("直接邀请服务商收入")
                    .wallet(wallet)
                    .orderNo(settlementWalletDTO.getOrderNo());
                balanceDetailsRepository.save(balanceDetails);
                break;

            case 8://1、b端收入余额
                BigDecimal balance1 = wallet.getBalance();
                BigDecimal addBalance1 = balance1.add(settlementWalletDTO.getAmount());
                wallet.setBalance(addBalance1);
                //添加明细记录
                BalanceDetails balanceDetails1 = new BalanceDetails();
                balanceDetails1.userid(wallet.getUserid())
                    .createdTime(now)
                    .balance(settlementWalletDTO.getAmount())
                    .addBalance(true)
                    .type(4)
                    .typeString("间接邀请服务商收入")
                    .wallet(wallet)
                    .orderNo(settlementWalletDTO.getOrderNo());
                balanceDetailsRepository.save(balanceDetails1);
                break;
            case 9:
                wallet.setBalance(wallet.getBalance().subtract(settlementWalletDTO.getAmount()));
                //增加明细
                BalanceDetails balanceDetaList = new BalanceDetails();
                balanceDetaList.setCreatedTime(Instant.now());
                balanceDetaList.setUserid(wallet.getUserid());
                balanceDetaList.wallet(wallet);
                balanceDetaList.typeString("线下付扣除商户的资金");
                balanceDetaList.setBalance(settlementWalletDTO.getAmount());
                balanceDetailsRepository.save(balanceDetaList);
                break;
            default:
                return;
        }
        wallet.setUpdatedTime(now);
        walletRepository.saveAndFlush(wallet);


    }

    @Override
	//public void settlementWallet(List<SettlementWalletDTO> settlementWalletDTOList) {
	public void settlementWallet(SettlementWalletDTO settlementWalletDTO) {
		Instant now = Instant.now();
		//for (SettlementWalletDTO settlementWalletDTO : settlementWalletDTOList) {
			Wallet wallet = walletRepository.findByUserid(settlementWalletDTO.getUserid());
			switch (settlementWalletDTO.getType()) {
			case 1://1、b端收入余额
				BigDecimal balance = wallet.getBalance();
				BigDecimal addBalance = balance.add(settlementWalletDTO.getAmount());
				wallet.setBalance(addBalance);
				//添加明细记录
				BalanceDetails balanceDetails = new BalanceDetails();
				balanceDetails.userid(wallet.getUserid())
				.createdTime(now)
				.balance(addBalance)
				.addBalance(true)
				.type(4)
				.typeString("卖出商品收入")
				.wallet(wallet)
				.orderNo(settlementWalletDTO.getOrderNo());
				balanceDetailsRepository.save(balanceDetails);
				break;
			case 2://2、c端获得积分
				wallet.setIntegral(wallet.getIntegral().add(settlementWalletDTO.getAmount()));
				//添加明细记录
				IntegralDetails integralDetails = new IntegralDetails();
				integralDetails.userid(wallet.getUserid())
				.createdTime(now)
				.integral(settlementWalletDTO.getAmount())
				.addIntegral(true)
				.type(1)
				.typeString("获得积分")
				.wallet(wallet)
				.orderNo(settlementWalletDTO.getOrderNo());
				integralDetailsRepository.save(integralDetails);
				break;
			case 3://3、b端获得积分
				wallet.setIntegral(wallet.getIntegral().add(settlementWalletDTO.getAmount()));
				//添加明细记录
				IntegralDetails integralDetails3 = new IntegralDetails();
				integralDetails3.userid(wallet.getUserid())
				.createdTime(now)
				.integral(settlementWalletDTO.getAmount())
				.addIntegral(true)
				.type(1)
				.typeString("获得积分")
				.wallet(wallet)
				.orderNo(settlementWalletDTO.getOrderNo());
				integralDetailsRepository.save(integralDetails3);
				break;
			case 4://4、邀请人获得积分
				wallet.setIntegral(wallet.getIntegral().add(settlementWalletDTO.getAmount()));
				//添加明细记录
				IntegralDetails integralDetails4 = new IntegralDetails();
				integralDetails4.userid(wallet.getUserid())
				.createdTime(now)
				.integral(settlementWalletDTO.getAmount())
				.addIntegral(true)
				.type(1)
				.typeString("获得积分")
				.wallet(wallet)
				.orderNo(settlementWalletDTO.getOrderNo());
				integralDetailsRepository.save(integralDetails4);
				break;
			default:
				return;
			}
			wallet.setUpdatedTime(now);
			walletRepository.saveAndFlush(wallet);
		//}
	}

    @Override
     public String commissionCash(CommissionDTO commissionDTO) {
        Instant now = Instant.now();
        BigDecimal balance =null;
        Wallet wallet = walletRepository.findByUserid(commissionDTO.getUserid());
        if (wallet==null){
            throw new BadRequestAlertException("无此用户钱包数据","wallet","userwalletNotFount");
        }
         balance = wallet.getBalance();
        if (balance==null){
            balance=commissionDTO.getAmount();
        }else {
            balance = balance.add(commissionDTO.getAmount());
        }
        wallet.setBalance(balance);
        BigDecimal integral=wallet.getIntegral();
        if (integral==null){
            integral=commissionDTO.getIntegral();
        }else {
            integral=integral.add(commissionDTO.getIntegral());
        }
        wallet.setIntegral(integral);
        wallet.setUpdatedTime(now);
        walletRepository.saveAndFlush(wallet);
        //添加明细记录
        BalanceDetails balanceDetails = new BalanceDetails();
        balanceDetails.userid(wallet.getUserid())
            .createdTime(now)
            .balance(commissionDTO.getAmount())
            .addBalance(true)
            .type(4)
            .typeString("卖出商品收入")
            .wallet(wallet)
            .orderNo(commissionDTO.getOrderNo());
        balanceDetailsRepository.save(balanceDetails);
        return "success";
    }

    @Override
	public void integralToTicket(Long id) {
		Instant now = Instant.now();
		Wallet wallet = walletRepository.findOne(id);
		if (wallet.getIntegral().doubleValue() >= 100.00) {
			int intValue = wallet.getIntegral().intValue() / 100 * 100;
			BigDecimal shifang = new BigDecimal("0.001").multiply(new BigDecimal(intValue));
			BigDecimal quan = shifang.multiply(new BigDecimal("0.9"));
			wallet
				.updatedTime(now)
				.integral(wallet.getIntegral().subtract(shifang))
				.ticket(wallet.getTicket().add(quan));

			integralDetailsRepository.save(new IntegralDetails()
										.createdTime(now)
										.type(2)
										.typeString("积分释放")
										.addIntegral(false)
										.integral(shifang)
										.userid(wallet.getUserid())
										.wallet(wallet));
			ticketDetailsRepository.save(new TicketDetails()
										.createdTime(now)
										.type(2)
										.typeString("积分释放")
										.addTicket(true)
										.ticket(quan)
										.userid(wallet.getUserid())
										.wallet(wallet));
		}
	}

	/**
     * 服务商直接跟间接增值
     * @author 蒋思
     * @version 1.0
     * @param
     */
	@Override
	public void serviceProviderChainReward(Long spid, Long serviceProviderID) {

		//奖励业务
		final BigDecimal reward = new BigDecimal(4000.00);
		Wallet wallet  = walletRepository.findByUserid(serviceProviderID);
		if(wallet == null){
				 throw new BadRequestAlertException("获取当前用户钱包失败 get wallet failed", "", "");
			}
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
			.type(5)
			.typeString("直接或间接服务商奖励")
			.wallet(wallet);
		balanceDetailsRepository.save(balanceDetails);
	}


    /**
     * 线下付添加对应的积分
     * @param settlementWalletDTO
     * @return
     */
    @Override
    public String AddUserIntegral(SettlementWalletDTO settlementWalletDTO) {

        Wallet wallet = walletRepository.findByUserid(settlementWalletDTO.getUserid());
        wallet.setIntegral(wallet.getIntegral().add(settlementWalletDTO.getAmount()));
        walletRepository.saveAndFlush(wallet);
        IntegralDetails integralDetails = integralDetailsRepository.findByUserid(settlementWalletDTO.getUserid());
        integralDetails.setTypeString("消费积分增加");
        integralDetails.setCreatedTime(Instant.now());
        integralDetails.setIntegral(integralDetails.getIntegral().add(settlementWalletDTO.getAmount()));
        return "ok";
    }

    /**
     * 扣除商户的资金
     * @param id
     * @param money
     * @return
     */
    @Override
    public String Deductmoney(Long id, BigDecimal money) {
        Wallet wallet = walletRepository.findByUserid(id);
        wallet.setBalance(wallet.getBalance().subtract(money));
        walletRepository.saveAndFlush(wallet);
        return "ok";
    }


    /**
     *
     * @param id
     * @return
     */
    @Override
    public WalletDTO getwalletInfos(Long id) {
        Wallet wallet= walletRepository.findByUserid(id);
        WalletDTO walletDTO = new WalletDTO();
        BeanUtils.copyProperties(wallet,walletDTO);
        return walletDTO;
    }
}
