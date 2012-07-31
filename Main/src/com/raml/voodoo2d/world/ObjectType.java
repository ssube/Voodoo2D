package com.raml.voodoo2d.world;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.raml.voodoo2d.common.JsonResource;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

@JsonSerialize(include = JsonSerialize.Inclusion.ALWAYS)
public class ObjectType extends JsonResource {
    public static class CoverSupport {
        public static final int None = 0;
        public static final int Grass = 1;
        public static final int LargePlants = 2;
        public static final int Fungi = 4;
        public static final int Trees = 8;
        public static final int all = Grass | LargePlants | Fungi | Trees;
    }

    public static class CollisionType {
        public static final int Liquid = 0;
        public static final int Character = 1;
        public static final int Ghost = 2;
        public static final int size = 3;
    }

    public static class ShiftDefinition {
        public float chance;
        public UUID key;
    }

    public static class EffectDefinition {
        public int resist;
        public float soak;
        public ShiftDefinition[] damage;
        public ShiftDefinition[] kill;
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
    float[] speed;

    @JsonProperty
    Map<EffectType, EffectDefinition> shifts;

    public UUID getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getTier() {
        return tier;
    }

    public int getValue() {
        return value;
    }

    public int getHealth() {
        return health;
    }

    public float getSpeed(int type) {
        if (speed != null && type >= 0 && type < CollisionType.size) {
            return speed[type];
        } else {
            return 0f;
        }
    }

    public int getResist(EffectType effect) {
        return shifts.get(effect).resist;
    }

    /**
     * Get this block's shift for a particular effect.
     *
     * @param effect The effect to check.
     * @param kill   Whether this effect destroyed the block (controls whether to use the modified or destroyed shift).
     * @return The UUID of the block's next state; this may be a block, creature, or item.
     */
    public UUID getShift(EffectType effect, boolean kill) {
        EffectDefinition effectDefinition = shifts.get(effect);

        if (effectDefinition == null) {
            return null;
        }

        ShiftDefinition[] shift;

        if (kill) {
            shift = effectDefinition.kill;
        } else {
            shift = effectDefinition.damage;
        }

        if (shift == null || shift.length == 0) {
            return null;
        }

        Random gen = new Random();
        float value = gen.nextFloat();
        float threshold = 0f;
        for (ShiftDefinition def : shift) {
            threshold += def.chance;
            if (value < threshold) {
                return def.key;
            }
        }

        return null;
    }
}
