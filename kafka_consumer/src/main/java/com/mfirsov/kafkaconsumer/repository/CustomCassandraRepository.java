package com.mfirsov.kafkaconsumer.repository;

import com.mfirsov.common.model.BankAccountInfo;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CustomCassandraRepository extends ReactiveCassandraRepository<BankAccountInfo, UUID> {

    Mono<BankAccountInfo> findBankAccountInfoByUuid(UUID uuid);

}
