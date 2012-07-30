package com.raml.voodoo2d.world;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raml.voodoo2d.common.PerlinNoise;
import com.raml.voodoo2d.engine.ResourceManager;
import com.raml.voodoo2d.engine.Sprite;
import com.raml.voodoo2d.engine.SpriteSheet;
import com.raml.voodoo2d.engine.Tileset;

import java.io.IOException;
import java.util.*;

public class World
{
    public enum WorldSize
    {
        Chunk(128,128),
        Demo(64,64),
        Small(2048, 1024),
        Medium(4096, 2048),
        Large(8192, 2048);
        
        private final int width, height;
        
        private WorldSize(int width, int height)
        {
            this.width = width;
            this.height = height;
        }
        
        public int width()
        {
            return this.width;
        }

        public int height()
        {
            return this.height;
        }
    }

    private UUID key;
    private String name;

    @JsonProperty("tileset")
    private String tileset_name;

    @JsonIgnore
    private Tileset tileset;

    private byte[][] data;
    private final int width;
    private final int height;
    private int maxTier;
    private int airTier;

    @JsonIgnore
    private Map<Byte, Sprite> spriteCache;

    public World(String name, String tileset, WorldSize size) throws IOException
    {
        ResourceManager resourceManager = ResourceManager.getInstance();

        this.name = name;
        this.tileset_name = tileset;
        this.tileset = resourceManager.getTileset(tileset_name);

        this.width = size.width();
        this.height = size.height();
        this.data = new byte[getWidth()][height];
        this.maxTier = 5;
        this.airTier = 3;

        List<Byte> byteList = buildTileList();
        PerlinNoise gen = new PerlinNoise(width, height);
        
        for (int x = 0; x < getWidth(); ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                float noise = gen.get(x / 16.0f, y / 16.0f, 6);
                noise = (float)Math.round(noise * (byteList.size() - 1));

                this.data[x][y] = byteList.get((int)noise);
            }
        }
    }

    private ArrayList<Byte> buildTileList()
    {
        ArrayList<Byte> byteList = new ArrayList<Byte>();
        ResourceManager manager = ResourceManager.getInstance();

        for (int r = 0; r < airTier; ++r)
        {
            byteList.add((byte)0);
        }

        Map<Byte, Tileset.Tile> tiles = tileset.getTiles();
        for (Byte key: tiles.keySet())
        {
            Tileset.Tile tile = tiles.get(key);
            if (tile != null)
            {
                BlockType type = manager.getBlockType(tile.getBlock());
                if (type != null)
                {
                    for (int r = 0; r < type.getTier() && r < maxTier; ++r)
                    {
                        byteList.add(key);
                    }
                }
            }
        }

        return byteList;
    }

    public UUID getKey()
    {
        return key;
    }

    public String getName()
    {
        return name;
    }
    
    public byte[][] getData()
    { 
        return data;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public Tileset getTileset()
    {
        return tileset;
    }

    public void cache()
    {
        tileset.cache();

        spriteCache = new HashMap<Byte, Sprite>(255);
        HashMap<Byte, Tileset.Tile> tiles = tileset.getTiles();
        SpriteSheet sheet = tileset.getSheet();

        for (Byte key: tiles.keySet())
        {
            Tileset.Tile tile = tiles.get(key);
            if (tile != null)
            {
                Sprite sprite = sheet.getSprite(tile.getSprite());
                spriteCache.put(key, sprite);
            }
        }
    }

    public Sprite getCachedSprite(byte key)
    {
        return spriteCache.get(key);
    }
}
