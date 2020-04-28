package com.mfirsov.kafka_producer.configuration;

import com.mfirsov.kafka_producer.client.BankAccountGeneratorClient;
import com.mfirsov.kafka_producer.client.BankAccountGeneratorClientImpl;
import com.mfirsov.kafka_producer.model.BankAccount;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

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
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, new JsonSerializer<BankAccount>().noTypeInfo().getClass());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "kafka_producer");
        return props;
    }

    @Bean
    public ProducerFactory<UUID, BankAccount> producerFactory() {
        return new DefaultKafkaProducerFactory<>(kafkaProducerProperties());
    }

    @Bean
    public KafkaTemplate<UUID, BankAccount> bankAccountKafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
