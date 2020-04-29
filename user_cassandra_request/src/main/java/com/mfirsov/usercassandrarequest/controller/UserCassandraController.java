package com.mfirsov.usercassandrarequest.controller;

import com.mfirsov.model.BankAccount;
import com.mfirsov.usercassandrarequest.repository.CustomCassandraRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Log4j2
public class UserCassandraController {

    @Autowired
    private CustomCassandraRepository customCassandraRepository;

    @GetMapping(value = "/bankaccount/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BankAccount> getBankAccountFromCassandra(@PathVariable UUID uuid) {
        BankAccount bankAccount = customCassandraRepository.findBankAccountByUuid(uuid).orElse(null);
        if (bankAccount != null) {
            log.info("Following BankAccount was requested " + bankAccount);
            return ResponseEntity.ok(bankAccount);
        } else {
            log.error("Bank account with requested UUID=" + uuid + " was not found");
            return ResponseEntity.ok(new BankAccount());
        }
    }

}
