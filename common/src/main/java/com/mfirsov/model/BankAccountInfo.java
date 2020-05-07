package com.mfirsov.model;

import com.datastax.driver.core.DataType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("bank_account_info")
public class BankAccountInfo {

    @Id
    @JsonIgnore
    @CassandraType(type = DataType.Name.UUID)
    private UUID uuid;

    @CassandraType(type = DataType.Name.UDT, userTypeName = "bank_account")
    private BankAccount bankAccount;

    @CassandraType(type = DataType.Name.UDT, userTypeName = "address")
    private Address address;

}
