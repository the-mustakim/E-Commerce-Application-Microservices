package com.demo.consumer.restclient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rest-client")
public class RestClientController {

    private final ProviderRestClient providerRestClient;

    @GetMapping("/instance")
    public String getInstance(){
//        RestClient restClient = RestClient.create();
//        String responce = restClient.get()
//                .uri("http://localhost:8081/instance-info")
//                .retrieve()
//                .body(String.class);
//        return responce;
        return providerRestClient.getInstanceInfor();
    }



}
