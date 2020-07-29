package com.mfirsov.bankaccountgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class BankAccountGeneratorStarter {
    public static void main(String[] args) {
        SpringApplication.run(BankAccountGeneratorStarter.class, args);
    }
}
