package com.mfirsov.grpcclientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfirsov.grpcclientservice.client.GRpcClient;
import com.mfirsov.grpcclientservice.model.BankAccountInfosResponse;
import com.mfirsov.grpcclientservice.service.BankAccountInfoResponse;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {RestClientController.class})
@WebMvcTest
@AutoConfigureMockMvc
class RestClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    GRpcClient gRpcClient;

    @Test
    @DisplayName("Verify RestController will return correct value")
    void successfulResponse() throws Exception {
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        long long1 = ThreadLocalRandom.current().nextLong();
        long long2 = ThreadLocalRandom.current().nextLong();
        BankAccountInfoResponse bankAccountInfoResponse = BankAccountInfoResponse
                .newBuilder()
                .addBankAccountInfo(com.mfirsov.grpcclientservice.service.BankAccountInfo
                        .newBuilder()
                        .setAddress(com.mfirsov.grpcclientservice.service.Address
                                .newBuilder()
                                .setCity("TestCity")
                                .setStreet("TestStreet")
                                .setState("TestState")
                                .build())
                        .setBankAccount(com.mfirsov.grpcclientservice.service.BankAccount
                                .newBuilder()
                                .setAccountTypeValue(com.mfirsov.grpcclientservice.service.BankAccount.AccountType.DEBIT_VALUE)
                                .setAccountNumber(long1)
                                .setFirstName("TestFirstName")
                                .setLastName("TestLastName")
                                .setPatronymic("TestPatronymic")
                                .setUuid(uuid1.toString())
                                .build()))
                .addBankAccountInfo(com.mfirsov.grpcclientservice.service.BankAccountInfo
                        .newBuilder()
                        .setAddress(com.mfirsov.grpcclientservice.service.Address
                                .newBuilder()
                                .setCity("TestCity1")
                                .setStreet("TestStreet1")
                                .setState("TestState1")
                                .build())
                        .setBankAccount(com.mfirsov.grpcclientservice.service.BankAccount
                                .newBuilder()
                                .setAccountTypeValue(com.mfirsov.grpcclientservice.service.BankAccount.AccountType.DEBIT_VALUE)
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
        Mockito.when(gRpcClient.getBankAccountInfo(BankAccount.AccountType.DEBIT.name())).thenReturn(bankAccountInfoResponse);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/getBankAccountInfos")
                        .param("accountType", BankAccount.AccountType.DEBIT.name())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        BankAccountInfosResponse actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), BankAccountInfosResponse.class);
        assertNotNull(actualResponse);
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Verify that RestController returns empty object when no one BankAccountInfo found")
    void emptyResponse() throws Exception {
        BankAccountInfoResponse stubResponse = BankAccountInfoResponse.newBuilder().build();
        Mockito.when(gRpcClient.getBankAccountInfo(BankAccount.AccountType.DEBIT.name())).thenReturn(stubResponse);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/getBankAccountInfos")
                        .param("accountType", BankAccount.AccountType.DEBIT.name())
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        ObjectMapper objectMapper = new ObjectMapper();
        BankAccountInfosResponse actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), BankAccountInfosResponse.class);
        assertNotNull(actualResponse);
        assertEquals(new BankAccountInfosResponse(new ArrayList<>()), actualResponse);
    }

    @Test
    @DisplayName("Verify RestController returns in case of incorrect AccountType")
    void incorrectAccountType() throws Exception {
        BankAccountInfoResponse stubResponse = BankAccountInfoResponse.newBuilder().build();
        Mockito.when(gRpcClient.getBankAccountInfo(BankAccount.AccountType.DEBIT.name())).thenReturn(stubResponse);
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/getBankAccountInfos")
                        .param("accountType", "not_a_account_type")
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
        assertEquals("No enum constant com.mfirsov.model.BankAccount.AccountType.not_a_account_type", result.getResponse().getContentAsString());
    }
}