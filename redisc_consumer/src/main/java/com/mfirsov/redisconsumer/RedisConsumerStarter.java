package com.mfirsov.redisconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisConsumerStarter {
    public static void main(String[] args) {
        SpringApplication.run(RedisConsumerStarter.class, args);
    }
}
