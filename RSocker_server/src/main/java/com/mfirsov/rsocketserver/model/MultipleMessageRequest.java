package com.mfirsov.rsocketserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultipleMessageRequest {

    private String origin;
    private String interaction;
    private long index;
    private long created = Instant.now().getEpochSecond();

    public MultipleMessageRequest(String origin, String interaction) {
        this.origin = origin;
        this.interaction = interaction;
    }

    public MultipleMessageRequest(String origin, String interaction, long index) {
        this(origin, interaction);
        this.index = index;
    }
}
