package com.mfirsov.redisconsumer.service;

import com.mfirsov.model.BankAccountInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
@Log4j2
public class RedisSubscriptionService {

    private final ReactiveRedisTemplate<String, BankAccountInfo> redisTemplate;

    public RedisSubscriptionService(ReactiveRedisTemplate<String, BankAccountInfo> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    public Mono<BankAccountInfo> receiveMessageFromRedisByUuid(UUID uuid) {
        return redisTemplate.opsForValue().get(uuid.toString());
    }

    public Flux<String> getAllBankAccountInfosUuids() {
        return redisTemplate.keys("bank-account-info:*");
    }
}
