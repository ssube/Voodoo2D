package com.raml.voodoo2d;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.appengine.api.datastore.Key;

/**
 * Represents a single block type, with identifying info for engine use. Can be stored to Google Datastore through JDO.
 * 
 * @author Sean
 */
@PersistenceCapable
public class BlockType extends ObjectType
{         
    /**
     * Block id for world purposes (used to refer to this block in world data).
     * 
     * @note This is not unique throughout the database/all object types, just within a single series.
     *      When the client receives block data, it will typically reorder them to be indexed by this
     *      field. It is safe to assume that a single series is loaded within a world at a time.
     */
    @Persistent
    @JsonProperty
    short index;
    
    /**
     * Whether this object blocks lighting.
     */
    @Persistent
    @JsonProperty
    float occlusion;
    
    /**
     * Whether block sticks (no gravity when touching other blocks)
     */
    @Persistent
    @JsonProperty
    boolean sticky;
    
    public static final BlockType example = new BlockType("example", "example", 0, 0, 0, null, null, null, (short) 0, 0, true);
    
    public BlockType()
    {       
    }
    
    public BlockType(String environment, String name, int tier, int value, int health, float[] resists, Key[] shifts, SpriteDefinition sprite, short index, float occlusion, boolean sticky)
    {
        this.environment = environment;
        this.name = name;
        this.tier = tier;
        this.value = value;
        this.health = health;
        if (resists != null)
        {
            this.resists = resists.clone();
        }
        if (shifts != null)
        {
            this.shifts = shifts.clone();
        }
        if (sprite != null)
        {
            this.sprite = new SpriteDefinition(sprite);
        }
        this.index = index;
        this.occlusion = occlusion;
        this.sticky = sticky;
    }
    
    public short getIndex()
    {
        return index;
    }
    
    public float getOcclusion()
    {
        return occlusion;
    }
    
    public boolean getSticky()
    {
        return sticky;
    }
}
