package com.eyun.wallet.repository;

import com.eyun.wallet.domain.WithdrawDeposit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WithdrawDeposit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WithdrawDepositRepository extends JpaRepository<WithdrawDeposit, Long>, JpaSpecificationExecutor<WithdrawDeposit> {

}
