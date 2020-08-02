package com.mfirsov.usercassandrarequest.repository;

import com.mfirsov.usercassandrarequest.entities.BankAccountInfoEntity;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CustomCassandraRepository extends ReactiveCassandraRepository<BankAccountInfoEntity, UUID> {

    Mono<BankAccountInfoEntity> findBankAccountInfoByUuid(UUID uuid);

}
