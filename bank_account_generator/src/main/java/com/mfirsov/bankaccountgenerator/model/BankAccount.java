package com.mfirsov.bankaccountgenerator.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class BankAccount extends AbstractBankAccount {

    public BankAccount(String firstName, String lastName, String patronymic) {
        super(firstName, lastName, patronymic);
    }

    public BankAccount() {
        super();
    }
}
