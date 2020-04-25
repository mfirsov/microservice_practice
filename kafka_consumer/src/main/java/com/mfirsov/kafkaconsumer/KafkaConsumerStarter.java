package com.mfirsov.kafkaconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class KafkaConsumerStarter {
    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerStarter.class, args);
    }
}
