package com.raml.voodoo2d.world;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.raml.voodoo2d.common.JsonResource;

import java.util.Map;
import java.util.UUID;

@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class ObjectType extends JsonResource
{    
    public static enum CoverSupport
    {
        None,
        Grass,
        LargePlants,
        Fungi,
        All
    }

    @JsonProperty
    private UUID key;
    @JsonProperty
    private String name;

    /**
     * Block rarity tier. Linked to spawn rate.
     */
    @JsonProperty
    int tier;
    
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
     * Speed multiplier to objects passing through.
     */
    @JsonProperty
    float speed;
    
    /**
     * Object resistances to effects.
     */
    @JsonProperty
    Map<EffectType, Float> resists;
    
    /**
     * Object state changes when touched by an effect. These have a lower priority than destruction shifts.
     */
    @JsonProperty
    Map<EffectType, UUID> shift_mod;

    /**
     * Object state changes when destroyed by an effect.
     */
    @JsonProperty
    Map<EffectType, UUID> shift_kill;

    public UUID getKey()
    {
        return key;
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

    public float getResist(EffectType effect)
    {
        return resists.get(effect);
    }

    /**
     * Get this block's shift for a particular effect.
     * @param   effect  The effect to check.
     * @param   kill    Whether this effect destroyed the block (controls whether to use the modified or destroyed shift).
     * @return  The UUID of the block's next state; this may be a block, creature, or item.
     */
    public UUID getShift(EffectType effect, boolean kill)
    {
        if (kill && shift_kill.containsKey(effect))
        {
            return shift_kill.get(effect);
        }
        else
        {
            return shift_mod.get(effect);
        }
    }
}
