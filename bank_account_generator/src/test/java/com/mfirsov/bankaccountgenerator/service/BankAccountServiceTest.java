package com.mfirsov.bankaccountgenerator.service;

import com.mfirsov.bankaccountgenerator.model.BankAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankAccountServiceTest {

    @Test
    @DisplayName("Trying to generate BankAccount with empty sources")
    void generateBankAccountWithEmptyTextFiles() {
        BankAccountService bankAccountService = new BankAccountService(
                "src/test/resources/EmptyFirstName.txt",
                "src/test/resources/EmptyLastName.txt",
                "src/test/resources/EmptyPatronimycs.txt",
                "src/test/resources/EmptyFirstName.txt",
                "src/test/resources/EmptyLastName.txt",
                "src/test/resources/EmptyPatronimycs.txt");

        BankAccount manBankAccount = bankAccountService.generateManBankAccount();
        assertNotNull(manBankAccount, "BankAccount is null");
        assertTrue(StringUtils.isBlank(manBankAccount.getFirstName()), "FirstName is not blank");
        assertTrue(StringUtils.isBlank(manBankAccount.getLastName()), "LastName is not blank");
        assertTrue(StringUtils.isBlank(manBankAccount.getPatronymic()), "Patronymic is not blank");
        assertTrue(manBankAccount.getAccountNumber() > 0, "AccountNumber is 0 or null");
        assertNotNull(manBankAccount.getUuid(), "UUID is null");

        BankAccount womanBankAccount = bankAccountService.generateWomanBankAccount();
        assertNotNull(womanBankAccount, "BankAccount is null");
        assertTrue(StringUtils.isBlank(womanBankAccount.getFirstName()), "FirstName is not blank");
        assertTrue(StringUtils.isBlank(womanBankAccount.getLastName()), "LastName is not blank");
        assertTrue(StringUtils.isBlank(womanBankAccount.getPatronymic()), "Patronymic is not blank");
        assertTrue(womanBankAccount.getAccountNumber() > 0, "AccountNumber is 0 or null");
        assertNotNull(womanBankAccount.getUuid(), "UUID is null");
    }
}