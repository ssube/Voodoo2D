package com.raml.voodoo2d;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;

import com.fasterxml.jackson.annotation.JsonProperty;

@PersistenceCapable
@EmbeddedOnly
public class SpriteDefinition
{    
    @JsonProperty
    Long tileset;
    
    @JsonProperty
    int x;
    
    @JsonProperty
    int y;
    
    @JsonProperty
    int width;
    
    @JsonProperty
    int height;
    
    public SpriteDefinition()
    {
        
    }
    
    public SpriteDefinition(SpriteDefinition other)
    {
        this.tileset = other.tileset;
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
    }
    
    public SpriteDefinition(Long tileset, int x, int y, int width, int height)
    {
        this.tileset = tileset;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;        
    }
    
    public String toString()
    {
        return "Sprite(" + tileset + ", " + x + ", " + y + ", " + width + ", " + height + ")";
    }
}