package com.raml.voodoo2d;

import java.util.Random;
import com.fasterxml.jackson.annotation.*;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class World
{
    @JsonProperty
    private byte[] data;
    
    public World()
    {
        Random gen = new Random();
        data = new byte[64];
        for (int x = 0; x < 64; ++x)
        {
            //for (int y = 0; y < 64; ++y)
            //{
                data[x] = (byte) (gen.nextInt() % Byte.MAX_VALUE);
            //}
        }
    }
    
    byte[] getData() { return this.data; }
    void setData(byte[] data) { this.data = data; }
}
