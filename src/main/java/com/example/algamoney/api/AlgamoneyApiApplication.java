package com.example.algamoney.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
/**
 * Classe de inicialização da api
 * @author Rodrigo
 * @EnableConfigurationProperties --> Informando classe de configuração
 */
@SpringBootApplication
@EnableConfigurationProperties(AlgamoneyApiApplication.class)
public class AlgamoneyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlgamoneyApiApplication.class, args);
	}
}
