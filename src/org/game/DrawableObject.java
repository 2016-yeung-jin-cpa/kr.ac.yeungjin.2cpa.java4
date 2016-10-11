package org.game;

import java.awt.Graphics2D;

public interface DrawableObject {
    
    void update(CanvasView g);

    void draw(CanvasView g, Graphics2D g2d);
}
