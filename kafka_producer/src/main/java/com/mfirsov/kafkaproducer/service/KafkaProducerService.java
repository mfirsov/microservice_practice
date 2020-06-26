package com.mfirsov.kafkaproducer.service;

import com.mfirsov.kafkaproducer.client.BankAccountGeneratorClient;
import com.mfirsov.model.BankAccount;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.SenderRecord;

import javax.annotation.PostConstruct;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Log4j2
public class KafkaProducerService {

    @Value("${kafka.topic:bank_account}")
    private String topic;

    private final ReactiveKafkaProducerTemplate<UUID, BankAccount> reactiveKafkaProducer;
    private final BankAccountGeneratorClient bankAccountGeneratorClient;

    public KafkaProducerService(ReactiveKafkaProducerTemplate<UUID, BankAccount> reactiveKafkaProducer, BankAccountGeneratorClient bankAccountGeneratorClient) {
        this.reactiveKafkaProducer = reactiveKafkaProducer;
        this.bankAccountGeneratorClient = bankAccountGeneratorClient;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Scheduled(fixedRate = 2000)
    public void produceToKafka() {
        Mono<BankAccount> bankAccount = bankAccountGeneratorClient
                .getBankAccount()
                .doOnSuccess(bankAccount1 -> log.info("Following message received: {}", bankAccount1));

        Mono<BankAccount> filteredBankAccount = bankAccount
                .map(this::setRandomAccountType)
                .filter(bankAccount1 -> bankAccount1.getFirstName().length() >= 5);
        reactiveKafkaProducer
                .send(filteredBankAccount
                        .map(filteredBankAccount1 -> SenderRecord.create(
                                new ProducerRecord<>(topic, filteredBankAccount1.getUuid(), filteredBankAccount1),
                                filteredBankAccount1.getUuid())))
                .doOnError(log::error)
                .subscribe(r -> {
                    RecordMetadata recordMetadata = r.recordMetadata();
                    log.info("Following metadata was received from kafka: topic-{}, offset-{}, partition-{}", recordMetadata.topic(), recordMetadata.offset(), recordMetadata.partition());
                });
    }

    private BankAccount setRandomAccountType(BankAccount bankAccount) {
        bankAccount.setAccountType(ThreadLocalRandom.current().nextBoolean() ? BankAccount.AccountType.CREDIT : BankAccount.AccountType.DEBIT);
        return bankAccount;
    }


}
