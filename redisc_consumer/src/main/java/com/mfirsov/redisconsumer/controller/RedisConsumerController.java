package com.mfirsov.redisconsumer.controller;

import com.mfirsov.model.BankAccountInfo;
import com.mfirsov.redisconsumer.service.RedisSubscriptionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.UUID;

@RestController
public class RedisConsumerController {

    private final RedisSubscriptionService redisSubscriptionService;

    public RedisConsumerController(RedisSubscriptionService redisSubscriptionService) {
        this.redisSubscriptionService = redisSubscriptionService;
    }

    @GetMapping("/api/v1/bank-accounts-infos/{uuid}")
    Mono<BankAccountInfo> getBankAccountInfoById(@PathVariable("uuid") UUID uuid) {
        return redisSubscriptionService.receiveMessageFromRedisByUuid(uuid);
    }

    @GetMapping(value = "/api/v1/bank-accounts-infos", produces = MediaType.APPLICATION_JSON_VALUE)
    Mono<ArrayList<String>> getBankAccountsIds() {
        return redisSubscriptionService.getAllBankAccountInfosUuids()
                .collect(ArrayList::new, ArrayList::add);
    }

}
