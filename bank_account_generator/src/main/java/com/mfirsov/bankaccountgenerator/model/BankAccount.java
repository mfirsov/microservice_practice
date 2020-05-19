package com.mfirsov.bankaccountgenerator.model;

public class BankAccount extends AbstractBankAccount {

    public BankAccount(String firstName, String lastName, String patronymic) {
        super(firstName, lastName, patronymic);
    }

    public BankAccount() {
        super();
    }
}
