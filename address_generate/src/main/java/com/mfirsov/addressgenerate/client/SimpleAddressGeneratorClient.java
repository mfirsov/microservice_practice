package com.mfirsov.addressgenerate.client;

import com.mfirsov.addressgenerate.model.ResponseBody;
import com.mfirsov.model.Address;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Log4j2
@Component
public class SimpleAddressGeneratorClient implements AddressGeneratorClient {

    @Value("${address.generator.address}")
    private String addressGeneratorAddress;

    @Override
    public Address getAddressFromAddressGenerator() {
        RestTemplate restTemplate = new RestTemplate();
        return Objects.requireNonNull(restTemplate.getForObject(addressGeneratorAddress, ResponseBody.class)).getResults();
    }

}
