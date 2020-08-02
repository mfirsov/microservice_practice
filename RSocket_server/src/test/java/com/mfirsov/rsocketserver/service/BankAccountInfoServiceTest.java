package com.mfirsov.rsocketserver.service;

import com.mfirsov.common.model.Address;
import com.mfirsov.common.model.BankAccount;
import com.mfirsov.common.model.BankAccountInfo;
import com.mfirsov.rsocketserver.entities.BankAccountInfoEntity;
import com.mfirsov.rsocketserver.repository.CustomCassandraRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@SpringBootTest(properties = {
        "spring.rsocket.server.port=7000",
        "spring.rsocket.server.transport=tcp"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BankAccountInfoServiceTest {

    @MockBean
    CustomCassandraRepository customCassandraRepository;

    @Autowired
    BankAccountInfoService bankAccountInfoService;

    @Autowired
    RSocketRequester.Builder builder;

    @Value("${spring.rsocket.server.port}")
    String rSocketServerPort;

    RSocketRequester rSocketRequester;

    @BeforeAll
    void beforeAll() {
        rSocketRequester = builder
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .connectTcp("localhost", Integer.parseInt(rSocketServerPort))
                .block();
    }

    @Test
    void getBankAccountInfoByUUID() {
        BankAccountInfo stubBankAccountInfo = new BankAccountInfo(
                new BankAccount(
                        UUID.randomUUID(),
                        "TestFirstName",
                        "TestLastName",
                        "TestPatronymic",
                        ThreadLocalRandom.current().nextLong(),
                        BankAccount.AccountType.DEBIT
                ),
                new Address(
                        "TestStreet",
                        "TestCity",
                        "TestState"
                ));
        BankAccountInfoEntity bankAccountInfoEntity = new BankAccountInfoEntity(stubBankAccountInfo);
        when(customCassandraRepository.findById(Mockito.any(UUID.class))).thenReturn(Mono.just(bankAccountInfoEntity));
        Mono<BankAccountInfo> result = rSocketRequester
                .route("getBankAccountInfoByUUID")
                .data(UUID.randomUUID())
                .retrieveMono(BankAccountInfo.class);
        BankAccountInfo actualResult = result.block();
        assertNotNull(actualResult);
        assertEquals(stubBankAccountInfo, actualResult);
    }

    @Test
    void getAllBankAccountInfo() {
        List<BankAccountInfo> stubList = new ArrayList<>();
        stubList.add(new BankAccountInfo(
                new BankAccount(
                        UUID.randomUUID(),
                        "TestFirstName1",
                        "TestLastName1",
                        "TestPatronymic1",
                        ThreadLocalRandom.current().nextLong(),
                        BankAccount.AccountType.DEBIT
                ),
                new Address(
                        "TestStreet",
                        "TestCity",
                        "TestState"
                )));
        stubList.add(new BankAccountInfo(
                new BankAccount(
                        UUID.randomUUID(),
                        "TestFirstName1",
                        "TestLastName1",
                        "TestPatronymic1",
                        ThreadLocalRandom.current().nextLong(),
                        BankAccount.AccountType.CREDIT
                ),
                new Address(
                        "TestStreet1",
                        "TestCity1",
                        "TestState1"
                )));
        List<BankAccountInfoEntity> bankAccountInfoEntities = stubList.stream().map(BankAccountInfoEntity::new).collect(Collectors.toList());
        when(customCassandraRepository.findAll()).thenReturn(Flux.fromIterable(bankAccountInfoEntities));
        Flux<BankAccountInfo> result = rSocketRequester
                .route("getAllBankAccountInfoList")
                .retrieveFlux(BankAccountInfo.class);
        assertNotNull(result);
        assertEquals(stubList, result.collectList().block());

    }

    @Test
    void deleteBankAccountInfoByUUID() {
        when(customCassandraRepository.deleteById(Mockito.any(UUID.class))).thenReturn(Mono.empty());
        Mono<Void> result = rSocketRequester
                .route("deleteBankAccountInfoByUUID")
                .send();
        assertNotNull(result);
    }
}