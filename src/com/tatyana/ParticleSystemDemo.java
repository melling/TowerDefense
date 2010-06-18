package com.tatyana;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ParticleSystemDemo extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mGLView = new ClearGLSurfaceView(this);
		setContentView(mGLView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGLView.onResume();
	}

	/* Creates the menu items */
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Visit bayninestudios.com");
		return true;
	}

	/* Handles item selections */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			Intent myIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri
					.parse("http://www.bayninestudios.com"));
			startActivity(myIntent);
			return true;
		}
		return false;
	}

	private GLSurfaceView mGLView;
}

class ClearGLSurfaceView extends GLSurfaceView {
	public ClearGLSurfaceView(Context context) {
		super(context);
		mRenderer = new ClearRenderer();
		setRenderer(mRenderer);
	}

	ClearRenderer mRenderer;
}

class ClearRenderer implements GLSurfaceView.Renderer {
	private ParticleSystem mParticleSystem;

	public ClearRenderer() {
		mParticleSystem = new ParticleSystem();
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// avoid division by zero
		if (height == 0)
			height = 1;
		// draw on the entire screen
		gl.glViewport(0, 0, width, height);
		// setup projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glDisable(GL10.GL_CULL_FACE);

		gl.glLoadIdentity();

		Log.d("SpriteRenderer", width + "x" + height);
		GLU.gluOrtho2D(gl, 0, 480, 0, 800);
		// GLU.gluOrtho2D(gl, 0, 480, 0, 320);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		// gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glLoadIdentity();

	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//		GLU.gluPerspective(gl, 15.0f, 80.0f / 48.0f, 1, 100);
//		GLU.gluLookAt(gl, 0f, -10f, 15f, 0.5f, 0.0f, 0f, 0.0f, 1.0f, 1.0f);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
	}

	public void onSurfaceChangedxxx(GL10 gl, int w, int h) {
		gl.glViewport(0, 0, w, h);
	}

	public void onDrawFrame(GL10 gl) {
		gl.glClearColor(0, 0, .5f, 1.0f);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		mParticleSystem.update();
		mParticleSystem.draw(gl);
	}

}
