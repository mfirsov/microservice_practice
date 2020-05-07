package com.mfirsov.rsocketserver.controller;

import com.mfirsov.model.BankAccountInfo;
import com.mfirsov.rsocketserver.service.BankAccountInfoService;
import com.mfirsov.rsocketserver.model.MultipleMessageRequest;
import com.mfirsov.rsocketserver.model.SingleMessageRequest;
import com.mfirsov.rsocketserver.model.SingleMessageResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

@Controller
@Log4j2
public class RSocketController {

    private static final String SERVER = "Server";
    private static final String RESPONSE = "Response";
    private static final String STREAM = "Stream";

    @Autowired
    private BankAccountInfoService bankAccountInfoService;

    @MessageMapping("request-response")
    SingleMessageResponse getBankAccountInfoByUUID(SingleMessageRequest singleMessageRequest) {
        log.info("Following message was received: {}", singleMessageRequest);
        SingleMessageResponse singleMessageResponse = new SingleMessageResponse();
        singleMessageResponse.setIndex(singleMessageRequest.getIndex() != 0 ? singleMessageRequest.getIndex() + 1 : 1);
        singleMessageResponse.setOrigin(SERVER);
        singleMessageResponse.setInteraction(RESPONSE);
        singleMessageResponse.setBankAccountInfo(bankAccountInfoService.getBankAccountInfoByUUID(singleMessageRequest.getUuid()));
        log.info("Following message was sent: {}", singleMessageResponse);
        return singleMessageResponse;
    }

    @MessageMapping("fire-and-forget")
    void deleteBankAccountInfoByUUID(SingleMessageRequest singleMessageRequest) {
        log.info("BankAccountInfo with following UUID: {} will be deleted", singleMessageRequest.getUuid());
        bankAccountInfoService.deleteBankAccountInfoByUUID(singleMessageRequest.getUuid());
        log.info("BankAccount info with following UUID: {} was deleted", singleMessageRequest.getUuid());
    }

    @MessageMapping("stream")
    Flux<BankAccountInfo> getAllBankAccountInfo(MultipleMessageRequest multipleMessageRequest) {
        log.info("Following message was received: {}", multipleMessageRequest);
        Flux<BankAccountInfo> bankAccountInfoFlux = bankAccountInfoService.getAllBankAccountInfo();
        log.info("Following message was sent to client: {}", bankAccountInfoFlux);
        return bankAccountInfoFlux;
    }

}
