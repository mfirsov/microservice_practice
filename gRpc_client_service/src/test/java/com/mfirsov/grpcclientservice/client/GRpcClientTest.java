package com.mfirsov.grpcclientservice.client;

import com.mfirsov.grpcservice.service.BankAccountInfoProto;
import com.mfirsov.grpcservice.service.BankAccountInfoServiceGrpc;
import com.mfirsov.grpcservice.service.ReactorBankAccountInfoServiceGrpc;
import io.grpc.ManagedChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class GRpcClientTest {

    @InjectMocks
    GRpcClient gRpcClient;

    @Mock
    ReactorBankAccountInfoServiceGrpc.ReactorBankAccountInfoServiceStub bankAccountInfoServiceStub;

    @Mock
    ManagedChannel channel;

    @Test
    void test() {
        BankAccountInfoProto.BankAccountInfoResponse bankAccountInfoResponse = BankAccountInfoProto.BankAccountInfoResponse
                .newBuilder()
                .addBankAccountInfo(BankAccountInfoProto.BankAccountInfo
                        .newBuilder()
                        .setAddress(BankAccountInfoProto.Address
                                .newBuilder()
                                .setCity("TestCity")
                                .setStreet("TestStreet")
                                .setState("TestState")
                                .build())
                        .setBankAccount(BankAccountInfoProto.BankAccount
                                .newBuilder()
                                .setAccountTypeValue(BankAccountInfoProto.BankAccount.AccountType.DEBIT_VALUE)
                                .setAccountNumber(ThreadLocalRandom.current().nextLong())
                                .setFirstName("TestFirstName")
                                .setLastName("TestLastName")
                                .setPatronymic("TestPatronymic")
                                .setUuid(UUID.randomUUID().toString())
                                .build()))
                .addBankAccountInfo(BankAccountInfoProto.BankAccountInfo
                        .newBuilder()
                        .setAddress(BankAccountInfoProto.Address
                                .newBuilder()
                                .setCity("TestCity1")
                                .setStreet("TestStreet1")
                                .setState("TestState1")
                                .build())
                        .setBankAccount(BankAccountInfoProto.BankAccount
                                .newBuilder()
                                .setAccountTypeValue(BankAccountInfoProto.BankAccount.AccountType.DEBIT_VALUE)
                                .setAccountNumber(ThreadLocalRandom.current().nextLong())
                                .setFirstName("TestFirstName1")
                                .setLastName("TestLastName1")
                                .setPatronymic("TestPatronymic1")
                                .setUuid(UUID.randomUUID().toString())
                                .build()))
                .build();
        lenient().when(bankAccountInfoServiceStub.getBankAccountInfo(Mockito.any(BankAccountInfoProto.BankAccountInfoRequest.class)))
                .thenReturn(Mono.just(bankAccountInfoResponse));
        assertNotNull(gRpcClient.getBankAccountInfo(BankAccountInfoProto.BankAccount.AccountType.DEBIT.name()));
    }
}