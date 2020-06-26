package com.mfirsov.rsocketserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RSocketServerStarter {
    public static void main(String[] args) {
        SpringApplication.run(RSocketServerStarter.class, args);
    }
}
