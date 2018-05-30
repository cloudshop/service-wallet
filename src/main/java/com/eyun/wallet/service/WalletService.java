package com.eyun.wallet.service;

import com.eyun.wallet.domain.BalanceDTO;
import com.eyun.wallet.domain.GiveIntegralDTO;
import com.eyun.wallet.domain.PayOrder;
import com.eyun.wallet.domain.Wallet;
import com.eyun.wallet.domain.WithdrawDeposit;
import com.eyun.wallet.service.dto.*;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Wallet.
 */
public interface WalletService {

    /**
     * Save a wallet.
     *
     * @param walletDTO the entity to save
     * @return the persisted entity
     */
    WalletDTO save(WalletDTO walletDTO);

    /**
     * Get all the wallets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<WalletDTO> findAll(Pageable pageable);

    /**
     * Get the "id" wallet.
     *
     * @param id the id of the entity
     * @return the entity
     */
    WalletDTO findOne(Long id);

    /**
     * Delete the "id" wallet.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    Wallet rechargeBalance(BalanceDTO balanceDTO);

    Wallet findByUserid(Long userid);

    String giveIntegral(Long fromUserId, Long toUserId, BigDecimal integral);

    PayOrder balancePay(Long walletId, BigDecimal balance, BigDecimal ticket, String orderNo);

    void update(Wallet wallet);

    void serviceProviderReward(Long spid, Long incrBID);

    void incrementUserReward(Long incrementUserID, Long incrementBusinessID);

    void settlementWallet(SettlementWalletDTO settlementWalletDTO);

    String commissionCash(CommissionDTO commissionDTO);

    void integralToTicket(Long id);

    public void serviceProviderChainReward(Long spid, Long serviceProviderID);

    String Deductmoney(Long id, BigDecimal money);


    String AddUserIntegral(SettlementWalletDTO settlementWalletDTO);

    WalletDTO getwalletInfos(Long id);

    void batchintegrals(SettlementWalletDTO settlementWalletDTO);
    
}
