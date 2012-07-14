package com.raml.voodoo2d;

import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.*;
import com.google.appengine.api.datastore.Key;

@Entity
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class World
{
    public enum WorldSize
    {
        Chunk(128,128),
        Demo(128,128),
        Small(2048, 1024),
        Medium(4096, 2048),
        Large(8192, 2048);
        
        private int width, height;
        
        private WorldSize(int width, int height)
        {
            this.width = width;
            this.height = height;
        }
        
        @JsonGetter
        public int width()
        {
            return this.width;
        }

        @JsonGetter
        public int height()
        {
            return this.height;
        }
    }

    @SuppressWarnings("unused")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Key key;
    
    @JsonProperty
    private String name;
    
    @JsonProperty
    private byte[][] data;
    
    @JsonProperty
    private int width;
    
    @JsonProperty
    private int height;
    
    public World(WorldSize size)
    {
        width = size.width();
        height = size.height();
        data = new byte[width][height];
        
        Random gen = new Random();
        
        for (int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                data[x][y] = (byte) (gen.nextInt() % Byte.MAX_VALUE);
            }
        }
    }
    
    byte[][] getData() 
    { 
        return this.data; 
    }
    
    void setData(byte[][] data) 
    { 
        this.data = data; 
    }
}
