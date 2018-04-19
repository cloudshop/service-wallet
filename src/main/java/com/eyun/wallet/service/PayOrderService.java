package com.eyun.wallet.service;

import com.eyun.wallet.service.dto.PayOrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PayOrder.
 */
public interface PayOrderService {

    /**
     * Save a payOrder.
     *
     * @param payOrderDTO the entity to save
     * @return the persisted entity
     */
    PayOrderDTO save(PayOrderDTO payOrderDTO);

    /**
     * Get all the payOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<PayOrderDTO> findAll(Pageable pageable);

    /**
     * Get the "id" payOrder.
     *
     * @param id the id of the entity
     * @return the entity
     */
    PayOrderDTO findOne(Long id);

    /**
     * Delete the "id" payOrder.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

	PayOrderDTO findPayOrderByPayNo(String payNo);
}
