package com.github.yjcpaj4.play_with_us.game.special_object;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.game.GameObject;
import com.github.yjcpaj4.play_with_us.geom.Circle;
import com.github.yjcpaj4.play_with_us.geom.CollisionDetection;
import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.layer.GameLayer;
import com.github.yjcpaj4.play_with_us.layer.InterativeLayer;
import com.github.yjcpaj4.play_with_us.math.Box2D;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.Graphics2D;

public class LibraryBookYellow extends GameObject {
    
    private static final int X = 38;
    private static final int Y = 116;
    private static final int WIDTH = 144;
    private static final int HEIGHT = 84;
    
    private static final String YES = "살펴본다.";
    private static final String NO = "그만둔다.";
    
    private Polygon mCollider = new Box2D(X, Y, WIDTH, HEIGHT).toPolygon();
    
    public Point2D getPosition() {
        return mCollider.getPosition();
    }
    
    @Override
    public void update(GameLayer g, long delta) {
        if (getMap().equals(g.getPlayer().getMap()) 
        && CollisionDetection.isCollide(mCollider, g.getPlayer().getCollider())
        && g.getInput().isKeyOnce(KeyEvent.VK_F)) {
            
            InterativeLayer l = new InterativeLayer(Application.getInstance()) {
                
                @Override
                protected void pause() {
                    super.pause();
                    
                    if (getCurrentAnswer().equals(YES)) {
                        g.showMessage("붉은 책이 참이면 초록 책은 거짓이다.", 3000);
                    }
                }
            };
            l.setQuestion("살펴 보시겠습니까?");
            l.setAnswers(new String[] { YES, NO });
            l.setBackground(g.getResource().getImage("img.bg.library.book.yellow"));
            g.showLayer(l);
        }
    }

    @Override
    public void draw(GameLayer g, long delta, Graphics2D g2d) {
        if (Application.DEBUG) {
            g2d.setColor(Color.RED);
            g2d.drawPolygon(mCollider.toAWTPolygon());
        }
    }
}
