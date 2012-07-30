package com.raml.voodoo2d.world;

public enum EffectType
{
    None(0),        // No effect type - standard placeholder
    General(1),     // General damage - usually mechanics
    Use(2),         // Use - click, interaction
    Blunt(3),       // Blunt - hammers, falling rocks
    Slash(4),       // Slash - swords, arrows
    Magic(5),       // Magic - general magic, arcane
    Fire(6),        // Fire - fires, lava, magic
    Ice(7),         // Ice - snow, hail, magic
    Poison(8),      // Poison - poisons, magic
    Shock(9),       // Shock - wires, magic
    Wind(10);       // Wind - wind, magic
    public static final int count = 11;
    
    private final int value;

    private EffectType(int value)
    {
        this.value = value;
    }
    
    public int getValue()
    {
        return this.value;
    }
} 