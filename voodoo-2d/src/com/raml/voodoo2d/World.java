package com.raml.voodoo2d;

import java.util.Random;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;

import com.fasterxml.jackson.annotation.*;

@PersistenceCapable
public class World
{
    public enum WorldSize
    {
        Chunk(128,128),
        Demo(64,64),
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

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    @JsonProperty
    private Long id;
    
    @JsonProperty
    private String name;
    
    @JsonProperty
    private String environment;
    
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
        data = new byte[getWidth()][height];
        
        PerlinNoise gen = new PerlinNoise(width, height);
        
        for (int x = 0; x < getWidth(); ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                float noise = gen.get(x, y, 6);
                
                //data[x][y] = (byte) (gen.nextInt() % Byte.MAX_VALUE);
                data[x][y] = (byte) (noise * 255);
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

    public Long getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getEnvironment()
    {
        return environment;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
