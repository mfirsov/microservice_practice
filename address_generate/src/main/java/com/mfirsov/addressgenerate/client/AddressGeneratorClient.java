package com.mfirsov.addressgenerate.client;

import com.mfirsov.common.model.Address;
import reactor.core.publisher.Mono;

public interface AddressGeneratorClient {

    Mono<Address> getAddressFromAddressGenerator();

}
