package com.mfirsov.addressgenerate.configuration;

import com.mfirsov.addressgenerate.client.AddressGeneratorClient;
import com.mfirsov.addressgenerate.client.AddressGeneratorClientImpl;
import com.mfirsov.model.BankAccount;
import com.mfirsov.addressgenerate.util.BankAccountToAddressValueMapper;
import com.mfirsov.model.Address;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
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
@EnableKafkaStreams
@Log4j2
@ComponentScan(basePackages = "com.mfirsov.addressgenerate")
public class KafkaStreamingConfiguration {

    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.kafka.streams.replication-factor}")
    private String replicationFactor;

    @Value("${spring.kafka.streams.application-id}")
    private String applicationId;

    @Value("${kafka.input.topic}")
    private String inputTopic;

    @Value("${kafka.address.topic}")
    private String addressTopic;

    @Value("${spring.kafka.streams.client-id}")
    private String clientId;

    @Bean
    public AddressGeneratorClient addressGeneratorClient() {
        return new AddressGeneratorClientImpl();
    }

    @Bean
    public ValueMapper<BankAccount, Address> valueMapper() {
        return new BankAccountToAddressValueMapper();
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, applicationId);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(StreamsConfig.REPLICATION_FACTOR_CONFIG, replicationFactor);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.UUID().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class);
        props.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);
        props.put(StreamsConfig.CLIENT_ID_CONFIG, clientId);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new KafkaStreamsConfiguration(props);
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_BUILDER_BEAN_NAME)
    public StreamsBuilderFactoryBean streamsBuilderFactoryBean(KafkaStreamsConfiguration kStreamsConfigs) {
        return new StreamsBuilderFactoryBean(kStreamsConfigs);
    }

    @Bean
    public KStream<UUID, BankAccount> kStream(StreamsBuilder streamsBuilderFactoryBean) {
        KTable<UUID, BankAccount> kTable = streamsBuilderFactoryBean.table(inputTopic);
        kTable.toStream().filter((k,v) -> v.getLastName().startsWith("А"))
                .mapValues(valueMapper())
                .to(addressTopic);
        return kTable.toStream();
    }

}
