package com.mfirsov.grpcservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mfirsov.repository", "com.mfirsov.grpcservice"})
public class GRpcServiceStarter {
    public static void main(String[] args) {
        SpringApplication.run(GRpcServiceStarter.class, args);
    }
}
