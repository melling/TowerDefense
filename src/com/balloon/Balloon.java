package com.balloon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * This class is an object representation of
 * a Square containing the vertex information
 * and drawing functionality, which is called
 * by the renderer.
 *
 * @author Savas Ziplies (nea/INsanityDesign)
 */

public class Balloon {

    private String TAG = "Balloon";
    /**
     * The buffer holding the vertices
     */
    private FloatBuffer vertexBuffer;
    public int yc = -1;
    public int xc = 1;
    public float redColor = 0.0f;
    public float blueColor = 0.0f;
    public float greenColor = 0.5f;
    public float angle = 0;

    private int[] boundingRect = new int[4];

    double nextWayPointX;
    double nextWayPointY;
    boolean isAlive = true;
//    boolean isVisible = false;
    List<WayPoint> wayPoints;
    int currentWayPoint = 0;
    /**
     * The initial vertex definition
     */
    float qWidth = 25f;

    private float vertices[] = {
            -qWidth, -qWidth, //Bottom Left
            qWidth, -qWidth,     //Bottom Right
            -qWidth, qWidth,     //Top Left
            qWidth, qWidth     //Top Right
    };

    /*   private float vertices[] = {
            0, 0, //Bottom Left
            qWidth, 0,     //Bottom Right
            0, qWidth,     //Top Left
            qWidth, qWidth     //Top Right
    };*/

    private int dx;
    private int dy;
    public int unitStartTime;

    static int TOP = 0;
    static int LEFT = 1;
    static int BOTTOM = 2;
    static int RIGHT = 3;
    private boolean isShot = false;

    /**
     * The Square constructor.
     * <p/>
     * Initiate the buffers.
     */
    public Balloon() {
        //
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);


    }

    /*
     *  Detect collision where the 0,0 is at the bottom left corner
     *
     */

    public boolean isCollision(int[] r2) {

        boolean collision;

        // Assume we don't have collisions if anything is zero.
        if (r2[LEFT] == 0
                || r2[RIGHT] == 0
                || r2[TOP] == 0
                || r2[BOTTOM] == 0) {
            return false;
        }
        if (r2[TOP] < 500 || r2[BOTTOM] > 730) {
            return false;
        }

        collision = !((r2[LEFT] > boundingRect[RIGHT])
                || (r2[RIGHT] < boundingRect[LEFT])
                || (r2[TOP] < boundingRect[BOTTOM])
                || (r2[BOTTOM] > boundingRect[TOP]));
        if (collision) {
            Log.i(TAG, "------------------------");

            Log.i(TAG, "Found Collision BR (Top,Left,Bottom,Right):" + boundingRect[0] + "," + boundingRect[1] + "," + boundingRect[2] + "," + boundingRect[3]);
            Log.i(TAG, "Found Collision r2 (Top,Left,Bottom,Right): " + r2[0] + "," + r2[1] + "," + r2[2] + "," + r2[3]);

        } else {

            Log.i(TAG, "------------------------");
            Log.i(TAG, "NO Collision BR (Top,Left,Bottom,Right):" + boundingRect[0] + "," + boundingRect[1] + "," + boundingRect[2] + "," + boundingRect[3]);
            Log.i(TAG, "NO Collision r2 (Top,Left,Bottom,Right): " + r2[0] + "," + r2[1] + "," + r2[2] + "," + r2[3]);
            int a = (r2[LEFT] - boundingRect[RIGHT]);
            int b = (r2[TOP] - boundingRect[BOTTOM]);
            Log.i(TAG, "r2[LEFT] > boundingRect[RIGHT] =>(" + r2[LEFT] + " > " + boundingRect[RIGHT] + ")");
            Log.i(TAG, "r2[RIGHT] < boundingRect[LEFT] =>(" + r2[RIGHT] + " < " + boundingRect[LEFT] + ")");
            Log.i(TAG, "r2[TOP] < boundingRect[BOTTOM] =>(" + r2[TOP] + " < " + boundingRect[BOTTOM] + ")");
            Log.i(TAG, "r2[BOTTOM] > boundingRect[BOTTOM] =>(" + r2[BOTTOM] + " > " + boundingRect[TOP] + ")");
        }
        return collision;
    }

    public void setWayPoints(List<WayPoint> wp) {
        this.wayPoints = wp;
    }

    /**
     * The object own drawing function.
     * Called from the renderer to redraw this instance
     * with possible changes in values.
     *
     * @param gl                 - The GL context
     * @param move               - Can we move
     * @param normalizedGameTime - Time in seconds since game began, ignoring user pauses
     */
    public void draw(GL10 gl, boolean move, long normalizedGameTime) {

        if (!isAlive) return; // TODO: Just make sure object is removed

        if (unitStartTime < normalizedGameTime) {
//            Log.i(TAG, "Time  to move yet: " + unitStartTime + "<" + normalizedGameTime);
        } else {
            move = false;
//            Log.i(TAG, "Unit waiting to move: " + unitStartTime + ">=" + normalizedGameTime);

        }
        //Set the face rotation


        if (move) {

            gl.glPushMatrix();
            gl.glTranslatef(xc, yc, 0f);
            gl.glRotatef(angle, 0, 0, 1);
//        if (move) {
//            angle += 25;
            yc = yc + dy;
            xc = xc + dx;
            boundingRect[TOP] = yc + (int) qWidth;
            boundingRect[LEFT] = xc - (int) qWidth;
            boundingRect[BOTTOM] = yc - (int) qWidth;
            boundingRect[RIGHT] = xc + (int) qWidth;

//            Log.i(TAG, "SETTING BR:" + boundingRect[0] + "," + boundingRect[1] + "," + boundingRect[2] + "," + boundingRect[3]);

            if (dy > 0 && dx == 0) { // Moving Up
                if (yc >= nextWayPointY) {
                    nextWayPoint();
                }

            } else if (dy == 0 && dx < 0) {  // Moving Left
                if (xc <= nextWayPointX) {
                    nextWayPoint();
                }
            } else if (dy < 0 && dx == 0) {  // Moving down
                if (yc <= nextWayPointY) {
                    nextWayPoint();
                }
            } else if (dy == 0 && dx > 0) {  // Moving right
                if (xc >= nextWayPointY) {
                    nextWayPoint();
                }
            } else if (dy <= 0 && dx > 0) {  // Moving diagonal to right

            }

//        Log.i("draw", xc + " , " + yc);
//        gl.glScalef(0.05f, 0.05f, 1.0f);


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

    public void nextWayPoint() {
        int nWayPoints = wayPoints.size();
        Log.i("Square", "Moving from wayPoint: " + currentWayPoint + "=>" + (currentWayPoint + 1) + " T:" + nWayPoints);
        if ((currentWayPoint + 1) < nWayPoints) {
            WayPoint wayPoint0 = wayPoints.get(currentWayPoint);
            WayPoint wayPoint1 = wayPoints.get(currentWayPoint + 1);
            dx = wayPoint0.dx;
            dy = wayPoint0.dy;
            nextWayPointX = wayPoint1.x;
            nextWayPointY = wayPoint1.y;
            Log.i("Square", "(x,y,dx,dy)=>("
                    + nextWayPointX + ","
                    + nextWayPointY + ","
                    + dx + ","
                    + dy + ")");

            currentWayPoint++;
        } else {
            Log.i(TAG, "Unit reached the end of waypoints: "
                    + (currentWayPoint + 1) + ">"
                    + nWayPoints);
            isAlive = false;
        }
    }

    /*
     *
     */

    public void initOrigin() {
        currentWayPoint = 0;
        nextWayPoint();

        /*WayPoint wayPoint0 = wayPoints.get(0);
        WayPoint wayPoint1 = wayPoints.get(1);
        dx = wayPoint0.dx;
        dy = wayPoint0.dy;
        nextWayPointX = wayPoint1.x;
        nextWayPointY = wayPoint1.y;
*/
//        currentWayPoint = 1; // 0 is the origin, 1 is the first destination

    }

    public void setShot() {
        isShot = true;

        redColor = 1.0f;
        blueColor = 0.0f;
        greenColor = 0.0f;
    }
}