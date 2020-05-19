package com.mfirsov.grpcclientservice.client;

import com.mfirsov.grpcclientservice.service.BankAccountInfoRequest;
import com.mfirsov.grpcclientservice.service.BankAccountInfoResponse;
import com.mfirsov.grpcclientservice.service.BankAccountInfoServiceGrpc;
import com.mfirsov.grpcclientservice.service.LogGrpcClientInterceptor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class GRpcClient {

    @GrpcClient(value = "grpc-client-service", interceptors = {LogGrpcClientInterceptor.class})
    private BankAccountInfoServiceGrpc.BankAccountInfoServiceBlockingStub bankAccountInfoServiceStub;

    public BankAccountInfoResponse getBankAccountInfo(String accountType) {
        BankAccountInfoRequest request = BankAccountInfoRequest.newBuilder().setAccountType(accountType).build();
        return bankAccountInfoServiceStub.getBankAccountInfo(request);
    }

}
