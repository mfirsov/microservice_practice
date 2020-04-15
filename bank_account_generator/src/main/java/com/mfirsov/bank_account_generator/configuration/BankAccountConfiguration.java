package com.mfirsov.bank_account_generator.configuration;

import com.mfirsov.bank_account_generator.service.BankAccountService;
import com.mfirsov.bank_account_generator.service.IBankAccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankAccountConfiguration {

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

    @Bean
    public IBankAccountService getManBankAccountService() {
        return new BankAccountService(manFirstNamesPath, manLastNamesPath, manPatronymicsPath);
    }

    @Bean
    public IBankAccountService getWomanBankAccountService() {
        return new BankAccountService(womanFirstNamesPath, womanLastNamesPath, womanPatronymicsPath);
    }

}
