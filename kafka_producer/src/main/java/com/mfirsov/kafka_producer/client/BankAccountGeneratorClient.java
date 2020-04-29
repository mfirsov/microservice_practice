package com.mfirsov.kafka_producer.client;

import com.mfirsov.model.BankAccount;

public interface BankAccountGeneratorClient {
    BankAccount getBankAccount();
}
