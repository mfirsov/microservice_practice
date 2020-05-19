package com.mfirsov.addressgenerate.util;

import com.mfirsov.addressgenerate.client.AddressGeneratorClient;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BankAccountToAddressValueMapper implements ValueMapper<BankAccount, Address> {

    @Autowired
    private AddressGeneratorClient addressGeneratorClient;

    @Override
    public Address apply(BankAccount value) {
        return addressGeneratorClient.getAddressFromAddressGenerator();
    }
}
