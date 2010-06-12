package com.td;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by IntelliJ IDEA.
 * User: melling
 * Date: Jun 1, 2010
 * Time: 9:01:00 PM
 */
public class GameGLSurfaceView extends GLSurfaceView {

    GameRenderer renderer;

    public GameGLSurfaceView(Context context) {
        super(context);

        renderer = new GameRenderer(context);
        setRenderer(renderer);
    }

}
