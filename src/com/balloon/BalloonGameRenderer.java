package com.balloon;

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

public class BalloonGameRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "GameRenderer";
    private Context context;
    private Player player;

    private List<Balloon> balloons;
    private List<Missile> missiles;
//
    private List<WayPoint> wayPoints;
    private long lastShotTime = 0;
    // Game Time vars
    private long gameTime;
    private long gameStartTime;
    private long normalizedGameTime;
    static int missileCount = 0;
//    private Circle circle;
//    Path2 path;
//    private Square2 square2;
//    private Square2 square3;
//    private Rect1 rect1;
//    Square3 backgroundSquare;


    /*
    * @param context - Our app context
    */

    public BalloonGameRenderer(Context context) {
        this.context = context;

//        square = new Square(context);
//        square = new Square();
        initGameTime();

    }

    private void newMissile(int xc, int yc) {
        Missile missile = new Missile();
        missile.xc = xc;
        missile.yc = yc;

        missiles.add(missile);

    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearColor(0, 0, 0, 0);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

        wayPoints = new ArrayList<WayPoint>();
        balloons = new ArrayList<Balloon>();
        missiles = new ArrayList<Missile>();

        loadBalloonWayPoints("balloonLevel1WayPoints.txt"); // Must load wayPoints first
        loadBalloons("level1Balloons.txt");

        player = new Player();
        player.redColor = 0f;
        player.greenColor = 1f;
        player.blueColor = 0f;

        player.xc = 100;
        player.yc = 50;

//        newMissile(120, 70);

    }


    public void moveLeft() {
        player.xc -= 10;
        if (player.xc < 0) {
            player.xc = 0;
        }
    }

    public void moveRight() {
        player.xc += 10;
        if (player.xc > 480) {
            player.xc = 480;
        }
    }

    public void shoot() {
        newMissile(player.xc, 70);
//            Log.i(TAG, "Shooting");
    }

    public void onDrawFrame(GL10 gl) {

        float _red = 0.5f;
        float _green = 0.5f;
        float _blue = 1f;

        long currentTime = System.currentTimeMillis();
        normalizedGameTime = (currentTime - gameStartTime) / 1000; // Time in seconds

//        if ((normalizedGameTime % 2 == 0) && normalizedGameTime > lastShotTime && missileCount < 15) {
//            missileCount++;
//            lastShotTime = normalizedGameTime;
//        }

        boolean move = false;
        if ((currentTime - gameTime) > 50) {
//            Log.i(TAG, "MOVE IT - GameTime: " + gameTime + ", " + (currentTime - gameTime));
            move = true;
            gameTime = currentTime;
        } else {
//            Log.i(TAG, "DON'T MOVE - GameTime: " +  gameTime + ", " + (currentTime - gameTime));

        }

        gl.glClearColor(_red, _green, _blue, 1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

//        backgroundSquare.draw(gl,true,gameTime);
        player.draw(gl, true, gameTime);

        // Collisions
//        Log.i(TAG, "== Go Boom? ==");
        for (Missile missile : missiles) {
            int[] missileRectangle = missile.getRect();
            if (missileRectangle[0] > 600) {
                for (Balloon balloon : balloons) {
                    boolean isCollision = balloon.isCollision(missileRectangle);
                    if (isCollision) {
                        Log.i(TAG, "COLLISION");
                        balloon.setShot();
                        missile.exploded();
                    } else {
//                    Log.i(TAG, "NO Collision: " + missileRectangle[0] + "," + missileRectangle[1] + missileRectangle[2] + "," + missileRectangle[3]);

                    }

                }
            }
        }

        // Missiles

        for (Missile missile : missiles) {
            missile.draw(gl, move);

        }

        // Balloons
        for (Balloon balloon : balloons) {
            balloon.draw(gl, move, normalizedGameTime);

        }
//        square2.draw(gl, move, normalizedGameTime);
//        square3.draw(gl, move, normalizedGameTime);
//        rect1.draw(gl, move, normalizedGameTime);

        // gl.glPopMatrix();
        try {
            Thread.sleep(100);
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
        GLU.gluOrtho2D(gl, 0, 480, 0, 800);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glLoadIdentity();


    }


    /*
    *
    *
    */

    public void initGameTime() {
//        this.context = context;
        gameTime = System.currentTimeMillis();
        Log.i(TAG, "GameStart: " + gameTime);
        gameStartTime = gameTime;
        normalizedGameTime = gameTime - gameStartTime;
    }


    /*
     *
     * 
     */

    void loadBalloons(String fileName) {
        String[] fields = new String[10];
        int nCol;
        int levelStartY = 600;
        int levelStartX = 450;
        int i;
        try {

            InputStream is = context.getAssets().open(fileName);

            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("#")) {  // Skip
                    Scanner lineScanner = new Scanner(line);
                    lineScanner.useDelimiter("\\|");
                    nCol = 0;
                    fields[nCol] = lineScanner.next();

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
/*
                        i++;
                        String startAngle = fields[i];
                        i++;
                        String turnAngle = fields[i];
*/

                        String[] rgbStr = colors.split(",");

                        float red = Float.parseFloat(rgbStr[0]);
                        float green = Float.parseFloat(rgbStr[1]);
                        float blue = Float.parseFloat(rgbStr[2]);

                        Balloon balloon = new Balloon();
                        balloon.redColor = red;
                        balloon.greenColor = green;
                        balloon.blueColor = blue;
                        balloon.unitStartTime = startTime;

                        balloon.xc = levelStartX;
                        balloon.yc = levelStartY;
                        levelStartX += 35;
                        balloon.angle = 0;
                        balloon.setWayPoints(wayPoints);
                        balloon.initOrigin();
                        balloons.add(balloon);
                    } else if (fields[0].startsWith("Circle")) {


                        /*         Circle circle = new Circle(200, levelStartY, 1, 8, 30);

                                                circle.setWayPoints(wayPoints);
                                                circle.initOrigin();
                                                circleUnits.add(circle);

                                                levelStartY += 15;
                        */
                    } else if (fields[0].startsWith("Triangle")) {
                        /*            Circle circle = new Circle(200, levelStartY, 1, 8, 30);

                      circle.setWayPoints(wayPoints);
                      circle.setSides(3);
                      circle.initOrigin();
                      circleUnits.add(circle);

                      levelStartY += 15;*/

                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }


    /*
    *
    */

    void loadBalloonWayPoints(String fileName) {
        String[] fields = new String[10];
        int nCol;
        int i;
        try {

            InputStream is = context.getAssets().open(fileName);

            Scanner scanner = new Scanner(is);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.startsWith("#")) {  // Skip
                    Scanner lineScanner = new Scanner(line);
                    lineScanner.useDelimiter("\\|");
                    nCol = 0;
                    fields[nCol] = lineScanner.next();

                    nCol++;
                    while (lineScanner.hasNext()) { // At this point we know we want the line

                        fields[nCol] = lineScanner.next();
//                    Log.i("=========== FOO", fields[nCol] + ",");
                        nCol++;
                    }

                    i = 0;
                    int seqNo = Integer.parseInt(fields[i]);
                    i++;
                    int x = Integer.parseInt(fields[i]);
                    i++;
                    int y = Integer.parseInt(fields[i]);
                    i++;
                    int dx = Integer.parseInt(fields[i]);
                    i++;
                    int dy = Integer.parseInt(fields[i]);
//                    i++;

                    WayPoint wp = new WayPoint(seqNo, x, y, dx, dy);


                    wayPoints.add(wp);
                }
            }

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG, "# WayPoints loaded: " + wayPoints.size());
    }


}

