package com.mfirsov.kafkaproducer.service;

import com.mfirsov.kafkaproducer.client.BankAccountGeneratorClient;
import com.mfirsov.model.BankAccount;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EmbeddedKafka(brokerProperties = {
        "advertised.listeners=PLAINTEXT_HOST://localhost:9092,PLAINTEXT://localhost:9091",
        "auto.create.topics.enable=true",
        "listener.security.protocol.map=PLAINTEXT_HOST:PLAINTEXT,PLAINTEXT:PLAINTEXT",
        "inter.broker.listener.name=PLAINTEXT",
        "listeners=PLAINTEXT_HOST://localhost:9092,PLAINTEXT://localhost:9091"
})
@ExtendWith(value = {SpringExtension.class, MockitoExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaProducerTest {

    private static final String TOPIC = "bank_account";

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @Mock
    private BankAccountGeneratorClient bankAccountGeneratorClient;

    BlockingQueue<ConsumerRecord<UUID, BankAccount>> records;
    KafkaMessageListenerContainer<UUID, BankAccount> container;
    Map<String, Object> props = new HashMap<>();
    KafkaTemplate<UUID, BankAccount> kafkaTemplate;
    private BankAccount bankAccount;

    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker));
        JsonDeserializer<BankAccount> bankAccountJsonDeserializer = new JsonDeserializer<>(BankAccount.class);
        bankAccountJsonDeserializer.addTrustedPackages("*");
        DefaultKafkaConsumerFactory<UUID, BankAccount> consumerFactory = new DefaultKafkaConsumerFactory<>(configs, new UUIDDeserializer(), bankAccountJsonDeserializer);
        ContainerProperties containerProperties = new ContainerProperties(TOPIC);
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<UUID, BankAccount>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "kafka_producer");
        kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
    }

    @BeforeEach
    void beforeEach() {
        bankAccount = new BankAccount("TestFirstName", "TestLastName", "TestPatronymic", BankAccount.AccountType.CREDIT);
    }

    @AfterAll
    void tearDown() {
        container.stop();
    }

    @Test
    public void kafkaSetup_withTopic_ensureSendMessageIsReceived() throws Exception {
        Mockito.when(bankAccountGeneratorClient.getBankAccount()).thenReturn(bankAccount);
        kafkaProducerService = new KafkaProducerService(kafkaTemplate, bankAccountGeneratorClient);
        kafkaProducerService.setTopic(TOPIC);
        kafkaProducerService.produceToKafka();
        ConsumerRecord<UUID, BankAccount> singleRecord = records.poll(100, TimeUnit.MILLISECONDS);
        assertNotNull(singleRecord);
        assertEquals(singleRecord.key(), bankAccount.getUuid());
        assertEquals(singleRecord.value(), bankAccount);
    }

}
