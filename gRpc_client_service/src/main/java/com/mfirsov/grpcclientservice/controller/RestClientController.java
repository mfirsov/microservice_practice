package com.mfirsov.grpcclientservice.controller;

import com.mfirsov.grpcclientservice.client.GRpcClient;
import com.mfirsov.grpcclientservice.model.BankAccountInfosResponse;
import com.mfirsov.grpcclientservice.service.GRpcToModelConverter;
import com.mfirsov.grpcservice.service.BankAccountInfoProto;
import com.mfirsov.model.BankAccount;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Log4j2
public class RestClientController {

    private final GRpcClient gRpcClient;

    public RestClientController(GRpcClient gRpcClient) {
        this.gRpcClient = gRpcClient;
    }

    @GetMapping(path = "/getBankAccountInfos",
            produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Mono<BankAccountInfoProto.BankAccountInfoResponse> getBankAccountInfo(@RequestParam("accountType") String accountType) {
        BankAccount.AccountType.valueOf(accountType);
//        BankAccountInfosResponse bankAccountInfosResponse = GRpcToModelConverter.convert(gRpcClient.getBankAccountInfo(accountType));
//        log.info("Following BankAccountInfo List was received from GRpc: " + bankAccountInfosResponse);
        return gRpcClient.getBankAccountInfo(accountType);
    }

}
