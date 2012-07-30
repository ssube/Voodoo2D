package com.raml.voodoo2d.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.raml.voodoo2d.common.JsonResource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class SpriteSheet extends JsonResource
{
    @JsonIgnore
    private String name;
    @JsonIgnore
    private Texture texture;

    @JsonProperty("texture")
    private String texture_name;
    @JsonProperty
    private HashMap<String, Sprite> sprites;

    static SpriteSheet load(String name) throws IOException {
        FileHandle manifest = Gdx.files.internal(ResourceManager.spritePath + "/" + name + ".json");

        if (manifest == null)
        {
            throw new FileNotFoundException("Unable to find resource files for sprite sheet " + name);
        }

        // Read the manifest
        try
        {
            SpriteSheet sheet = mapper.readValue(manifest.file(), SpriteSheet.class);
            sheet.name = name;

            FileHandle texture = Gdx.files.internal(ResourceManager.spritePath + File.separator + sheet.texture_name);
            if (texture == null)
            {
                throw new FileNotFoundException("Unable to find image for sprite sheet " + name);
            }

            sheet.texture = new Texture(texture);

            return sheet;
        }
        catch (JsonParseException exc)
        {
            throw new IOException("Error parsing JSON" , exc);
        }
        catch (JsonMappingException exc)
        {
            throw new IOException("Error mapping JSON", exc);
        }
    }

    public SpriteSheet()
    {
        this.sprites = new HashMap<String, Sprite>();
        sprites.put("rock", new Sprite(32, 32, 32, 32));
    }

    public void addSprite(String key, Sprite sprite)
    {
        this.sprites.put(key, sprite);
    }

    public Sprite getSprite(String key)
    {
        return this.sprites.get(key);
    }

    public HashMap<String, Sprite> getSprites()
    {
        return this.sprites;
    }

    public String getName()
    {
        return this.name;
    }

    public Texture getTexture()
    {
        return this.texture;
    }

    /**
     * Builds the texture region and animation cache for all sprites contained in this sheet. Greatly improves
     * performance, but can't be done until the texture is created.
     */
    public void cache()
    {
        for (String key: sprites.keySet())
        {
            sprites.get(key).cache(texture);
        }
    }
}
