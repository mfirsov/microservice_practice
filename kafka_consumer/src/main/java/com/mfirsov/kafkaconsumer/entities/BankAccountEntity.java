package com.mfirsov.kafkaconsumer.entities;

import com.mfirsov.common.model.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@UserDefinedType("bank_account")
public class BankAccountEntity {

    @Column("uuid")
    private UUID uuid;
    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("patronymic")
    private String patronymic;
    @Column("account_number")
    private long accountNumber;
    @Column("account_type")
    private AccountType accountType;

    public BankAccountEntity(BankAccount bankAccount) {
        this.accountNumber = bankAccount.getAccountNumber();
        this.uuid = bankAccount.getUuid();
        this.accountType = AccountType.valueOf(bankAccount.getAccountType().name());
        this.firstName = bankAccount.getFirstName();
        this.lastName = bankAccount.getLastName();
        this.patronymic = bankAccount.getPatronymic();
    }

    public enum AccountType {
        DEBIT,CREDIT
    }

    public BankAccountEntity(String firstName, String lastName, String patronymic, AccountType accountType) {
        this.uuid = UUID.randomUUID();
        this.accountNumber = ThreadLocalRandom.current().nextLong();
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.accountType = accountType;
    }
}
