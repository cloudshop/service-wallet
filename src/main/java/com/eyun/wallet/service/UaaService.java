package com.eyun.wallet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.eyun.wallet.client.AuthorizedFeignClient;
import com.eyun.wallet.service.dto.UserDTO;

@AuthorizedFeignClient(name="uaa",decode404=true)
public interface UaaService {

	@GetMapping("/api/account")
	public UserDTO getAccount();
	
	@GetMapping("/api/users/{login}")
	public ResponseEntity<UserDTO> getUserByLogin(@PathVariable("login") String login);
	
}
