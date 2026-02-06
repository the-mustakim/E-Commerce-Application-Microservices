package com.demo.consumer.restclient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ProviderRestClient  {

    private final RestClient restClient;

    public String getInstanceInfor(){
        return restClient
                .get()
                .uri("/instance-info")
                .retrieve()
                .body(String.class);

    }
}
