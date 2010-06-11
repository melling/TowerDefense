package com.example;

/**
 * Created by IntelliJ IDEA.
 * User: melling
 * Date: Jun 3, 2010
 * Time: 3:04:54 PM
 */
public class WayPoint {
    int wayPointNumber;

    public WayPoint(int wayPointNumber, float x, float y, float dx, float dy) {
        this.wayPointNumber = wayPointNumber;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    float x;
    float y;
    float dx;
    float dy;
}
