package com.mfirsov.bank_account_generator.controller;

import com.mfirsov.bank_account_generator.model.BankAccount;
import com.mfirsov.bank_account_generator.service.IBankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class BankAccountController {

    private final Random random = new Random();

    @Autowired
    private IBankAccountService getManBankAccountService;

    @Autowired
    private IBankAccountService getWomanBankAccountService;

    @GetMapping(path = "/getBankAccount",
                produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody BankAccount getBankAccount() {
        //true = getManBankAccountService, false = getWomanBankAccountService
        if (random.nextBoolean()) {
            return getManBankAccountService.generateBankAccount();
        } else {
            return getWomanBankAccountService.generateBankAccount();
        }
    }

}
