package com.nhsd.a2si.capacityserviceclient;

import com.nhsd.a2si.capacityinformation.domain.CapacityInformation;

import java.util.Map;
import java.util.Set;

public interface CapacityServiceClient {

    Map<String, String> getCapacityInformation(Set<String> serviceIds);

    void saveCapacityInformation(CapacityInformation capacityInformation);
}
