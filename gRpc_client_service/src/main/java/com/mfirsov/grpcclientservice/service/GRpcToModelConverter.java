package com.mfirsov.grpcclientservice.service;

import com.mfirsov.common.model.Address;
import com.mfirsov.common.model.BankAccount;
import com.mfirsov.common.model.BankAccountInfo;
import com.mfirsov.grpcclientservice.model.BankAccountInfosResponse;
import com.mfirsov.grpcservice.service.BankAccountInfoProto;

import java.util.UUID;
import java.util.stream.Collectors;

public class GRpcToModelConverter {

    public static BankAccountInfosResponse convert(BankAccountInfoProto.BankAccountInfoResponse bankAccountInfoResponse) {
        return new BankAccountInfosResponse(bankAccountInfoResponse.getBankAccountInfoList().stream().map(bankAccountInfo -> {
            BankAccount bankAccount = convertBankAccount(bankAccountInfo.getBankAccountEntity());
            return new BankAccountInfo(bankAccount.getUuid(), bankAccount, convertAddress(bankAccountInfo.getAddressEntity()));
        }).collect(Collectors.toList()));
    }

    private static BankAccount convertBankAccount(BankAccountInfoProto.BankAccount bankAccount) {
        return new BankAccount(UUID.fromString(bankAccount.getUuid()),
                bankAccount.getFirstName(),
                bankAccount.getLastName(),
                bankAccount.getPatronymic(),
                bankAccount.getAccountNumber(),
                BankAccount.AccountType.valueOf(bankAccount.getAccountType().name()));
    }

    private static Address convertAddress(BankAccountInfoProto.Address address) {
        return new Address(address.getStreet(), address.getCity(), address.getState());
    }

}
