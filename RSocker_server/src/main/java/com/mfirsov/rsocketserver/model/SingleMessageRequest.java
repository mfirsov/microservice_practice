package com.mfirsov.rsocketserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SingleMessageRequest {

    private String origin;
    private String interaction;
    private long index;
    private long created = Instant.now().getEpochSecond();
    private UUID uuid;

    public SingleMessageRequest(String origin, String interaction, UUID uuid) {
        this.origin = origin;
        this.interaction = interaction;
        this.uuid = uuid;
    }

    public SingleMessageRequest(String origin, String interaction, long index, UUID uuid) {
        this(origin, interaction, uuid);
        this.index = index;
    }

}
