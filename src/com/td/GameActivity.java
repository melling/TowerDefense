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


/**
 * Created by IntelliJ IDEA.
 * User: melling
 * Date: May 31, 2010
 * Time: 3:02:03 PM
 */
public class GameActivity extends Activity {

    private GameGLSurfaceView surface;
    private GLSurfaceView surfaceProAndroid;
   
    
  
    //
    private PolygonRenderer renderer;
//    private GLRenderer renderer1;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        GLSurfaceView glSurface = new GameGLSurfaceView(this);
        setContentView(glSurface);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.pro_android) {
            Log.i("FOO", "Clicked Pro Android II");
//            surface = new GameGLSurfaceView(this);
            surfaceProAndroid = new GLSurfaceView(this);
            // renderer = new GLRenderer();
            renderer = new com.pro.PolygonRenderer(this);
            surfaceProAndroid.setRenderer(renderer);
//             renderer.setContext(this);
            setContentView(surfaceProAndroid);
            

        } else if (item.getItemId() == R.id.tower_defense) {
            Log.i("FOO", "Clicked TowerDefense");
            
//            surface1 = new GLSurfaceView(this);
            surface = new GameGLSurfaceView(this);
//            renderer1 = new com.td.GameRenderer(this);
//            renderer1 = new GLRenderer();
//             renderer = new PolygonRenderer(this);
//            surface.setRenderer(renderer1);
//            renderer1.setContext(this);
            setContentView(surface);

        }
        
        else if (item.getItemId() == R.id.mid_OpenGL_SimpleTriangle) {
            //Tatyana's
        	Log.i("Particals", "Particals");
//             renderer.setContext(this);
       // 	ParticleSystemDemo d = new ParticleSystemDemo();
            Intent i = new Intent(this,ParticleSystemDemo.class);
            startActivity(i);
         //   setContentView(surfaceProAndroidi);

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
