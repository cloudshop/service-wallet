package com.eyun.wallet.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eyun.wallet.client.AuthorizedFeignClient;


@AuthorizedFeignClient(name="push")
public interface PushService {

	@PostMapping("/api/push/user/{userid}")
    public void sendPushByUserid(@PathVariable("userid") String userid,@RequestBody String content);
	
}
