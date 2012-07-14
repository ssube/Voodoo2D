package com.raml.voodoo2d;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a single block type, with identifying info for engine use. Can be stored to Google Datastore through JPA.
 * @author Sean
 *
 */
@Entity
public class BlockType extends ObjectType
{    
    /**
     * Whether this object blocks lighting.
     */
    @JsonProperty
    boolean occluder;
    
    /**
     * Whether block sticks (no gravity when touching other blocks)
     */
    @JsonProperty
    boolean sticky;
}
