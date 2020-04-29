package com.mfirsov.kafkaconsumer.repository;

import com.mfirsov.model.BankAccountInfo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountInfoRepository extends CassandraRepository<BankAccountInfo, UUID> {
}
