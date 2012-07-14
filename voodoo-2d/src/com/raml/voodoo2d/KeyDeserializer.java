package com.raml.voodoo2d;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class KeyDeserializer extends JsonDeserializer<Key>
{    
    @Override
    public Key deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException, JsonProcessingException
    {
        String value = arg0.getText();
        String[] parts = value.split(":");   
        if (parts.length != 2)
        {
            throw new IOException("invalid key (expected 2 sections, found " + parts.length + ")");
        }
        long id = Long.parseLong(parts[1]);
        Key key = KeyFactory.createKey(parts[0], id);       
        
        return key;
    }
}

