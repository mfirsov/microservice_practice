package com.mfirsov.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@AllArgsConstructor
@UserDefinedType("bank_account")
public class BankAccount {

    @CassandraType(type = CassandraType.Name.UUID)
    private UUID uuid;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String firstName;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String lastName;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String patronymic;
    @CassandraType(type = CassandraType.Name.DECIMAL)
    private long accountNumber;
    @CassandraType(type = CassandraType.Name.TEXT)
    private AccountType accountType;

    public enum AccountType {
        DEBIT,CREDIT
    }

    public BankAccount(String firstName, String lastName, String patronymic, AccountType accountType) {
        this.uuid = UUID.randomUUID();
        this.accountNumber = ThreadLocalRandom.current().nextLong();
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.accountType = accountType;
    }
}
