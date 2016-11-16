package com.github.yjcpaj4.play_with_us.resource;

import com.github.yjcpaj4.play_with_us.geom.Polygon;
import com.github.yjcpaj4.play_with_us.map.GameObject;
import com.github.yjcpaj4.play_with_us.map.Lightless;
import com.github.yjcpaj4.play_with_us.map.NotWalkable;
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

    public static StageResource loadFromJSON(File f) {
        StageResource r;
        BufferedImage b;
        
        try {
            r = new Gson().fromJson(FileUtil.getContents(f), StageResource.class);
            b = ImageIO.read(new File(r.mImagePath));
        }
        catch(Exception e) {
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

    protected transient BufferedImage mImage;
    
    public StageResource(File f) {
        mImagePath = f.getName();
            
        try {
            mImage = ImageIO.read(new File(mImagePath));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public Stage toStage() {
        List<GameObject> l = new ArrayList();
        
        for (List<Point2D> o : mNotWalkable) {
            l.add(new NotWalkable(o));
        }
        
        for (List<Point2D> o : mLightless) {
            l.add(new Lightless(o));
        }
        
        return new Stage(mImage, l);
    }
}
