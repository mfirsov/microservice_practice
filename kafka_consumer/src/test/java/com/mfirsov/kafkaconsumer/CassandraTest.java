package com.mfirsov.kafkaconsumer;

import com.mfirsov.kafkaconsumer.entities.AddressEntity;
import com.mfirsov.kafkaconsumer.entities.BankAccountEntity;
import com.mfirsov.kafkaconsumer.entities.BankAccountInfoEntity;
import com.mfirsov.kafkaconsumer.repository.CustomCassandraRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Log4j2
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CassandraTest {

    @Autowired
    private CustomCassandraRepository customCassandraRepository;

    @Test
    public void test() {
        UUID uuid = UUID.randomUUID();
        BankAccountInfoEntity entity = BankAccountInfoEntity.builder()
                .addressEntity(AddressEntity.builder()
                        .city("City")
                        .state("State")
                        .street("Street")
                        .build())
                .bankAccountEntity(BankAccountEntity.builder()
                        .accountNumber(ThreadLocalRandom.current().nextLong())
                        .accountType(BankAccountEntity.AccountType.CREDIT)
                        .firstName("Name")
                        .lastName("Lastname")
                        .patronymic("Patronymic")
                        .uuid(uuid)
                        .build())
                .uuid(uuid)
                .build();

        log.debug("Following Object will be written to Cassandra:{}", entity);
        customCassandraRepository.insert(entity)
                .doOnError(log::error)
                .doOnSuccess(baie -> log.debug("Following object was written to Cassandra:{}", baie))
                .subscribe();
    }

}
