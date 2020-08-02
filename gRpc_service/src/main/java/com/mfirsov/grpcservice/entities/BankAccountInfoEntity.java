package com.mfirsov.grpcservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mfirsov.common.model.BankAccountInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.CassandraType;
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

    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "bank_account")
    private BankAccountEntity bankAccountEntity;

    @CassandraType(type = CassandraType.Name.UDT, userTypeName = "address")
    private AddressEntity addressEntity;

    public BankAccountInfoEntity(BankAccountEntity bankAccountEntity, AddressEntity addressEntity) {
        this.bankAccountEntity = bankAccountEntity;
        this.addressEntity = addressEntity;
    }

    public BankAccountInfoEntity(BankAccountInfo bankAccountInfo) {
        this.addressEntity = new AddressEntity(bankAccountInfo.getAddress());
        this.bankAccountEntity = new BankAccountEntity(bankAccountInfo.getBankAccount());
        this.uuid = bankAccountInfo.getUuid();
    }
}
