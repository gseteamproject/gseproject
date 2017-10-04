package gseproject.infrastructure.serialization;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SerializationController {

    public static final SerializationController Instance = new SerializationController();

    private static final Charset CHARSET = StandardCharsets.ISO_8859_1;

    private HashMap<Class<?>, CustomSerializers<?>> serializationDictionary;

    public SerializationController(){
        serializationDictionary = new HashMap<>();
    }

    public <T> void RegisterSerializator(Class<T> serializableClass, IWriter<T> writer, IReader<T> reader) {
        CustomSerializers<T> streams = new CustomSerializers<T>();
        streams.writer = writer;
        streams.reader = reader;

        /*if (serializationDictionary.containsKey(serializableClass))
            throw new Exception(serializableClass.toString() + " already in serialization dictionary!");*/

        serializationDictionary.put(serializableClass, streams);
    }


    //todo: fix exceptions handling
    public <T> T Deserialize(Class<T> deserializableClass, String data){
        //todo: throw exception if null
        CustomSerializers serializers = serializationDictionary.get(deserializableClass);
        if (serializers == null){
            return null;
        }
        IReader<T> reader = serializers.reader;

        T resultObject = null;

        try (ByteArrayInputStream bufferedStream = new ByteArrayInputStream(data.getBytes(CHARSET))){
            try (DataInputStream dataInputStream = new DataInputStream(bufferedStream)) {
                resultObject = reader.read(dataInputStream);
            }
            catch (IOException e) {
                //todo: log?
            }
        }
        catch (IOException b){
            //todo: log?
        }

        return resultObject;
    }

    public <T> String Serialize(T data){
        Class serializableClass = data.getClass();

        //todo: throw exception if null
        CustomSerializers serializers = serializationDictionary.get(serializableClass);
        if (serializers == null){
            System.out.println("the class you want to serialize is not in dictionary");
            return null;
        }
        IWriter<T> writer = serializers.writer;

        String resultString = null;
        try(ByteArrayOutputStream byteArray = new ByteArrayOutputStream()) {
            try (DataOutputStream dataOutputStream = new DataOutputStream(byteArray)) {
                writer.write(data, dataOutputStream);
                resultString = new String(byteArray.toByteArray(), CHARSET);
            }
            catch (IOException e) {
            }
        }
        catch (IOException e){
        }

        return resultString;
    }

    class CustomSerializers<T>{
        public IWriter<T> writer;
        public IReader<T> reader;
    }
}
