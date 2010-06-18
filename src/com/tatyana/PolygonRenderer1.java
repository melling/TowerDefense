package com.tatyana;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

public class PolygonRenderer1 extends AbstractRenderer
{
   //Number of points or vertices we want to use
   private final static int VERTS = 20;

   //A raw native buffer to hold the point coordinates
   private FloatBuffer mFVertexBuffer;

   //A raw native buffer to hold indices
   //allowing a reuse of points.
   private ShortBuffer mIndexBuffer;
   private int numOfIndices = 0;
   private long prevtime = SystemClock.uptimeMillis();
   private int sides = 20;
   private float radius =0.1f;

   public PolygonRenderer1(Context context)
   {
      //EvenPolygon t = new EvenPolygon(0,0,0,1,3);
      //EvenPolygon t = new EvenPolygon(0,0,0,1,4);
      prepareBuffers(sides, radius, 0,0,0);
   }
   public void prepareBuffers(int sides, float radius, float x, float y, float z)
   {
	  this.radius = radius;
      RegularPolygon t = new RegularPolygon(0,0,0,radius,sides);
      this.mFVertexBuffer = t.getVertexBuffer();
      this.mIndexBuffer = t.getIndexBuffer();
      this.numOfIndices = t.getNumberOfIndices();
      this.mFVertexBuffer.position(0);
      this.mIndexBuffer.position(0);
   }
   //overriden method
   @Override
   public void draw(GL10 gl)
   {
      long curtime = SystemClock.uptimeMillis();
      
  
      if ((curtime - prevtime) > 200)
      {
         prevtime = curtime;
         sides += 1;
         if (sides > 50)
         {
            sides = 3;
         }
         this.prepareBuffers(sides,radius, 0,0,0);
      }
      //EvenPolygon.test();
   //   gl.glColor4f(1.0f, 0, 0, 0.3f);
      gl.glColor4f(1.0f, 0.5f, 0.5f, 0.3f);
      gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
      gl.glDrawElements(GL10.GL_TRIANGLES, this.numOfIndices,
      GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
   }

   public void onDrawFrame(GL10 gl)
   {
      gl.glDisable(GL10.GL_DITHER);
      gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
      gl.glMatrixMode(GL10.GL_MODELVIEW);
      gl.glLoadIdentity();
      GLU.gluLookAt(gl, 0, 0, -5, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
      gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      draw(gl);
   }

}
