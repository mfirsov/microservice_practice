package com.mfirsov.kafkaconsumer.util;

import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import org.apache.kafka.streams.kstream.ValueJoiner;
import org.springframework.stereotype.Component;

@Component
public class BankAccountAndAddressValueJoiner implements ValueJoiner<BankAccount, Address, BankAccountInfo> {
    @Override
    public BankAccountInfo apply(BankAccount value1, Address value2) {
        return new BankAccountInfo(value1.getUuid(), value1, value2);
    }
}
