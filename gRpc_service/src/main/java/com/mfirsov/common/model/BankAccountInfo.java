package com.mfirsov.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountInfo {

    @JsonIgnore
    private UUID uuid;

    private BankAccount bankAccount;

    private Address address;

    public BankAccountInfo(BankAccount bankAccount, Address address) {
        this.bankAccount = bankAccount;
        this.address = address;
    }
}
