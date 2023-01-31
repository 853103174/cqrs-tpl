package com.sdnc.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

// @EnableDsMonitor
// @EnableCaching
@SpringBootApplication
@Component("com.sdnc")
public class TradeHallApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeHallApplication.class, args);
		System.out.println("-------->trade-hall start-up success");
	}

}
