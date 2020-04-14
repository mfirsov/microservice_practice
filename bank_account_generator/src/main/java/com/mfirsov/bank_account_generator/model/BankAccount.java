package com.mfirsov.bank_account_generator.model;

import java.util.UUID;

public class BankAccount extends AbstractBankAccount {

    public BankAccount(UUID uuid, String firstName, String lastName, String patronymic, long accountNumber) {
        super(uuid, firstName, lastName, patronymic, accountNumber);
    }
}
