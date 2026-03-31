package com.demo.consumer.webclient;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean("WebClientConfigBuilder")
    @LoadBalanced
    public WebClient.Builder loadBalanceWebClient(){
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(@Qualifier("WebClientConfigBuilder") WebClient.Builder builder){
        return builder.baseUrl("http://provider").build();
    }

}
