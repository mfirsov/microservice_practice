package com.mfirsov.bankaccountgenerator.service;

import com.mfirsov.bankaccountgenerator.model.BankAccount;

public interface IBankAccountService {

    BankAccount generateManBankAccount();

    BankAccount generateWomanBankAccount();

}
