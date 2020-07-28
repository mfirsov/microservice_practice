package com.mfirsov.kafkaproducer.client;

import com.mfirsov.common.model.BankAccount;
import reactor.core.publisher.Mono;

public interface BankAccountGeneratorClient {
    Mono<BankAccount> getBankAccount();
}
