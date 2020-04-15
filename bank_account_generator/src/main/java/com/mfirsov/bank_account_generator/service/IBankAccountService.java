package com.mfirsov.bank_account_generator.service;

import com.mfirsov.bank_account_generator.model.BankAccount;
import org.springframework.stereotype.Component;

public interface IBankAccountService {

    public BankAccount generateBankAccount();

}
