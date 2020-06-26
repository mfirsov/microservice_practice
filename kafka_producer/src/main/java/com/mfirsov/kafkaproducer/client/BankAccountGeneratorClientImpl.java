package com.mfirsov.kafkaproducer.client;

import com.mfirsov.model.BankAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URI;

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
