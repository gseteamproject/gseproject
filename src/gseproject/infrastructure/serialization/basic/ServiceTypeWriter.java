package gseproject.infrastructure.serialization.basic;

import java.io.DataOutputStream;
import java.io.IOException;

import gseproject.infrastructure.contracts.ServiceTypeContract;
import gseproject.infrastructure.serialization.IWriter;

public class ServiceTypeWriter implements IWriter<ServiceTypeContract> {

    public void write(ServiceTypeContract object, DataOutputStream stream) throws IOException {

        stream.writeUTF(object.serviceType.name());

    }
}
