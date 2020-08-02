package com.mfirsov.grpcservice.repository;

import com.mfirsov.common.model.BankAccountInfo;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface CustomCassandraRepository extends ReactiveCassandraRepository<BankAccountInfo, UUID> {

    Mono<BankAccountInfo> findBankAccountInfoByUuid(UUID uuid);

}
