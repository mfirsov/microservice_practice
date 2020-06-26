package com.mfirsov.kafkaconsumerredis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Log4j2
public class KafkaStreamsService {

    @Value("${kafka.bank.topic}")
    private String bankAccountTopic;

    @Value("${kafka.address.topic}")
    private String addressTopic;

    private final ReactiveRedisTemplate<String, BankAccountInfo> reactiveRedisTemplate;

    private final ValueJoiner<BankAccount, Address, BankAccountInfo> valueJoiner;

    public KafkaStreamsService(ReactiveRedisTemplate<String, BankAccountInfo> reactiveRedisTemplate, ValueJoiner<BankAccount, Address, BankAccountInfo> valueJoiner) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.valueJoiner = valueJoiner;
    }

    @Bean
    public KStream<UUID, BankAccountInfo> kStream(StreamsBuilder streamsBuilderFactoryBean) {
        KTable<UUID, BankAccount> uuidBankAccountKTable = streamsBuilderFactoryBean.table(bankAccountTopic, Consumed.with(Serdes.UUID(), new JsonSerde<>(BankAccount.class, new ObjectMapper())));
        KTable<UUID, Address> uuidAddressKTable = streamsBuilderFactoryBean.table(addressTopic, Consumed.with(Serdes.UUID(), new JsonSerde<>(Address.class, new ObjectMapper())));
        KTable<UUID, BankAccountInfo> uuidBankAccountInfoKTable = uuidBankAccountKTable.join(uuidAddressKTable, valueJoiner);
        KStream<UUID, BankAccountInfo> uuidBankAccountInfoKStream = uuidBankAccountInfoKTable.toStream();
        uuidBankAccountInfoKStream.peek(((key, value) -> log.info("Message possibly sent to Redis: key - {}, value - {}", key, value)))
                .peek((key, value) -> reactiveRedisTemplate.opsForValue().set("bank-account-info:" + key, value)
                                .doOnSuccess((v) -> log.info("Is successful?: {}", v))
                                .subscribe()
                );

        return uuidBankAccountInfoKStream;
    }

}
