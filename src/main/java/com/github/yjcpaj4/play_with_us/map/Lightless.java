package com.github.yjcpaj4.play_with_us.map;

import java.awt.Color;
import java.awt.Graphics2D;
import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.GraphicLooper;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.geom.Rect; 
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import java.util.List;

public class Lightless extends PhysicsObject {
    
    private Polygon mCollider;
    
    public Lightless(List<Point2D> l) {
        mCollider = new Polygon(l);
    }
    
    public Polygon getCollider() {
        return mCollider;
    }
    
    public Point2D getPosition () {
        return mCollider.getPosition();
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
    }

    @Override
    public void update(GameLayer g, long delta) { 
    }
    
}