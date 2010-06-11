package com.example;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by IntelliJ IDEA.
 * User: melling
 * Date: May 31, 2010
 * Time: 4:09:04 PM
 */

public class GLRenderer implements GLSurfaceView.Renderer {

    private final String TAG = "GLRenderer";

    private Path path;
    private List<WayPoint> wayPoints;
    private List<Square> enemyUnits;
    private float yc = -1.2f;
    private float xc = 0.9f;
    int i = 0;
    private Context context;
    private long gameTime;
    private long gameStartTime;
    private long normalizedGameTime;

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Square square;
        Context context;

        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0, 0, 0, 0);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        path = new Path();


        enemyUnits = new ArrayList<Square>();
        wayPoints = new ArrayList<WayPoint>();
        /*

                1,0.9,-1.35,0.0,0.05
        2,0.9,1.45,0.05,0.0
        3,-1.0,1.45,0.0,-0.05
        */
        WayPoint wp;
        float standardDelta = 0.05f;
        wp = new WayPoint(1, 0.9f, -1.35f, 0.00f, standardDelta);
        wayPoints.add(wp);

        wp = new WayPoint(2, 0.9f, 1.45f, -standardDelta, 0.0f);
        wayPoints.add(wp);

        wp = new WayPoint(3, -1.0f, 1.45f, 0.0f, -standardDelta);
        wayPoints.add(wp);

        wp = new WayPoint(4, 0.9f, -1.35f, standardDelta, -standardDelta);
        wayPoints.add(wp);

        loadEnemyUnits();


    }

    public void onDrawFrame(GL10 gl) {

        long currentTime = System.currentTimeMillis();
        normalizedGameTime = (currentTime - gameStartTime)/1000; // Time in seconds
        boolean move = false;
        if ((currentTime - gameTime) > 100) {
//            Log.i(TAG, "GameTime: " +  gameTime + ", " + (currentTime - gameTime));
            move = true;
            gameTime = currentTime;
        }

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        //Path
        gl.glPushMatrix();
        gl.glTranslatef(1.0f, 2.1f, -6.0f);    //Move down 1.2 Unit And Into The Screen 6.0
        gl.glScalef(0.5f, 0.5f, 1.0f);
        path.draw(gl);
        gl.glPopMatrix();

        /// Square
        int i = 0;
        for (Square e : enemyUnits) {
            Log.i("onDraw", "" + i);
            i++;
            //square.draw(gl);
            e.draw(gl, move, normalizedGameTime);

        }
        // gl.glPopMatrix();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Draw the square
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

    void loadEnemyUnits() {
        String[] fields = new String[10];
        int nCol;
        float levelStartY = -1.35f;

        try {

            InputStream is = context.getAssets().open("level1EnemyUnits.txt");

            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("#")) {  // Skip
                    Scanner lineScanner = new Scanner(line);
                    lineScanner.useDelimiter("\\|");
                    nCol = 0;
                    fields[nCol] = lineScanner.next();
                    String category = fields[0];

                    nCol++;
                    while (lineScanner.hasNext()) { // At this point we know we want the line

                        fields[nCol] = lineScanner.next();
//                    Log.i("=========== FOO", fields[nCol] + ",");
                        nCol++;
                    }

                    i = 1;
                    if (fields[0].startsWith("Square")) {
                        int startTime = Integer.parseInt(fields[i]);
                        i++;
                        String colors = fields[i];
                        i++;
                        String startAngle = fields[i];
                        i++;
                        String turnAngle = fields[i];
                        i++;
                        String[] rgbStr = colors.split(",");

                        float red = Float.parseFloat(rgbStr[0]);
                        float green = Float.parseFloat(rgbStr[1]);
                        float blue = Float.parseFloat(rgbStr[2]);

                        Square square = new Square();
                        square.redColor = red;
                        square.greenColor = green;
                        square.blueColor = blue;
                        square.startTime = startTime;

                        square.xc = 0.9f;
                        square.yc = levelStartY;
                        levelStartY -= 0.2f;
                        square.angle = 75;
                        square.setWayPoints(wayPoints);
                        square.initOrigin();
                        enemyUnits.add(square);
                    }
                }
            }

        }

        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void setContext(Context context) {
        this.context = context;
        gameTime = System.currentTimeMillis();
        Log.i(TAG, "GameStart: " + gameTime);
        gameStartTime = gameTime;
        normalizedGameTime = gameTime - gameStartTime;
    }
}