package com.raml.voodoo2d;

import javax.jdo.annotations.PersistenceCapable;

import com.fasterxml.jackson.annotation.JsonProperty;

@PersistenceCapable
public class Tileset extends StoredJsonClass
{    
    @JsonProperty
    private String environment;
    
    @JsonProperty
    private String name;
    
    @JsonProperty
    private String uri;
    
    public static final Tileset example = new Tileset("example", "example", "example.png");
    
    public Tileset()
    {
    }
    
    public Tileset(String environment, String name, String uri)
    {
        this.environment = environment;
        this.name = name;
        this.uri = uri;
    }

    public String getEnvironment()
    {
        return environment;
    }

    public String getName()
    {
        return name;
    }

    public String getUri()
    {
        return uri;
    }
    
    public void copy(Tileset other)
    {
        this.environment = other.environment;
        this.name = other.name;
        this.uri = other.uri;
    }
}
