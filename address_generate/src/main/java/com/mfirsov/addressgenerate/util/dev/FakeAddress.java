package com.mfirsov.addressgenerate.util.dev;

import com.github.javafaker.Faker;
import com.mfirsov.common.model.Address;

public class FakeAddress {

    public static Address getFakedAddress() {
        final Faker faker = new Faker();
        return Address.builder()
                .city(faker.address().city())
                .street(faker.address().streetAddress())
                .state(faker.address().state())
                .build();
    }

}
