package com.raml.voodoo2d.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.raml.voodoo2d.common.JsonResource;
import com.raml.voodoo2d.common.ResourceNotFoundException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class Tileset extends JsonResource {
    public static class Tile extends JsonResource {
        @JsonProperty
        private String sprite;

        @JsonProperty
        private UUID block;

        public Tile() {

        }

        public Tile(String sprite, UUID block) {
            this.sprite = sprite;
            this.block = block;
        }

        public String getSprite() {
            return sprite;
        }

        public UUID getBlock() {
            return block;
        }
    }

    @JsonProperty
    private UUID key;
    @JsonProperty
    private String name;
    @JsonProperty
    private HashMap<Byte, Tile> tiles;
    @JsonProperty("sheet")
    private String sheet_name;
    @JsonIgnore
    private SpriteSheet sheet;

    public static Tileset load(String name) throws IOException {
        FileHandle manifest = Gdx.files.internal(ResourceManager.tilesetPath + "/" + name + ".json");

        if (manifest == null) {
            throw new FileNotFoundException("Unable to find manifest for tileset " + name);
        }

        try {
            Tileset tileset = mapper.readValue(manifest.file(), Tileset.class);
            tileset.name = name;

            return tileset;
        } catch (JsonParseException exc) {
            throw new IOException("Error parsing JSON", exc);
        } catch (JsonMappingException exc) {
            throw new IOException("Error mapping JSON", exc);
        }
    }

    public Tileset() {
    }

    public String getName() {
        return name;
    }

    @JsonIgnore
    public SpriteSheet getSheet() {
        return sheet;
    }

    public Tile getTile(byte index) {
        return tiles.get(index);
    }

    public HashMap<Byte, Tile> getTiles() {
        return tiles;
    }

    public void cache() throws ResourceNotFoundException {
        sheet = ResourceManager.getInstance().getSpriteSheet(sheet_name);
        if (sheet == null) {
            throw new ResourceNotFoundException("Unable to find sprite sheet " + sheet_name);
        }
        sheet.cache();
    }
}
