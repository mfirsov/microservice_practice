package com.mfirsov.grpcclientservice.client;

import com.mfirsov.grpcclientservice.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class GRpcClientTest {

    @InjectMocks
    GRpcClient gRpcClient;

    @Mock
    BankAccountInfoServiceGrpc.BankAccountInfoServiceBlockingStub bankAccountInfoServiceStub;

    @Test
    void test() {
        BankAccountInfoResponse bankAccountInfoResponse = BankAccountInfoResponse
                .newBuilder()
                .addBankAccountInfo(BankAccountInfo
                        .newBuilder()
                        .setAddress(Address
                                .newBuilder()
                                .setCity("TestCity")
                                .setStreet("TestStreet")
                                .setState("TestState")
                                .build())
                        .setBankAccount(BankAccount
                                .newBuilder()
                                .setAccountTypeValue(BankAccount.AccountType.DEBIT_VALUE)
                                .setAccountNumber(ThreadLocalRandom.current().nextLong())
                                .setFirstName("TestFirstName")
                                .setLastName("TestLastName")
                                .setPatronymic("TestPatronymic")
                                .setUuid(UUID.randomUUID().toString())
                                .build()))
                .addBankAccountInfo(BankAccountInfo
                        .newBuilder()
                        .setAddress(Address
                                .newBuilder()
                                .setCity("TestCity1")
                                .setStreet("TestStreet1")
                                .setState("TestState1")
                                .build())
                        .setBankAccount(BankAccount
                                .newBuilder()
                                .setAccountTypeValue(BankAccount.AccountType.DEBIT_VALUE)
                                .setAccountNumber(ThreadLocalRandom.current().nextLong())
                                .setFirstName("TestFirstName1")
                                .setLastName("TestLastName1")
                                .setPatronymic("TestPatronymic1")
                                .setUuid(UUID.randomUUID().toString())
                                .build()))
                .build();
        when(bankAccountInfoServiceStub.getBankAccountInfo(Mockito.any(BankAccountInfoRequest.class))).thenReturn(bankAccountInfoResponse);
        assertEquals(bankAccountInfoResponse, gRpcClient.getBankAccountInfo(BankAccount.AccountType.DEBIT.name()));
    }
}