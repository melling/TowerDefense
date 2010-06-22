package com.balloon;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.KeyEvent;

/**
 * Created by IntelliJ IDEA.
 * User: melling
 * Date: Jun 1, 2010
 * Time: 9:01:00 PM
 */
public class GameGLSurfaceView extends GLSurfaceView {

    private static String TAG = "GameGLSurfaceView";
    BalloonGameRenderer renderer;

    public GameGLSurfaceView(Context context) {
        super(context);

        renderer = new BalloonGameRenderer(context);
        setRenderer(renderer);

        setClickable(true);
        setFocusable(true);
        setFocusableInTouchMode(true);
        requestFocus();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "Key Down: " + keyCode);
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_SPACE) {
            Log.i(TAG, "Pausing...");
//			renderer.setPlayerVelocity(0);
            return true;
            // left/q -> left
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_Q) {
            Log.i(TAG, "Move left");
            renderer.moveLeft();
            return true;
            // right/w -> right
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_W) {
            Log.i(TAG, "Move right");
            renderer.moveRight();
            return true;
            // up -> pause
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            renderer.shoot();

            return true;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            Log.i(TAG, "down.");
            return true;
        }
        return false;
    }

}
