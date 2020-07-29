package com.mfirsov.kafkaproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaProducerStarter {
    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerStarter.class, args);
    }
}
