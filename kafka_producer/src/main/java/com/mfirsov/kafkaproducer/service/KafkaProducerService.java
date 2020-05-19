package com.mfirsov.kafkaproducer.service;

import com.mfirsov.kafkaproducer.client.BankAccountGeneratorClient;
import com.mfirsov.model.BankAccount;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Log4j2
public class KafkaProducerService {

    @Value("${kafka.topic:bank_account}")
    private String topic;

    private final KafkaTemplate<UUID, BankAccount> bankAccountKafkaTemplate;

    private final BankAccountGeneratorClient bankAccountGeneratorClient;

    public KafkaProducerService(KafkaTemplate<UUID, BankAccount> bankAccountKafkaTemplate, BankAccountGeneratorClient bankAccountGeneratorClient) {
        this.bankAccountKafkaTemplate = bankAccountKafkaTemplate;
        this.bankAccountGeneratorClient = bankAccountGeneratorClient;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Scheduled(fixedRate = 2000)
    public void produceToKafka() {
        BankAccount bankAccount = bankAccountGeneratorClient.getBankAccount();
        setRandomAccountType(bankAccount);
        if (bankAccount.getFirstName().length() >= 5) {
            bankAccountKafkaTemplate.send(topic, bankAccount.getUuid(), bankAccount);
            log.info("Following message was sent to Kafka: " + bankAccount);
        } else {
            log.info("Following message was filtered: " + bankAccount);
        }
    }

    private void setRandomAccountType(BankAccount bankAccount) {
        bankAccount.setAccountType(ThreadLocalRandom.current().nextBoolean() ? BankAccount.AccountType.CREDIT : BankAccount.AccountType.DEBIT);
    }


}
