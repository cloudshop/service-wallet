package com.eyun.wallet.repository;

import com.eyun.wallet.domain.IntegralDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the IntegralDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IntegralDetailsRepository extends JpaRepository<IntegralDetails, Long>, JpaSpecificationExecutor<IntegralDetails> {

    IntegralDetails findByUserid(Long id);

    List<IntegralDetails> findByUseridAndType(Long userId, Integer type);

}
