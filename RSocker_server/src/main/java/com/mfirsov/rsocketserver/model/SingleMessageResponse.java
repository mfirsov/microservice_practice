package com.mfirsov.rsocketserver.model;

import com.mfirsov.model.BankAccountInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleMessageResponse {

    private String origin;
    private String interaction;
    private long index;
    private long created = Instant.now().getEpochSecond();
    private Mono<BankAccountInfo> bankAccountInfo;

    public SingleMessageResponse(String origin, String interaction, Mono<BankAccountInfo> bankAccountInfo) {
        this.origin = origin;
        this.interaction = interaction;
        this.bankAccountInfo = bankAccountInfo;
    }

    public SingleMessageResponse(String origin, String interaction, long index, Mono<BankAccountInfo> bankAccountInfo) {
        this(origin, interaction, bankAccountInfo);
        this.index = index;
    }
}
