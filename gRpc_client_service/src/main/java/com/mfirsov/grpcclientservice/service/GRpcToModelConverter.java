package com.mfirsov.grpcclientservice.service;

import com.mfirsov.grpcclientservice.model.BankAccountInfosResponse;
import com.mfirsov.grpcservice.service.BankAccountInfoProto;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;

public class GRpcToModelConverter {

//    public static BankAccountInfosResponse convert(Mono<BankAccountInfoProto.BankAccountInfoResponse> bankAccountInfoResponse) {
//        return new BankAccountInfosResponse(bankAccountInfoResponse.getBankAccountInfoList().stream().map(bankAccountInfo -> {
//            BankAccount bankAccount = convertBankAccount(bankAccountInfo.getBankAccount());
//            return new BankAccountInfo(bankAccount.getUuid(), bankAccount, convertAddress(bankAccountInfo.getAddress()));
//        }).collect(Collectors.toList()));
//    }
//
//    private static BankAccount convertBankAccount(com.mfirsov.grpcclientservice.service.BankAccount bankAccount) {
//        return new BankAccount(UUID.fromString(bankAccount.getUuid()),
//                bankAccount.getFirstName(),
//                bankAccount.getLastName(),
//                bankAccount.getPatronymic(),
//                bankAccount.getAccountNumber(),
//                BankAccount.AccountType.valueOf(bankAccount.getAccountType().name()));
//    }
//
//    private static Address convertAddress(com.mfirsov.grpcclientservice.service.Address address) {
//        return new Address(address.getStreet(), address.getCity(), address.getState());
//    }

}
