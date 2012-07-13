package com.raml.voodoo2d;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockType
{
    public class StatePair
    {
        public float resist = 1.0f;
        public short shift = -1;
    }
    
    @JsonProperty
    short id;
    @JsonProperty
    int tier;
    @JsonProperty
    String name;
    @JsonProperty
    int value;
    @JsonProperty
    boolean occluder;
    @JsonProperty
    int health;
    @JsonProperty
    float strength;
    @JsonProperty
    float speed;
    @JsonProperty
    boolean sticky;
    @JsonProperty
    Map<EffectType, StatePair> states;
}
