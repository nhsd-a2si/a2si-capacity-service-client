package com.nhsd.a2si.capacityserviceclient;

import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CapacityServiceClientConfiguration {

    // The Rest Template is configured to use the components defined in the http client, which include
    // the pooling connection manager and the request configuration
    @Bean
    public RestTemplate restTemplate(CloseableHttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        return new RestTemplate(requestFactory);
    }

}
