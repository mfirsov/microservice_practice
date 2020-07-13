package com.mfirsov.grpcclientservice.model;

import com.mfirsov.model.BankAccountInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankAccountInfosResponse {

    private List<BankAccountInfo> bankAccountInfos;

}
