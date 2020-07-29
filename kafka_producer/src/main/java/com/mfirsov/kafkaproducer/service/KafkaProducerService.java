package com.mfirsov.kafkaproducer.service;

import com.mfirsov.common.model.BankAccount;
import com.mfirsov.kafkaproducer.client.BankAccountGeneratorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderRecord;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@RequiredArgsConstructor
public class KafkaProducerService implements ApplicationRunner {

    @Value("${kafka.topic:bank_account}")
    private String topic;

    private final ReactiveKafkaProducerTemplate<UUID, BankAccount> reactiveKafkaProducer;
    private final BankAccountGeneratorClient bankAccountGeneratorClient;

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void produceToKafka() {
        Mono<BankAccount> bankAccount = bankAccountGeneratorClient
                .getBankAccount()
                .map(this::setRandomAccountType)
                .filter(ba -> ba.getFirstName().length() >= 5)
                .doOnSuccess(ba -> log.debug("Following message received: {}", ba));

        reactiveKafkaProducer
                .send(bankAccount
                        .map(ba -> SenderRecord.create(
                                new ProducerRecord<>(topic, ba.getUuid(), ba),
                                ba.getUuid())))
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


    @Override
    public void run(ApplicationArguments args) {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(this::produceToKafka, 0, 2, TimeUnit.SECONDS);
    }
}
