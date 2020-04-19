package com.mfirsov.kafka_producer.client;

import com.mfirsov.kafka_producer.model.BankAccount;

import java.io.IOException;

public interface BankAccountGeneratorClient {
    BankAccount getBankAccount();
}
