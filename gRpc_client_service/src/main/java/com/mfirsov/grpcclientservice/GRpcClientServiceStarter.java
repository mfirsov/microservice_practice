package com.mfirsov.grpcclientservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;

@SpringBootApplication(exclude = ArtemisAutoConfiguration.class)
public class GRpcClientServiceStarter {
    public static void main(String[] args) {
        SpringApplication.run(GRpcClientServiceStarter.class, args);
    }
}
