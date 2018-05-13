package com.eyun.wallet.service;

import org.springframework.web.bind.annotation.GetMapping;

import com.eyun.wallet.client.AuthorizedFeignClient;

@AuthorizedFeignClient(name="verify")
public interface VerifyService {

	@GetMapping("/api/verify/wallet")
	public String getVerifyCode();
	
}
