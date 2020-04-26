package com.mfirsov.usercassandrarequest.repository;

import com.mfirsov.usercassandrarequest.model.BankAccount;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomCassandraRepository extends CassandraRepository<BankAccount, UUID> {

    Optional<BankAccount> findBankAccountByUuid(UUID uuid);

}
