package com.mfirsov.kafka_producer.service;

import com.mfirsov.kafka_producer.client.BankAccountGeneratorClient;
import com.mfirsov.kafka_producer.model.BankAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class KafkaProducerService {

    @Value("${kafka.topic:bank_account}")
    private String topic;

    @Autowired
    private KafkaTemplate<Integer, BankAccount> bankAccountKafkaTemplate;

    private final BankAccountGeneratorClient bankAccountGeneratorClient;

    @Autowired
    public KafkaProducerService(BankAccountGeneratorClient bankAccountGeneratorClient) {
        this.bankAccountGeneratorClient = bankAccountGeneratorClient;
    }

    @Scheduled(fixedRate = 5000)
    public void produceToKafka() {
        BankAccount bankAccount = bankAccountGeneratorClient.getBankAccount();

        setRandomAccountType(bankAccount);
        System.out.println(bankAccount);
        if (bankAccount.getFirstName().length() >= 5) {
            Message<BankAccount> bankAccountMessage = MessageBuilder.withPayload(bankAccount)
                    .setHeader(KafkaHeaders.TOPIC, topic)
                    .build();
            bankAccountKafkaTemplate.send(bankAccountMessage);
        }
    }

    private void setRandomAccountType(BankAccount bankAccount) {
        bankAccount.setAccountType(ThreadLocalRandom.current().nextBoolean() ? BankAccount.AccountType.CREDIT : BankAccount.AccountType.DEBIT);
    }


}
