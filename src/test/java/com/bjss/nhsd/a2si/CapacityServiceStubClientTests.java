package com.bjss.nhsd.a2si;

import com.bjss.nhsd.a2si.capacityinformation.domain.CapacityInformation;
import com.bjss.nhsd.a2si.capacityserviceclient.CapacityServiceStubClient;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * When a class is annotated with @RunWith, JUnit will invoke the class in which is annotated so as to run the tests,
 * instead of using the runner built into JUnit.
 *
 * The @SpringBootTest annotation tells Spring Boot to go and look for a main configuration class
 * (one with @SpringBootApplication for instance), and use that to start a Spring application context
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("test-capacity-service-stub-client")
public class CapacityServiceStubClientTests {


    private static final String defaultServiceId = "defaultServiceId";
    private static final String defaultMessage = "Default Message";
    private static final LocalDateTime now = LocalDateTime.now();

    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private CapacityServiceStubClient capacityServiceStubClient;

    @Before
    public void prePopulate() {

        capacityServiceStubClient = new CapacityServiceStubClient();
        capacityServiceStubClient.init();

        CapacityInformation savedCapacityInformation = new CapacityInformation();
        savedCapacityInformation.setServiceId(defaultServiceId);
        savedCapacityInformation.setMessage(defaultMessage);
        savedCapacityInformation.setLastUpdated(dateTimeFormatter.format(now));

        capacityServiceStubClient.saveCapacityInformation(savedCapacityInformation);

    }

    @Test
    public void testPrePopulatedCapacityInformationCanBeRetrieved() {

        CapacityInformation capacityInformation = capacityServiceStubClient.getCapacityInformation(defaultServiceId);

        assertEquals(capacityInformation.getServiceId(), defaultServiceId);
        assertEquals(capacityInformation.getMessage(), defaultMessage);
        assertEquals(capacityInformation.getLastUpdated(), dateTimeFormatter.format(now));

    }

    @Test
    public void testSavedCapacityInformationCanBeRetrieved() {

        String newServiceId = "newServiceId";
        String newMessage = "New Message for newServiceId";

        CapacityInformation capacityInformationToSave = new CapacityInformation();
        capacityInformationToSave.setServiceId(newServiceId);
        capacityInformationToSave.setMessage(newMessage);
        capacityInformationToSave.setLastUpdated(dateTimeFormatter.format(now));

        capacityServiceStubClient.saveCapacityInformation(capacityInformationToSave);

        CapacityInformation capacityInformationThatIsSaved = capacityServiceStubClient.getCapacityInformation(newServiceId);

        assertEquals(capacityInformationThatIsSaved.getServiceId(), newServiceId);
        assertEquals(capacityInformationThatIsSaved.getMessage(), newMessage);
        assertEquals(capacityInformationThatIsSaved.getLastUpdated(), dateTimeFormatter.format(now));

    }

    @Test
    public void testGetNonExistentCapacityInformation() {

        String nonExistentServiceId = "nonExistentServiceId";


        CapacityInformation nonExistentCapacityInformation = capacityServiceStubClient.getCapacityInformation(nonExistentServiceId);

        assertNull(nonExistentCapacityInformation);

    }
}
