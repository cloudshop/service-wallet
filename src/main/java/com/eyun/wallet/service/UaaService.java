package com.eyun.wallet.service;

import org.springframework.web.bind.annotation.GetMapping;

import com.eyun.wallet.client.AuthorizedFeignClient;
import com.eyun.wallet.service.dto.UserDTO;

@AuthorizedFeignClient(name="uaa")
public interface UaaService {

	@GetMapping("/api/account")
	public UserDTO getAccount();
	
}
