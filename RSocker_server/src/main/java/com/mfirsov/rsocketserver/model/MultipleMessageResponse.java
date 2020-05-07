package com.mfirsov.rsocketserver.model;

import com.mfirsov.model.BankAccountInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleMessageResponse {

    private String origin;
    private String interaction;
    private long index;
    private long created = Instant.now().getEpochSecond();
    private List<BankAccountInfo> bankAccountInfos;

    public MultipleMessageResponse(String origin, String interaction, List<BankAccountInfo> bankAccountInfos) {
        this.origin = origin;
        this.interaction = interaction;
        this.bankAccountInfos = bankAccountInfos;
    }

    public MultipleMessageResponse(String origin, String interaction, long index, List<BankAccountInfo> bankAccountInfos) {
        this(origin, interaction, bankAccountInfos);
        this.index = index;
    }

}
