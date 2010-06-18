package com.balloon;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

/**
 * This class is an object representation of
 * a Player containing the vertex information
 * and drawing functionality, which is called
 * by the renderer.
 *
 * @author Savas Ziplies (nea/INsanityDesign)
 */

public class Player {

    private String TAG = "Player";
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
    int currentWayPoint = 0;
    /**
     * The initial vertex definition
     */
    float qWidth = 15f;

    private float vertices[] = {
            -qWidth, -qWidth, //Bottom Left
            qWidth, -qWidth,     //Bottom Right
            -qWidth, qWidth,     //Top Left
            qWidth, qWidth     //Top Right
    };
    private int dx;
    private int dy;
    public int startTime;

    /**
     * The Player constructor.
     * <p/>
     * Initiate the buffers.
     */
    public Player() {
        //
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);


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
        gl.glTranslatef(xc, yc, 0f);
        gl.glRotatef(angle, 0, 0, 1);
        if (startTime > normalizedGameTime) {
            Log.i(TAG, "Not time to move yet: " + startTime + ">" + normalizedGameTime);
        }
//        if (move && (unitStartTime > normalizedGameTime)) {
       /* if (move) {
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
*/
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