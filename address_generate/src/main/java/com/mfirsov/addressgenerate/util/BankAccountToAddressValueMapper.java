package com.mfirsov.addressgenerate.util;

import com.mfirsov.addressgenerate.client.AddressGeneratorClient;
import com.mfirsov.common.model.Address;
import com.mfirsov.common.model.BankAccount;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class BankAccountToAddressValueMapper implements ValueMapper<BankAccount, Address> {

    private final AddressGeneratorClient addressGeneratorClient;

    public BankAccountToAddressValueMapper(AddressGeneratorClient addressGeneratorClient) {
        this.addressGeneratorClient = addressGeneratorClient;
    }

    @Override
    public Address apply(BankAccount value) {
        Address address = addressGeneratorClient.getAddressFromAddressGenerator();
        log.info("Following message received from random API: {}", address);
        return address;
    }
}
