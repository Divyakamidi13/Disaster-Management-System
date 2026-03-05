package com.varsha.disastermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.cache.annotation.EnableCaching;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableScheduling
@EnableCaching
@ComponentScan(basePackages = {"com.varsha.disastermanagement", "com.gfg.demo.disastermonitor"})
@EnableJpaRepositories(basePackages = {"com.varsha.disastermanagement", "com.gfg.demo.disastermonitor"})
public class DisastermanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisastermanagementApplication.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
