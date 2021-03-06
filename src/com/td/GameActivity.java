package com.td;

import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import com.pro.PolygonRenderer;
import com.balloon.GameGLSurfaceView;

/**
 * Created by IntelliJ IDEA. User: melling Date: May 31, 2010 Time: 3:02:03 PM
 */
public class GameActivity extends Activity {

	private GameGLSurfaceView surface;
	private GLSurfaceView surfaceProAndroid;

	//
	private PolygonRenderer renderer;

	// private GLRenderer renderer1;
	

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(new com.balloon.GameGLSurfaceView(this)); // Set default
		// game
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.pro_android) {
			Log.i("Menu Android ii", "Clicked Pro Android II");
			// surface = new GameGLSurfaceView(this);
			GLSurfaceView surfaceProAndroid = new GLSurfaceView(this);
			// renderer = new GLRenderer();
			PolygonRenderer renderer = new PolygonRenderer(this);
			surfaceProAndroid.setRenderer(renderer);
			// renderer.setContext(this);
			setContentView(surfaceProAndroid);

		} else if (item.getItemId() == R.id.tower_defense) {
			Log.i("TowerDefence", "Clicked TowerDefense");

			// surface1 = new GLSurfaceView(this);
			GameGLSurfaceView surface = new GameGLSurfaceView(this);
			// renderer1 = new com.td.GameRenderer(this);
			// renderer1 = new GLRenderer();
			// renderer = new PolygonRenderer(this);
			// surface.setRenderer(renderer1);
			// renderer1.setContext(this);
			setContentView(surface);

		} else if (item.getItemId() == R.id.balloon_menu) {
			Log.i("FOO", "Clicked Balloon");
			setContentView(new com.balloon.GameGLSurfaceView(this));

		}

		else if (item.getItemId() == R.id.tatyana_test) {
			// Tatyana's
			Log.i("Particals", "Particals");
			setContentView(new com.td.GameGLSurfaceView(this)); 
		//	Intent i = new Intent(this, com.particles.ParticleSystemDemo.class);
		//	startActivity(i);
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu1, menu);

		return true;
	}
}
