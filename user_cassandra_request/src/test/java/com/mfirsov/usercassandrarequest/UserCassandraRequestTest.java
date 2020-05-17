package com.mfirsov.usercassandrarequest;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import com.mfirsov.model.BankAccountInfoResponse;
import com.mfirsov.repository.CustomCassandraRepository;
import com.mfirsov.usercassandrarequest.controller.UserCassandraController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jackson.JsonObjectDeserializer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.UUID;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {UserCassandraController.class})
@WebMvcTest
@AutoConfigureMockMvc
public class UserCassandraRequestTest {

    @MockBean
    CustomCassandraRepository customCassandraRepository;

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Verify /bankaccountinfo/{uuid} with correct and existing UUID returns BankAccountInfo")
    void successfulResult() throws Exception {
        BankAccount bankAccountStub = new BankAccount("TestName", "TestLastName", "TestPatronymic", BankAccount.AccountType.DEBIT);
        Address addressStub = new Address("TestStreet", "TestCity", "TestState");
        BankAccountInfo bankAccountInfo = new BankAccountInfo(bankAccountStub, addressStub);
        Mockito.when(customCassandraRepository.findBankAccountInfoByUuid(Mockito.any(UUID.class))).thenReturn(java.util.Optional.of(bankAccountInfo));

        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/bankaccountinfo/{uuid}", bankAccountStub.getUuid().toString()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertEquals(bankAccountInfo, objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BankAccountInfoResponse.class).getBankAccountInfo());
    }

    @Test
    @DisplayName("Verify /bankaccountinfo/{uuid} with correct but doesnt exist UUID will return empty BankAccountInfo")
    void uuidDoesntExist() throws Exception {
        Mockito.when(customCassandraRepository.findBankAccountInfoByUuid(Mockito.any(UUID.class))).thenReturn(Optional.empty());
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/bankaccountinfo/{uuid}", UUID.randomUUID().toString()).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        Assertions.assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        Assertions.assertNotNull(mvcResult.getResponse().getContentAsString());
        BankAccountInfo actualBankAccountInfo = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BankAccountInfoResponse.class).getBankAccountInfo();
        Assertions.assertAll(() -> {
            Assertions.assertNull(actualBankAccountInfo.getAddress());
            Assertions.assertNull(actualBankAccountInfo.getBankAccount());
        });
    }

    @Test
    @DisplayName("Verify /bankaccountinfo/{uuid} with incorrect UUID will return 400:BAD_REQUEST")
    void incorrectUuid() throws Exception {
        BankAccount bankAccountStub = new BankAccount("TestName", "TestLastName", "TestPatronymic", BankAccount.AccountType.DEBIT);
        Address addressStub = new Address("TestStreet", "TestCity", "TestState");
        BankAccountInfo bankAccountInfo = new BankAccountInfo(bankAccountStub, addressStub);
        Mockito.when(customCassandraRepository.findBankAccountInfoByUuid(Mockito.any(UUID.class))).thenReturn(Optional.of(bankAccountInfo));
        MvcResult mvcResult = mockMvc
                .perform(MockMvcRequestBuilders.get("/bankaccountinfo/{uuid}", "dfgdfg").accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
        Assertions.assertTrue(mvcResult.getResponse().getContentAsString().isBlank());
    }



}
