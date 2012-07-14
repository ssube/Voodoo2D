package com.raml.voodoo2d;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.appengine.api.datastore.Key;

public class KeySerializer extends JsonSerializer<Key>
{
    @Override
    public void serialize(Key arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException, JsonProcessingException
    {
        String serialKey = arg0.getKind() + ":" + arg0.getId();
        arg1.writeString(serialKey);
    }
}
