package com.raml.voodoo2d.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.raml.voodoo2d.common.JsonResource;
import com.raml.voodoo2d.world.BlockType;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class TemplateSheet extends JsonResource {
    ArrayList<BlockType> blocks;
    //ArrayList<ItemType> items;
    // ArrayList<CharacterType> characters;

    public static TemplateSheet load(String name) throws IOException {
        FileHandle manifest = Gdx.files.internal(ResourceManager.templatePath + "/" + name + ".json");

        if (manifest == null) {
            throw new FileNotFoundException("Unable to find manifest for tileset " + name);
        }

        try {
            TemplateSheet sheet = mapper.readValue(manifest.file(), TemplateSheet.class);

            return sheet;
        } catch (JsonParseException exc) {
            throw new IOException("Error parsing JSON", exc);
        } catch (JsonMappingException exc) {
            throw new IOException("Error mapping JSON", exc);
        }
    }

    public TemplateSheet() {

    }

    public ArrayList<BlockType> getBlockTypes() {
        return blocks;
    }
}
