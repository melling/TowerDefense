package com.tatyana;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.util.Log;

public class ParticleSystem {
	private Particle[] mParticles;
	private int PARTICLECOUNT = 20;

	// for use to draw the particle
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mIndexBuffer;

	public ParticleSystem() {
		mParticles = new Particle[PARTICLECOUNT];

		// setup the random number generator
		Random gen = new Random(System.currentTimeMillis());
		// loop through all the particles and create new instances of each one
		for (int i = 0; i < PARTICLECOUNT; i++) {
			mParticles[i] = new Particle(gen.nextFloat()*200, gen.nextFloat()*200, 0);
		}

		// a simple triangle, kinda like this ^
//		float[] coords = { -0.1f, 0.0f, 0.0f, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f,0.1f };
		float[] coords = { 1f, 0.0f, 0.0f,
							10f, 0.0f, 0.0f, 
							0.0f, 10.0f,0f };
		short[] icoords = { 0, 1, 2 };

		mVertexBuffer = makeFloatBuffer(coords);
		mIndexBuffer = makeShortBuffer(icoords);
	}

	// used to make native order float buffers
	private FloatBuffer makeFloatBuffer(float[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer fb = bb.asFloatBuffer();
		fb.put(arr);
		fb.position(0);
		return fb;
	}

	// used to make native order short buffers
	private ShortBuffer makeShortBuffer(short[] arr) {
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		ShortBuffer ib = bb.asShortBuffer();
		ib.put(arr);
		ib.position(0);
		return ib;
	}

	public void draw(GL10 gl) {
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
		gl.glColor4f(1f, 1f, 1f, 1f);
		int s=10;
		for (int i = 0; i < PARTICLECOUNT; i++) {
			gl.glPushMatrix();
            //Enable vertex buffer
			
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glTranslatef(mParticles[i].x+200, mParticles[i].y+200, 1f);
//			gl.glTranslatef(mParticles[i].x, mParticles[i].y, mParticles[i].z);
			float xx = mParticles[i].x;
			float yy =mParticles[i].y;
			float zz =mParticles[i].z;
			Log.i("coordinates",xx+"::x::"+yy+":yy:"+zz+"::zz::");
//			gl.glScalef(100, 100, 1);
			gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT,
					mIndexBuffer);
			gl.glPopMatrix();
			
			
		}
	}
/*
 * 
 *   gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        prepareBuffers(this.xc, this.yc, this.cz, radius, sides);
        // EvenPolygon.test();
        gl.glColor4f(1.0f, 0f, 0f, 0.3f);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
//		gl.glFrontFace(GL10.GL_CW);
        gl.glDrawElements(GL10.GL_TRIANGLES, this.numOfIndices,
                GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
 
 */
	// simply have the particles fall at a hard coded gravity rate
	// and when they hit zero, bump them back up to a z of 1.0f
	public void update() {
		for (int i = 0; i < PARTICLECOUNT; i++) {
			mParticles[i].y = mParticles[i].y - 10f;
			if (mParticles[i].y < 0.0f) {
				mParticles[i].y = 10.0f;
			}
		}
	}
}
