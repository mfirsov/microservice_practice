package com.mfirsov.grpcclientservice.controller;

import com.mfirsov.grpcclientservice.client.GRpcClient;
import com.mfirsov.grpcclientservice.service.GRpcToModelConverter;
import com.mfirsov.model.BankAccount;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
public class RestClientController {

    private final GRpcClient gRpcClient;

    public RestClientController(GRpcClient gRpcClient) {
        this.gRpcClient = gRpcClient;
    }

    @GetMapping(path = "/api/v1/bank_account_infos",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<ResponseEntity<Object>> getBankAccountInfo(@RequestParam("accountType") String accountType) {
        try {
            BankAccount.AccountType.valueOf(accountType);
        } catch (IllegalArgumentException e) {
            return Mono.just(ResponseEntity.badRequest().body(e.getMessage()));
        }
        return gRpcClient.getBankAccountInfo(accountType)
                .map(GRpcToModelConverter::convert)
                .map(bankAccountInfosResponse -> {
                    log.info("Following message was requested: {}", bankAccountInfosResponse);
                    return ResponseEntity.ok(bankAccountInfosResponse);
                });
    }

}
