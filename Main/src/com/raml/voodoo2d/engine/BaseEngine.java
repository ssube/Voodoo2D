package com.raml.voodoo2d.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.raml.voodoo2d.world.BlockType;
import com.raml.voodoo2d.world.World;

import java.io.IOException;
import java.util.UUID;

public class BaseEngine extends Game {
    private Texture player, texture;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Vector2 playerPos;
    private long frames, frameRound, lastFrameTime;
    private World world;

    private final int tile = 32;
    private final int source = 32;

    @Override
    public void create()
    {
        this.player = new Texture(Gdx.files.internal("actors/player.png"));
        this.playerPos = new Vector2(400, 300);
        this.texture = new Texture(Gdx.files.internal("sprites/blocks1.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);

        batch = new SpriteBatch();

        createWorld();

        lastFrameTime = System.nanoTime();
    }

    private void createWorld()
    {
        try
        {
            world = new World("test", "test_tiles", World.WorldSize.Demo);
            world.cache();
        } catch (IOException exc) {
            System.out.println("Error: " + exc.getMessage());
            Throwable exc2 = exc.getCause();
            if (exc2 != null)
            {
                System.out.println("Error (cause): " + exc2.getMessage());
            }
        }
    }

    public void resize(int width, int height)
    {
        camera.setToOrtho(false, width, height);
    }

    public void dispose()
    {

    }

    public void render()
    {
        long now = System.nanoTime();
        long delta = now - lastFrameTime;
        lastFrameTime = now;
        float nowSeconds = now / 1000000000f;

        calcPerf(delta);
        handleInput(delta);

        Gdx.gl.glClearColor(0, 0, 0.20f, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        byte[][] data = world.getData();
        for (int x = 0; x < data.length; ++x)
        {
            byte[] row = data[x];
            for (int y = 0; y < row.length; ++y)
            {
                byte cell = row[y];
                if (cell != 0)
                {
                    Sprite sprite = world.getCachedSprite(cell);
                    if (sprite != null)
                    {
                        //Rectangle rect = sprite.getFrame(0);
                        TextureRegion region = sprite.getRegion(nowSeconds);
                        //batch.draw(texture, x * tile, y * tile, (int)rect.x, (int)rect.y, (int)rect.width, (int)rect.height);
                        batch.draw(region, x * tile, y * tile);
                    }
                }
            }
        }

        batch.draw(player, playerPos.x, playerPos.y, tile, tile);

        batch.end();
    }

    public void calcPerf(long delta)
    {
        ++frames;
        frameRound += delta;
        if (frameRound > 1000000000)
        {
            float rate = frames / (frameRound / 1000000000f);
            System.out.println(String.format("FPS: %f (%d / %d)", rate, frames, frameRound));

            frames = 0;
            frameRound = 0;
        }
    }

    public void handleInput(long delta)
    {
        double mult = delta / 1000000000f;
        double distance = tile * 4;

        Vector2 newPos = new Vector2(playerPos);

        if (Gdx.input.isTouched())
        {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPos);
            newPos.set(touchPos.x, touchPos.y);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A))
        {
            newPos.x -= (distance * mult);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D))
        {
            newPos.x += (distance * mult);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S))
        {
            newPos.y -= (distance * mult);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            newPos.y += (distance * mult);
        }

        float speedMult = checkCollision(newPos);
        Vector2 diff = new Vector2(newPos).sub(playerPos);
        playerPos.add(diff.mul(speedMult));

        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
        {
            createWorld();
        }
    }

    public float checkCollision(Vector2 newPos)
    {
        byte[][] cells = world.getData();

        // Figure out what cell this corresponds to
        Vector2[] newCells = new Vector2[4];
        newCells[0] = new Vector2(newPos).mul(1.0f / tile);
        newCells[1] = new Vector2(newPos).add(4, tile - 4f).mul(1.0f / tile);
        newCells[2] = new Vector2(newPos).add(tile - 4f, 4).mul(1.0f / tile);
        newCells[3] = new Vector2(newPos).add(tile - 4f, tile - 4f).mul(1.0f / tile);

        // Find the average multiplier of the cells
        float mult = 1f;

        for (int i = 0; i < 4; ++i)
        {
            if (newCells[i].x < 0 || newCells[i].y < 0 || newCells[i].x >= world.getWidth() || newCells[i].y >= world.getHeight())
            {
                return 0f;
            }

            int cellX = (int)Math.floor(newCells[i].x);
            int cellY = (int)Math.floor(newCells[i].y);

            byte cell = cells[cellX][cellY];

            if (cell != 0)
            {
                UUID blockKey = world.getTileset().getTile(cell).getBlock();
                BlockType block = ResourceManager.getInstance().getBlockType(blockKey);
                float speed = block.getSpeed();
                if (speed < mult)
                {
                    mult = speed;
                }
            }
        }

        return mult;
    }

    public void pause()
    {

    }

    public void resume()
    {

    }
}
