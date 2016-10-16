package gseproject.infrastructure.serialization;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.UUID;

public class SerializationController {

    private HashMap<UUID, Streams<?>> serializationDictionary;

    public SerializationController(){
        serializationDictionary = new HashMap<UUID, Streams>();
    }

    public static <T> void RegisterSerializator(UUID id, IWriter writer, IReader reader){

    }

    public static <T> T Deserialize(UUID id, String data){

    }

    public static <T> String Serialize(UUID id, T data){

    }

    class Streams<T>{
        public IWriter<T> writer;
        public IReader<T> reader;
    }
}
