package com.mfirsov.bankaccountgenerator.service;

import com.mfirsov.bankaccountgenerator.model.BankAccount;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.ThreadLocalRandom.current;

@Service
public class BankAccountService implements IBankAccountService {

    @Value("${man.firstnames.path:bank_account_generator/src/main/resources/MansNames.txt}")
    private String manFirstNamesPath;

    @Value("${man.lastnames.path:bank_account_generator/src/main/resources/MansSurnames.txt}")
    private String manLastNamesPath;

    @Value("${man.patronymics.path:bank_account_generator/src/main/resources/MansPatronymics.txt}")
    private String manPatronymicsPath;

    @Value("${woman.firstnames.path:bank_account_generator/src/main/resources/WomansNames.txt}")
    private String womanFirstNamesPath;

    @Value("${woman.lastnames.path:bank_account_generator/src/main/resources/WomansSurnames.txt}")
    private String womanLastNamesPath;

    @Value("${woman.patronymics.path:bank_account_generator/src/main/resources/WomansPatronymics.txt}")
    private String womanPatronymicsPath;

    private final List<String> manFirstNames = new ArrayList<>();
    private final List<String> manLastNames = new ArrayList<>();
    private final List<String> manPatronymics = new ArrayList<>();
    private final List<String> womanFirstNames = new ArrayList<>();
    private final List<String> womanLastNames = new ArrayList<>();
    private final List<String> womanPatronymics = new ArrayList<>();

    public BankAccountService() {
        manFirstNames.addAll(getAllStrings(Path.of(manFirstNamesPath)));
        manLastNames.addAll(getAllStrings(Path.of(manLastNamesPath)));
        manPatronymics.addAll(getAllStrings(Path.of(manPatronymicsPath)));
        womanFirstNames.addAll(getAllStrings(Path.of(womanFirstNamesPath)));
        womanLastNames.addAll(getAllStrings(Path.of(womanLastNamesPath)));
        womanPatronymics.addAll(getAllStrings(Path.of(womanPatronymicsPath)));

    }

    public BankAccountService(String manFirstNamePath, String manLastNamePath, String manPatronymicsPath,
                              String womanFirstNamePath, String womanLastNamePath, String womanPatronymicsPath) {
        manFirstNames.addAll(getAllStrings(Path.of(manFirstNamePath)));
        manLastNames.addAll(getAllStrings(Path.of(manLastNamePath)));
        manPatronymics.addAll(getAllStrings(Path.of(manPatronymicsPath)));
        womanFirstNames.addAll(getAllStrings(Path.of(womanFirstNamePath)));
        womanLastNames.addAll(getAllStrings(Path.of(womanLastNamePath)));
        womanPatronymics.addAll(getAllStrings(Path.of(womanPatronymicsPath)));
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

    private BankAccount generateBankAccount(Sex sex) {
        BankAccount bankAccount = new BankAccount();
        if (Sex.FEMALE.equals(sex)) {
            bankAccount.setFirstName(womanFirstNames.get(current().nextInt(manFirstNames.size())));
            bankAccount.setLastName(womanLastNames.get(current().nextInt(womanLastNames.size())));
            bankAccount.setPatronymic(womanPatronymics.get(current().nextInt(womanPatronymics.size())));
        }
        if (Sex.MALE.equals(sex)) {

            bankAccount.setFirstName(manFirstNames.get(current().nextInt(manFirstNames.size())));
            bankAccount.setLastName(manLastNames.get(current().nextInt(manLastNames.size())));
            bankAccount.setPatronymic(manPatronymics.get(current().nextInt(manPatronymics.size())));
        }

        return bankAccount;
    }

    @Override
    public BankAccount generateManBankAccount() {
        return generateBankAccount(Sex.MALE);
    }

    @Override
    public BankAccount generateWomanBankAccount() {
        return generateBankAccount(Sex.FEMALE);
    }

    private enum Sex {
        MALE,FEMALE
    }
}
