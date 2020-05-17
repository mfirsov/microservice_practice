package com.mfirsov.kafkaconsumer;

import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import com.mfirsov.repository.CustomCassandraRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.cassandra.ReactiveSession;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@EmbeddedKafka(brokerProperties = {
        "advertised.listeners=PLAINTEXT_HOST://localhost:9092,PLAINTEXT://localhost:9091",
        "auto.create.topics.enable=true",
        "listener.security.protocol.map=PLAINTEXT_HOST:PLAINTEXT,PLAINTEXT:PLAINTEXT",
        "inter.broker.listener.name=PLAINTEXT",
        "listeners=PLAINTEXT_HOST://localhost:9092,PLAINTEXT://localhost:9091"},
        topics = {"bank_account", "bank_account_info", "address"})
@ExtendWith(value = {SpringExtension.class, MockitoExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class KafkaStreamsTests {

    @MockBean
    CustomCassandraRepository customCassandraRepository;

    @MockBean
    CassandraSessionFactoryBean cassandraSessionFactoryBean;

    @MockBean
    SessionFactory sessionFactory;

    @MockBean
    ReactiveSession reactiveSession;

    @Autowired(required = false)
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    BlockingQueue<ConsumerRecord<UUID, BankAccountInfo>> records;
    KafkaMessageListenerContainer<UUID, BankAccountInfo> container;
    Producer<UUID, BankAccount> bankAccountProducer;
    Producer<UUID, Address> addressProducer;
    BankAccountInfo cassandraBankAccountInfo;

    @BeforeAll
    void beforeAll() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker));
        JsonDeserializer<BankAccountInfo> bankAccountJsonDeserializer = new JsonDeserializer<>(BankAccountInfo.class);
        bankAccountJsonDeserializer.addTrustedPackages("*");
        DefaultKafkaConsumerFactory<UUID, BankAccountInfo> consumerFactory = new DefaultKafkaConsumerFactory<>(configs, new UUIDDeserializer(), bankAccountJsonDeserializer);
        ContainerProperties containerProperties = new ContainerProperties("bank_account_info");
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<UUID, BankAccountInfo>) records::add);
        container.start();
        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
    }

    @AfterAll
    void tearDown() {
        container.stop();
        embeddedKafkaBroker.destroy();
    }

    @BeforeEach
    void beforeEach() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        bankAccountProducer = new DefaultKafkaProducerFactory<UUID, BankAccount>(configs, new UUIDSerializer(), new JsonSerializer<>()).createProducer();
        addressProducer = new DefaultKafkaProducerFactory<UUID, Address>(configs, new UUIDSerializer(), new JsonSerializer<>()).createProducer();
        cassandraBankAccountInfo = null;
    }

    @Test
    @DisplayName("Verify that BankAccount and Address with the same UUID are combined into BankAccountInfo")
    void testKafkaStream() throws InterruptedException {
        BankAccount bankAccount = new BankAccount("Testname", "TestLastName", "TestPatronymic", BankAccount.AccountType.DEBIT);
        Address address = new Address("TestStreet", "TestCity", "TestState");
        Mockito.when(customCassandraRepository.insert(Mockito.any(BankAccountInfo.class)))
                .thenAnswer(invocation -> cassandraBankAccountInfo = invocation.getArgument(0));
        bankAccountProducer.send(new ProducerRecord<>("bank_account", bankAccount.getUuid(), bankAccount));
        addressProducer.send(new ProducerRecord<>("address", bankAccount.getUuid(), address));
        bankAccountProducer.flush();
        addressProducer.flush();
        ConsumerRecord<UUID, BankAccountInfo> consumerRecord = records.poll(2000, TimeUnit.MILLISECONDS);
        BankAccountInfo kafkaBankAccountInfo = consumerRecord.value();
        assertNotNull(kafkaBankAccountInfo);
        assertNotNull(cassandraBankAccountInfo);
    }

    @Test
    @DisplayName("Verify that BankAccount and Address with different UUID are not combined into BankAccountInfo")
    void nullBankAccountInfoFromKafka() throws InterruptedException {
        BankAccount bankAccount = new BankAccount("Testname1", "TestLastName1", "TestPatronymic1", BankAccount.AccountType.CREDIT);
        Address address = new Address("TestStreet1", "TestCity1", "TestState1");
        Mockito.when(customCassandraRepository.insert(Mockito.any(BankAccountInfo.class)))
                .thenAnswer(invocation -> cassandraBankAccountInfo = invocation.getArgument(0));
        bankAccountProducer.send(new ProducerRecord<>("bank_account", bankAccount.getUuid(), bankAccount));
        addressProducer.send(new ProducerRecord<>("address", UUID.randomUUID(), address));
        bankAccountProducer.flush();
        addressProducer.flush();
        ConsumerRecord<UUID, BankAccountInfo> consumerRecord = records.poll(2000, TimeUnit.MILLISECONDS);
        assertNull(consumerRecord);
        assertNull(cassandraBankAccountInfo);
    }

}
