package com.mfirsov.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@UserDefinedType("address")
public class Address {

    private String street;

    private String city;

    private String state;

}
