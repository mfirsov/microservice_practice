package com.mfirsov.kafka_producer.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class JsonBodyHandler<W> implements HttpResponse.BodyHandler<W> {

    private final Class<W> wClass;

    public JsonBodyHandler(Class<W> wClass) {
        this.wClass = wClass;
    }

    @Override
    public HttpResponse.BodySubscriber<W> apply(HttpResponse.ResponseInfo responseInfo) {
        return asJSON(wClass);
    }

    private static <W> HttpResponse.BodySubscriber<W> asJSON(Class<W> targetType) {
        HttpResponse.BodySubscriber<String> upstream = HttpResponse.BodySubscribers.ofString(StandardCharsets.UTF_8);

        return HttpResponse.BodySubscribers.mapping(
                upstream,
                (String body) -> {
                    try {
                        ObjectMapper objectMapper = new ObjectMapper();
                        return objectMapper.readValue(body, targetType);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
        );

    }
}
