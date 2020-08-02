package com.mfirsov.kafkaconsumer.entities;

import com.datastax.driver.core.DataType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mfirsov.common.model.BankAccountInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("bank_account_info")
public class BankAccountInfoEntity implements Serializable {

    @PrimaryKey
    @JsonIgnore
    private UUID uuid;

    @CassandraType(type = DataType.Name.UDT, userTypeName = "bank_account_type")
    private BankAccountEntity bankAccountEntity;

    @CassandraType(type = DataType.Name.UDT, userTypeName = "address_type")
    private AddressEntity addressEntity;

    public BankAccountInfoEntity(BankAccountEntity bankAccountEntity, AddressEntity addressEntity) {
        this.bankAccountEntity = bankAccountEntity;
        this.addressEntity = addressEntity;
    }

    public BankAccountInfoEntity(BankAccountInfo bankAccountInfo) {
        this.bankAccountEntity = new BankAccountEntity(bankAccountInfo.getBankAccount());
        this.addressEntity = new AddressEntity(bankAccountInfo.getAddress());
        this.uuid = bankAccountInfo.getUuid();
    }
}
