package com.example;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;

/**
 * Created by IntelliJ IDEA.
 * User: melling
 * Date: May 31, 2010
 * Time: 3:02:03 PM
 */
public class GameActivity extends Activity {

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        GLSurfaceView glSurface = new GameGLSurfaceView(this);
        setContentView(glSurface);
    }
}
