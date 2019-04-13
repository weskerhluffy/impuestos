package org.nada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class ImpuestosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImpuestosApplication.class, args);
	}

}
