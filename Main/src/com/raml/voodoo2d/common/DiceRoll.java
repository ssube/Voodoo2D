package com.raml.voodoo2d.common;

import java.util.Random;

public class DiceRoll {
    private static Random generator = new Random();

    public int count;
    public int sides;

    public DiceRoll()
    {

    }

    public DiceRoll(int count, int sides)
    {
        this.count = count;
        this.sides = sides;
    }

    public boolean check(int bonus, int opposing)
    {
        return (roll(bonus) > opposing);
    }

    public int roll(int bonus)
    {
        int roll = bonus;

        for (int i = 0; i < count; ++i)
        {
            roll += generator.nextInt(sides) + 1;
        }

        return roll;
    }
}
