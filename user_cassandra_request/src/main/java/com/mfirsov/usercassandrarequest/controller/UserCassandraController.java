package com.mfirsov.usercassandrarequest.controller;

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
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@Log4j2
public class UserCassandraController {

    private final CustomCassandraRepository customCassandraRepository;

    public UserCassandraController(CustomCassandraRepository customCassandraRepository) {
        this.customCassandraRepository = customCassandraRepository;
    }

    @GetMapping(value = "/bankaccountinfo/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<BankAccountInfoResponse> getBankAccountFromCassandra(@PathVariable String uuid) {
        UUID parsedUuid = UUID.fromString(uuid);
        Mono<BankAccountInfo> bankAccountInfo = customCassandraRepository.findBankAccountInfoByUuid(parsedUuid);
        return bankAccountInfo.map(BankAccountInfoResponse::new);
    }

}
