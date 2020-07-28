package com.mfirsov.kafkaproducer.service;

import com.mfirsov.kafkaproducer.client.BankAccountGeneratorClient;
import com.mfirsov.common.model.BankAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.SenderRecord;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Log4j2
@RequiredArgsConstructor
public class KafkaProducerService {

    @Value("${kafka.topic:bank_account}")
    private String topic;

    private final ReactiveKafkaProducerTemplate<UUID, BankAccount> reactiveKafkaProducer;
    private final BankAccountGeneratorClient bankAccountGeneratorClient;

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Scheduled(fixedRate = 2000)
    public void produceToKafka() {
        Mono<BankAccount> bankAccount = bankAccountGeneratorClient
                .getBankAccount()
                .map(this::setRandomAccountType)
                .filter(ba -> ba.getFirstName().length() >= 5)
                .doOnSuccess(ba -> log.debug("Following message received: {}", ba));

//        Mono<BankAccount> filteredBankAccount = bankAccount
//                .map(this::setRandomAccountType)
//                .filter(ba -> ba.getFirstName().length() >= 5);

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


}
