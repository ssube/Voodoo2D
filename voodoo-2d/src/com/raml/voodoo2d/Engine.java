package com.raml.voodoo2d;

import com.fasterxml.jackson.annotation.*;

public class Engine
{
    @JsonProperty
    final static String name = "Voodoo2D";
    @JsonProperty
    final static int version = 1;
    
    @JsonIgnore
    private static Engine instance;
    
    public static Engine getInstance()
    {
        if (instance == null)
        {
            instance = new Engine();
        }
        
        return instance;
    }
    
    private Engine()
    {
        
    }
}
