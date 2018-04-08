package com.eyun.wallet.service.impl;

import com.eyun.wallet.service.WalletService;
import com.eyun.wallet.domain.BalanceDTO;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.domain.WalletDetails;
import com.eyun.wallet.repository.WalletDetailsRepository;
import com.eyun.wallet.repository.WalletRepository;
import com.eyun.wallet.service.dto.WalletDTO;
import com.eyun.wallet.service.mapper.WalletMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final WalletDetailsRepository walletDetailsRepository;
    
    private final WalletRepository walletRepository;

    private final WalletMapper walletMapper;

    public WalletServiceImpl(WalletRepository walletRepository, WalletMapper walletMapper, WalletDetailsRepository walletDetailsRepository) {
    	this.walletDetailsRepository = walletDetailsRepository;
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
    }

    /**
     * Save a wallet.
     *
     * @param walletDTO the entity to save
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
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<WalletDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Wallets");
        return walletRepository.findAll(pageable)
            .map(walletMapper::toDto);
    }

    /**
     * Get one wallet by id.
     *
     * @param id the id of the entity
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
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Wallet : {}", id);
        walletRepository.delete(id);
    }

	@Override
	public Wallet rechargeBalance(BalanceDTO balanceDTO) {
		Wallet wallet = walletRepository.findByUserid(balanceDTO.getUserid());
		BigDecimal balance = wallet.getBalance();//获取当前账户余额
		BigDecimal balance1 = balance.add(balanceDTO.getMoney());//添加金额
		wallet.setBalance(balance1);
		wallet.setUpdatedTime(Instant.now());
		wallet.setVersion(wallet.getVersion()+1);
		walletRepository.save(wallet);
		//添加账户明细
		WalletDetails wd = new WalletDetails();
		wd.setUserid(wallet.getUserid());
		wd.setType(1);//1:充值
		wd.setCreatedTime(Instant.now());
		wd.setBalance(balanceDTO.getMoney());
		wd.setOrderNo(balanceDTO.getOrderNo());
		wd.setWallet(wallet);
		walletDetailsRepository.save(wd);
		return wallet;
	}
}
