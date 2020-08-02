package com.mfirsov.grpcservice.service;

import com.mfirsov.grpcservice.repository.CustomCassandraRepository;
import com.salesforce.grpc.contrib.spring.GrpcService;
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
                                .getBankAccountEntity()
                                .getAccountType()
                                .name()
                                .equals(bankAccountInfoRequest.getAccountType())))
                .map(bankAccountInfo -> BankAccountInfoProto.BankAccountInfo.newBuilder()
                        .setAddressEntity(BankAccountInfoProto.Address.newBuilder()
                                .setCity(bankAccountInfo.getAddressEntity().getCity())
                                .setState(bankAccountInfo.getAddressEntity().getCity())
                                .setStreet(bankAccountInfo.getAddressEntity().getStreet())
                                .build())

                        .setBankAccountEntity(BankAccountInfoProto.BankAccount.newBuilder()
                                .setAccountType(BankAccountInfoProto.BankAccount.AccountType.valueOf(bankAccountInfo.getBankAccountEntity().getAccountType().name()))
                                .setAccountNumber(bankAccountInfo.getBankAccountEntity().getAccountNumber())
                                .setFirstName(bankAccountInfo.getBankAccountEntity().getFirstName())
                                .setLastName(bankAccountInfo.getBankAccountEntity().getLastName())
                                .setPatronymic(bankAccountInfo.getBankAccountEntity().getPatronymic())
                                .setUuid(bankAccountInfo.getBankAccountEntity().getUuid().toString())
                                .build())

                        .build())
                .collectList()
                .map(bankAccountInfos -> BankAccountInfoProto.BankAccountInfoResponse.newBuilder().addAllBankAccountInfo(bankAccountInfos).build());
    }
}
