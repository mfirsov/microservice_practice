package com.mfirsov.bankaccountgenerator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BankAccount {

    private UUID uuid = UUID.randomUUID();
    private String firstName;
    private String lastName;
    private String patronymic;
    private long accountNumber = ThreadLocalRandom.current().nextLong(0, Long.MAX_VALUE);

    public BankAccount(String firstName, String lastName, String patronymic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
    }
}