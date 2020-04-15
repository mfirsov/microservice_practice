package com.mfirsov.bank_account_generator.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public abstract class AbstractBankAccount {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String patronymic;
    private long accountNumber;

    public AbstractBankAccount(String firstName, String lastName, String patronymic) {
        this.uuid = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.accountNumber = ThreadLocalRandom.current().nextLong(0L, Long.MAX_VALUE);
    }
}
