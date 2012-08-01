package com.raml.voodoo2d.world;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.raml.voodoo2d.common.PerlinNoise;
import com.raml.voodoo2d.common.ResourceNotFoundException;
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

    public static final int indexByte = 0;
    public static final int subByte = 1;
    public static final int healthByte = 2;
    public static final int metaByte = 3;
    public static final int cellSize = 4;

    private UUID key;
    private String name;

    @JsonProperty("tileset")
    private String tileset_name;

    @JsonIgnore
    private Tileset tileset;

    private byte[][][] data;
    private final int width;
    private final int height;
    private int maxTier;
    private int airTier;

    @JsonIgnore
    private ArrayList<Byte> byteList;

    @JsonIgnore
    private Map<Byte, Sprite> spriteCache;

    public static World generate(String name, String tileset, WorldSize size)
            throws ResourceNotFoundException
    {
        World world = new World(name, tileset, size);
            world.cache();
        world.generateData();
        return world;
    }

    public World(String name, String tileset, WorldSize size)
    {
        this.name = name;
        this.tileset_name = tileset;

        this.width = size.width();
        this.height = size.height();
        this.data = new byte[width][height][cellSize];
        this.maxTier = 5;
        this.airTier = 3;
    }

    public void generateData()
    {
        PerlinNoise gen = new PerlinNoise(width, height);
        Random random = new Random();
        
        for (int x = 0; x < getWidth(); ++x)
        {
            for (int y = 0; y < height; ++y)
            {
                float noise = gen.get(x / 16.0f, y / 16.0f, 6);
                noise = (float)Math.round(noise * (byteList.size() - 1));

                random.nextBytes(data[x][y]);
                data[x][y][indexByte] = byteList.get((int)noise);
            }
        }
    }

    private ArrayList<Byte> buildTileList()
    {
        byteList = new ArrayList<Byte>();
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
    
    public byte[][][] getData()
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

    public void cache() throws ResourceNotFoundException
    {
        tileset = ResourceManager.getInstance().getTileset(tileset_name);

        if (tileset == null)
        {
            throw new ResourceNotFoundException("Unable to find tileset " +tileset_name);
        }

        tileset.cache();

        buildTileList();

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

    static Random gen = new Random();
    public byte getNewBlock(int x, int y)
    {
        int index = gen.nextInt();
        if (index < 0) { index = 0 - index; }
        index %= byteList.size();
        return byteList.get(index);
    }
}
