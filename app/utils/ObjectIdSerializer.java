package utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class ObjectIdSerializer  extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator jsonGen,SerializerProvider provider) throws IOException {
        jsonGen.writeString(value.toString());
    }
}
