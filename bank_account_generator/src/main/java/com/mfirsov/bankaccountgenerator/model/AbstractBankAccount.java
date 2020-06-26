package com.mfirsov.bankaccountgenerator.model;

import lombok.*;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Data
@AllArgsConstructor
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

    public AbstractBankAccount() {
        this.uuid = UUID.randomUUID();
        this.accountNumber = ThreadLocalRandom.current().nextLong(0L, Long.MAX_VALUE);
    }
}
