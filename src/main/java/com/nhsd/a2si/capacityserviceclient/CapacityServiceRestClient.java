package com.nhsd.a2si.capacityserviceclient;

import com.nhsd.a2si.capacityinformation.domain.CapacityInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    private RestTemplate capacityServiceClientRestTemplate;

    private static final String capacityServiceApiUsernameHttpHeaderName = "capacity-service-api-username";
    private static final String capacityServiceApiPasswordHttpHeaderName = "capacity-service-api-password";

    public CapacityServiceRestClient(){}

    public CapacityServiceRestClient(RestTemplate capacityServiceClientRestTemplate) {
        this.capacityServiceClientRestTemplate = capacityServiceClientRestTemplate;
    }

    private HttpHeaders createApiHeaders() {
        return new HttpHeaders() {{
            set("Accept", "application/json");
            set("Content-Type", "application/json");

        }};
    }

    @Override
    public Map<String, String> getCapacityInformation(Set<String> serviceIds) {

        Map<String, String> messages = new HashMap<>();
        try {
            HttpHeaders apiHeaders = createApiHeaders();

            for(String id: serviceIds) {
                apiHeaders.add("serviceId", id);
            }

            HttpEntity<String> httpEntity = new HttpEntity<String>(apiHeaders);

            logger.debug("Calling {}", capacityServiceUrl + "/capacities");
            ResponseEntity<CapacityInformation[]> responseEntity =
                    capacityServiceClientRestTemplate.exchange(
                            capacityServiceUrl + "/capacities",
                            HttpMethod.GET,
                            httpEntity,
                            CapacityInformation[].class);
            logger.debug("Response received from {}", capacityServiceUrl + "/capacities");
            if(responseEntity.getBody() != null){
                for(CapacityInformation ci: responseEntity.getBody()){
                    messages.put(ci.getServiceId(), ci.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error("Unable to get response from Capacity Service for Service Id {}", serviceIds, e);
            throw e;
        }
        return messages;
    }

    @Override
    public void saveCapacityInformation(CapacityInformation capacityInformation) {
        try {

            HttpEntity <CapacityInformation> httpEntity = new HttpEntity<>(capacityInformation, createApiHeaders());

            capacityServiceClientRestTemplate.exchange(
                    capacityServiceUrl + "/capacity",
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
