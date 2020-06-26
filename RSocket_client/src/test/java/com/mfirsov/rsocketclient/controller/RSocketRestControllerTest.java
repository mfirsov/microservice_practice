package com.mfirsov.rsocketclient.controller;

import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@WebMvcTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {RSocketRestController.class})
class RSocketRestControllerTest {

    @MockBean
    RSocketRequester rSocketRequester;

    @Mock
    RSocketRequester.RequestSpec requestSpec;

    @Autowired
    MockMvc mockMvc;

    @Test
    void getBankAccountInfoByUUID() throws Exception {
        UUID stubUuid = UUID.randomUUID();
        BankAccountInfo stubBankAccountInfo = new BankAccountInfo(
                new BankAccount(
                        stubUuid,
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
        Mockito.when(rSocketRequester.route("getBankAccountInfoByUUID")).thenReturn(requestSpec);
        Mockito.when(requestSpec.data(stubUuid)).thenReturn(requestSpec);
        Mockito.when(requestSpec.retrieveMono(BankAccountInfo.class)).thenReturn(Mono.just(stubBankAccountInfo));

        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders.get("/getBankAccountInfoByUUID")
                        .param("uuid", stubUuid.toString())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().isBlank());
        BankAccountInfo actualResult = (BankAccountInfo) result.getAsyncResult();
        assertNotNull(actualResult);
        assertEquals(stubBankAccountInfo, actualResult);

    }

    @Test
    @SuppressWarnings("all")
    void deleteBankAccountInfoByUUID() throws Exception {
        UUID stubUuid = UUID.randomUUID();
        Mockito.when(rSocketRequester.route("deleteBankAccountInfoByUUID")).thenReturn(requestSpec);
        Mockito.when(requestSpec.data(stubUuid)).thenReturn(requestSpec);
        Mockito.when(requestSpec.send()).thenReturn(Mono.empty());

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/deleteBankAccountInfoByUUID").param("uuid", stubUuid.toString()))
                .andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().isBlank());
        assertNull(result.getAsyncResult());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getAllBankAccountInfoList() throws Exception {
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
        Mockito.when(rSocketRequester.route("getAllBankAccountInfoList")).thenReturn(requestSpec);
        Mockito.when(requestSpec.retrieveFlux(BankAccountInfo.class)).thenReturn(Flux.fromIterable(stubList));

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/getAllBankAccountInfoList").accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        System.out.println("Following Object was received: " + result.getAsyncResult());
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertTrue(result.getResponse().getContentAsString().isBlank());
        List<BankAccountInfo> actualResult = (List<BankAccountInfo>) result.getAsyncResult();
        assertNotNull(actualResult);
        assertEquals(stubList, actualResult);
    }
}