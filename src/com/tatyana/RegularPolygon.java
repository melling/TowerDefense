package com.tatyana;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.SystemClock;
import android.util.Log;

public class RegularPolygon {
	// Space to hold (x,y,z) of the center: cx,cy,cz
	// and the radius "r"
	private float cx, cy, cz, r;
	private int sides;

	// coordinate array: (x,y) vertex points
	private float[] xarray = null;
	private float[] yarray = null;

	// texture arrray: (x,y) also called (s,t) points
	// where the figure is going to be mapped to a texture bitmap
	private float[] sarray = null;
	private float[] tarray = null;

	// Number of points or vertices we want to use
	private final static int VERTS = 20;

	// A raw native buffer to hold the point coordinates
	private FloatBuffer mFVertexBuffer;
	// A raw native buffer to hold indices
	// allowing a reuse of points.
	private ShortBuffer mIndexBuffer;
	private int numOfIndices = 0;
	private long prevtime = SystemClock.uptimeMillis();
	private float radius = 0.1f;

	public void prepareBuffers(float x, float y, float z,
			float radius, int sides) {
		this.radius = radius;
		RegularPolygon t = new RegularPolygon(x,y,z,radius,sides);
		this.sides = sides;
		this.radius = radius;
		this.mFVertexBuffer = t.getVertexBuffer();
		this.mIndexBuffer = t.getIndexBuffer();
		this.numOfIndices = t.getNumberOfIndices();
		this.mFVertexBuffer.position(0);
		this.mIndexBuffer.position(0);
	}

	public void draw(GL10 gl,
			boolean move,
			long gameTime) 
	{
		gl.glEnable(GL10.GL_DITHER);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glPushMatrix();
		cx=cx+0.01f;
		float dispx = cx;
		float dispy =cy;
		//gl.glTranslatef(this.cx, this.cy, -4.0f);
		Log.i("COORDINATES",dispx+" X  Y ::"+dispy);
		GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		long curtime = SystemClock.uptimeMillis();
		if ((curtime - prevtime) > 200) {
			prevtime = curtime;
			sides += 1;
			if (sides > 50) {
				sides = 3;
			}
			this.prepareBuffers( this.cx, this.cy, this.cz,radius,sides);
		}
		// EvenPolygon.test();
		// gl.glColor4f(1.0f, 0, 0, 0.3f);
		gl.glColor4f(1.0f, 0.5f, 0.5f, 0.3f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
		gl.glFrontFace(GL10.GL_CW);
		gl.glDrawElements(GL10.GL_TRIANGLES, this.numOfIndices,
				GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
		gl.glPopMatrix();
	}

	// **********************************************
	// Constructor
	// **********************************************
	public RegularPolygon(float incx, float incy, float incz, // (x,y,z) center
			float inr, // radius
			int insides) // number of sides
	{
		
		cx = incx;
		cy = incy;
		cz = incz;
		r = inr;
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
			float xcoord = cx + r * curm;
			xarray[i] = xcoord;
		}
		this.printArray(xarray, "xarray");

		// calc yarray: do the same for y coordinates
		for (int i = 0; i < sides; i++) {
			float curm = ymarray[i];
			float ycoord = cy + r * curm;
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
			float xcoord = 0.5f + 0.5f * curm;
			sarray[i] = xcoord;
		}
		this.printArray(sarray, "sarray");
		// calc yarray
		for (int i = 0; i < sides; i++) {
			float curm = ymarray[i];
			float ycoord = 0.5f + 0.5f * curm;
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
		int floatsize = 4;
		int spacePerVertex = coordinates * floatsize;

		ByteBuffer vbb = ByteBuffer.allocateDirect(spacePerVertex * vertices);
		vbb.order(ByteOrder.nativeOrder());

		FloatBuffer mFVertexBuffer = vbb.asFloatBuffer();

		// Put the first coordinate (x,y,z:0,0,0)
		mFVertexBuffer.put(cx); // x
		mFVertexBuffer.put(cy); // y
		mFVertexBuffer.put(0.0f); // z

		int totalPuts = 3;
		for (int i = 0; i < sides; i++) {
			mFVertexBuffer.put(xarray[i]); // x
			mFVertexBuffer.put(yarray[i]); // y
			mFVertexBuffer.put(0.0f); // z
			totalPuts += 3;
		}
//		Log.d("total puts:", Integer.toString(totalPuts));
		return mFVertexBuffer;
	}

	// **********************************************
	// Convert texture buffer to an nio buffer
	// **********************************************
	public FloatBuffer getTextureBuffer() {
		int vertices = sides + 1;
		int coordinates = 2;
		int floatsize = 4;
		int spacePerVertex = coordinates * floatsize;

		ByteBuffer vbb = ByteBuffer.allocateDirect(spacePerVertex * vertices);
		vbb.order(ByteOrder.nativeOrder());

		FloatBuffer mFTextureBuffer = vbb.asFloatBuffer();

		// Put the first coordinate (x,y (s,t):0,0)
		mFTextureBuffer.put(0.5f); // x or s
		mFTextureBuffer.put(0.5f); // y or t
		int totalPuts = 2;
		for (int i = 0; i < sides; i++) {
			mFTextureBuffer.put(sarray[i]); // x
			mFTextureBuffer.put(tarray[i]); // y
			totalPuts += 2;
		}
//		Log.d("total texture puts:", Integer.toString(totalPuts));
		return mFTextureBuffer;
	}

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
		float[] ymultiplierArray = new float[sides];

		for (int i = 0; i < angleArray.length; i++) {
			float curAngle = angleArray[i];
			float sinvalue = (float) Math.sin(Math.toRadians(curAngle));
			float absSinValue = Math.abs(sinvalue);
			if (isYPositiveQuadrant(curAngle)) {
				sinvalue = absSinValue;
			} else {
				sinvalue = -absSinValue;
			}
			ymultiplierArray[i] = this.getApproxValue(sinvalue);
		}

		this.printArray(ymultiplierArray, "ymultiplierArray");
		return ymultiplierArray;
	}

	// **********************************************
	// This function may not be needed
	// Test it yourself and discard it if you dont need
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
	// Test it yourself and discard it if you dont need
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
		RegularPolygon triangle = new RegularPolygon(0, 0, 0, 0.1f, 3);
	}

	private void printArray(float array[], String tag) {
		StringBuilder sb = new StringBuilder(tag);
		for (int i = 0; i < array.length; i++) {
			sb.append(";").append(array[i]);
		}
//		Log.d("hh", sb.toString());
	}

	private void printShortArray(short array[], String tag) {
		StringBuilder sb = new StringBuilder(tag);
		for (int i = 0; i < array.length; i++) {
			sb.append(";").append(array[i]);
		}
//		Log.d(tag, sb.toString());
	}
}
