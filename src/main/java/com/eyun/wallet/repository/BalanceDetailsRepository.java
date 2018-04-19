package com.eyun.wallet.repository;

import com.eyun.wallet.domain.BalanceDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BalanceDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BalanceDetailsRepository extends JpaRepository<BalanceDetails, Long>, JpaSpecificationExecutor<BalanceDetails> {

}
