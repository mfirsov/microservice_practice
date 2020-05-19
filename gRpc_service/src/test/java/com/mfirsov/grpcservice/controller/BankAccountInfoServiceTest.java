package com.mfirsov.grpcservice.controller;

import com.mfirsov.grpcservice.service.BankAccount;
import com.mfirsov.grpcservice.service.BankAccountInfoRequest;
import com.mfirsov.grpcservice.service.BankAccountInfoResponse;
import com.mfirsov.model.Address;
import com.mfirsov.model.BankAccountInfo;
import com.mfirsov.repository.CustomCassandraRepository;
import io.grpc.internal.testing.StreamRecorder;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.cassandra.ReactiveSession;
import org.springframework.data.cassandra.SessionFactory;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@Log4j2
class BankAccountInfoServiceTest {

    @InjectMocks
    BankAccountInfoService bankAccountInfoService;

    @Mock
    CustomCassandraRepository customCassandraRepository;

    @Test
    @DisplayName("Only 1 BankAccountInfo will be return")
    void successfulReturn() throws Exception {
        List<BankAccountInfo> bankAccountInfos = new ArrayList<>();
        bankAccountInfos.add(new BankAccountInfo(
                UUID.randomUUID(),
                new com.mfirsov.model.BankAccount("TestName", "TestLastName", "TestPatronymic", com.mfirsov.model.BankAccount.AccountType.DEBIT),
                new Address("TestStreet", "TestCity", "TestState")));
        bankAccountInfos.add(new BankAccountInfo(
                UUID.randomUUID(),
                new com.mfirsov.model.BankAccount("TestName1", "TestLastName1", "TestPatronymic1", com.mfirsov.model.BankAccount.AccountType.CREDIT),
                new Address("TestStreet1", "TestCity1", "TestState1")));
        when(customCassandraRepository.findAll()).thenReturn(bankAccountInfos);
        BankAccountInfoRequest bankAccountInfoRequest = BankAccountInfoRequest.newBuilder()
                .setAccountType(BankAccount.AccountType.CREDIT.name())
                .build();
        StreamRecorder<BankAccountInfoResponse> responseObserver = StreamRecorder.create();
        bankAccountInfoService.getBankAccountInfo(bankAccountInfoRequest, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        assertNull(responseObserver.getError());
        List<BankAccountInfoResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());
        BankAccountInfoResponse actualResponse = results.get(0);
        log.info("Following object was received: {}", actualResponse.toString());
        assertNotNull(actualResponse);
    }

    @Test
    @DisplayName("Empty return")
    void emptyReturn() throws Exception {
        List<BankAccountInfo> bankAccountInfos = new ArrayList<>();
        when(customCassandraRepository.findAll()).thenReturn(bankAccountInfos);
        BankAccountInfoRequest bankAccountInfoRequest = BankAccountInfoRequest.newBuilder()
                .setAccountType(BankAccount.AccountType.CREDIT.name())
                .build();
        StreamRecorder<BankAccountInfoResponse> responseObserver = StreamRecorder.create();
        bankAccountInfoService.getBankAccountInfo(bankAccountInfoRequest, responseObserver);
        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS)) {
            fail("The call did not terminate in time");
        }
        assertNull(responseObserver.getError());
        List<BankAccountInfoResponse> results = responseObserver.getValues();
        BankAccountInfoResponse actualResponse = results.get(0);
        log.info("Following object was received: {}", actualResponse.toString());
        assertNotNull(actualResponse);
        assertTrue(actualResponse.toString().isBlank());
    }
}