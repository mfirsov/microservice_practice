package com.mfirsov.bank_account_generator.controller;

import com.mfirsov.bank_account_generator.model.BankAccount;
import com.mfirsov.bank_account_generator.service.IBankAccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@Log4j2
public class BankAccountController {

    @Autowired
    private IBankAccountService getManBankAccountService;

    @Autowired
    private IBankAccountService getWomanBankAccountService;

    @GetMapping(path = "/getBankAccount",
                produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody BankAccount getBankAccount() {
        //true = getManBankAccountService, false = getWomanBankAccountService
        BankAccount bankAccount = ThreadLocalRandom.current().nextBoolean() ?
                getManBankAccountService.generateBankAccount() : getWomanBankAccountService.generateBankAccount();
        log.info("Following message was requested " + bankAccount);
        return bankAccount;
    }

}
