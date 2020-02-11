package com.nutanix.hack.seeksmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableJpaAuditing
@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
public class ConcurApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcurApplication.class, args);
	}

}
