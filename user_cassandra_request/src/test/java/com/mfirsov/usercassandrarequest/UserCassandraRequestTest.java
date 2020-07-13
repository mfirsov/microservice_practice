package com.mfirsov.usercassandrarequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import com.mfirsov.model.BankAccountInfoResponse;
import com.mfirsov.repository.CustomCassandraRepository;
import com.mfirsov.usercassandrarequest.controller.UserCassandraController;
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

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Verify api/v1/bank_account_info/{uuid} with correct and existing UUID returns BankAccountInfo")
    void successfulResult() throws Exception {
        BankAccount bankAccountStub = new BankAccount("TestName", "TestLastName", "TestPatronymic", BankAccount.AccountType.DEBIT);
        Address addressStub = new Address("TestStreet", "TestCity", "TestState");
        BankAccountInfo bankAccountInfo = new BankAccountInfo(bankAccountStub, addressStub);
        Mockito.when(customCassandraRepository.findBankAccountInfoByUuid(Mockito.any(UUID.class))).thenReturn(Mono.just(bankAccountInfo));

//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/bankaccountinfo/{uuid}", bankAccountStub.getUuid().toString()).accept(MediaType.APPLICATION_JSON_VALUE))
//                .andReturn();

        webClient.get()
                .uri("api/v1/bank_account_info/{uuid}", bankAccountStub.getUuid().toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccountInfoResponse.class);

//        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
//        Assertions.assertEquals(bankAccountInfo, objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BankAccountInfoResponse.class).getBankAccountInfo());
    }

    @Test
    @DisplayName("Verify /bankaccountinfo/{uuid} with correct but doesnt exist UUID will return empty BankAccountInfo")
    void uuidDoesntExist() throws Exception {
        Mockito.when(customCassandraRepository.findBankAccountInfoByUuid(Mockito.any(UUID.class))).thenReturn(Mono.empty());
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/bankaccountinfo/{uuid}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON_VALUE))
//                .andReturn();

        webClient.get()
                .uri("api/v1/bank_account_info/{uuid}", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccountInfoResponse.class)
                .isEqualTo(new BankAccountInfoResponse(new BankAccountInfo()));

//        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
//        Assertions.assertNotNull(mvcResult.getResponse().getContentAsString());
//        BankAccountInfo actualBankAccountInfo = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BankAccountInfoResponse.class).getBankAccountInfo();
//        Assertions.assertAll(() -> {
//            Assertions.assertNull(actualBankAccountInfo.getAddress());
//            Assertions.assertNull(actualBankAccountInfo.getBankAccount());
//        });
    }

    @Test
    @DisplayName("Verify /bankaccountinfo/{uuid} with incorrect UUID will return 400:BAD_REQUEST")
    void incorrectUuid() throws Exception {
        BankAccount bankAccountStub = new BankAccount("TestName", "TestLastName", "TestPatronymic", BankAccount.AccountType.DEBIT);
        Address addressStub = new Address("TestStreet", "TestCity", "TestState");
        BankAccountInfo bankAccountInfo = new BankAccountInfo(bankAccountStub, addressStub);
        Mockito.when(customCassandraRepository.findBankAccountInfoByUuid(Mockito.any(UUID.class))).thenReturn(Mono.just(bankAccountInfo));
//        MvcResult mvcResult = mockMvc
//                .perform(MockMvcRequestBuilders.get("/bankaccountinfo/{uuid}", "dfgdfg").accept(MediaType.APPLICATION_JSON_VALUE))
//                .andReturn();

        webClient.get()
                .uri("api/v1/bank_account_info/{uuid}", UUID.randomUUID().toString())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();

//        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
//        Assertions.assertTrue(mvcResult.getResponse().getContentAsString().isBlank());
    }



}
