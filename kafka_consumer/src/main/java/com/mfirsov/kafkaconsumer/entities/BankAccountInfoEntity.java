package com.mfirsov.kafkaconsumer.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("bank_account_info")
public class BankAccountInfoEntity {

    @Id
    @JsonIgnore
    private UUID uuid;

//    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "bank_account")
    private BankAccountEntity bankAccountEntity;

//    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "address")
    private AddressEntity addressEntity;

    public BankAccountInfoEntity(BankAccountEntity bankAccountEntity, AddressEntity addressEntity) {
        this.bankAccountEntity = bankAccountEntity;
        this.addressEntity = addressEntity;
    }
}
