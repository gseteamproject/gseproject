package gseproject.infrastructure.serialization.basic;


import gseproject.core.ServiceType;
import gseproject.infrastructure.contracts.ServiceTypeContract;
import gseproject.infrastructure.serialization.IReader;

import java.io.DataInputStream;
import java.io.IOException;

public class ServiceTypeWriter implements IReader<ServiceTypeContract> {

    public ServiceTypeContract read(DataInputStream stream) throws IOException {
        ServiceTypeContract serviceType = new ServiceTypeContract();
        serviceType.serviceType = ServiceType.valueOf(stream.readUTF());
        return serviceType;
    }

}
