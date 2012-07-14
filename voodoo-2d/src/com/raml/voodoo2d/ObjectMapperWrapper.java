package com.raml.voodoo2d;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperWrapper
{
    private static final ObjectMapper mapper = new ObjectMapper();
    
    public static ObjectMapper get()
    {
        return mapper;
    }
}
