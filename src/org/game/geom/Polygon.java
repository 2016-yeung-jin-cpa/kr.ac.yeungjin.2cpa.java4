package org.game.geom;

import java.util.ArrayList;
import java.util.List;
import org.game.math.Matrix2D;
import org.game.math.Point2D;
import org.game.math.Vector2D;

public class Polygon {

    private List<Vector2D> e = new ArrayList<>();
    private List<Vector2D> norm = new ArrayList<>();
    private List<Point2D> vtx = new ArrayList<>();

    public Polygon() {
    }

    public Polygon(List<Point2D> l) {
        int len = l.size();
        
        for (int n = 0; n < len; ++n) {
            Point2D p1 = l.get(n);
            Point2D p2 = l.get(n < len - 1 ? n + 1 : 0);
            
            Vector2D e = new Vector2D(p2).sub(p1);
            Vector2D norm = e.perp().normalize();
            
            this.e.add(e);
            this.norm.add(norm);
        }
        
        vtx.addAll(l);
    }

    public int[] getXPoints() {
        return Point2D.getXPoints(vtx);
    }

    public int[] getYPoints() {
        return Point2D.getYPoints(vtx);
    }

    public List<Point2D> getVertex() {
        return vtx;
    }

    public void transform(Matrix2D m) {
        transform(m, getPosition());
    }

    public void transform(Matrix2D m, Point2D about) {
        m = Matrix2D.translate(about.getX(), about.getY()).concat(m).concat(Matrix2D.translate(-about.getX(), -about.getY()));

        for (int n = 0; n < vtx.size(); ++n) {
            Point2D p = vtx.get(n);

            double x = p.getX();
            double y = p.getY();

            p.setX((int) (x * m.getA() + y * m.getC() + m.getE()));
            p.setY((int) (x * m.getB() + y * m.getD() + m.getF()));
        }
    }

    public Point2D getPosition() {
        Vector2D v = new Vector2D();

        for (int n = 0; n < vtx.size(); n++) {
            v = v.sum(vtx.get(n));
        }

        int x = (int) (v.getX() / (float) vtx.size());
        int y = (int) (v.getY() / (float) vtx.size());

        return new Point2D(x, y);
    }
}
