package com.mfirsov.kafkaconsumer.service;

import com.mfirsov.kafkaconsumer.model.BankAccount;
import com.mfirsov.kafkaconsumer.repository.BankAccountRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;

@Service
@Log4j2
@KafkaListener(topics = "${kafka.topic:bank_account}", containerFactory = "kafkaListenerContainerFactory")
public class KafkaConsumerService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    Queue<BankAccount> bankAccounts = new LinkedList<>();
    int k=0;

    @KafkaHandler
    public void writeDataFromKafkaToQueue(@Payload BankAccount bankAccount) {
        bankAccounts.offer(bankAccount);
        log.info("Send to Queue: " + bankAccount);
    }

    @Scheduled(fixedRate = 32000)
    public void writeFromQueueToCassandra() {
        while (!bankAccounts.isEmpty()) {
            BankAccount bankAccount = bankAccounts.poll();
            log.info("Write following item into Cassandra: #" + k + " " + bankAccount);
            bankAccountRepository.insert(bankAccount);
            k++;
        }
    }



}
