package com.mfirsov.usercassandrarequest.controller;

import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import com.mfirsov.model.BankAccountInfoResponse;
import com.mfirsov.repository.CustomCassandraRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping(value = "/bankaccountinfo/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BankAccountInfoResponse> getBankAccountFromCassandra(@PathVariable String uuid) {
        UUID parsedUuid;
        try {
            parsedUuid = UUID.fromString(uuid);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        BankAccountInfo bankAccountInfo = customCassandraRepository.findBankAccountInfoByUuid(parsedUuid).orElse(null);
        if (bankAccountInfo != null) {
            log.info("Following BankAccount was requested " + bankAccountInfo);
            return ResponseEntity.ok(new BankAccountInfoResponse(bankAccountInfo));
        } else {
            log.error("Bank account with requested UUID=" + parsedUuid + " was not found");
            return ResponseEntity.ok(new BankAccountInfoResponse(new BankAccountInfo()));
        }
    }

}
