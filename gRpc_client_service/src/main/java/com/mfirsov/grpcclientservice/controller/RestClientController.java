package com.mfirsov.grpcclientservice.controller;

import com.mfirsov.grpcclientservice.client.GRpcClient;
import com.mfirsov.grpcclientservice.model.BankAccountInfosResponse;
import com.mfirsov.grpcclientservice.service.GRpcToModelConverter;
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

@RestController
@Log4j2
public class RestClientController {

    @Autowired
    private GRpcClient gRpcClient;

    @GetMapping(path = "/getBankAccountInfos",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<Object> getBankAccountInfos(@RequestParam("accountType") String accountType) {
        try {
            BankAccount.AccountType.valueOf(accountType);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        BankAccountInfosResponse bankAccountInfosResponse = GRpcToModelConverter.convert(gRpcClient.getBankAccountInfo(accountType));
        log.info("Following BankAccountInfo List was received from GRpc: " + bankAccountInfosResponse);
        return ResponseEntity.ok(bankAccountInfosResponse);
    }

}
