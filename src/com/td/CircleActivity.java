package com.td;


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

public class CircleActivity extends Activity {

    private GLSurfaceView surface;
    private GLRenderer renderer;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        surface = new GLSurfaceView(this);
        renderer = new GLRenderer();
        surface.setRenderer(renderer);
        renderer.setContext(this);
        setContentView(surface);
//        setContentView(R.layout.main);
    }
}