package gseproject.infrastructure.serialization.basic;

import gseproject.core.ServiceType;
import gseproject.infrastructure.contracts.ServiceTypeContract;
import gseproject.infrastructure.serialization.IWriter;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServiceTypeReader implements IWriter<ServiceTypeContract> {

    public void write(ServiceTypeContract object, DataOutputStream stream) throws IOException {

        stream.writeUTF(object.serviceType.name());

    }
}
