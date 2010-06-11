package com.td;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import com.example.R;
import com.pro.PolygonRenderer;
import com.td.GLRenderer;
//import com.td.R;
//import com.pro.PolygonRenderer;
/**
 * Created by IntelliJ IDEA.
 * User: melling
 * Date: May 31, 2010
 * Time: 3:02:03 PM
 */

public class TowerDefenseOld extends Activity {

    private GLSurfaceView surface;
    private PolygonRenderer renderer;
    private GLRenderer renderer1;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

    
//        setContentView(R.layout.main);
//        Intent intent = getIntent();

//        int mid = intent.getIntExtra("com.ai.menuid", R.id.mid_OpenGL_AnimatedTriangle15);



//        if (mid == R.id.mid_OpenGL_AnimatedTriangle15)

//        {
//        	Log.i("FOO", "Clicked mid_OpenGL_AnimatedTriangle15");

//           mTestHarness.setRenderer(new TexturedPolygonRenderer(this));
//
//           mTestHarness.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
//
//           setContentView(mTestHarness);
//
//           return;

//        }  
        
    }
    
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//    	if (item.getItemId() == R.id.mid_OpenGL_AnimatedTriangle15) {
//        	Log.i("FOO", "Clicked mid_OpenGL_AnimatedTriangle15 II");
//            surface = new GLSurfaceView(this);
//            // renderer = new GLRenderer();
//             renderer = new com.pro.PolygonRenderer(this);
//             surface.setRenderer(renderer);
////             renderer.setContext(this);
//             setContentView(surface);
//
//    	}else if (item.getItemId() == R.id.mid_rectangle) {
//        	Log.i("FOO", "Clicked mid_OpenGL_AnimatedTriangle15 II");
//            surface = new GLSurfaceView(this);
//             renderer1 = new GLRenderer();
////             renderer = new PolygonRenderer(this);
//             surface.setRenderer(renderer1);
//             renderer1.setContext(this);
//             setContentView(surface);
//
//    	}
//    	return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//    	MenuInflater inflater = getMenuInflater();
//    	inflater.inflate(R.menu.menu1, menu);
//
//    	return true;
//    }
}