package com.mfirsov.bank_account_generator.service;

import com.mfirsov.bank_account_generator.model.BankAccount;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.ThreadLocalRandom.current;

public class BankAccountService implements IBankAccountService {

    private final List<String> firstNames;
    private final List<String> lastNames;
    private final List<String> patronymics;

    public BankAccountService(String firstNamePath, String lastNamePath, String patronymicsPath) {
        this.firstNames = this.getAllStrings(Path.of(firstNamePath));
        this.lastNames = this.getAllStrings(Path.of(lastNamePath));
        this.patronymics = this.getAllStrings(Path.of(patronymicsPath));
    }


    private List<String> getAllStrings(Path path) {
        List<String> stringList = new ArrayList<>();
        try {
            stringList.addAll(Files.readAllLines(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringList;
    }

    @Override
    public BankAccount generateBankAccount() {
        return new BankAccount(firstNames.get(current().nextInt(firstNames.size())),
                               lastNames.get(current().nextInt(lastNames.size())),
                               patronymics.get(current().nextInt(patronymics.size())));
    }
}
