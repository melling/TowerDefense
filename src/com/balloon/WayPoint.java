package com.balloon;

/**
 * Created by IntelliJ IDEA.
 * User: melling
 * Date: Jun 3, 2010
 * Time: 3:04:54 PM
 */
public class WayPoint {
    int wayPointNumber;

    public WayPoint () {
    	
    }
    public WayPoint(int wayPointNumber, int x, int y, int dx, int dy) {
        this.wayPointNumber = wayPointNumber;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    int x;
    int y;
    int dx;
    int dy;
}
