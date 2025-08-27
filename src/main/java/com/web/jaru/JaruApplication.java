package com.web.jaru;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JaruApplication {

	public static void main(String[] args) {
		SpringApplication.run(JaruApplication.class, args);
	}

}
