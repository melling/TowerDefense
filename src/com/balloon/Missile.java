package com.balloon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * This class is an object representation of
 * a Missile containing the vertex information
 * and drawing functionality, which is called
 * by the renderer.
 *
 * @author Savas Ziplies (nea/INsanityDesign)
 */

public class Missile {

    /**
     * The buffer holding the vertices
     */
    private FloatBuffer vertexBuffer;
    public int yc = -1;
    public int xc = 1;
    public float redColor = 0.0f;
    public float blueColor = 1f;
    public float greenColor = 0f;
    public float angle = 0;
    private int[] boundingRect = new int[4];

    static int TOP = 0;
    static int LEFT = 1;
    static int BOTTOM = 2;
    static int RIGHT = 3;

//    double nextWayPointX;
//    double nextWayPointY;
    boolean isAlive = true;
//    boolean isVisible = false;
//    List<WayPoint> wayPoints;
//    int currentWayPoint = 0;
    /**
     * The initial vertex definition
     */
    float qWidth = 5f;
    float qHeight = 10f;

    private float vertices[] = {
            0, 0, //Bottom Left
            qWidth, 0,     //Bottom Right
            0, qHeight,     //Top Left
            qWidth, qHeight     //Top Right
    };

    /**
     * The Square constructor.
     * <p/>
     * Initiate the buffers.
     */
    public Missile() {
        String TAG = "Missile";
        Log.i(TAG, "Creating Missile");
        //
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);


    }

    public int[] getRect() {
        return boundingRect;
    }

//    public void setWayPoints(List<WayPoint> wp) {
//        Log.i(TAG, "Setting wayPoints");
//        this.wayPoints = wp;
//    }

    /**
     * The object own drawing function.
     * Called from the renderer to redraw this instance
     * with possible changes in values.
     *
     * @param gl   - The GL context
     * @param move - Can we move
     */
    public void draw(GL10 gl, boolean move) {

        if (!isAlive) return; // TODO: Just make sure object is removed

//        if (unitStartTime < normalizedGameTime) {
//            Log.i(TAG, "Time  to move yet: " + unitStartTime + "<" + normalizedGameTime);
//        } else {
//            move = false;
//            Log.i(TAG, "Unit waiting to move: " + unitStartTime + ">=" + normalizedGameTime);
//
//        }
        //Set the face rotation


        if (move) {

            gl.glPushMatrix();
            gl.glTranslatef(xc, yc, 0f);
            gl.glRotatef(angle, 0, 0, 1);
//        if (move) {
//            angle += 25;
            int dy = 5;
            yc = yc + dy;
            int i = 0;
            boundingRect[TOP] = yc + (int)qHeight/2;   // TOP
            boundingRect[LEFT] = xc - (int)qWidth/2;    // LEFT
            boundingRect[BOTTOM] = yc - (int)qHeight/2; // BOTTOM
            boundingRect[RIGHT] = xc + (int)qWidth/2; // RIGHT


            //Point to our vertex buffer
            gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);

            //Enable vertex buffer
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

            //Set The Color To Blue
            gl.glColor4f(redColor, greenColor, blueColor, 1);

            //Draw the vertices as triangle strip
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 2);
            //Disable the client state before leaving
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glPopMatrix();
        }

    }


    /*
    *
    */

//    public void nextWayPoint() {
//        int nWayPoints = wayPoints.size();
//        Log.i("Square", "Moving from wayPoint: " + currentWayPoint + "=>" + (currentWayPoint + 1) + " T:" + nWayPoints);
//        if ((currentWayPoint + 1) < nWayPoints) {
//            WayPoint wayPoint0 = wayPoints.get(currentWayPoint);
//            WayPoint wayPoint1 = wayPoints.get(currentWayPoint + 1);
//            dx = wayPoint0.dx;
//            dy = wayPoint0.dy;
//            nextWayPointX = wayPoint1.x;
//            nextWayPointY = wayPoint1.y;
//            Log.i("Square", "(x,y,dx,dy)=>("
//                    + nextWayPointX + ","
//                    + nextWayPointY + ","
//                    + dx + ","
//                    + dy + ")");
//
//            currentWayPoint++;
//        } else {
//            Log.i(TAG, "Unit reached the end of waypoints: "
//                    + (currentWayPoint + 1) + ">"
//                    + nWayPoints);
//            isAlive = false;
//        }
//    }
//
//    /*
//     *
//     */
//
//    public void initOrigin() {
//        currentWayPoint = 0;
//        nextWayPoint();
//
//        /*WayPoint wayPoint0 = wayPoints.get(0);
//        WayPoint wayPoint1 = wayPoints.get(1);
//        dx = wayPoint0.dx;
//        dy = wayPoint0.dy;
//        nextWayPointX = wayPoint1.x;
//        nextWayPointY = wayPoint1.y;
//*/
////        currentWayPoint = 1; // 0 is the origin, 1 is the first destination
//
//    }

    public void exploded() {
        isAlive = false;
    }
}