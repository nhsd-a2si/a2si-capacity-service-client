package com.nhsd.a2si;

import com.nhsd.a2si.capacityinformation.domain.CapacityInformation;
import com.nhsd.a2si.capacityserviceclient.CapacityServiceClientConfiguration;
import com.nhsd.a2si.capacityserviceclient.CapacityServiceRestClient;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

/**
 * This class is an integration test, relying on a locally running Capacity Service so this test is annotated
 * with Ignore so it is not run as part of a build.
 *
 * When a class is annotated with @RunWith, JUnit will invoke the class in which is annotated so as to run the tests,
 * instead of using the runner built into JUnit.
 *
 * The @SpringBootTest annotation tells Spring Boot to go and look for a main configuration class
 * (one with @SpringBootApplication for instance), and use that to start a Spring application context
 *
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test-capacity-service-rest-client")
@ContextConfiguration(classes = {CapacityServiceRestClient.class, CapacityServiceClientConfiguration.class})
public class CapacityServiceRestClientTests {

    private static final String defaultServiceId = "defaultServiceId";
    private static final String defaultMessage = "Default Message";
    private static final LocalDateTime now = LocalDateTime.now();

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private CapacityServiceRestClient capacityServiceRestClient;

    @Before
    public void prePopulate() {

        CapacityInformation savedCapacityInformation = new CapacityInformation();
        savedCapacityInformation.setServiceId(defaultServiceId);
        savedCapacityInformation.setLastUpdated(dateTimeFormatter.format(now));

        capacityServiceRestClient.saveCapacityInformation(savedCapacityInformation);

    }

    @Test
    public void testPrePopulatedCapacityInformationCanBeRetrieved() {

        CapacityInformation capacityInformation = capacityServiceRestClient.getCapacityInformation(defaultServiceId);

        assertEquals(capacityInformation.getServiceId(), defaultServiceId);
        assertEquals(capacityInformation.getMessage(), defaultMessage);
        assertEquals(capacityInformation.getLastUpdated(), dateTimeFormatter.format(now));

    }
}
