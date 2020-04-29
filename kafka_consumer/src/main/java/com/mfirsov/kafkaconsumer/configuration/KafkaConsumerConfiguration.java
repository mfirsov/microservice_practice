package com.mfirsov.kafkaconsumer.configuration;

import com.mfirsov.model.BankAccount;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.connect.json.JsonDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {

    @Value("${kafka.address:localhost:9092}")
    private String kafkaAddress;

    @Bean
    public Map<String, Object> kafkaConsumerProperties() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "kafka_consumer");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "banking");
        return props;
    }

    @Bean
    public ConsumerFactory<UUID, BankAccount> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(kafkaConsumerProperties(), new UUIDDeserializer(),
                new org.springframework.kafka.support.serializer.JsonDeserializer<>(BankAccount.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, BankAccount> kafkaListenerContainerFactory(ConsumerFactory<UUID, BankAccount> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<UUID, BankAccount> kafkaListenerContainerFactory =
                new ConcurrentKafkaListenerContainerFactory<>();
        kafkaListenerContainerFactory.setConsumerFactory(consumerFactory);
        return kafkaListenerContainerFactory;
    }

}
