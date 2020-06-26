package com.mfirsov.kafkaproducer.client;

import com.mfirsov.model.BankAccount;
import reactor.core.publisher.Mono;

public interface BankAccountGeneratorClient {
    Mono<BankAccount> getBankAccount();
}
