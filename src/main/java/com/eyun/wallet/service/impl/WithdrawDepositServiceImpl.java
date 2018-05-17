package com.eyun.wallet.service.impl;

import com.eyun.wallet.service.WithdrawDepositService;
import com.eyun.wallet.domain.BalanceDetails;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.domain.WithdrawDeposit;
import com.eyun.wallet.repository.BalanceDetailsRepository;
import com.eyun.wallet.repository.WalletRepository;
import com.eyun.wallet.repository.WithdrawDepositRepository;
import com.eyun.wallet.service.dto.PutForwardDTO;
import com.eyun.wallet.service.dto.UserDTO;
import com.eyun.wallet.service.dto.WithdrawDepositDTO;
import com.eyun.wallet.service.mapper.WithdrawDepositMapper;
import com.eyun.wallet.web.rest.errors.BadRequestAlertException;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing WithdrawDeposit.
 */
@Service
@Transactional
public class WithdrawDepositServiceImpl implements WithdrawDepositService {

    private final Logger log = LoggerFactory.getLogger(WithdrawDepositServiceImpl.class);

    private final WithdrawDepositRepository withdrawDepositRepository;

    private final WithdrawDepositMapper withdrawDepositMapper;
    
    @Autowired
    private WalletRepository walletRepository;
    
    @Autowired
    private BalanceDetailsRepository balanceDetailsRepository;

    public WithdrawDepositServiceImpl(WithdrawDepositRepository withdrawDepositRepository, WithdrawDepositMapper withdrawDepositMapper) {
        this.withdrawDepositRepository = withdrawDepositRepository;
        this.withdrawDepositMapper = withdrawDepositMapper;
    }

    /**
     * Save a withdrawDeposit.
     *
     * @param withdrawDepositDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public WithdrawDepositDTO save(WithdrawDepositDTO withdrawDepositDTO) {
        log.debug("Request to save WithdrawDeposit : {}", withdrawDepositDTO);
        WithdrawDeposit withdrawDeposit = withdrawDepositMapper.toEntity(withdrawDepositDTO);
        withdrawDeposit = withdrawDepositRepository.save(withdrawDeposit);
        return withdrawDepositMapper.toDto(withdrawDeposit);
    }

    /**
     * Get all the withdrawDeposits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WithdrawDepositDTO> findAll(Pageable pageable) {
        log.debug("Request to get all WithdrawDeposits");
        return withdrawDepositRepository.findAll(pageable)
            .map(withdrawDepositMapper::toDto);
    }

    /**
     * Get one withdrawDeposit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public WithdrawDepositDTO findOne(Long id) {
        log.debug("Request to get WithdrawDeposit : {}", id);
        WithdrawDeposit withdrawDeposit = withdrawDepositRepository.findOne(id);
        return withdrawDepositMapper.toDto(withdrawDeposit);
    }

    /**
     * Delete the withdrawDeposit by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete WithdrawDeposit : {}", id);
        //withdrawDepositRepository.delete(id);
    }

	@Override
	public void putForward(PutForwardDTO putForwardDTO, UserDTO user) {
		Wallet wallet = walletRepository.findByUserid(user.getId());
		if (!putForwardDTO.getPassword().equals(wallet.getPassword()) ) {
			throw new BadRequestAlertException("支付密码输入错误！", "password", "password.err");
		}
		if (wallet.getBalance().doubleValue() < putForwardDTO.getMoney().doubleValue()) {
			throw new BadRequestAlertException("余额不足！", "money", "money.err");
		}
		
		//提现逻辑
		Instant now = Instant.now();
		wallet
			.balance(wallet.getBalance().subtract(putForwardDTO.getMoney()))
			.updatedTime(now);
		walletRepository.save(wallet);
		//余额明细
		BalanceDetails balanceDetails = new BalanceDetails();
		balanceDetails
			.balance(putForwardDTO.getMoney())
			.addBalance(false)
			.createdTime(now)
			.wallet(wallet)
			.userid(wallet.getUserid())
			.type(5)
			.typeString("提现");
		balanceDetailsRepository.save(balanceDetails);
		
		//添加提现记录
		WithdrawDeposit withdrawDeposit = new WithdrawDeposit();
		withdrawDeposit
			.createdTime(now)
			.updatedTime(now)
			.cardholder(putForwardDTO.getCardholder())
			.bankcardNumber(putForwardDTO.getBankcardNumber())
			.openingBank(putForwardDTO.getOpeningBank())
			.money(putForwardDTO.getMoney())
			.userid(user.getId())
			.wallet(wallet)
			.status(1)
			.statusString("申请提现");
		withdrawDepositRepository.save(withdrawDeposit);
	}
}
