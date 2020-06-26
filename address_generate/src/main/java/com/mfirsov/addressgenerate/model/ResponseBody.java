package com.mfirsov.addressgenerate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mfirsov.model.Address;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBody {

    private Address results;

}
