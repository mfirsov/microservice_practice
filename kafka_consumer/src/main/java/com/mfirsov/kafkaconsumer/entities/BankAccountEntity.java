package com.mfirsov.kafkaconsumer.entities;

import com.datastax.driver.core.DataType;
import com.mfirsov.common.model.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@UserDefinedType("bank_account_type")
public class BankAccountEntity implements Serializable {

//    @Column("uuid")
    @CassandraType(type = DataType.Name.UUID)
    private UUID uuid;
//    @Column("first_name")
    @CassandraType(type = DataType.Name.TEXT)
    private String firstName;
//    @Column("last_name")
    @CassandraType(type = DataType.Name.TEXT)
    private String lastName;
//    @Column("patronymic")
    @CassandraType(type = DataType.Name.TEXT)
    private String patronymic;
//    @Column("account_number")
    @CassandraType(type = DataType.Name.BIGINT)
    private long accountNumber;
//    @Column("account_type")
    @CassandraType(type = DataType.Name.TEXT)
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
