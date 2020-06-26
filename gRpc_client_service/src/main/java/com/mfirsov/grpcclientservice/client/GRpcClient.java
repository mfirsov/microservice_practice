package com.mfirsov.grpcclientservice.client;

import com.mfirsov.grpcservice.service.BankAccountInfoProto;
import com.mfirsov.grpcservice.service.ReactorBankAccountInfoServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class GRpcClient {

    private static final int PORT = 9999;

    private final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", PORT).usePlaintext().build();
    private final ReactorBankAccountInfoServiceGrpc.ReactorBankAccountInfoServiceStub bankAccountInfoServiceStub = ReactorBankAccountInfoServiceGrpc.newReactorStub(channel);

    public Mono<BankAccountInfoProto.BankAccountInfoResponse> getBankAccountInfo(String accountType) {
        BankAccountInfoProto.BankAccountInfoRequest request = BankAccountInfoProto.BankAccountInfoRequest.newBuilder().setAccountType(accountType).build();
        return bankAccountInfoServiceStub.getBankAccountInfo(request);
    }

}
