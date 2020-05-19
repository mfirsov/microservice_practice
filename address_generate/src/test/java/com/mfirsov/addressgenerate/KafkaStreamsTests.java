package com.mfirsov.addressgenerate;

import com.mfirsov.addressgenerate.client.AddressGeneratorClient;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
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

@EmbeddedKafka(brokerProperties = {
        "advertised.listeners=PLAINTEXT_HOST://localhost:9092,PLAINTEXT://localhost:9091",
        "auto.create.topics.enable=true",
        "listener.security.protocol.map=PLAINTEXT_HOST:PLAINTEXT,PLAINTEXT:PLAINTEXT",
        "inter.broker.listener.name=PLAINTEXT",
        "listeners=PLAINTEXT_HOST://localhost:9092,PLAINTEXT://localhost:9091"},
        topics = {"bank_account", "address"})
@ExtendWith(value = {SpringExtension.class, MockitoExtension.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class KafkaStreamsTests {

    @MockBean
    AddressGeneratorClient addressGeneratorClient;

    @Autowired(required = false)
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    BlockingQueue<ConsumerRecord<UUID, Address>> records;
    KafkaMessageListenerContainer<UUID, Address> container;
    Producer<UUID, BankAccount> bankAccountProducer;

    @BeforeAll
    void beforeAll() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("consumer", "false", embeddedKafkaBroker));
        JsonDeserializer<Address> addressJsonDeserializer = new JsonDeserializer<>(Address.class);
        addressJsonDeserializer.addTrustedPackages("*");
        DefaultKafkaConsumerFactory<UUID, Address> consumerFactory = new DefaultKafkaConsumerFactory<>(configs, new UUIDDeserializer(), addressJsonDeserializer);
        ContainerProperties containerProperties = new ContainerProperties("address");
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
        records = new LinkedBlockingQueue<>();
        container.setupMessageListener((MessageListener<UUID, Address>) records::add);
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
    }

    @Test
    @DisplayName("Verify that Address will be generated when LastName of BankAccount starts with A")
    void lastNameStartsWithATest() throws InterruptedException {
        BankAccount bankAccount = new BankAccount("TestName", "АТестФамилия", "TestPatronymic", BankAccount.AccountType.DEBIT);
        bankAccountProducer.send(new ProducerRecord<>("bank_account", bankAccount.getUuid(), bankAccount));
        bankAccountProducer.flush();
        Address address = new Address("TestStreet", "TestCity", "TestState");
        Mockito.when(addressGeneratorClient.getAddressFromAddressGenerator()).thenReturn(address);
        ConsumerRecord<UUID, Address> consumerRecord = records.poll(10000, TimeUnit.MILLISECONDS);
        Assertions.assertNotNull(consumerRecord);
        Assertions.assertEquals(address, consumerRecord.value());
    }

    @Test
    @DisplayName("Verify that Address will not be generated when LastName of BankAccount doesnt start with A")
    void lastNameDoesntStartWithATest() throws InterruptedException {
        BankAccount bankAccount = new BankAccount("TestName", "ТестФамилия", "TestPatronymic", BankAccount.AccountType.DEBIT);
        bankAccountProducer.send(new ProducerRecord<>("bank_account", bankAccount.getUuid(), bankAccount));
        bankAccountProducer.flush();
        Address address = new Address("TestStreet", "TestCity", "TestState");
        Mockito.when(addressGeneratorClient.getAddressFromAddressGenerator()).thenReturn(address);
        ConsumerRecord<UUID, Address> consumerRecord = records.poll(10000, TimeUnit.MILLISECONDS);
        Assertions.assertNull(consumerRecord);
    }

}
