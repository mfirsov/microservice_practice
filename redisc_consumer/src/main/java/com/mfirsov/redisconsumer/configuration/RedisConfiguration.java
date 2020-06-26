package com.mfirsov.redisconsumer.configuration;

import com.mfirsov.model.BankAccountInfo;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.ReadFrom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.UUID;

@Configuration
public class RedisConfiguration {

    @Bean
    public ReactiveRedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.MASTER_PREFERRED)
                .commandTimeout(Duration.ofSeconds(2))
                .shutdownTimeout(Duration.ZERO)
                .clientName("redis-consumer")
                .clientOptions(ClientOptions.builder()
                        .autoReconnect(true)
                        .disconnectedBehavior(ClientOptions.DisconnectedBehavior.DEFAULT)
                        .build())
                .build();
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(), lettuceClientConfiguration);
    }

    @Bean
    public ReactiveRedisTemplate<String, BankAccountInfo> reactiveRedisTemplate(ReactiveRedisConnectionFactory redisConnectionFactory) {
        return new ReactiveRedisTemplate<>(redisConnectionFactory, RedisSerializationContext.<String, BankAccountInfo>newSerializationContext()
                .key(new StringRedisSerializer())
                .value(new Jackson2JsonRedisSerializer<>(BankAccountInfo.class))
                .hashKey(new StringRedisSerializer())
                .hashValue(new Jackson2JsonRedisSerializer<>(BankAccountInfo.class))
                .string(new StringRedisSerializer())
                .build());
    }

}
