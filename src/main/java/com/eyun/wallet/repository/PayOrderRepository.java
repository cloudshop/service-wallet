package com.eyun.wallet.repository;

import com.eyun.wallet.domain.PayOrder;
import com.eyun.wallet.service.dto.PayOrderDTO;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PayOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayOrderRepository extends JpaRepository<PayOrder, Long>, JpaSpecificationExecutor<PayOrder> {

	PayOrderDTO findByPayNo(String payNo);

	PayOrder findByOrderNo(String orderNo);

}
