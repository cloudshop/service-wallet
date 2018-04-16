package com.eyun.wallet.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eyun.wallet.client.AuthorizedUserFeignClient;
import com.eyun.wallet.service.dto.ProOrderDTO;

@AuthorizedUserFeignClient(name="order")
public interface OrderService {

	@GetMapping("/api/findOrderByOrderNo/{orderNo}")
    public ProOrderDTO findOrderByOrderNo(@PathVariable("orderNo")String orderNo);
    
}
