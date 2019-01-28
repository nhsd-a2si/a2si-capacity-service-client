package com.nhsd.a2si.capacityserviceclient;

import com.nhsd.a2si.capacityinformation.domain.CapacityInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The Capacity Service Stub client is a drop in replacement for the ReST based client.
 * It does not make outgong http calls and uses a small set of Capacity Information data defined in the "init" method
 * It is of very limited use but can be useful when testing client systems without having to rely on
 * other companents being up, running and accessible
 */
@Profile({            "doswrapper-local-dos-stub-na-cpsc-stub-na",
          "doswrapper-local-dos-soap-local-wiremock-cpsc-stub-na",
                     "doswrapper-local-dos-soap-uat-cpsc-stub-na",
                    "doswrapper-local-dos-soap-prod-cpsc-stub-na",

                        "doswrapper-aws-dos-stub-na-cpsc-stub-na",
                       "doswrapper-aws-dos-soap-uat-cpsc-stub-na",
              "doswrapper-aws-dos-soap-aws-wiremock-cpsc-stub-na"})
@Component
public class CapacityServiceStubClient implements CapacityServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(CapacityServiceStubClient.class);

    private Map<String, CapacityInformation> capacityInformationMap;

    @PostConstruct
    public void init() {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(CapacityInformation.STRING_DATE_FORMAT);

        capacityInformationMap = new HashMap<>();

        CapacityInformation capacityInformation;

        capacityInformation = new CapacityInformation();
        capacityInformation.setServiceId("1234567890");

        LocalDateTime localDateTime = LocalDateTime.now().minusHours(2);
        capacityInformation.setLastUpdated(dateTimeFormatter.format(localDateTime));

        capacityInformationMap.put(capacityInformation.getServiceId(), capacityInformation);

        capacityInformation = new CapacityInformation();
        capacityInformation.setServiceId("0987654321");
        localDateTime = LocalDateTime.now().minusHours(4);
        capacityInformation.setLastUpdated(dateTimeFormatter.format(localDateTime));

        capacityInformationMap.put(capacityInformation.getServiceId(), capacityInformation);

        capacityInformation = new CapacityInformation();
        capacityInformation.setServiceId("1323777632");

        localDateTime = LocalDateTime.now().minusHours(8);
        capacityInformation.setLastUpdated(dateTimeFormatter.format(localDateTime));

        capacityInformationMap.put(capacityInformation.getServiceId(), capacityInformation);

        capacityInformation = new CapacityInformation();
        capacityInformation.setServiceId("1323782502");

        localDateTime = LocalDateTime.now().minusHours(16);
        capacityInformation.setLastUpdated(dateTimeFormatter.format(localDateTime));

        capacityInformationMap.put(capacityInformation.getServiceId(), capacityInformation);

    }

    @Override
    public Map<String, String> getCapacityInformation(Set<String> serviceIds, Long logHeaderId) {

    	final Map<String, String> messages = new HashMap<>();
    	final List<CapacityInformation> cis = new ArrayList<>();
        
    	for(String serviceId : serviceIds)
    	{
    		cis.add(createStubCapacityInfo(serviceId));
    	}
    	
    	for(CapacityInformation ci: cis)
    	{
            messages.put(ci.getServiceId(), ci.getMessage());
        }

        return messages;

    }

    @Override
    public void saveCapacityInformation(CapacityInformation capacityInformation) {

        logger.debug("Saving Capacity Information {} using key {}", capacityInformation, capacityInformation.getServiceId());

        capacityInformationMap.put(capacityInformation.getServiceId(), capacityInformation);

        logger.debug("Saved Capacity Information {} using key {}", capacityInformation, capacityInformation.getServiceId());

    }

    @Override
    public String toString() {
        return "CapacityServiceStubClient{}";
    }
    
    /**
     * Returns dummy capacity information record with the service identifier passed in.
     * Note that waiting time returned is random.
     * 
     * @param serviceId
     * @return {@link CapacityInformation}
     */
    private CapacityInformation createStubCapacityInfo(String serviceId)
    {
    	CapacityInformation ci = new CapacityInformation();
    	ci.setServiceId(serviceId);
    	ci.setServiceName("Test Service " + serviceId);
        ci.setWaitingTimeMins(ThreadLocalRandom.current().nextInt(1, 100));
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	ci.setLastUpdated(format.format( new Date()));
    	
    	return ci;
    }
}
