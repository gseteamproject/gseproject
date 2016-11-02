package gseproject.infrastructure.serialization;

import java.io.DataInputStream;
import java.io.IOException;

public interface IReader<T> {

    T read(DataInputStream stream) throws IOException;

}
