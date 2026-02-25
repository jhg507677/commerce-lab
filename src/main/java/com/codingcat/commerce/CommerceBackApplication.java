package com.codingcat.commerce;

import com.codingcat.commerce.module.security.token.TokenProperties;
import com.codingcat.commerce.module.security.token.TokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CommerceBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceBackApplication.class, args);
	}
}
