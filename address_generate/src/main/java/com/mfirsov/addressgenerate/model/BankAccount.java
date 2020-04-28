package com.mfirsov.addressgenerate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    private UUID uuid;
    private String firstName;
    private String lastName;
    private String patronymic;
    private long accountNumber;
    private AccountType accountType;

    public enum AccountType {
        DEBIT,CREDIT;
    }
}
