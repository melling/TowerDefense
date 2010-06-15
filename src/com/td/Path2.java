package com.td;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Path2 {
    // Our vertices.
    private float vertices[] = {
            10.0f, 0f,  // 0
            10.0f, 1f,  // 1
            -30.0f, 1f,  // 2
            -30.0f, 0f,  // 3
            45f, 0f, // 4
            10.0f, -80.0f,  // 5
            45f, -80.0f // 6

    };

    // The order we like to connect them.
    private short[] indices = {0, 1, 2, 0, 2, 3, 0, 4, 5, 4, 6, 5};

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;

    // Our index buffer.
    private ShortBuffer indexBuffer;

    public Path2() {
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);

        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
    }

    /**
     * This function draws our square on screen.
     *
     * @param gl
     */
    public void draw(GL10 gl) {

        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glScalef(20, 20, 1.0f);
        gl.glTranslatef(150, 150, -1);
        // Enabled the vertices buffer for writing and to be used during
        // rendering.
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);


        gl.glColor4f(1.0f, 0.0f, 0.0f, 1f);

        // Specifies the location and data format of an array of vertex
        // coordinates to use when rendering.
        gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);

        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, indices.length,
                GL10.GL_UNSIGNED_SHORT, indexBuffer);
        //
        // Disable the vertices buffer.
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glPopMatrix();

	}
	

}
