package com.td;

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
			mParticles[i] = new Particle(gen.nextFloat(), gen.nextFloat(), gen
					.nextFloat());
		}

		// a simple triangle, kinda like this ^
		float[] coords = { -0.1f, 0.0f, 0.0f, 0.1f, 0.0f, 0.0f, 0.0f, 0.0f,
				0.1f };
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
		for (int i = 0; i < PARTICLECOUNT; i++) {
			gl.glPushMatrix();
			gl.glTranslatef(mParticles[i].x, mParticles[i].y, mParticles[i].z);
			gl.glDrawElements(GL10.GL_TRIANGLES, 3, GL10.GL_UNSIGNED_SHORT,
					mIndexBuffer);
			gl.glPopMatrix();
		}
	}

	// simply have the particles fall at a hard coded gravity rate
	// and when they hit zero, bump them back up to a z of 1.0f
	public void update() {
		for (int i = 0; i < PARTICLECOUNT; i++) {
			mParticles[i].z = mParticles[i].z - 0.01f;
			if (mParticles[i].z < 0.0f) {
				mParticles[i].z = 1.0f;
			}
		}
	}
}