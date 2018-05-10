package com.eyun.wallet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eyun.wallet.service.dto.WalletDTO;

@Component
public class QuartzService {
	
	@Autowired
	WalletService walletService;

	@Scheduled(cron = "0 0 7 ? * MON-FRI")
    public void timerToNow(){
		Page<WalletDTO> page = walletService.findAll(null);
		List<WalletDTO> list = page.getContent();
		for (WalletDTO walletDTO : list) {
			if (walletDTO.getIntegral().doubleValue() >= 100.00) {
				walletService.integralToTicket(walletDTO.getId());
			}
		}
    }
	
}
