package com.raml.voodoo2d.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.raml.voodoo2d.common.ResourceNotFoundException;
import com.raml.voodoo2d.world.BlockType;
import org.apache.commons.io.FilenameUtils;

public class ResourceManager {
    public class ResourceFilter implements FileFilter {
        public boolean accept(File file) {
            return (FilenameUtils.getExtension(file.getName()).equals("json"));
        }
    }

    public static final String templatePath = "templates";
    public static final String spritePath = "sprites";
    public static final String tilesetPath = "tilesets";

    private static ResourceManager instance;

    public static ResourceManager getInstance() {
        if (instance == null) {
            instance = new ResourceManager();
            instance.reload();
        }

        return instance;
    }

    FileHandle spriteHandle;
    FileHandle tilesetHandle;
    FileHandle templateHandle;
    File spriteDir;
    File tilesetDir;
    File templateDir;

    HashMap<String, SpriteSheet> spritesheets;
    HashMap<String, Tileset> tilesets;
    HashMap<String, TemplateSheet> templatesheets;
    HashMap<UUID, BlockType> blockTypes;
    //HashMap<UUID, ItemType> itemTypes;
    //HashMap<UUID, CharacterType> characterTypes;

    private ResourceManager() {
        spritesheets = new HashMap<String, SpriteSheet>();
        tilesets = new HashMap<String, Tileset>();
        templatesheets = new HashMap<String, TemplateSheet>();
        blockTypes = new HashMap<UUID, BlockType>();
    }

    public void reload() {
        spritesheets.clear();
        tilesets.clear();
        templatesheets.clear();

        blockTypes.clear();

        if (Gdx.files == null) {
            return;
        }

        ResourceFilter filter = new ResourceFilter();

        spriteHandle = Gdx.files.internal(spritePath);
        if (spriteHandle != null) {
            spriteDir = spriteHandle.file();

            for (File spriteFile : spriteDir.listFiles(filter)) {
                String name = FilenameUtils.getBaseName(spriteFile.getName());
                try {
                    SpriteSheet sheet = SpriteSheet.load(name);
                    spritesheets.put(name, sheet);
                } catch (IOException exc) {
                    System.err.println("Error loading sprite sheet '" + name + "': " + exc.getMessage());
                }
            }
        }

        tilesetHandle = Gdx.files.internal(tilesetPath);
        if (tilesetHandle != null) {
            tilesetDir = tilesetHandle.file();

            for (File tilesetFile : tilesetDir.listFiles(filter)) {
                String name = FilenameUtils.getBaseName(tilesetFile.getName());
                try {
                    Tileset tileset = Tileset.load(name);
                    tilesets.put(name, tileset);
                } catch (IOException exc) {
                    System.err.println("Error loading tileset '" + name + "': " + exc.getMessage());
                }
            }
        }

        templateHandle = Gdx.files.internal(templatePath);
        if (templateHandle != null) {
            templateDir = templateHandle.file();

            for (File templateFile : templateDir.listFiles(filter)) {
                String name = FilenameUtils.getBaseName(templateFile.getName());
                try {
                    TemplateSheet templateSheet = TemplateSheet.load(name);
                    templatesheets.put(name, templateSheet);
                } catch (IOException exc) {
                    System.err.println("Error loading template sheet '" + name + "': " + exc.getMessage());
                }
            }

            for (String key : templatesheets.keySet()) {
                TemplateSheet sheet = templatesheets.get(key);
                List<BlockType> blocks = sheet.getBlockTypes();
                for (BlockType type : blocks) {
                    blockTypes.put(type.getKey(), type);
                }
            }
        }
    }

    public SpriteSheet getSpriteSheet(String name) {
        if (spritesheets == null) {
            return null;
        }

        return spritesheets.get(name);
    }

    public Sprite getSprite(String sheetName, String name) {
        SpriteSheet sheet = getSpriteSheet(sheetName);

        if (sheet != null) {
            return sheet.getSprite(name);
        }

        return null;
    }

    public Tileset getTileset(String name) {
        if (tilesets != null) {
            return tilesets.get(name);
        }

        return null;
    }

    public BlockType getBlockType(UUID key) {
        if (blockTypes != null) {
            return blockTypes.get(key);
        }

        return null;
    }
}
