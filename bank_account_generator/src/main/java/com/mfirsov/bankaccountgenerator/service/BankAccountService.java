package com.mfirsov.bankaccountgenerator.service;

import com.mfirsov.bankaccountgenerator.model.BankAccount;

public interface BankAccountService {

    BankAccount generateManBankAccount();

    BankAccount generateWomanBankAccount();

}
