package com.td;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Particle {

    // location
    public float x;
    public float y;
    public float z;
    
    public Particle() {     
    }

    // the constructor which also assigns location
    public Particle(float newx, float newy, float newz) {
            super();
            this.x = newx;
            this.y = newy;
            this.z = newz;
    }
}
