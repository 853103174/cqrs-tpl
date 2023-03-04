package com.sdnc.trade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// @EnableDsMonitor
// @EnableCaching
@SpringBootApplication
@ComponentScan("com.sdnc")
public class TradeHallApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradeHallApplication.class, args);
		System.out.println("-------->trade-hall start-up success");
	}

}
