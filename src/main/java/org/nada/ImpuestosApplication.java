package org.nada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// XXX: https://stackoverflow.com/questions/23636368/how-to-disable-spring-security-login-screen
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ImpuestosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ImpuestosApplication.class, args);
	}

}
