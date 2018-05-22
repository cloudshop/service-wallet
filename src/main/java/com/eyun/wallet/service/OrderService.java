package com.eyun.wallet.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eyun.wallet.client.AuthorizedUserFeignClient;
import com.eyun.wallet.service.dto.FaceOrderDTO;
import com.eyun.wallet.service.dto.PayNotifyDTO;
import com.eyun.wallet.service.dto.ProOrderDTO;

@AuthorizedUserFeignClient(name="order",decode404=true)
public interface OrderService {

	@GetMapping("/api/findOrderByOrderNo/{orderNo}")
    public ProOrderDTO findOrderByOrderNo(@PathVariable("orderNo")String orderNo);
	
	@PutMapping("/api/pro-order/pay/notify")
    public ResponseEntity<ProOrderDTO> proOrderNotify(@RequestBody PayNotifyDTO payNotifyDTO);
	
	@GetMapping("/face-order/findFaceOrderByOrderNo/{orderNo}")
    public ResponseEntity<FaceOrderDTO> findFaceOrderByOrderNo(@PathVariable String orderNo);
}
