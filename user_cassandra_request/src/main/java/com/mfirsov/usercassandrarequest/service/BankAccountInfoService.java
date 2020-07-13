package com.mfirsov.usercassandrarequest.service;

import com.mfirsov.model.BankAccountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class BankAccountInfoService {

    private final CustomCassandraRepository cassandraRepository;

    public BankAccountInfoService(CustomCassandraRepository cassandraRepository) {
        this.cassandraRepository = cassandraRepository;
    }

    public Mono<BankAccountInfo> getBankAccountInfoByUuid(UUID uuid) {
        return cassandraRepository.findBankAccountInfoByUuid(uuid);
    }
}
