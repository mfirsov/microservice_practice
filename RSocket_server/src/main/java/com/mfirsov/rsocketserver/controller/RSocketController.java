package com.mfirsov.rsocketserver.controller;

import com.mfirsov.common.model.BankAccountInfo;
import com.mfirsov.rsocketserver.service.BankAccountInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Controller
@Log4j2
public class RSocketController {

    private final BankAccountInfoService bankAccountInfoService;

    public RSocketController(BankAccountInfoService bankAccountInfoService) {
        this.bankAccountInfoService = bankAccountInfoService;
    }

    @MessageMapping("getBankAccountInfoByUUID")
    Mono<BankAccountInfo> getBankAccountInfoByUUID(UUID uuid) {
        log.info("Searching BankAccountInfo with UUID: {}", uuid);
        Mono<BankAccountInfo> bankAccountInfoMono = bankAccountInfoService.getBankAccountInfoByUUID(uuid);
        log.info("Following message was sent: {}", bankAccountInfoMono);
        return bankAccountInfoMono;
    }

    @MessageMapping("deleteBankAccountInfoByUUID")
    Mono<Void> deleteBankAccountInfoByUUID(UUID uuid) {
        log.info("BankAccountInfo with following UUID: {} will be deleted", uuid);
        Mono<Void> voidMono = bankAccountInfoService.deleteBankAccountInfoByUUID(uuid);
        log.info("BankAccount info with following UUID: {} was deleted", uuid);
        return voidMono;
    }

    @MessageMapping("getAllBankAccountInfoList")
    Flux<BankAccountInfo> getAllBankAccountInfoList() {
        log.info("Request for all BankAccountInfos received");
        Flux<BankAccountInfo> bankAccountInfoFlux = bankAccountInfoService.getAllBankAccountInfo();
        log.info("Following message was sent to client: {}", bankAccountInfoFlux);
        return bankAccountInfoFlux;
    }

}
