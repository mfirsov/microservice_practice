package com.mfirsov.addressgenerate.client;

import com.mfirsov.model.Address;
import org.springframework.stereotype.Component;

@Component
public interface AddressGeneratorClient {

    Address getAddressFromAddressGenerator();

}
