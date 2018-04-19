package com.eyun.wallet.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.eyun.wallet.client.AuthorizedUserFeignClient;
import com.eyun.wallet.service.dto.ProOrderDTO;

@AuthorizedUserFeignClient(name="order")
public interface OrderService {

	@GetMapping("/api/findOrderByOrderNo/{orderNo}")
    public ProOrderDTO findOrderByOrderNo(@PathVariable("orderNo")String orderNo);
	
	@PutMapping("/api/pro-order/wallet/notify")
	public void fun();
    
}
