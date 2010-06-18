package com.tatyana;
import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

public class SaturdayHobokenRenderer implements Renderer {
	
	
	ByteBuffer vbb = ByteBuffer.allocateDirect(3*3*4);
	

	@Override
	public void onDrawFrame(GL10 gl) {
		Log.i("HOBOKENREND","HobokenREnd");
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		Log.i("HOBOKENREND","HobokenREnd");
	}

}
