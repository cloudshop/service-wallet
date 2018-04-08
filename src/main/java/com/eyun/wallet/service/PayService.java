package com.eyun.wallet.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eyun.wallet.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name="pay")
public interface PayService {

	@GetMapping("/api/alipay/order/{orderNo}")
	public String queryOrder (@PathVariable("orderNo") String orderNo);
	
}
