package com.mfirsov.addressgenerate.configuration;

import com.mfirsov.addressgenerate.util.dev.FakeAddress;
import com.mfirsov.common.model.Address;
import com.mfirsov.common.model.BankAccount;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Profile("dev")
@Log4j2
public class KafkaBindingConfigurationDev {

    @Bean(name = "address")
    public Function<KTable<UUID, BankAccount>, KStream<UUID, Address>> address() {
        return input -> input
                .toStream()
                .filter((uuid, bankAccount) -> bankAccount.getLastName().startsWith("А"))
                .mapValues(bankAccount -> {
                    log.debug("Following BankAccount proceed filtering: {}", bankAccount);
                    return FakeAddress.getFakedAddress();
                });
    }

}
