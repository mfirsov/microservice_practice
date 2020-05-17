package com.mfirsov.kafkaproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaProducerStarter {
    public static void main(String[] args) {
        SpringApplication.run(KafkaProducerStarter.class, args);
    }
}
