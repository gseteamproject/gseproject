package gseproject.infrastructure.serialization.basic;


import gseproject.grid.ServiceType;
import gseproject.infrastructure.serialization.IWriter;

import java.io.DataOutputStream;
import java.io.IOException;

public class ServiceTypeReader implements IWriter<ServiceType> {

    public void write(ServiceType object, DataOutputStream stream) throws IOException {
        stream.writeByte(object.);
    }
}
