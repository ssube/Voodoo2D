package com.raml.voodoo2d;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.raml.voodoo2d.engine.BaseEngine;

public class DesktopLoader {
    public static void main(String[] args)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Voodoo2D";
        config.useGL20 = true;
        config.width = 800;
        config.height = 600;

        new LwjglApplication(new BaseEngine(), config);
    }
}
