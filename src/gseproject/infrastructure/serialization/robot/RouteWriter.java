package gseproject.infrastructure.serialization.robot;

import gseproject.infrastructure.contracts.RouteContract;
import gseproject.infrastructure.serialization.IWriter;

import java.io.DataOutputStream;
import java.io.IOException;

public class RouteWriter implements IWriter<RouteContract> {

    public void write(RouteContract object, DataOutputStream stream) throws IOException {
        stream.writeInt(object.route.size());
        for (int i = 0; i < object.route.size(); ++i){
            stream.writeUTF(object.route.get(i).toString());
        }
    }
}
