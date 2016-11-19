package com.github.yjcpaj4.play_with_us.resource;

import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.map.GameObject;
import com.github.yjcpaj4.play_with_us.map.Lightless;
import com.github.yjcpaj4.play_with_us.map.NotWalkable;
import com.github.yjcpaj4.play_with_us.map.Player;
import com.github.yjcpaj4.play_with_us.map.Stage;
import com.github.yjcpaj4.play_with_us.math.Point2D;
import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class StageResource {
    
    public static StageResource loadFromJSON(String s) {
        return loadFromJSON(new File(s));
    }

    public static StageResource loadFromJSON(File f) {
        StageResource r;
        BufferedImage b;
        
        try {
            r = new Gson().fromJson(FileUtil.getContents(f), StageResource.class);
            b = ImageIO.read(new File(f.getParentFile(), r.mImagePath));
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        r.mImage = b;
        
        return r;
    }
    
    @SerializedName("img")
    protected String mImagePath;
    
    @SerializedName("not_walkable")
    protected List<List<Point2D>> mNotWalkable = new ArrayList();
    
    @SerializedName("lightless")
    protected List<List<Point2D>> mLightless = new ArrayList();
    
    @SerializedName("player_spawn")
    protected Point2D mPlayerSpawn = new Point2D(-1, -1);

    protected transient BufferedImage mImage;
    
    public StageResource(File f) {
        mImagePath = f.getName();
            
        try {
            mImage = ImageIO.read(f);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public File getImageFile() {
        return new File(mImagePath);
    }
    
    public BufferedImage getImage() {
        return mImage;
    }
    
    public int getWidth() {
        return mImage.getWidth();
    }
    
    public int getHeight() {
        return mImage.getHeight();
    }
    
    public void addNotWalkable(List<Point2D> l) {
        mNotWalkable.add(l);
    }
    
    public void addLightless(List<Point2D> l) {
        mLightless.add(l);
    }
    
    public List<NotWalkable> getNotWalkable() {
        List<NotWalkable> l = new ArrayList<>();
        for (List<Point2D> o : mNotWalkable) {
            l.add(new NotWalkable(o));
        }
        return l;
    }
    
    public void setPlayerSpawn(Point2D p) {
        mPlayerSpawn.set(p.getX(), p.getY());
    }
    
    public Point2D getPlayerSpwan() {
        return mPlayerSpawn;
    }
    
    public boolean hasPlayerSpawn() {
        return mPlayerSpawn != null && mPlayerSpawn.getX() != -1 && mPlayerSpawn.getY() != -1;
    }
    
    public List<Lightless> getLightless() {
        List<Lightless> l = new ArrayList<>();
        for (List<Point2D> o : mLightless) {
            l.add(new Lightless(o));
        }
        return l;
    }
    
    public Stage toStage() {
        List<GameObject> l = new ArrayList();
        l.addAll(getNotWalkable());
        l.addAll(getLightless());
        return new Stage(mImage, l);
    }
}