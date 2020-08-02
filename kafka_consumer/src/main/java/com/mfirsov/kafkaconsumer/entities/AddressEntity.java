package com.mfirsov.kafkaconsumer.entities;

import com.datastax.driver.core.DataType;
import com.mfirsov.common.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@UserDefinedType("address_type")
public class AddressEntity implements Serializable {
    //    @Column("street")
    @CassandraType(type = DataType.Name.TEXT)
    private String street;
    //    @Column("city")
    @CassandraType(type = DataType.Name.TEXT)
    private String city;
    //    @Column("state")
    @CassandraType(type = DataType.Name.TEXT)
    private String state;

    public AddressEntity(Address address) {
        this.street = address.getStreet();
        this.city = address.getCity();
        this.state = address.getState();
    }
}
