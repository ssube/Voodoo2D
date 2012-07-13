package com.raml.voodoo2d;

import com.fasterxml.jackson.annotation.*;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tileset
{
    @JsonProperty
    private int id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String fg_uri;
    @JsonProperty
    private String bg_uri;
    
    public Tileset()
    {
        this.id = 1;
        this.name = "test";
        this.fg_uri = "foreground.png";
        this.bg_uri = "background.png";
    }
    
    public Tileset(int id)
    {
        this.id = id;
        this.name = "test" + id;
        this.fg_uri = "foreground.png";
        this.bg_uri = "background.png";        
    }
}
