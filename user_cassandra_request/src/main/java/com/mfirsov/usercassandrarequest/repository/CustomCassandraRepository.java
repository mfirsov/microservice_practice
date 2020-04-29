package com.mfirsov.usercassandrarequest.repository;

import com.mfirsov.model.BankAccountInfo;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomCassandraRepository extends CassandraRepository<BankAccountInfo, UUID> {

    Optional<BankAccountInfo> findBankAccountInfoByUuid(UUID uuid);

}
