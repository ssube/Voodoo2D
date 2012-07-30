package com.raml.voodoo2d;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.raml.voodoo2d.engine.BaseEngine;

public class AndroidLoader extends AndroidApplication
{
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL20 = false;
        config.useAccelerometer = false;
        config.useCompass = false;

        initialize(new BaseEngine(), config);
    }
}

