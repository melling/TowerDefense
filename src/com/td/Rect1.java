package com.td;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * This class is an object representation of
 * a Rect1 containing the vertex information
 * and drawing functionality, which is called
 * by the renderer.
 *
 * @author Savas Ziplies (nea/INsanityDesign)
 */

public class Rect1 {

    private String TAG = "Rect1";
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

    double nextWayPointX;
    double nextWayPointY;
    boolean isAlive = true;
//    boolean isVisible = false;
    List<WayPoint> wayPoints;
    int currentWayPoint = 0;
    /**
     * The initial vertex definition
     */
    float qWidth = 5f;

    private float vertices[] = {
            0, 0, //Bottom Left
            20, 0,     //Bottom Right
            2, 0,     //Top Left
            2, 20     //Top Right
    };
    private int dx;
    private int dy;
    public int startTime;

    /**
     * The Rect1 constructor.
     * <p/>
     * Initiate the buffers.
     */
    public Rect1() {
        //
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);


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

        //Set the face rotation
        gl.glPushMatrix();
        gl.glTranslatef(xc, yc, -4.0f);
        gl.glRotatef(angle, 0, 0, 1);
        if (startTime > normalizedGameTime) {
            Log.i(TAG, "Not time to move yet: " + startTime + ">" + normalizedGameTime);
        }
//        if (move && (startTime > normalizedGameTime)) {
        if (move) {
            angle += 25;
            yc = yc + dy;
            xc = xc + dx;
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

    /*
     *
     */

    public void nextWayPoint() {
        int nWayPoints = wayPoints.size();
        Log.i("Rect1", "Moving from wayPoint: " + currentWayPoint + "=>" + (currentWayPoint + 1) + " T:" + nWayPoints);
        if ((currentWayPoint + 1) < nWayPoints) {
            WayPoint wayPoint0 = wayPoints.get(currentWayPoint);
            WayPoint wayPoint1 = wayPoints.get(currentWayPoint + 1);
            dx = wayPoint0.dx;
            dy = wayPoint0.dy;
            nextWayPointX = wayPoint1.x;
            nextWayPointY = wayPoint1.y;
            Log.i("Rect1", "(x,y,dx,dy)=>("
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
}