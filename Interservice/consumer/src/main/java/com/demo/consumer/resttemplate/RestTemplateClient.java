package com.demo.consumer.resttemplate;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateClient {

    //private static final String PROVIDER_URL = "http://localhost:8081";

    private static final String PROVIDER_URL = "http://PROVIDER";

    private final RestTemplate restTemplate;

    public RestTemplateClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String getInstanceInfo(){
        return restTemplate.getForObject(PROVIDER_URL+"/instance-info",String.class);
    }



}
