package com.td;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: melling
 * Date: May 31, 2010
 * Time: 4:09:04 PM
 */

public class GameRenderer0 implements GLSurfaceView.Renderer {

    private static final String TAG = "GameRenderer";
    private Context context;
    private Square square;

    private List<Square> enemyUnits;
    private List<Circle> circleUnits;
    private List<WayPoint> wayPoints;

    // Game Time vars
    private long gameTime;
    private long gameStartTime;
    private long normalizedGameTime;
    private Circle circle;

    /*
    * @param context Our context
    */

    public GameRenderer0(Context context) {
        this.context = context;

//        square = new Square(context);
//        square = new Square();
        initGameTime();

    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0, 0, 0, 0);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        
        circle = new Circle(0,0,0,0.5f,30);
        
/*
    	rp = new Circle(0, 0, 0, 0.5f, 30);
		//rp.prepareBuffers(0,0,0,0.5f,30);
    	//t = new RegularPolygon(x,y,z,radius,sides);
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
        circleUnits = new ArrayList<Circle>();
 * 
 */

        wayPoints = new ArrayList<WayPoint>();
        /*

                1,0.9,-1.35,0.0,0.05
        2,0.9,1.45,0.05,0.0
        3,-1.0,1.45,0.0,-0.05
        */
        enemyUnits = new ArrayList<Square>();

        WayPoint wp;
        float standardDelta = 5f;
        wp = new WayPoint(1, 20f, 20f, 0f, standardDelta);
        wayPoints.add(wp);

        wp = new WayPoint(2, 20f, 300f, -standardDelta, 0f);
        wayPoints.add(wp);

        wp = new WayPoint(3, 20.0f, 300.45f, 0.0f, -standardDelta);
        wayPoints.add(wp);

        wp = new WayPoint(4, 20f, -300.0f, standardDelta, -standardDelta);
        wayPoints.add(wp);

//        loadEnemyUnits();

    }

    public void onDrawFrame(GL10 gl) {

        long currentTime = System.currentTimeMillis();
        normalizedGameTime = (currentTime - gameStartTime) / 1000; // Time in seconds

        boolean move = false;
        if ((currentTime - gameTime) > 50) {
//            Log.i(TAG, "MOVEIT - GameTime: " + gameTime + ", " + (currentTime - gameTime));
            move = true;
            gameTime = currentTime;
        } else {
//            Log.i(TAG, "DONT MOVE - GameTime: " +  gameTime + ", " + (currentTime - gameTime));

        }


        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

//        gl.glTranslatef(0.0f, -1.2f, -2.0f);    //Move down 1.2 Unit And Into The Screen 2.0
//        square.draw(gl, move, normalizedGameTime);                                                //Draw the square
//        square.draw(gl);                                                //Draw the square
        /// Square
        int i = 0;
        for (Square e : enemyUnits) {
//            Log.i("onDraw", "" + i);
            i++;
            //square.draw(gl);
            e.draw(gl, move, normalizedGameTime);

        }
        // gl.glPopMatrix();
        
        for (Circle cir : circleUnits) {
            Log.i("onDrawCircle", "onDrawCircle" + i);
            i++;
            //square.draw(gl);
            cir.draw(gl, move, gameTime);

        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // avoid division by zero
        if (height == 0) height = 1;
        // draw on the entire screen
        gl.glViewport(0, 0, width, height);
        // setup projection matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glDisable(GL10.GL_CULL_FACE);

        gl.glLoadIdentity();

        Log.d("SpriteRenderer", width + "x" + height);
        GLU.gluOrtho2D(gl, 0, 480, 0, 320);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();


    }
//    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        // avoid division by zero
//        if (height == 0) height = 1;
//        // draw on the entire screen
//        gl.glViewport(0, 0, width, height);
//        // setup projection matrix
//        gl.glMatrixMode(GL10.GL_PROJECTION);
//        gl.glLoadIdentity();
//
//        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 1.0f, 100.0f);
//        gl.glMatrixMode(GL10.GL_MODELVIEW);     //Select The Modelview Matrix
//
//        gl.glLoadIdentity();
//
//    }

    public void initGameTime() {
//        this.context = context;
        gameTime = System.currentTimeMillis();
        Log.i(TAG, "GameStart: " + gameTime);
        gameStartTime = gameTime;
        normalizedGameTime = gameTime - gameStartTime;
    }

    void loadEnemyUnits() {
        String[] fields = new String[10];
        int nCol;
        float levelStartY = 10f;
        int i;
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

                        square.xc = 400f;
                        square.yc = levelStartY;
                        levelStartY += 15f;
                        square.angle = 75;
                        square.setWayPoints(wayPoints);
                        square.initOrigin();
                        enemyUnits.add(square);
                    } else if (fields[0].startsWith("Circle")) {
                    	
//                    	int startTime = Integer.parseInt(fields[i]);
//                         i++;
//                         String colors = fields[i];
//                         i++;
//                         String startAngle = fields[i];
//                         i++;
//                         String turnAngle = fields[i];
//                         i++;
//                         String[] rgbStr = colors.split(",");
                    	
                    	circle = new Circle(0,levelStartY,0,1,30);
                    	circle.setWayPoints(wayPoints);

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
}

