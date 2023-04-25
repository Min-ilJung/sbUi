package com.xe.ui;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthCheckController {
	
	@GetMapping("/healthy")
	public String healthy() {
		log.info("Finish health check successfully");
		String path = System.getProperty("user.dir");
		return "{\"status\":\"UP\",\"path\":\""+path+"\"}";
	}
}
