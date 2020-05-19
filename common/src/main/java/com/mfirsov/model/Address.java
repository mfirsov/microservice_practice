package com.mfirsov.model;

import com.datastax.driver.core.DataType;
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

    @CassandraType(type = DataType.Name.TEXT)
    private String street;

    @CassandraType(type = DataType.Name.TEXT)
    private String city;

    @CassandraType(type = DataType.Name.TEXT)
    private String state;

}
