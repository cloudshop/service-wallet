package com.eyun.wallet.service.impl;

import com.eyun.wallet.service.PayOrderService;
import com.eyun.wallet.domain.PayOrder;
import com.eyun.wallet.repository.PayOrderRepository;
import com.eyun.wallet.service.dto.PayOrderDTO;
import com.eyun.wallet.service.mapper.PayOrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing PayOrder.
 */
@Service
@Transactional
public class PayOrderServiceImpl implements PayOrderService {

    private final Logger log = LoggerFactory.getLogger(PayOrderServiceImpl.class);

    private final PayOrderRepository payOrderRepository;

    private final PayOrderMapper payOrderMapper;

    public PayOrderServiceImpl(PayOrderRepository payOrderRepository, PayOrderMapper payOrderMapper) {
        this.payOrderRepository = payOrderRepository;
        this.payOrderMapper = payOrderMapper;
    }

    /**
     * Save a payOrder.
     *
     * @param payOrderDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public PayOrderDTO save(PayOrderDTO payOrderDTO) {
        log.debug("Request to save PayOrder : {}", payOrderDTO);
        PayOrder payOrder = payOrderMapper.toEntity(payOrderDTO);
        payOrder = payOrderRepository.save(payOrder);
        return payOrderMapper.toDto(payOrder);
    }

    /**
     * Get all the payOrders.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<PayOrderDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PayOrders");
        return payOrderRepository.findAll(pageable)
            .map(payOrderMapper::toDto);
    }

    /**
     * Get one payOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public PayOrderDTO findOne(Long id) {
        log.debug("Request to get PayOrder : {}", id);
        PayOrder payOrder = payOrderRepository.findOne(id);
        return payOrderMapper.toDto(payOrder);
    }

    /**
     * Delete the payOrder by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete PayOrder : {}", id);
        payOrderRepository.delete(id);
    }

	@Override
	public PayOrderDTO findPayOrderByPayNo(String payNo) {
		return payOrderRepository.findPayOrderByPayNo(payNo);
	}
}
