package com.mfirsov.grpcclientservice.controller;

import com.mfirsov.grpcclientservice.client.GRpcClient;
import com.mfirsov.grpcclientservice.model.BankAccountInfosResponse;
import com.mfirsov.grpcclientservice.service.GRpcToModelConverter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class RestClientController {

    @Autowired
    private GRpcClient gRpcClient;

    @GetMapping(path = "/getBankAccountInfos",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<BankAccountInfosResponse> getBankAccountInfos(@RequestParam("accountType") String accountType) {
        BankAccountInfosResponse bankAccountInfosResponse = GRpcToModelConverter.convert(gRpcClient.getBankAccountInfo(accountType));
        log.info("Following BankAccountInfo List was received from GRpc: " + bankAccountInfosResponse);
        return ResponseEntity.ok(bankAccountInfosResponse);
    }

}
