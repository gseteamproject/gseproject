package gseproject.infrastructure.serialization;


import java.io.DataOutputStream;
import java.io.IOException;

public interface IWriter<T> {

    void write(T object, DataOutputStream stream) throws IOException;

}
