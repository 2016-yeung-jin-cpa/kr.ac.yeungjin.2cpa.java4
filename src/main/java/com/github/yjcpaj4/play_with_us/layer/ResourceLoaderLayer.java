package com.github.yjcpaj4.play_with_us.layer;

import com.github.yjcpaj4.play_with_us.Application;
import com.github.yjcpaj4.play_with_us.Layer;
import com.github.yjcpaj4.play_with_us.DropboxClient;
import com.github.yjcpaj4.play_with_us.ResourceManager;
import com.github.yjcpaj4.play_with_us.resource.MovieResource;
import com.github.yjcpaj4.play_with_us.util.FileUtil;
import com.google.gson.Gson;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ResourceLoaderLayer extends Layer {
    
    private final String DBX_CLIENT_ID = "play_with_us/v0.1";
    private final String DBX_ACCESS_TOKEN = "BG8Lirg0MOAAAAAAAAAAG2rc4quBs-xL2yoFBPxrUdyembJNsZyBrrZ6v8JlmSg5";
    private final String DBX_RESOURCE_DIR = "/resources";
        
    private static final int PADDING = 25;

    private final String RESOURCE_CHECKSUM_FILE = "checksum.json";
    private final File RESOURCE_DIR = new File("res");
    private DropboxClient mDropbox;
    private float mProgress;
    private String mMessage;

    public ResourceLoaderLayer(Application c) {
        super(c);
        
        mDropbox = new DropboxClient(DBX_CLIENT_ID, DBX_ACCESS_TOKEN);
        
        if ( ! RESOURCE_DIR.exists()) {
            RESOURCE_DIR.mkdirs();
        }
    }
    
    private void loadResources() throws IOException {
        ResourceManager r = getResource();
        
        // 맵정보를 불러옵니다.
        r.load("res/map.kitchen.json",              "kitchen");
        r.load("res/map.library.json",              "library");
        r.load("res/map.clothesroom.json",          "clothesroom");
        r.load("res/map.bathroom.json",             "bathroom");
        r.load("res/map.livingroom.json",           "livingroom");
        
        // 걸어다닐때 (동,서,남,북)
        r.load("res/sprite.player.walk.n.json",     "player.walk.n");
        r.load("res/sprite.player.walk.e.json",     "player.walk.e");
        r.load("res/sprite.player.walk.s.json",     "player.walk.s");
        r.load("res/sprite.player.walk.w.json",     "player.walk.w");
        
        // 가만히 있을때 (동,서,남,북)
        r.load("res/sprite.player.idle.n.json",     "player.idle.n");
        r.load("res/sprite.player.idle.e.json",     "player.idle.e");
        r.load("res/sprite.player.idle.s.json",     "player.idle.s");
        r.load("res/sprite.player.idle.w.json",     "player.idle.w");
        
        // 걸어다닐때 (동,서,남,북)
        r.load("res/sprite.ghost.walk.n.json",     "ghost.walk.n");
        r.load("res/sprite.ghost.walk.e.json",     "ghost.walk.e");
        r.load("res/sprite.ghost.walk.s.json",     "ghost.walk.s");
        r.load("res/sprite.ghost.walk.w.json",     "ghost.walk.w");
        
        // 가만히 있을때 (동,서,남,북)
        r.load("res/sprite.ghost.idle.n.json",     "ghost.idle.n");
        r.load("res/sprite.ghost.idle.e.json",     "ghost.idle.e");
        r.load("res/sprite.ghost.idle.s.json",     "ghost.idle.s");
        r.load("res/sprite.ghost.idle.w.json",     "ghost.idle.w");
    }

    @Override
    protected void resume() {
        super.resume();
        
        mProgress = 0f;
        mMessage = "게임에 필요한 리소스를 검사합니다.";
        
        new Thread() {

            @Override
            public void run() {

                File f = new File(RESOURCE_DIR, RESOURCE_CHECKSUM_FILE);

                if (f.exists()) {
                    f.delete();
                }

                /*final Map<String, String> m;

                try { // 체크섬 파일 다운받기
                    mDropbox.download(DBX_RESOURCE_DIR + "/" + RESOURCE_CHECKSUM_FILE, f);

                    m = new Gson().fromJson(FileUtil.getContents(f), Map.class);
                }
                catch(Exception e) {
                    throw new RuntimeException(e);
                }

                float n = 0;
                for(String s : m.keySet()) { // 체크섬 목록을 토대로 리소스의 유효성검사
                    File res = new File(RESOURCE_DIR, s);


                    if (res.exists()) { // 리소스가 이미 있고   
                        if ( ! FileUtil.getChecksum(res).equals(m.get(s))) { // 체크섬 검사가 올바르지 않으면

                            if (res.delete()) { // 리소스 제거
                                res.deleteOnExit();
                            }
                        }
                    }

                    if ( ! res.exists()) { // 위에서 리소스가 제거되었거나 없는 리소스라면 드롭박스에서 다운받기
                        try {
                            mDropbox.download(DBX_RESOURCE_DIR + "/" + s, res);
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                            // TODO... 오류처리
                        }
                    } 

                    mProgress = ++n / m.size();
                    mMessage = String.format("게임에 필요한 리소스를 내려받고 있습니다. (%d%%)", (int) (mProgress * 100));
                }*/

                mProgress = 1.0f;
                mMessage = "게임에 필요한 리소스를 불러오는중 입니다.";

                try {
                    loadResources();
                }
                catch (Exception e) {
                    mProgress = 0;
                    mMessage = "불러오는 도중 알수없는 오류가 발생하였습니다.";
                    throw new RuntimeException(e);
                }
                
                VideoLayer l = getContext().getLayer(VideoLayer.class);
                l.load(MovieResource.MOV_INTRO);
                showLayer(l); 

                finishLayer();  
            }
        }.start();
    }

    private void drawMessage(Graphics2D g2d) {
        if (mMessage == null) {
            return;
        }

        Font f = new Font(getApplicationCanvas().getFont().getName(), Font.PLAIN, 30);
        FontMetrics m = g2d.getFontMetrics(f);

        int w = getApplicationCanvas().getWidth();
        int h = getApplicationCanvas().getHeight();

        g2d.setFont(f);
        g2d.drawString(mMessage, 
                       w / 2 - m.stringWidth(mMessage) / 2, 
                       h - PADDING - 50 - 10 - m.getHeight());
    }

    private void drawProgress(Graphics2D g2d) {
        int x = PADDING;
        int y = getApplicationCanvas().getHeight() - PADDING - 50;            

        g2d.setColor(Color.RED);
        g2d.drawRect(x, y, getApplicationCanvas().getWidth() - PADDING * 2, 50);
        g2d.fillRect(x, y, (int) (getApplicationCanvas().getWidth() * mProgress) - PADDING * 2, 50);
    }

    @Override
    protected void draw(long delta, Graphics2D g2d) {
        super.draw(delta, g2d);
        
        Font f = new Font(getApplicationCanvas().getFont().getName(), Font.PLAIN, 50);
        FontMetrics m = g2d.getFontMetrics(f);
        int n = (int) (delta / 500 % 4);
        String s = "Loading";

        for (int l = 0; l < n; ++l) {
            s += ".";
        }

        g2d.setFont(f);
        g2d.setColor(Color.red);
        g2d.drawString(s, PADDING, PADDING + m.getHeight());

        drawProgress(g2d);
        drawMessage(g2d);
    }
}