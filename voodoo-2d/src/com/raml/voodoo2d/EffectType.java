package com.raml.voodoo2d;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EffectType
{
    None(0),       // No effect type - standard placeholder
    General(1),    // General damage - usually mechanics
    Blunt(2),      // Blunt - hammers, falling rocks
    Slash(3),      // Slash - swords, arrows
    Magic(4),      // Magic - general magic, arcane
    Fire(5),       // Fire - fires, lava, magic
    Ice(6),        // Ice - snow, hail, magic
    Poison(7),     // Poison - poisons, magic
    Shock(8);      // Shock - wires, magic
    
    private int value;
    
    private EffectType(int value)
    {
        this.value = value;
    }
    
    @JsonValue
    public int value()
    {
        return this.value;
    }
}
