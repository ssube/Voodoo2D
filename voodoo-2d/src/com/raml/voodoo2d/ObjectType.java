package com.raml.voodoo2d;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public class ObjectType
{    
    public static enum CoverSupport
    {
        None,
        Grass,
        LargePlants,
        Fungi,
        All;
    }
    
    public static final Map<String, Class> subtypes;
    
    static 
    {
        subtypes = new HashMap<String, Class>();
        subtypes.put("BlockType", BlockType.class);
    }
    
    /**
     * Datastore identifier.
     */
    @JsonProperty
    @JsonSerialize(using = KeySerializer.class)
    @JsonDeserialize(using = KeyDeserializer.class)
    @SuppressWarnings("unused")
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key id;
    
    /**
     * Set of objects this belongs to (eg "normal", "space"). Each world is bound to a particular environment.
     */
    @JsonProperty
    @Persistent
    String environment;
    
    /**
     * Object visible name, used in inventory and tooltips.
     */
    @JsonProperty
    @Persistent
    String name;
    
    /**
     * Block rarity tier. Controls the likelihood of spawning.
     */
    @JsonProperty
    @Persistent
    int tier;
    
    /**
     * Object value (sold from inventory, base value).
     */
    @JsonProperty
    @Persistent
    int value;
    
    /**
     * Object base health.
     */
    @JsonProperty
    @Persistent
    int health;
    
    /**
     * Speed multiplier to objects passing through.
     */
    @JsonProperty
    @Persistent
    float speed;
    
    /**
     * Object resistances to effects.
     */
    @JsonProperty
    @Persistent
    float[] resists;
    
    /**
     * Object state changes when destroyed by an effect.
     */
    @JsonProperty
    @Persistent
    Key[] shifts;
    
    /**
     * Sprite information: source tileset and image chunk.
     */
    @JsonProperty
    @Persistent
    @Embedded
    SpriteDefinition sprite;
    
    public Key getId()
    {
        return id;
    }
    
    public String getEnvironment()
    {
        return environment;
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getTier()
    {
        return tier;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public int getHealth()
    {
        return health;
    }
    
    public float getSpeed()
    {
        return speed;
    }
    
    public final float[] getResists()
    {
        return resists;
    }

    public final Key[] getShifts()
    {
        return shifts;
    }
    
    public final SpriteDefinition getSprite()
    {
        return sprite;
    }
    
    public void copy(ObjectType other)
    {
        if (other == null)
        {
            throw new InvalidParameterException("cannot update from null");
        }
        
        this.environment = other.environment;
        this.health = other.health;
        this.name = other.name;
        if (other.resists != null)
        {
            this.resists = other.resists.clone();
        }
        else
        {
            this.resists = null;
        }
        if (other.shifts != null)
        {
            this.shifts = other.shifts.clone();
        }
        else
        {
            this.shifts = null;
        }
        this.speed = other.speed;
        if (other.sprite != null)
        {
            this.sprite = new SpriteDefinition(other.sprite);
        }
        else
        {
            this.sprite = null;
        }
        this.tier = other.tier;
        this.value = other.value;
    }
}
