package com.ingrupo.cpfvalidator;

import com.ingrupo.cpfvalidator.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class CpfValidatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CpfValidatorApplication.class, args);
	}

}
