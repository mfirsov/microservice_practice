package com.mfirsov.grpcclientservice.service;

import io.grpc.*;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogGrpcClientInterceptor implements ClientInterceptor {

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        log.info(methodDescriptor.getFullMethodName());
        return channel.newCall(methodDescriptor, callOptions);
    }
}
