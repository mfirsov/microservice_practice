package com.mfirsov.rsocketserver.service;

import com.mfirsov.model.BankAccountInfo;
import com.mfirsov.rsocketserver.repository.CustomReactiveCassandraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class BankAccountInfoService {

    @Autowired
    private CustomReactiveCassandraRepository customReactiveCassandraRepository;

    public Mono<BankAccountInfo> getBankAccountInfoByUUID(UUID uuid) {
        return customReactiveCassandraRepository.findById(uuid);
    }

    public Flux<BankAccountInfo> getAllBankAccountInfo() {
        return customReactiveCassandraRepository.findAll();
    }

    public Mono<Void> deleteBankAccountInfoByUUID(UUID uuid) {
       return customReactiveCassandraRepository.deleteById(uuid);
    }

}
