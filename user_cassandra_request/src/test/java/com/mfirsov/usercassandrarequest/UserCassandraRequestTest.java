package com.mfirsov.usercassandrarequest;

import com.mfirsov.common.model.Address;
import com.mfirsov.common.model.BankAccount;
import com.mfirsov.common.model.BankAccountInfo;
import com.mfirsov.common.model.BankAccountInfoResponse;
import com.mfirsov.usercassandrarequest.controller.UserCassandraController;
import com.mfirsov.usercassandrarequest.entities.AddressEntity;
import com.mfirsov.usercassandrarequest.entities.BankAccountEntity;
import com.mfirsov.usercassandrarequest.entities.BankAccountInfoEntity;
import com.mfirsov.usercassandrarequest.repository.CustomCassandraRepository;
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

import java.util.UUID;

@ExtendWith({MockitoExtension.class})
@WebFluxTest(controllers = UserCassandraController.class)
public class UserCassandraRequestTest {

    @MockBean
    CustomCassandraRepository customCassandraRepository;

    @Autowired
    WebTestClient webClient;

    @Test
    @DisplayName("Verify api/v1/bank_account_info/{uuid} with correct and existing UUID returns BankAccountInfo")
    void successfulResult() {
        BankAccount bankAccountStub = new BankAccount("TestName", "TestLastName", "TestPatronymic", BankAccount.AccountType.DEBIT);
        Address addressStub = new Address("TestStreet", "TestCity", "TestState");
        BankAccountInfoEntity bankAccountInfoEntity = new BankAccountInfoEntity(new BankAccountEntity(bankAccountStub), new AddressEntity(addressStub));
        Mockito.when(customCassandraRepository.findBankAccountInfoByUuid(Mockito.any(UUID.class))).thenReturn(Mono.just(bankAccountInfoEntity));

        webClient.get()
                .uri("api/v1/bank_account_info/{uuid}", bankAccountStub.getUuid().toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccountInfoResponse.class);
    }

    @Test
    @DisplayName("Verify /bankaccountinfo/{uuid} with correct but doesnt exist UUID will return empty BankAccountInfo")
    void uuidDoesntExist() {
        Mockito.when(customCassandraRepository.findBankAccountInfoByUuid(Mockito.any(UUID.class))).thenReturn(Mono.empty());
        webClient.get()
                .uri("api/v1/bank_account_info/{uuid}", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccountInfoResponse.class)
                .isEqualTo(new BankAccountInfoResponse(new BankAccountInfo()));

    }

    @Test
    @DisplayName("Verify /bankaccountinfo/{uuid} with incorrect UUID will return 400:BAD_REQUEST")
    void incorrectUuid() {
        BankAccount bankAccountStub = new BankAccount("TestName", "TestLastName", "TestPatronymic", BankAccount.AccountType.DEBIT);
        Address addressStub = new Address("TestStreet", "TestCity", "TestState");
        BankAccountInfoEntity bankAccountInfoEntity = new BankAccountInfoEntity(new BankAccountEntity(bankAccountStub), new AddressEntity(addressStub));
        Mockito.when(customCassandraRepository.findBankAccountInfoByUuid(Mockito.any(UUID.class))).thenReturn(Mono.just(bankAccountInfoEntity));

        webClient.get()
                .uri("api/v1/bank_account_info/{uuid}", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }



}
