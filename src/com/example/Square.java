package com.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * This class is an object representation of
 * a Square containing the vertex information
 * and drawing functionality, which is called
 * by the renderer.
 *
 * @author Savas Ziplies (nea/INsanityDesign)
 */
public class Square {

    /**
     * The buffer holding the vertices
     */
    private FloatBuffer vertexBuffer;
    /**
     * The buffer holding the texCoords coordinates
     */
    private FloatBuffer textureBuffer;
    private Context context;
    /**
     * The initial vertex definition
     */
    private float vertices[] = {
            -1.0f, -1.0f, 0.0f, //Bottom Left
            1.0f, -1.0f, 0.0f,     //Bottom Right
            -1.0f, 1.0f, 0.0f,     //Top Left
            1.0f, 1.0f, 0.0f     //Top Right

    };

    /*
    * Need for texture mapping.
    *
    */

    private float texCoords[] = {
            //Mapping coordinates for the vertices
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f
    };

    /**
     * The Square constructor.
     * <p/>
     * Initiate the buffers.
     */
    public Square(Context context) {

        this.context = context;
        //
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        vertexBuffer = byteBuf.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // Setup for texture map of icon
        byteBuf = ByteBuffer.allocateDirect(texCoords.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuf.asFloatBuffer();
        textureBuffer.put(texCoords);
        textureBuffer.position(0);
    }

    /**
     * The object own drawing function.
     * Called from the renderer to redraw this instance
     * with possible changes in values.
     *
     * @param gl - The GL context
     */
    public void draw(GL10 gl) {

        int[] textures = new int[1];

        InputStream is = context.getResources().openRawResource(R.drawable.icon);
		Bitmap bitmap = null;
		try {
			//BitmapFactory is an Android graphics utility for images
			bitmap = BitmapFactory.decodeStream(is);

		} finally {
			//Always clear and close
			try {
				is.close();
				is = null;
			} catch (IOException e) {
			}
		}
        gl.glGenTextures(1, textures, 0);



//        int[] tmp_tex = new int[textureFiles.length];
//        gl.glGenTextures(textureFiles.length, tmp_tex, 0);

        int textureId = R.drawable.icon;
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);

        // Blend image's alpha with background
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL10.GL_BLEND);

        //Point to our vertex buffer
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

        //Enable vertex buffer
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);


        //Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
