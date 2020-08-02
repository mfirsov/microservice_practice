package com.mfirsov.kafkaconsumer.entities;

import com.mfirsov.common.model.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.UserDefinedType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@UserDefinedType("address")
public class AddressEntity {
    @Column("street")
    private String street;
    @Column("city")
    private String city;
    @Column("state")
    private String state;

    public AddressEntity(Address address) {
        this.street = address.getStreet();
        this.city = address.getCity();
        this.state = address.getState();
    }
}
