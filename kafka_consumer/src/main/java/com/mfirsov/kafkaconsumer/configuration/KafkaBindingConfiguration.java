package com.mfirsov.kafkaconsumer.configuration;

import com.mfirsov.common.model.Address;
import com.mfirsov.common.model.BankAccount;
import com.mfirsov.common.model.BankAccountInfo;
import com.mfirsov.kafkaconsumer.entities.BankAccountInfoEntity;
import com.mfirsov.kafkaconsumer.repository.CustomCassandraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.function.BiFunction;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class KafkaBindingConfiguration {

    private final CustomCassandraRepository customCassandraRepository;

    @Bean
    public BiFunction<KTable<UUID, BankAccount>, KTable<UUID, Address>, KStream<UUID, BankAccountInfo>> bankAccountInfo() {
        return (uuidBankAccountKTable, uuidAddressKTable) -> uuidBankAccountKTable
                .join(uuidAddressKTable, (bankAccount, address) -> BankAccountInfo.builder()
                        .address(address)
                        .bankAccount(bankAccount)
                        .uuid(bankAccount.getUuid())
                        .build())
                .toStream()
                .peek((key, value) -> log.debug("Following Object was combined with key: {} and value: {}", key, value))
                .peek((uuid, bankAccountInfo) -> {
                    BankAccountInfoEntity entity = new BankAccountInfoEntity(bankAccountInfo);
                    customCassandraRepository
                            .insert(entity)
                            .doOnSuccess(bai -> log.debug("Following BankAccountInfoEntity was saved in Cassandra:{}", bai))
                            .doOnError(log::error)
                            .subscribe();
                });
    }

}
