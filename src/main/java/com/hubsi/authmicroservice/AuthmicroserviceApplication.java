package com.hubsi.authmicroservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Minha API",
				version = "1.0",
				description = "Documentação da API com SpringDoc"
		)
)
public class AuthmicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthmicroserviceApplication.class, args);
	}

}
