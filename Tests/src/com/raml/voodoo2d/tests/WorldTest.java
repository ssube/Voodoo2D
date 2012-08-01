package com.raml.voodoo2d.tests;

import com.raml.voodoo2d.world.World;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WorldTest {
    static final String worldName = "test";
    static final String worldTileset = "test";
    static final World.WorldSize worldSize = World.WorldSize.Demo;

    private World world;

    @Before
    public void setUp()
    {
        world = new World(worldName, worldTileset, worldSize);
    }

    @Test
    public void createWorld()
    {
        assertNotNull(world);
    }

    @Test
    public void getName()
    {
        String name = world.getName();
        assertTrue(name.equals(worldName));
    }
}
