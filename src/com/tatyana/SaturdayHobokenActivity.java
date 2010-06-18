package com.tatyana;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;

public class SaturdayHobokenActivity extends Activity{
	GLSurfaceView glview;
	Renderer renderer;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		glview = new GLSurfaceView(this);
		renderer = new SaturdayHobokenRenderer();
		glview.setRenderer(renderer);
		setContentView(glview);		
	}

}
