package com.ecommerce.order.clients;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.http.HttpHeaders;
import java.util.Optional;

@Configuration
public class ProductServiceConfig {

    @Autowired(required = false)
    private Tracer tracer;

    @Autowired(required = false)
    private Propagator propagator;


    @Bean
    public ProductServiceClient productServiceClient(LoadBalancerClient loadBalancerClient) {
//        RestClient restClient = RestClient.builder()
//                .baseUrl("http://product-service")
//                .defaultStatusHandler(HttpStatusCode::is4xxClientError, ((request, response) -> Optional.empty()))
//                .requestInterceptor(new LoadBalancerInterceptor(loadBalancerClient))
//                .requestInterceptor(createTracingInterceptor())
//                .build();

        RestClient.Builder builder = RestClient.builder()
                .baseUrl("http://product-service")
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        (request, response) -> Optional.empty()
                )
                .requestInterceptor(
                        new LoadBalancerInterceptor(loadBalancerClient)
                );

        // Add tracing only if tracing dependencies exist
        if (tracer != null && propagator != null) {
            builder.requestInterceptor(
                    createTracingInterceptor()
            );
        }

        RestClient restClient = builder.build();

        RestClientAdapter adapter =
                RestClientAdapter.create(restClient);

        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory
                        .builderFor(adapter)
                        .build();

        return factory.createClient(
                ProductServiceClient.class
        );
    }

    private ClientHttpRequestInterceptor createTracingInterceptor() {

        return (request, body, execution) -> {

            if (tracer.currentSpan() != null) {

                HttpHeaders headers = request.getHeaders();

                propagator.inject(
                        tracer.currentSpan().context(),
                        headers,
                        HttpHeaders::add
                );
            }
            return execution.execute(
                    request,
                    body
            );
        };
    }
}
