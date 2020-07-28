package com.mfirsov.kafkaproducer.client;

import com.mfirsov.common.model.BankAccount;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BankAccountGeneratorClientImpl implements BankAccountGeneratorClient {

    private final String bankAccountGeneratorAddress;

    public BankAccountGeneratorClientImpl(String bankAccountGeneratorAddress) {
        this.bankAccountGeneratorAddress = bankAccountGeneratorAddress;
    }

    @Override
    public Mono<BankAccount> getBankAccount() {
        WebClient webClient = WebClient.create(bankAccountGeneratorAddress);
        return webClient
                .get()
                .uri("/api/v1/bank_account")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(BankAccount.class);
    }

}
