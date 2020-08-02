package com.mfirsov.grpcservice.controller;

import com.mfirsov.common.model.Address;
import com.mfirsov.common.model.BankAccount;
import com.mfirsov.common.model.BankAccountInfo;
import com.mfirsov.grpcservice.entities.BankAccountInfoEntity;
import com.mfirsov.grpcservice.repository.CustomCassandraRepository;
import com.mfirsov.grpcservice.service.BankAccountInfoProto;
import com.mfirsov.grpcservice.service.BankAccountInfoService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

@ExtendWith({MockitoExtension.class})
@Log4j2
class BankAccountInfoServiceTest {

    @InjectMocks
    BankAccountInfoService bankAccountInfoService;

    @Mock
    CustomCassandraRepository customCassandraRepository;

    @Test
    @DisplayName("Only 1 BankAccountInfo will be return")
    void successfulReturn() {
        List<BankAccountInfo> bankAccountInfos = new ArrayList<>();
        bankAccountInfos.add(new BankAccountInfo(
                UUID.randomUUID(),
                new BankAccount("TestName", "TestLastName", "TestPatronymic", BankAccount.AccountType.DEBIT),
                new Address("TestStreet", "TestCity", "TestState")));
        bankAccountInfos.add(new BankAccountInfo(
                UUID.randomUUID(),
                new BankAccount("TestName1", "TestLastName1", "TestPatronymic1", BankAccount.AccountType.CREDIT),
                new Address("TestStreet1", "TestCity1", "TestState1")));
        List<BankAccountInfoEntity> bankAccountInfoEntities = bankAccountInfos.stream().map(BankAccountInfoEntity::new).collect(Collectors.toList());
        lenient().when(customCassandraRepository.findAll()).thenReturn(Flux.fromIterable(bankAccountInfoEntities));
        BankAccountInfoProto.BankAccountInfoRequest bankAccountInfoRequest = BankAccountInfoProto.BankAccountInfoRequest.newBuilder()
                .setAccountType(BankAccountInfoProto.BankAccount.AccountType.CREDIT.name())
                .build();
        Mono<BankAccountInfoProto.BankAccountInfoResponse> actualBankAccountInfo = bankAccountInfoService.getBankAccountInfo(Mono.just(bankAccountInfoRequest));
        assertNotNull(actualBankAccountInfo.subscribe(bankAccountInfoResponse -> log.info("Following object was received: {}", bankAccountInfoResponse)));

    }

    @Test
    @DisplayName("Empty return")
    void emptyReturn() {
        lenient().when(customCassandraRepository.findAll()).thenReturn(Flux.empty());
        BankAccountInfoProto.BankAccountInfoRequest bankAccountInfoRequest = BankAccountInfoProto.BankAccountInfoRequest.newBuilder()
                .setAccountType(BankAccountInfoProto.BankAccount.AccountType.CREDIT.name())
                .build();
        Mono<BankAccountInfoProto.BankAccountInfoResponse> actualBankAccountInfo = bankAccountInfoService.getBankAccountInfo(Mono.just(bankAccountInfoRequest));
        log.info("Following object was received: {}", actualBankAccountInfo);
        assertNotNull(actualBankAccountInfo.block());
        assertTrue(actualBankAccountInfo.hasElement().block());
    }
}