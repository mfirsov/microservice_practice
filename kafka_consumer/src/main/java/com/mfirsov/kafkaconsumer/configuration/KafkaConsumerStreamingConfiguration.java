package com.mfirsov.kafkaconsumer.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfirsov.kafkaconsumer.util.BankAccountAndAddressValueJoiner;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import com.mfirsov.repository.CustomCassandraRepository;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
@EnableKafkaStreams
@Import(value = {CassandraConfiguration.class})
@Log4j2
public class KafkaConsumerStreamingConfiguration {

    @Value("${spring.kafka.streams.bootstrap-servers}")
    private String bootstrapServer;

    @Value("${spring.kafka.streams.replication-factor}")
    private String replicationFactor;

    @Value("${spring.kafka.streams.client-id}")
    private String streamClientId;

    @Value("${spring.kafka.streams.application-id}")
    private String applicationId;

    @Value("${kafka.bank.topic}")
    private String bankAccountTopic;

    @Value("${kafka.address.topic}")
    private String addressTopic;

    @Value("${kafka.bank.account.info.topic}")
    private String bankAccountInfoTopic;

    private final CustomCassandraRepository customCassandraRepository;

    public KafkaConsumerStreamingConfiguration(CustomCassandraRepository customCassandraRepository) {
        this.customCassandraRepository = customCassandraRepository;
    }

    @Bean
    public ValueJoiner<BankAccount, Address, BankAccountInfo> valueJoiner() {
        return new BankAccountAndAddressValueJoiner();
    }

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
        properties.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Address.class);
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

    @Bean
    public KStream<UUID, BankAccountInfo> kStream(StreamsBuilder streamsBuilderFactoryBean) {
        KTable<UUID, BankAccount> uuidBankAccountKTable = streamsBuilderFactoryBean.table(bankAccountTopic, Consumed.with(Serdes.UUID(), new JsonSerde<>(BankAccount.class, new ObjectMapper())));
        KTable<UUID, Address> uuidAddressKTable = streamsBuilderFactoryBean.table(addressTopic, Consumed.with(Serdes.UUID(), new JsonSerde<>(Address.class, new ObjectMapper())));
        KTable<UUID, BankAccountInfo> uuidBankAccountInfoKTable = uuidBankAccountKTable.join(uuidAddressKTable, valueJoiner());
        KStream<UUID, BankAccountInfo> uuidBankAccountInfoKStream = uuidBankAccountInfoKTable.toStream();
        uuidBankAccountInfoKStream.foreach((key, value) -> customCassandraRepository.insert(value));
        uuidBankAccountInfoKStream.to(bankAccountInfoTopic);
        return uuidBankAccountInfoKStream;
    }

}
