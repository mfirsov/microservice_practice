package com.mfirsov.addressgenerate.client;

import com.mfirsov.addressgenerate.model.ResponseBody;
import com.mfirsov.common.model.Address;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Log4j2
@Component
@Profile("!dev")
public class SimpleAddressGeneratorClient implements AddressGeneratorClient {

    @Value("${address.generator.address}")
    private String addressGeneratorAddress;

    @Override
    public Mono<Address> getAddressFromAddressGenerator() {
        WebClient webClient = WebClient.create(addressGeneratorAddress);
        return webClient
                .get()
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ResponseBody.class)
                .doOnSuccess(responseBody -> log.debug("Following Response was received: {}", responseBody))
                .map(ResponseBody::getResults);
    }

}
