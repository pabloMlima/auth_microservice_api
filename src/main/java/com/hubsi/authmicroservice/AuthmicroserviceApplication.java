package com.hubsi.authmicroservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Authmicroservice API",
				version = "1.0",
				description = "É uma api de autenticação."
		)
)
public class AuthmicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthmicroserviceApplication.class, args);
	}

}
