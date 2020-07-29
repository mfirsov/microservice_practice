package com.mfirsov.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@UserDefinedType(value = "address")
public class Address {

    @CassandraType(type = CassandraType.Name.TEXT)
    private String street;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String city;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String state;

}
