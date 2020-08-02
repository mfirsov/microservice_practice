package com.mfirsov.kafkaconsumer.converters;

import com.mfirsov.common.model.BankAccountInfo;
import com.mfirsov.kafkaconsumer.entities.AddressEntity;
import com.mfirsov.kafkaconsumer.entities.BankAccountEntity;
import com.mfirsov.kafkaconsumer.entities.BankAccountInfoEntity;
import org.springframework.stereotype.Component;

@Component
public class BankAccountInfoConverter {

    public BankAccountInfoEntity from(BankAccountInfo bankAccountInfo) {
        return BankAccountInfoEntity.builder()
                .addressEntity(new AddressEntity(bankAccountInfo.getAddress()))
                .bankAccountEntity(new BankAccountEntity(bankAccountInfo.getBankAccount()))
                .uuid(bankAccountInfo.getUuid())
                .build();
    }

}
