package com.microservices.multiplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class MicroservicesMultiplicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicesMultiplicationApplication.class, args);
	}
}
