package com.mfirsov.rsocketserver.repository;

import com.mfirsov.model.BankAccountInfo;
import org.springframework.data.cassandra.repository.ReactiveCassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomReactiveCassandraRepository extends ReactiveCassandraRepository<BankAccountInfo, UUID> {
}
