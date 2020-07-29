package com.mfirsov.addressgenerate.configuration;

import com.mfirsov.addressgenerate.client.AddressGeneratorClient;
import com.mfirsov.common.model.Address;
import com.mfirsov.common.model.BankAccount;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Profile("!dev")
public class KafkaBindingConfiguration {

    final AddressGeneratorClient addressGeneratorClient;

    @Bean
    public Function<KTable<UUID, BankAccount>, KStream<UUID, Address>> address() {
        return input -> input
                .filter((uuid, bankAccount) -> bankAccount.getLastName().startsWith("А"))
                .mapValues(bankAccount -> addressGeneratorClient.getAddressFromAddressGenerator().block())
                .toStream();
    }

}
