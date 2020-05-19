package com.mfirsov.rsocketclient.controller;

import com.mfirsov.model.BankAccountInfo;
import lombok.extern.log4j.Log4j2;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Log4j2
public class RSocketRestController {

    @Autowired
    private RSocketRequester rSocketRequester;

    @GetMapping(path = "/getBankAccountInfoByUUID")
    @ResponseBody Publisher<BankAccountInfo> getBankAccountInfoByUUID(@RequestParam("uuid") UUID uuid) {
        log.info("Requested BankAccountInfo with UUID: {}", uuid);
        Publisher<BankAccountInfo> bankAccountInfoPublisher = rSocketRequester
                .route("getBankAccountInfoByUUID")
                .data(uuid)
                .retrieveMono(BankAccountInfo.class);
        log.info("Following message was sent to client: {}", bankAccountInfoPublisher);
        return bankAccountInfoPublisher;
    }

    @DeleteMapping(path = "/deleteBankAccountInfoByUUID")
    Publisher<Void> deleteBankAccountInfoByUUID(@RequestParam("uuid") UUID uuid) {
        log.info("Request for deleting BankAccountInfo with following UUID: {} received", uuid);
        return rSocketRequester
                .route("deleteBankAccountInfoByUUID")
                .data(uuid)
                .send();
    }

    @GetMapping(path = "/getAllBankAccountInfoList",
                produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody Publisher<BankAccountInfo> getAllBankAccountInfoList() {
        Publisher<BankAccountInfo> bankAccountInfoPublisher = rSocketRequester
                .route("getAllBankAccountInfoList")
                .retrieveFlux(BankAccountInfo.class);
        log.info("Following data was requested: {}", bankAccountInfoPublisher);
        return bankAccountInfoPublisher;
    }

}
