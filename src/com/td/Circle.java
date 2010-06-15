package com.td;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.os.SystemClock;
import android.util.Log;

public class Circle {

    private String TAG = "Circle";

    // Space to hold (x,y,z) of the center: xc,yc,cz
    // and the radius "radiusOfCircle"
    private float xc, yc, cz, radiusOfCircle;

    public void setSides(int sides) {
        this.sides = sides;
    }

    private int sides=30;

    // coordinate array: (x,y) vertex points
    private float[] xarray = null;
    private float[] yarray = null;

    // texture arrray: (x,y) also called (s,t) points
    // where the figure is going to be mapped to a texture bitmap
    private float[] sarray = null;
    private float[] tarray = null;

    // Number of points or vertices we want to use
//    private final static int VERTS = 20;

    // A raw native buffer to hold the point coordinates
    private FloatBuffer mFVertexBuffer;
    // A raw native buffer to hold indices
    // allowing a reuse of points.
    private ShortBuffer mIndexBuffer;
    private int numOfIndices = 0;
    private long prevtime = SystemClock.uptimeMillis();
    private float radius;

    private int dx;
    private int dy;

    double nextWayPointX;
    double nextWayPointY;
    boolean isAlive = true;
   
    //    boolean isVisible = false;
    List<WayPoint> wayPoints;
    int currentWayPoint = 0;

    public void prepareBuffers(float x, float y, float z, float radius,
                               int sides) {
        this.radius = radius;
        Circle t = new Circle(x, y, z, radius, sides);
        this.sides = sides;
        this.radius = radius;
        this.mFVertexBuffer = t.getVertexBuffer();
        this.mIndexBuffer = t.getIndexBuffer();
        this.numOfIndices = t.getNumberOfIndices();
        this.mFVertexBuffer.position(0);
        this.mIndexBuffer.position(0);
    }

    public void draw(GL10 gl, boolean move, long gameTime) {
        gl.glPushMatrix();

        gl.glTranslatef(xc, yc, 0f);

        Log.i("COORDINATES", xc + " X  Y ::" + yc);
//		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        long curtime = SystemClock.uptimeMillis();

        if ((curtime - prevtime) > 200) {
            prevtime = curtime;
//            sides += 1;
//            if (sides > 50) {
//                sides = 3;
//            }

//            sides=3;
            if (move) {
             //   angle += 25;
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

        }
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        prepareBuffers(this.xc, this.yc, this.cz, radius, sides);
        // EvenPolygon.test();
        gl.glColor4f(1.0f, 0f, 0f, 0.3f);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
//		gl.glFrontFace(GL10.GL_CW);
        gl.glDrawElements(GL10.GL_TRIANGLES, this.numOfIndices,
                GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        gl.glPopMatrix();
    }

    // **********************************************
    // Constructor
    // **********************************************
    public Circle(float incx, float incy, float incz, // (x,y,z) center
                  float inr, // radius
                  int insides) // number of sides
    {

        xc = incx;
        yc = incy;
        cz = incz;
        radiusOfCircle = inr;
        radius = inr;
        sides = insides;

        // allocate memory for the arrays
        xarray = new float[sides];
        yarray = new float[sides];

        // allocate memory for texture point arrays
        sarray = new float[sides];
        tarray = new float[sides];

        // calculate vertex points
        calcArrays();

        // calculate texture points
        calcTextureArrays();
    }

    public void setWayPoints(List<WayPoint> wp) {
        this.wayPoints = wp;
    }

    // **********************************************
    // Get and convert the vertex coordinates
    // based on origin and radius.
    // Real logic of angles happen inside getMultiplierArray() functions
    // **********************************************
    private void calcArrays() {
        // Get the vertex points assuming a circle
        // with a radius of "1" and located at "origin" zero
        float[] xmarray = this.getXMultiplierArray();
        float[] ymarray = this.getYMultiplierArray();

        // calc xarray: get the vertex
        // by adding the "x" portion of the origin
        // multiply the coordinate with radius (scale)
        for (int i = 0; i < sides; i++) {
            float curm = xmarray[i];
            float xcoord = xc + radiusOfCircle * curm;
            xarray[i] = xcoord;
        }
        this.printArray(xarray, "xarray");

        // calc yarray: do the same for y coordinates
        for (int i = 0; i < sides; i++) {
            float curm = ymarray[i];
            float ycoord = yc + radiusOfCircle * curm;
            yarray[i] = ycoord;
        }
        this.printArray(yarray, "yarray");
    }

    // **********************************************
    // Calculate texture arrays
    // See Texture subsection for more discussion on this
    // Very similar approach.
    // In this case the polygon has to map into a space
    // that is a square
    // **********************************************
    private void calcTextureArrays() {
        float[] xmarray = this.getXMultiplierArray();
        float[] ymarray = this.getYMultiplierArray();

        // calc xarray
        for (int i = 0; i < sides; i++) {
            float curm = xmarray[i];
            float xcoord = 2f +  curm;
//            float xcoord = 2f + 2f * curm;
            sarray[i] = xcoord;
        }
        this.printArray(sarray, "sarray");
        // calc yarray
        for (int i = 0; i < sides; i++) {
            float curm = ymarray[i];
            float ycoord = 2f + curm;
//            float ycoord = 2f + 2f * curm;
            tarray[i] = ycoord;
        }
        this.printArray(tarray, "tarray");
    }

    // **********************************************
    // Convert the java array of vertices
    // into an nio float buffer
    // **********************************************
    public FloatBuffer getVertexBuffer() {
        int vertices = sides + 1;
        int coordinates = 3;
        int floatSize = 4;
        int spacePerVertex = coordinates * floatSize;

        ByteBuffer vbb = ByteBuffer.allocateDirect(spacePerVertex * vertices);
        vbb.order(ByteOrder.nativeOrder());

        FloatBuffer mFVertexBuffer = vbb.asFloatBuffer();

        // Put the first coordinate (x,y,z:0,0,0)
        mFVertexBuffer.put(xc); // x
        mFVertexBuffer.put(yc); // y
        mFVertexBuffer.put(0f); // z

//        int totalPuts = 3;
        for (int i = 0; i < sides; i++) {
            mFVertexBuffer.put(xarray[i]); // x
            mFVertexBuffer.put(yarray[i]); // y
            mFVertexBuffer.put(0f); // z
//            totalPuts += 3;
        }
        // Log.d("total puts:", Integer.toString(totalPuts));
        return mFVertexBuffer;
    }

    // **********************************************
    // Convert texture buffer to an nio buffer
    // **********************************************
//    public FloatBuffer getTextureBuffer() {
//        int vertices = sides + 1;
//        int coordinates = 2;
//        int floatsize = 4;
//        int spacePerVertex = coordinates * floatsize;
//
//        ByteBuffer vbb = ByteBuffer.allocateDirect(spacePerVertex * vertices);
//        vbb.order(ByteOrder.nativeOrder());
//
//        FloatBuffer mFTextureBuffer = vbb.asFloatBuffer();
//
//        // Put the first coordinate (x,y (s,t):0,0)
//        mFTextureBuffer.put(2f); // x or s
//        mFTextureBuffer.put(2f); // y or t
//        int totalPuts = 2;
//        for (int i = 0; i < sides; i++) {
//            mFTextureBuffer.put(sarray[i]); // x
//            mFTextureBuffer.put(tarray[i]); // y
//            totalPuts += 2;
//        }
//        // Log.d("total texture puts:", Integer.toString(totalPuts));
//        return mFTextureBuffer;
//    }

    // **********************************************
    // Calculate indices forming multiple triangles.
    // Start with the center vertex which is at 0
    // Then count them in a clockwise direction such as
    // 0,1,2, 0,2,3, 0,3,4..etc
    // **********************************************
    public ShortBuffer getIndexBuffer() {
        short[] iarray = new short[sides * 3];
        ByteBuffer ibb = ByteBuffer.allocateDirect(sides * 3 * 2);
        ibb.order(ByteOrder.nativeOrder());

        ShortBuffer mIndexBuffer = ibb.asShortBuffer();
        for (int i = 0; i < sides; i++) {
            short index1 = 0;
            short index2 = (short) (i + 1);
            short index3 = (short) (i + 2);
            if (index3 == sides + 1) {
                index3 = 1;
            }
            mIndexBuffer.put(index1);
            mIndexBuffer.put(index2);
            mIndexBuffer.put(index3);
            iarray[i * 3 + 0] = index1;
            iarray[i * 3 + 1] = index2;
            iarray[i * 3 + 2] = index3;
        }
        this.printShortArray(iarray, "index array");
        return mIndexBuffer;
    }

    // **********************************************
    // This is where you take the angle array
    // for each vertex and calculate their projection multiplier
    // on the x axis
    // **********************************************
    private float[] getXMultiplierArray() {
        float[] angleArray = getAngleArrays();
        float[] xmultiplierArray = new float[sides];
        for (int i = 0; i < angleArray.length; i++) {
            float curAngle = angleArray[i];
            float sinvalue = (float) Math.cos(Math.toRadians(curAngle));
            float absSinValue = Math.abs(sinvalue);
            if (isXPositiveQuadrant(curAngle)) {
                sinvalue = absSinValue;
            } else {
                sinvalue = -absSinValue;
            }
            xmultiplierArray[i] = this.getApproxValue(sinvalue);
        }
        this.printArray(xmultiplierArray, "xmultiplierArray");
        return xmultiplierArray;
    }

    // **********************************************
    // This is where you take the angle array
    // for each vertex and calculate their projection multiplier
    // on the y axis
    // **********************************************
    private float[] getYMultiplierArray() {
        float[] angleArray = getAngleArrays();
        float[] yMultiplierArray = new float[sides];

        for (int i = 0; i < angleArray.length; i++) {
            float curAngle = angleArray[i];
            float sinValue = (float) Math.sin(Math.toRadians(curAngle));
            float absSinValue = Math.abs(sinValue);
            if (isYPositiveQuadrant(curAngle)) {
                sinValue = absSinValue;
            } else {
                sinValue = -absSinValue;
            }
            yMultiplierArray[i] = this.getApproxValue(sinValue);
        }

        this.printArray(yMultiplierArray, "yMultiplierArray");
        return yMultiplierArray;
    }

    // **********************************************
    // This function may not be needed
    // Test it yourself and discard it if you don't need
    // **********************************************
    private boolean isXPositiveQuadrant(float angle) {
        if ((0 <= angle) && (angle <= 90)) {
            return true;
        }
        if ((angle < 0) && (angle >= -90)) {
            return true;
        }
        return false;
    }

    // **********************************************
    // This function may not be needed
    // Test it yourself and discard it if you don't need
    // **********************************************
    private boolean isYPositiveQuadrant(float angle) {
        if ((0 <= angle) && (angle <= 90)) {
            return true;
        }
        if ((angle < 180) && (angle >= 90)) {
            return true;
        }
        return false;
    }

    // **********************************************
    // This is where you calculate angles
    // for each line going from center to each vertex
    // **********************************************
    private float[] getAngleArrays() {
        float[] angleArray = new float[sides];
        float commonAngle = 360.0f / sides;
        float halfAngle = commonAngle / 2.0f;
        float firstAngle = 360.0f - (90 + halfAngle);
        angleArray[0] = firstAngle;
        float curAngle = firstAngle;

        for (int i = 1; i < sides; i++) {
            float newAngle = curAngle - commonAngle;
            angleArray[i] = newAngle;
            curAngle = newAngle;
        }
        printArray(angleArray, "angleArray");
        return angleArray;
    }

    // **********************************************
    // Some rounding if needed
    // **********************************************
    private float getApproxValue(float f) {
        return (Math.abs(f) < 0.001) ? 0 : f;
    }

    // **********************************************
    // Return how many Indices you will need
    // given the number of sides
    // This is the count of number of triangles needed
    // to make the polygon multiplied by 3
    // It just happens that the number of triangles is
    // same as the number of sides
    // **********************************************
    public int getNumberOfIndices() {
        return sides * 3;
    }

    public static void test() {
        Circle triangle = new Circle(0, 0, 0, 0.1f, 3);
    }

    private void printArray(float array[], String tag) {
        StringBuilder sb = new StringBuilder(tag);
        for (int i = 0; i < array.length; i++) {
            sb.append(";").append(array[i]);
        }
        // Log.d("hh", sb.toString());
    }

    private void printShortArray(short array[], String tag) {
        StringBuilder sb = new StringBuilder(tag);
        for (int i = 0; i < array.length; i++) {
            sb.append(";").append(array[i]);
        }
        // Log.d(tag, sb.toString());
    }


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
            Log.i(TAG, "(x,y,dx,dy)=>("
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

    public void initOrigin() {
          currentWayPoint = 0;
          nextWayPoint();
    }    
}
