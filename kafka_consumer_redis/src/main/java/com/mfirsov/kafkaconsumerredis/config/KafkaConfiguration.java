package com.mfirsov.kafkaconsumerredis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafka
@EnableKafkaStreams
//@Import({RedisConfiguration.class})
public class KafkaConfiguration {

    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.streams.replication-factor}")
    private String replicationFactor;

    @Value("${spring.kafka.streams.client-id}")
    private String streamClientId;

    @Value("${spring.kafka.streams.application-id}")
    private String applicationId;


    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
        properties.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);
        properties.put(StreamsConfig.CLIENT_ID_CONFIG, streamClientId);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.UUID().getClass());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        properties.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, BankAccountInfo.class);
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        return new KafkaStreamsConfiguration(properties);
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME)
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean(KafkaStreamsConfiguration kStreamsConfigs) {
        return new StreamsBuilderFactoryBean(kStreamsConfigs);
    }

    @Bean(name = "addressJsonDeserializer")
    public JsonDeserializer<Address> addressJsonDeserializer() {
        return new JsonDeserializer<>(Address.class);
    }

    @Bean(name = "bankAccountJsonDeserializer")
    public JsonDeserializer<BankAccount> bankAccountJsonDeserializer() {
        return new JsonDeserializer<>(BankAccount.class);
    }

}
