package com.mfirsov.grpcservice.repository;

import com.mfirsov.grpcservice.entities.BankAccountInfoEntity;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CustomCassandraRepository extends ReactiveCassandraRepository<BankAccountInfoEntity, UUID> {

    Mono<BankAccountInfoEntity> findBankAccountInfoByUuid(UUID uuid);

}
