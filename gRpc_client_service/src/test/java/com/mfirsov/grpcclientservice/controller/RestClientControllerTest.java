package com.mfirsov.grpcclientservice.controller;

import com.mfirsov.grpcclientservice.client.GRpcClient;
import com.mfirsov.grpcclientservice.model.BankAccountInfosResponse;
import com.mfirsov.grpcservice.service.BankAccountInfoProto;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@ExtendWith({MockitoExtension.class})
@WebFluxTest(controllers = RestClientController.class)
class RestClientControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    GRpcClient gRpcClient;

    @Test
    @DisplayName("Verify RestController will return correct value")
    void successfulResponse() {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        long long1 = ThreadLocalRandom.current().nextLong();
        long long2 = ThreadLocalRandom.current().nextLong();
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
                                .setAccountNumber(long1)
                                .setFirstName("TestFirstName")
                                .setLastName("TestLastName")
                                .setPatronymic("TestPatronymic")
                                .setUuid(uuid1.toString())
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
                                .setAccountNumber(long2)
                                .setFirstName("TestFirstName1")
                                .setLastName("TestLastName1")
                                .setPatronymic("TestPatronymic1")
                                .setUuid(uuid2.toString())
                                .build()))
                .build();
        List<BankAccountInfo> bankAccountInfos = new ArrayList<>();
        bankAccountInfos.add(new BankAccountInfo(
                null,
                new BankAccount(uuid1, "TestFirstName", "TestLastName", "TestPatronymic", long1, BankAccount.AccountType.DEBIT),
                new Address("TestStreet", "TestCity", "TestState")));
        bankAccountInfos.add(new BankAccountInfo(
                null,
                new BankAccount(uuid2, "TestFirstName1", "TestLastName1", "TestPatronymic1", long2, BankAccount.AccountType.DEBIT),
                new Address("TestStreet1", "TestCity1", "TestState1")));
        BankAccountInfosResponse expectedResponse = new BankAccountInfosResponse();
        expectedResponse.setBankAccountInfos(bankAccountInfos);
        Mockito.when(gRpcClient.getBankAccountInfo(Mockito.anyString())).thenReturn(Mono.just(bankAccountInfoResponse));
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/bank_account_infos")
                        .queryParam("accountType", BankAccount.AccountType.DEBIT.name())
                        .build())
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON)
                .expectBody(BankAccountInfosResponse.class)
                .isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("Verify that RestController returns empty object when no one BankAccountInfo found")
    void emptyResponse() {
        BankAccountInfoProto.BankAccountInfoResponse stubResponse = BankAccountInfoProto.BankAccountInfoResponse.newBuilder().build();
        Mockito.when(gRpcClient.getBankAccountInfo(BankAccount.AccountType.DEBIT.name())).thenReturn(Mono.just(stubResponse));

        webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/bank_account_infos")
                        .queryParam("accountType", BankAccount.AccountType.DEBIT.name())
                        .build())
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(BankAccountInfosResponse.class)
                .isEqualTo(BankAccountInfosResponse
                        .builder()
                        .bankAccountInfos(new ArrayList<>())
                        .build());
    }

    @Test
    @DisplayName("Verify RestController returns in case of incorrect AccountType")
    void incorrectAccountType() {
        BankAccountInfoProto.BankAccountInfoResponse stubResponse = BankAccountInfoProto.BankAccountInfoResponse.newBuilder().build();
        Mockito.when(gRpcClient.getBankAccountInfo(Mockito.anyString())).thenReturn(Mono.just(stubResponse));
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/bank_account_infos")
                        .queryParam("accountType", "not_a_account_type")
                        .build())
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("No enum constant com.mfirsov.model.BankAccount.AccountType.not_a_account_type");
    }
}