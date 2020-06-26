package com.mfirsov.bankaccountgenerator.controller;

import com.mfirsov.bankaccountgenerator.model.BankAccount;
import com.mfirsov.bankaccountgenerator.service.IBankAccountService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@Log4j2
public class BankAccountController {

    private final IBankAccountService bankAccountService;

    public BankAccountController(IBankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping(path = "/api/v1/bank_account",
                produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<BankAccount> getBankAccount() {
        //true = getManBankAccountService, false = getWomanBankAccountService
        BankAccount bankAccount = ThreadLocalRandom.current().nextBoolean() ?
                bankAccountService.generateManBankAccount() : bankAccountService.generateWomanBankAccount();
//        log.info("Following message was requested " + bankAccount);
        return Mono.just(bankAccount).doOnSuccess(bankAccount1 -> log.info("Following message was requested " + bankAccount1));
    }

}
