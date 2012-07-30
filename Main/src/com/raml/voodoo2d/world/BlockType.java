package com.raml.voodoo2d.world;

import com.badlogic.gdx.math.Vector3;

/**
 * Represents a single block type, with identifying info for engine use.
 */
public class BlockType extends ObjectType
{

    /**
     * How much light is blocked by this object, per-channel. (1,1,1) is full occlusion (no light).
     */
    private Vector3 occlusion;

    /**
     * Indicates whether liquids can flow through this block.
     */
    private boolean permeable;
    
    /**
     * Whether block sticks (no gravity when touching other blocks)
     */
    private boolean sticky;
    
    public BlockType()
    {       
    }
    
    public Vector3 getOcclusion()
    {
        return occlusion;
    }

    public boolean getPermeable()
    {
        return permeable;
    }
    
    public boolean getSticky()
    {
        return sticky;
    }
}
