package com.raml.voodoo2d;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class ObjectType
{
    public class StatePair
    {
        public float resist = 1.0f;
        public short shift = -1;
    }
    
    public enum CoverSupport
    {
        None,
        Grass,
        LargePlants,
        Fungi,
        All;
    }
    
    /**
     * Datastore identifier.
     */
    @SuppressWarnings("unused")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty
    private Key key;
    
    /**
     * Set of blocks this belongs to (eg "normal", "space").
     */
    @JsonProperty
    String series;
    
    /**
     * Block id for world purposes (used to refer to this block in world data).
     */
    @JsonProperty
    byte id;
    
    /**
     * Block rarity tier.
     */
    @JsonProperty
    int tier;
    
    /**
     * Object visible name.
     */
    @JsonProperty
    String name;
    
    /**
     * Object value (sold from inventory, base value).
     */
    @JsonProperty
    int value;
    
    /**
     * Object base health.
     */
    @JsonProperty
    int health;
    
    /**
     * Object resistances to effects and state transition when destroyed in that fashion.
     */
    @JsonProperty
    Map<EffectType, StatePair> states;
    
    /**
     * Speed multiplier to objects passing through.
     */
    @JsonProperty
    float speed;
}
