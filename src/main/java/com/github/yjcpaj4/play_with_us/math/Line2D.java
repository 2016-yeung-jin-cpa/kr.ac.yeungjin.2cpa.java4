package com.github.yjcpaj4.play_with_us.math;

public class Line2D {

    private final float mX1, mY1;
    private final float mX2, mY2;

    public Line2D(float x1, float y1, float x2, float y2) {
        mX1 = x1;
        mY1 = y1;

        mX2 = x2;
        mY2 = y2;
    }

    public float getX1() {
        return mX1;
    }

    public float getY1() {
        return mY1;
    } 

    public float getX2() {
        return mX2;
    }

    public float getY2() {
        return mY2;
    } 
    
    public Point2D getStartPoint() {
        return new Point2D((int) mX1, (int) mY1);
    }     
    
    public Point2D getEndPoint() {
        return new Point2D((int) mX2, (int) mY2);
    }     
}
