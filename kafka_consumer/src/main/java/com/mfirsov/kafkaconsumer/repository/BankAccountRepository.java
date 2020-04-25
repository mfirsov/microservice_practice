package com.mfirsov.kafkaconsumer.repository;

import com.mfirsov.kafkaconsumer.model.BankAccount;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BankAccountRepository extends CassandraRepository<BankAccount, UUID> {



}
