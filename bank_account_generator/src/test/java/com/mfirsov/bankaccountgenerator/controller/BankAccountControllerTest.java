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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class BankAccountControllerTest {

    @InjectMocks
    BankAccountController bankAccountController;

    @Mock
    IBankAccountService bankAccountService;

    @Test
    @DisplayName("Basic BankAccountController unit test")
    void getBankAccount() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));

        lenient().when(bankAccountService.generateManBankAccount()).thenReturn(new BankAccount("TestFirstName", "TestLastName", "TestPatronymic"));
        lenient().when(bankAccountService.generateWomanBankAccount()).thenReturn(new BankAccount("TestFirstName1", "TestLastName1", "TestPatronymic1"));

        BankAccount bankAccount = bankAccountController.getBankAccount();
        assertNotNull(bankAccount);
        assertTrue(bankAccount.getAccountNumber() > 0, "AccountNumber is 0 or null");
        assertTrue(StringUtils.isNotBlank(bankAccount.getFirstName()));
        assertTrue(StringUtils.isNotBlank(bankAccount.getLastName()));
        assertTrue(StringUtils.isNotBlank(bankAccount.getPatronymic()));
        assertNotNull(bankAccount.getUuid(), "UUID is null");
    }

    @Test
    @DisplayName("Trying to get BankAccount through BankAccountController with empty FNP")
    void getBankAccountWithEmptyStrings() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockHttpServletRequest));

        lenient().when(bankAccountService.generateManBankAccount()).thenReturn(new BankAccount());
        lenient().when(bankAccountService.generateWomanBankAccount()).thenReturn(new BankAccount());

        BankAccount bankAccount = bankAccountController.getBankAccount();
        assertNotNull(bankAccount);
        assertNull(bankAccount.getFirstName());
        assertNull(bankAccount.getLastName());
        assertNull(bankAccount.getPatronymic());
        assertNotNull(bankAccount.getUuid());
        assertTrue(bankAccount.getAccountNumber() > 0);
    }

}