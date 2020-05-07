package com.mfirsov.grpcservice.configuration;

import com.mfirsov.grpcservice.service.LogGrpcInterceptor;
import net.devh.boot.grpc.server.interceptor.GlobalServerInterceptorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GRpcServiceConfiguration {

    @Bean
    public GlobalServerInterceptorConfigurer globalServerInterceptorConfigurer() {
        return globalServerInterceptorRegistry -> globalServerInterceptorRegistry.addServerInterceptors(new LogGrpcInterceptor());
    }


}
