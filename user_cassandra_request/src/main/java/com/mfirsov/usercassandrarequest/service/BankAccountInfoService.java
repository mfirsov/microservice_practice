package com.mfirsov.usercassandrarequest.service;

import com.mfirsov.usercassandrarequest.entities.BankAccountInfoEntity;
import com.mfirsov.usercassandrarequest.repository.CustomCassandraRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class BankAccountInfoService {

    private final CustomCassandraRepository cassandraRepository;

    public BankAccountInfoService(CustomCassandraRepository cassandraRepository) {
        this.cassandraRepository = cassandraRepository;
    }

    public Mono<BankAccountInfoEntity> getBankAccountInfoByUuid(UUID uuid) {
        return cassandraRepository.findBankAccountInfoByUuid(uuid);
    }
}
