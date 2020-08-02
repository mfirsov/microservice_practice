package com.mfirsov.kafkaconsumer.repository;

import com.mfirsov.kafkaconsumer.entities.BankAccountInfoEntity;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CustomCassandraRepository extends ReactiveCassandraRepository<BankAccountInfoEntity, UUID> {

    Mono<BankAccountInfoEntity> findBankAccountInfoByUuid(UUID uuid);

}
