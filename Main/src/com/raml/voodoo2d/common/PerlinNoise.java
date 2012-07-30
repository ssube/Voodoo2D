package com.raml.voodoo2d.common;

import java.util.Random;

public class PerlinNoise
{
    private final int width, height;
    private final float[][] bucket;
    
    public PerlinNoise(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.bucket = new float[width][height];

        Random gen = new Random();

        for (int x = 0; x < width; ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                this.bucket[x][y] = gen.nextFloat();
            }
        }
    }
    
    public float get(float x, float y)
    {
        int cellx = (int) Math.floor(x); int nextx = cellx + 1;
        int celly = (int) Math.floor(y); int nexty = celly + 1;
            cellx = cellx % this.width;      nextx = nextx % this.width;         
            celly = celly % this.height;     nexty = nexty % this.height;
            
        float offx = x - cellx;
        float offy = y - celly;
        
        float x0y0 = this.bucket[cellx][celly];
        float x0y1 = this.bucket[cellx][nexty];
        float x1y0 = this.bucket[nextx][celly];
        float x1y1 = this.bucket[nextx][nexty];
        float x0   = this.interpCos(x0y0, x0y1, offy);
        float x1   = this.interpCos(x1y0, x1y1, offy);
        float sum  = this.interpCos(x0, x1, offx);
        
        return sum;
    }

    public float get(float x, float y, int octaves)
    {
        float result = 0;
        
        for (int octave = 0; octave < octaves; ++octave)
        {
            float factor = (float) Math.pow(2, octave + 1);
            result += this.get(x * factor, y * factor) / factor;
        }
        
        return result;
    }

    private float interpCos(float a, float b, float x)
    {
        float ft = (float) (x * Math.PI);
        float f  = (float) ((1 - Math.cos(ft)) * 0.5);
        return     (a * (1 - f)) + (b * f);
    }
}
