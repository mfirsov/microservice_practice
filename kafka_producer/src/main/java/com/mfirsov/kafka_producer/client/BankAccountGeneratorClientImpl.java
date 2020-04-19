package com.mfirsov.kafka_producer.client;

import com.mfirsov.kafka_producer.model.BankAccount;
import com.mfirsov.kafka_producer.util.JsonBodyHandler;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class BankAccountGeneratorClientImpl implements BankAccountGeneratorClient {

    private final String bankAccountGeneratorAddress;

    public BankAccountGeneratorClientImpl(String bankAccountGeneratorAddress) {
        this.bankAccountGeneratorAddress = bankAccountGeneratorAddress;
    }

    @Override
    public BankAccount getBankAccount() {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(bankAccountGeneratorAddress + "/getBankAccount"))
                .GET()
                .setHeader("User-Agent", "Java 11 HttpClient")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
        try {
            return httpClient.send(request, new JsonBodyHandler<>(BankAccount.class)).body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new BankAccount();
        }
    }

}
