package com.mfirsov.addressgenerate.client;

import com.mfirsov.addressgenerate.model.Address;
import com.mfirsov.addressgenerate.model.ResponseBody;
import com.mfirsov.addressgenerate.util.JsonBodyHandler;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Arrays;

@Log4j2
public class AddressGeneratorClientImpl implements AddressGeneratorClient {

    @Value("${address.generator.address}")
    private String addressGeneratorAddress;

    @Override
    public Address getAddressFromAddressGenerator() {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(addressGeneratorAddress))
                .GET()
                .setHeader("User-Agent", "Java 11 HttpClient")
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .build();
        try {
            ResponseBody response = httpClient.send(request, new JsonBodyHandler<>(ResponseBody.class)).body();
            log.info("Following Address was received from RandomAPI: " + response);
            return response.getResults();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return new Address();
        }
    }

}
