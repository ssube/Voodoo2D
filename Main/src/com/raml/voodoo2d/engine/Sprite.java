package com.raml.voodoo2d.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.raml.voodoo2d.common.JsonResource;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class Sprite extends JsonResource
{
    @JsonProperty
    Rectangle bounds;

    @JsonProperty
    ArrayList<Rectangle> frames;

    @JsonProperty
    boolean looping;

    @JsonProperty
    float interval;

    @JsonIgnore
    TextureRegion region;

    @JsonIgnore
    Animation animation;

    public Sprite()
    {
        
    }
    
    public Sprite(Sprite other)
    {
        if (other.bounds != null)
        {
            this.bounds = new Rectangle(other.bounds);
        }

        if (other.frames != null)
        {
            this.frames = new ArrayList<Rectangle>(other.frames.size());
            for (Rectangle frame: other.frames)
            {
                this.frames.add(new Rectangle(frame));
            }
        }
    }
    
    public Sprite(float x, float y, float width, float height)
    {
        this.bounds = new Rectangle(x, y, width, height);
    }

    public void cache(Texture texture)
    {
        if (bounds != null)
        {
            region = new TextureRegion(texture, (int)bounds.x, (int)bounds.y, (int)bounds.width, (int)bounds.height);
        }

        if (frames != null)
        {
            List<TextureRegion> frameRegions = new ArrayList<TextureRegion>(frames.size());
            for (Rectangle frameBounds: frames)
            {
                frameRegions.add(new TextureRegion(texture, (int)frameBounds.x, (int)frameBounds.y,
                        (int)frameBounds.width, (int)frameBounds.height));
            }
            int loop = (looping ? Animation.LOOP : Animation.NORMAL);
            animation = new Animation(interval, frameRegions, loop);
        }
    }

    public String toString()
    {
        return "Sprite(" + (this.bounds != null ? this.bounds : "animated") + ")";
    }

    @JsonIgnore
    public boolean isAnimated()
    {
        return (this.frames != null);
    }

    @JsonIgnore
    public int getFrameCount()
    {
        if (this.frames != null)
        {
            return this.frames.size();
        }
        else
        {
            return 0;
        }
    }

    public Rectangle getFrame(int index)
    {
        if (this.frames == null)
        {
            return this.bounds;
        }
        else if (index < 0 || index >= this.frames.size())
        {
            return null;
        }
        else
        {
            return this.frames.get(index);
        }
    }

    public TextureRegion getRegion(float time)
    {
        if (this.animation != null)
        {
            return animation.getKeyFrame(time);
        }
        else
        {
            return region;
        }
    }
}