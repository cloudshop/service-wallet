package com.eyun.wallet.repository;

import com.eyun.wallet.domain.WithdrawDeposit;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WithdrawDeposit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WithdrawDepositRepository extends JpaRepository<WithdrawDeposit, Long>, JpaSpecificationExecutor<WithdrawDeposit> {
	@Query(value = "SELECT * FROM `withdraw_deposit` WHERE DATE(`created_time`) >= DATE(?1) AND DATE(`created_time`) <= DATE(?2)",nativeQuery = true)
	public List<WithdrawDeposit> findSubDetil(String frist,String last);
	
	@Query(value = "SELECT * FROM `withdraw_deposit` WHERE DATE(`created_time`) <= DATE(?1)",nativeQuery = true)
	public List<WithdrawDeposit> findLefDetil(String last);
	
	@Query(value = "SELECT * FROM `withdraw_deposit` WHERE DATE(`created_time`) >= DATE(?1)",nativeQuery = true)
	public List<WithdrawDeposit> findRitDetil(String last);
}
