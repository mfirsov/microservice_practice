package com.mfirsov.grpcservice.controller;

import com.mfirsov.grpcservice.service.Address;
import com.mfirsov.grpcservice.service.BankAccountInfoRequest;
import com.mfirsov.grpcservice.service.BankAccountInfoResponse;
import com.mfirsov.grpcservice.service.BankAccountInfoServiceGrpc;
import com.mfirsov.model.BankAccount;
import com.mfirsov.model.BankAccountInfo;
import com.mfirsov.repository.CustomCassandraRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@GrpcService
public class BankAccountInfoService extends BankAccountInfoServiceGrpc.BankAccountInfoServiceImplBase {

    @Autowired
    private CustomCassandraRepository customCassandraRepository;

    @Override
    public void getBankAccountInfo(BankAccountInfoRequest request, StreamObserver<BankAccountInfoResponse> responseObserver) {
        List<BankAccountInfo> bankAccountInfos = customCassandraRepository.findAll();

        BankAccountInfoResponse bankAccountInfoResponse = BankAccountInfoResponse.newBuilder()
                .addAllBankAccountInfo(bankAccountInfos.stream()
                        .filter(bankAccountInfo -> bankAccountInfo.getBankAccount().getAccountType().name().equals(request.getAccountType()))
                        .map(bankAccountInfo -> com.mfirsov.grpcservice.service.BankAccountInfo
                            .newBuilder()
                            .setAddress(Address.newBuilder()
                                .setCity(bankAccountInfo.getAddress().getCity())
                                .setState(bankAccountInfo.getAddress().getCity())
                                .setStreet(bankAccountInfo.getAddress().getStreet())
                                .build())
                            .setBankAccount(com.mfirsov.grpcservice.service.BankAccount.newBuilder()
                                .setAccountType(com.mfirsov.grpcservice.service.BankAccount.AccountType.valueOf(bankAccountInfo.getBankAccount().getAccountType().name()))
                                .setAccountNumber(bankAccountInfo.getBankAccount().getAccountNumber())
                                .setFirstName(bankAccountInfo.getBankAccount().getFirstName())
                                .setLastName(bankAccountInfo.getBankAccount().getLastName())
                                .setPatronymic(bankAccountInfo.getBankAccount().getPatronymic())
                                .setUuid(bankAccountInfo.getBankAccount().getUuid().toString())
                                .build())
                            .build())
                        .collect(Collectors.toList()))
                .build();

        responseObserver.onNext(bankAccountInfoResponse);
        responseObserver.onCompleted();

    }
}
