package com.mfirsov.usercassandrarequest.util;

import com.mfirsov.common.model.Address;
import com.mfirsov.common.model.BankAccount;
import com.mfirsov.common.model.BankAccountInfo;
import com.mfirsov.usercassandrarequest.entities.AddressEntity;
import com.mfirsov.usercassandrarequest.entities.BankAccountEntity;
import com.mfirsov.usercassandrarequest.entities.BankAccountInfoEntity;

public class BankAccountInfoEntityModelConverter {

    public static BankAccountInfoEntity to(BankAccountInfo source) {
        return BankAccountInfoEntity.builder()
                .addressEntity(new AddressEntity(source.getAddress()))
                .bankAccountEntity(new BankAccountEntity(source.getBankAccount()))
                .uuid(source.getUuid())
                .build();
    }

    public static BankAccountInfo from(BankAccountInfoEntity source) {
        return BankAccountInfo.builder()
                .address(Address.builder()
                        .city(source.getAddressEntity().getCity())
                        .state(source.getAddressEntity().getState())
                        .street(source.getAddressEntity().getStreet())
                        .build())
                .bankAccount(BankAccount.builder()
                        .accountNumber(source.getBankAccountEntity().getAccountNumber())
                        .accountType(BankAccount.AccountType.valueOf(source.getBankAccountEntity().getAccountType().name()))
                        .firstName(source.getBankAccountEntity().getFirstName())
                        .lastName(source.getBankAccountEntity().getLastName())
                        .patronymic(source.getBankAccountEntity().getPatronymic())
                        .uuid(source.getUuid())
                        .build())
                .build();
    }

}
