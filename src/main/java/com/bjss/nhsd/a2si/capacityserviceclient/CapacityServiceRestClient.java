package com.bjss.nhsd.a2si.capacityserviceclient;

import com.bjss.nhsd.a2si.capacityinformation.domain.CapacityInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/*
  Capacity Service ReST client encapsulates the Rest template that makes the actual HTTP call to the capacity service
*/
@Profile({            "doswrapper-local-dos-stub-na-cpsc-rest-local",
          "doswrapper-local-dos-soap-local-wiremock-cpsc-rest-local",
                     "doswrapper-local-dos-soap-uat-cpsc-rest-local",
                    "doswrapper-local-dos-soap-prod-cpsc-rest-local",

                        "doswrapper-aws-dos-stub-na-cpsc-rest-aws",
                       "doswrapper-aws-dos-soap-uat-cpsc-rest-aws",
              "doswrapper-aws-dos-soap-aws-wiremock-cpsc-rest-aws",
                      "doswrapper-aws-dos-soap-prod-cpsc-rest-aws",
                               "test-capacity-service-rest-client"})
@Component
public class CapacityServiceRestClient implements CapacityServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(CapacityServiceRestClient.class);

    @Value("${capacity.service.client.api.url}")
    private String capacityServiceUrl;

    @Value("${capacity.service.client.api.username}")
    private String capacityServiceApiUsername;

    @Value("${capacity.service.client.api.password}")
    private String capacityServiceApiPassword;

    private RestTemplate capacityServiceClientRestTemplate;

    private static final String capacityServiceApiUsernameHttpHeaderName = "capacity-service-api-username";
    private static final String capacityServiceApiPasswordHttpHeaderName = "capacity-service-api-password";

    @Autowired
    public CapacityServiceRestClient(RestTemplate capacityServiceClientRestTemplate) {
        this.capacityServiceClientRestTemplate = capacityServiceClientRestTemplate;
    }

    private HttpHeaders createApiHeaders() {
        return new HttpHeaders() {{
            set(capacityServiceApiUsernameHttpHeaderName, capacityServiceApiUsername);
            set(capacityServiceApiPasswordHttpHeaderName, capacityServiceApiPassword);

        }};
    }

    @Override
    public CapacityInformation getCapacityInformation(String serviceId) {

        CapacityInformation capacityInformation;
        try {
            HttpEntity<?> httpEntity = new HttpEntity<>(createApiHeaders());

            ResponseEntity<CapacityInformation> responseEntity =
                    capacityServiceClientRestTemplate.exchange(
                            capacityServiceUrl + "/" + serviceId,
                            HttpMethod.GET,
                            httpEntity,
                            CapacityInformation.class);

            capacityInformation = responseEntity.getBody();

        } catch (Exception e) {
            logger.error("Unable to get response from Capacity Service for Service Id {}", serviceId, e);
            throw e;
        }

        return capacityInformation;
    }

    @Override
    public void saveCapacityInformation(CapacityInformation capacityInformation) {
        try {

            HttpEntity <CapacityInformation> httpEntity = new HttpEntity<>(capacityInformation, createApiHeaders());

            capacityServiceClientRestTemplate.exchange(
                    capacityServiceUrl,
                    HttpMethod.POST,
                    httpEntity,
                    CapacityInformation.class);

        } catch (Exception e) {
            logger.error("Unable to save Capacity Information {} in Capacity Service", capacityInformation, e);
            throw e;
        }
    }

    @Override
    public String toString() {
        return "CapacityServiceRestClient{" +
                "capacityServiceUrl='" + capacityServiceUrl + '\'' +
                ", capacityServiceClientRestTemplate=" + capacityServiceClientRestTemplate +
                '}';
    }
}
