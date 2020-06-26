package com.mfirsov.grpcservice.service;

import com.mfirsov.repository.CustomCassandraRepository;
import com.salesforce.grpc.contrib.spring.GrpcService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@GrpcService
public class BankAccountInfoService extends ReactorBankAccountInfoServiceGrpc.BankAccountInfoServiceImplBase {

    private final CustomCassandraRepository customCassandraRepository;

    public BankAccountInfoService(CustomCassandraRepository customCassandraRepository) {
        this.customCassandraRepository = customCassandraRepository;
    }

    @Override
    public Mono<BankAccountInfoProto.BankAccountInfoResponse> getBankAccountInfo(Mono<BankAccountInfoProto.BankAccountInfoRequest> request) {
        return request
                .flatMapMany(bankAccountInfoRequest -> customCassandraRepository.findAll()
                        .filter(bankAccountInfo -> bankAccountInfo
                                .getBankAccount()
                                .getAccountType()
                                .name()
                                .equals(bankAccountInfoRequest.getAccountType())))
                .map(bankAccountInfo -> BankAccountInfoProto.BankAccountInfo.newBuilder()

                        .setAddress(BankAccountInfoProto.Address.newBuilder()
                                .setCity(bankAccountInfo.getAddress().getCity())
                                .setState(bankAccountInfo.getAddress().getCity())
                                .setStreet(bankAccountInfo.getAddress().getStreet())
                                .build())

                        .setBankAccount(BankAccountInfoProto.BankAccount.newBuilder()
                                .setAccountType(BankAccountInfoProto.BankAccount.AccountType.valueOf(bankAccountInfo.getBankAccount().getAccountType().name()))
                                .setAccountNumber(bankAccountInfo.getBankAccount().getAccountNumber())
                                .setFirstName(bankAccountInfo.getBankAccount().getFirstName())
                                .setLastName(bankAccountInfo.getBankAccount().getLastName())
                                .setPatronymic(bankAccountInfo.getBankAccount().getPatronymic())
                                .setUuid(bankAccountInfo.getBankAccount().getUuid().toString())
                                .build())

                        .build())
                .collectList()
                .map(bankAccountInfos -> BankAccountInfoProto.BankAccountInfoResponse.newBuilder().addAllBankAccountInfo(bankAccountInfos).build());
    }
}
