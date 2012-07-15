package com.raml.voodoo2d;

import com.fasterxml.jackson.annotation.*;

public class Engine
{
    @JsonProperty
    final String name = "Voodoo2D";
    
    @JsonProperty
    final int version = 3;
    
    @JsonProperty
    final String resourceRoot = "/resources";
    
    @JsonIgnore
    public static final Engine instance = new Engine();
    
    public Engine()
    {
        
    }
}
