package com.mfirsov.bankaccountgenerator.service;

import com.mfirsov.bankaccountgenerator.model.BankAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

class SimpleBankAccountServiceTest {

    @Test
    @DisplayName("Trying to generate BankAccount with empty sources")
    void generateBankAccountWithEmptyTextFiles() {
        SimpleBankAccountService simpleBankAccountService = new SimpleBankAccountService(
                "src/test/resources/EmptyFirstName.txt",
                "src/test/resources/EmptyLastName.txt",
                "src/test/resources/EmptyPatronimycs.txt",
                "src/test/resources/EmptyFirstName.txt",
                "src/test/resources/EmptyLastName.txt",
                "src/test/resources/EmptyPatronimycs.txt");

        BankAccount manBankAccount = simpleBankAccountService.generateManBankAccount();
        assertNotNull(manBankAccount, "BankAccount is null");
        assertTrue(StringUtils.isBlank(manBankAccount.getFirstName()), "FirstName is not blank");
        assertTrue(StringUtils.isBlank(manBankAccount.getLastName()), "LastName is not blank");
        assertTrue(StringUtils.isBlank(manBankAccount.getPatronymic()), "Patronymic is not blank");
        assertEquals(0, manBankAccount.getAccountNumber(), "AccountNumber is not equal 0");
        assertNull(manBankAccount.getUuid(), "UUID is not null");

        BankAccount womanBankAccount = simpleBankAccountService.generateWomanBankAccount();
        assertNotNull(womanBankAccount, "BankAccount is null");
        assertTrue(StringUtils.isBlank(womanBankAccount.getFirstName()), "FirstName is not blank");
        assertTrue(StringUtils.isBlank(womanBankAccount.getLastName()), "LastName is not blank");
        assertTrue(StringUtils.isBlank(womanBankAccount.getPatronymic()), "Patronymic is not blank");
        assertEquals(0, womanBankAccount.getAccountNumber(), "AccountNumber is not equal 0");
        assertNull(womanBankAccount.getUuid(), "UUID is not null");
    }
}