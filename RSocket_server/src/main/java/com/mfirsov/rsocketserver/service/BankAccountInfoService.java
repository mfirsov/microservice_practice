package com.mfirsov.rsocketserver.service;

import com.mfirsov.rsocketserver.entities.BankAccountInfoEntity;
import com.mfirsov.rsocketserver.repository.CustomCassandraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BankAccountInfoService {

    private final CustomCassandraRepository customCassandraRepository;

    public Mono<BankAccountInfoEntity> getBankAccountInfoByUUID(UUID uuid) {
        return customCassandraRepository.findById(uuid);
    }

    public Flux<BankAccountInfoEntity> getAllBankAccountInfo() {
        return customCassandraRepository.findAll();
    }

    public Mono<Void> deleteBankAccountInfoByUUID(UUID uuid) {
       return customCassandraRepository.deleteById(uuid);
    }

}
