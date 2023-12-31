package com.lcwd.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @implNote This application is an API Gat-Way server which route all the request through itself and reach to all 
 * services throughout it. It is create using 'spring-cloud-starter-gateway' and 'spring-boot-starter-webflux' 
 * dependencies.
 */
@SpringBootApplication
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
		System.out.println("API-GATEWAY Started !!");
	}

}
