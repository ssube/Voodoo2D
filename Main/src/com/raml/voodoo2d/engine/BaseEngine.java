package com.raml.voodoo2d.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.raml.voodoo2d.world.BlockType;
import com.raml.voodoo2d.world.ObjectType;
import com.raml.voodoo2d.world.World;
import sun.rmi.transport.ObjectTable;

import java.io.IOException;
import java.util.UUID;

public class BaseEngine extends Game {
    private Texture player, texture;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Vector2 playerPos;
    private long frames, frameRound, lastFrameTime;
    private World world;
    private float gametime;
    private Pixmap overlay;
    private Texture overlay_texture;

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
        overlay = new Pixmap(800, 600, Pixmap.Format.RGBA8888);
        texture = new Texture(800, 600, Pixmap.Format.RGBA8888);

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
        if (overlay != null)
        {
            overlay.dispose();
        }
        overlay = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        if (overlay_texture != null)
        {
            overlay_texture.dispose();
        }
        overlay_texture = new Texture(width, height, Pixmap.Format.RGBA8888);
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

        byte[][][] data = world.getData();
        for (int x = 0; x < data.length; ++x)
        {
            byte[][] row = data[x];
            for (int y = 0; y < row.length; ++y)
            {
                byte[] cell = row[y];
                if (cell[World.indexByte] != 0)
                {
                    Sprite sprite = world.getCachedSprite(cell[World.indexByte]);
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

        drawDebugMap();
        batch.draw(overlay_texture, 0, 0);

        batch.end();
    }

    public void drawDebugMap()
    {
        byte[][][] data = world.getData();
        for (int x = 0; x < data.length; ++x)
        {
            byte[][] row = data[x];
            for (int y = 0; y < row.length; ++y)
            {
                byte[] cell = row[y];
                byte index = cell[World.indexByte];
                overlay.setColor(index/2f, index/2f, index/2f, 1f);
                overlay.fillRectangle(x*2, y*2, 2, 2);
            }
        }
        overlay_texture.draw(overlay, 0, 0);
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
        gametime += mult;
        double distance = tile * 4;

        Vector2 newPos = new Vector2(playerPos);

        if (gametime > 0.25f)
        {
            if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE))
            {
                createWorld();
            }
            if (Gdx.input.isTouched())
            {
                Vector3 touchPos = new Vector3();
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);

                if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT))
                {
                    checkCreate(touchPos);
                }
                else
                {
                    checkDamage(touchPos);
                }
            }
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

    }

    public float checkCollision(Vector2 newPos)
    {
        byte[][][] cells = world.getData();

        // Figure out what cell this corresponds to
        Vector2[] newCells = new Vector2[2];
        newCells[0] = new Vector2(newPos).add(tile / 2f, 4).mul(1.0f / tile);
        newCells[1] = new Vector2(newPos).add(tile / 2f, tile - 4).mul(1.0f / tile);

        // Find the average multiplier of the cells
        float mult = 1f;

        for (int i = 0; i < 2; ++i)
        {
            if (newCells[i].x < 0 || newCells[i].y < 0 || newCells[i].x >= world.getWidth() || newCells[i].y >= world.getHeight())
            {
                return 0f;
            }

            int cellX = (int)Math.floor(newCells[i].x);
            int cellY = (int)Math.floor(newCells[i].y);

            byte cell = cells[cellX][cellY][World.indexByte];

            if (cell != 0)
            {
                UUID blockKey = world.getTileset().getTile(cell).getBlock();
                BlockType block = ResourceManager.getInstance().getBlockType(blockKey);
                float speed = block.getSpeed(ObjectType.CollisionType.Character);
                if (speed < mult)
                {
                    mult = speed;
                }
            }
        }

        return mult;
    }

    public void checkDamage(Vector3 pos)
    {
        Vector2 cellPos = new Vector2(pos.x, pos.y).mul(1f / tile);
        int cellX = (int)Math.floor(cellPos.x);
        int cellY = (int)Math.floor(cellPos.y);

        byte[][][] cells = world.getData();

        if (cellX < 0 || cellY < 0 || cellX >= world.getWidth() || cellY >= world.getHeight())
        {
            return;
        }

        byte[] cell = cells[cellX][cellY];
        byte health = cell[World.healthByte];
        health -= 10;
        if (health < 0)
        {
            for (int i = 0; i < World.cellSize; ++i)
            {
                cells[cellX][cellY][i] = 0;
            }
        }
        else
        {
            cells[cellX][cellY][World.healthByte] = health;
        }
    }

    public void checkCreate(Vector3 pos)
    {
        Vector2 cellPos = new Vector2(pos.x, pos.y).mul(1f / tile);
        int cellX = (int)Math.floor(cellPos.x);
        int cellY = (int)Math.floor(cellPos.y);

        byte[][][] cells = world.getData();

        if (cellX < 0 || cellY < 0 || cellX >= world.getWidth() || cellY >= world.getHeight())
        {
            return;
        }

        byte[] cell = cells[cellX][cellY];
        if (cell[World.indexByte] == 0)
        {
            for (int i = 0; i < World.cellSize; ++i)
            {
                cells[cellX][cellY][i] = 50;
            }
            cells[cellX][cellY][World.indexByte] = world.getNewBlock(cellX, cellY);
        }
    }

    public void pause()
    {

    }

    public void resume()
    {

    }
}
