package com.eyun.wallet.repository;

import com.eyun.wallet.domain.WalletDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WalletDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletDetailsRepository extends JpaRepository<WalletDetails, Long>, JpaSpecificationExecutor<WalletDetails> {

}
