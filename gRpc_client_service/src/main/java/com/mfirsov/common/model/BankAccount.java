package com.mfirsov.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccount {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String patronymic;
    private long accountNumber;
    private AccountType accountType;

    public enum AccountType {
        DEBIT,CREDIT
    }

    public BankAccount(String firstName, String lastName, String patronymic, AccountType accountType) {
        this.uuid = UUID.randomUUID();
        this.accountNumber = ThreadLocalRandom.current().nextLong();
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.accountType = accountType;
    }
}
