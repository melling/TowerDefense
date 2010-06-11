package com.td;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by IntelliJ IDEA.
 * User: melling
 * Date: May 31, 2010
 * Time: 4:09:04 PM
 */

public class GameRenderer implements GLSurfaceView.Renderer {



    private Context context;
    private Square square;

    /*
    * @param context Our context
    */

    public GameRenderer(Context context) {
        this.context = context;

//        square = new Square(context);
        square = new Square();


    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0, 0, 0, 0);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);


    }

    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        gl.glTranslatef(0.0f, -1.2f, -2.0f);    //Move down 1.2 Unit And Into The Screen 2.0
        square.draw(gl, false, 10L);                                                //Draw the square
//        square.draw(gl);                                                //Draw the square
    }


    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // avoid division by zero
        if (height == 0) height = 1;
        // draw on the entire screen
        gl.glViewport(0, 0, width, height);
        // setup projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 1.0f, 100.0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);     //Select The Modelview Matrix

        gl.glLoadIdentity();

    }




}

