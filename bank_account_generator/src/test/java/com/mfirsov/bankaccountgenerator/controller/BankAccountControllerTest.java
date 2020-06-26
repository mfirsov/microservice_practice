package com.mfirsov.bankaccountgenerator.controller;

import com.mfirsov.bankaccountgenerator.model.BankAccount;
import com.mfirsov.bankaccountgenerator.service.IBankAccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith({MockitoExtension.class})
@WebFluxTest(controllers = BankAccountController.class)
class BankAccountControllerTest {

    @MockBean
    IBankAccountService bankAccountService;

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("Basic BankAccountController unit test")
    void getBankAccount() {

        lenient().when(bankAccountService.generateManBankAccount()).thenReturn(new BankAccount("TestFirstName", "TestLastName", "TestPatronymic"));
        lenient().when(bankAccountService.generateWomanBankAccount()).thenReturn(new BankAccount("TestFirstName1", "TestLastName1", "TestPatronymic1"));


        webClient.get()
                .uri("/api/v1/bank_account")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccount.class);
    }

    @Test
    @DisplayName("Trying to get BankAccount through BankAccountController with empty FNP")
    void getBankAccountWithEmptyStrings() {

        lenient().when(bankAccountService.generateManBankAccount()).thenReturn(new BankAccount());
        lenient().when(bankAccountService.generateWomanBankAccount()).thenReturn(new BankAccount());

        webClient.get()
                .uri("/api/v1/bank_account")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BankAccount.class);
    }

}