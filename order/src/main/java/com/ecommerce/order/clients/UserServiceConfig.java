package com.ecommerce.order.clients;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Optional;

@Configuration
public class UserServiceConfig {


    @Autowired(required = false)
    private Tracer tracer;

    @Autowired(required = false)
    private Propagator propagator;

    @Bean
    public UserServiceClient userServiceClient(LoadBalancerClient loadBalancerClient) {
//        RestClient restClient = RestClient.builder()
//                .baseUrl("http://user-service")
//                .defaultStatusHandler(HttpStatusCode::is4xxClientError, ((request, response) -> Optional.empty()))
//                .requestInterceptor(new LoadBalancerInterceptor(loadBalancerClient))
//                .build();

        RestClient.Builder builder = RestClient.builder()
                .baseUrl("http://user-service")
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        (request, response) -> Optional.empty()
                )
                .requestInterceptor(
                        new LoadBalancerInterceptor(loadBalancerClient)
                );

        // optional tracing (same pattern as ProductServiceConfig)
        if (tracer != null && propagator != null) {
            builder.requestInterceptor(
                    createTracingInterceptor()
            );
        }

        RestClient restClient = builder.build();

        RestClientAdapter adapter =
                RestClientAdapter.create(restClient);

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(adapter)
                        .build();

        return factory.createClient(UserServiceClient.class);
    }

    private ClientHttpRequestInterceptor createTracingInterceptor() {

        return (request, body, execution) -> {

            if (tracer.currentSpan() != null) {

                propagator.inject(
                        tracer.currentSpan().context(),
                        request.getHeaders(),
                        HttpHeaders::add
                );
            }

            return execution.execute(request, body);
        };
    }

}
