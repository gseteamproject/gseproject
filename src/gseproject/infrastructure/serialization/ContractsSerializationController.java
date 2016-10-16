package gseproject.infrastructure.serialization;

import gseproject.infrastructure.contracts.IContract;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

public class ContractsSerializationController {

    public static final ContractsSerializationController Instance = new ContractsSerializationController();

    private static final Charset charset = StandardCharsets.UTF_8;

    private HashMap<UUID, StreamsTuple> serializationDictionary;

    public ContractsSerializationController(){
        serializationDictionary = new HashMap<>();
    }

    public <T extends IContract> void RegisterSerializator(UUID id, IWriter writer, IReader reader){
        StreamsTuple<T> streams = new StreamsTuple();
        streams.writer = writer;
        streams.reader = reader;

        serializationDictionary.put(id, streams);
    }


    //todo: fix exceptions handling
    public <T extends IContract> T Deserialize(UUID id, String data){
        StreamsTuple streamsTuple = serializationDictionary.get(id);
        IReader<T> reader = streamsTuple.reader;

        T resultObject = null;
        try(DataInputStream dataInputStream = ConvertStringToStream(data)){
            resultObject = reader.read(dataInputStream);
        }
        catch (IOException e){
            //todo: log?
        }

        return resultObject;
    }

    public <T extends IContract> String Serialize(T data){
        UUID id = data.getId();
        StreamsTuple streamsTuple = serializationDictionary.get(id);
        IWriter<T> writer = streamsTuple.writer;

        String resultString = null;
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try(DataOutputStream dataOutputStream = new DataOutputStream(byteArray)){
            writer.write(data, dataOutputStream);
            resultString = byteArray.toString();
        }
        catch (IOException e){
        }

        return resultString;
    }

    private static DataInputStream ConvertStringToStream(String str){
        DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(str.getBytes(charset)));
        return dataInputStream;
    }

    class StreamsTuple<T>{
        public IWriter<T> writer;
        public IReader<T> reader;
    }
}
