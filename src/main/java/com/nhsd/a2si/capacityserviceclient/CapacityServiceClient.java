package com.nhsd.a2si.capacityserviceclient;

import com.nhsd.a2si.capacityinformation.domain.CapacityInformation;

public interface CapacityServiceClient {

    CapacityInformation getCapacityInformation(String serviceId);

    void saveCapacityInformation(CapacityInformation capacityInformation);
}
