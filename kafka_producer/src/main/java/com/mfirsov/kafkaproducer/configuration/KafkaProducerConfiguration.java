package com.mfirsov.kafkaproducer.configuration;

import com.mfirsov.kafkaproducer.client.BankAccountGeneratorClient;
import com.mfirsov.kafkaproducer.client.BankAccountGeneratorClientImpl;
import com.mfirsov.common.model.BankAccount;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafka
public class KafkaProducerConfiguration {

    @Value("${bank.account.generator.address:http://localhost:8081}")
    private String bankAccountGeneratorAddress;

    @Value("${kafka.address:localhost:9092}")
    private String kafkaAddress;

    @Bean
    public BankAccountGeneratorClient bankAccountGeneratorClient() {
        return new BankAccountGeneratorClientImpl(bankAccountGeneratorAddress);
    }

    @Bean
    public Map<String, Object> kafkaProducerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "kafka_producer");
        return props;
    }

    @Bean
    public ReactiveKafkaProducerTemplate<UUID, BankAccount> reactiveKafkaProducer() {
        SenderOptions<UUID, BankAccount> senderOptions = SenderOptions.create(kafkaProducerProperties());
        return new ReactiveKafkaProducerTemplate<>(senderOptions.maxInFlight(512));
    }

}
